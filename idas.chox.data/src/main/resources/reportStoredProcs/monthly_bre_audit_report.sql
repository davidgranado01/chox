--DROP FUNCTION monthly_bre_audit_report(IN insurerIds integer[], IN choIds integer[],IN startPeriod text, IN endPeriod text, IN claimTypes integer[]);
-- Example use:
--     select * from monthly_bre_audit_report(array[6], null::integer[], '2019-06-01', '2019-07-01', null::integer[]);
CREATE OR REPLACE FUNCTION monthly_bre_audit_report(IN insurerIds integer[], IN choIds integer[], IN startPeriod text, IN endPeriod text, IN claimTypes integer[])
    RETURNS TABLE (
        "Supplier Reference" character varying(128),
        "CHO" character varying(128),
        "Insurer" character varying(128),
        "Claim Type" text,
        "Claim Status" varchar(40),
        "Invoice Upload Date" text,
        "Vehicle Class" varchar(10),
        "Overall BRE Result" text,
        "Full Total Requested" numeric(10,2),
        "Hire Net" numeric(10,2),
        "Hire Net Ceiling - Rule 040" numeric(10,2),
        "Hire Net Ceiling Expected Result" text,
        "Hire Net Ceiling Actual Result" text,
        "Hire Net Ceiling (Vehicle Class) - Rule 003" numeric(10,2),
        "Hire Net Ceiling (Vehicle Class) Expected Result" text,
        "Hire Net Ceiling (Vehicle Class) Actual Result" text,
        "Repair Net" numeric(10,2),
        "Repair Net Ceiling - Rule 041" numeric(10,2),
        "Repair Net Ceiling Expected Result" text,
        "Repair Net Ceiling Actual Result" text,
        "Repair Net Ceiling (Vehicle Class) - Rule 023" numeric(10,2),
        "Repair Net Ceiling (Vehicle Class) Expected Result" text,
        "Repair Net Ceiling (Vehicle Class) Actual Result" text,
        "Hire Days" numeric(4,0),
        "Hire Days Ceiling - Rule 004" integer,
        "Hire Days Ceiling Expected Result" text,
        "Hire Days Ceiling Actual Result" text,
        "Storage Recovery Net Ceiling - Rule 093" numeric(10,2),
        "Storage Recovery Net Ceiling Expected Result" text,
        "Storage Recovery Net Ceiling Actual Result" text,
        "Labour Rate" numeric(10,2),
        "Labour Rate Standard Vehicles & Vans - Rule 087" numeric(10,2),
        "Labour Rate Standard Vehicles & Vans Expected Result" text,
        "Labour Rate Standard Vehicles & Vans Actual Result" text,
        "Labour Rate Prestige and Special Vehicles & Vans - Rule 088" numeric(10,2),
        "Labour Rate Prestige and Special Vehicles & Vans Expected Result" text,
        "Labour Rate Prestige and Special Vehicles & Vans Actual Result" text,
        "Flagged for Manual Review" text,
        "Flagged for Manual Review Expected Result" text,
        "Flagged for Manual Review Actual Result" text
    )
AS $$ DECLARE
    startDate date;
    endDate date;
BEGIN
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

    select c.cho_reference, cho.name, ins.name, getClaimType(c.claim_type), c.status, to_char(i.created_date, 'dd/mm/yyyy'), vhvc.name,
           case when exists(select * from audit_trail at where at.claim_id=c.id and at.new_status in ('InvoiceApprovedByBRE','ManualInvoiceBREApproved') and at.reverted=false and not exists (select * from audit_trail at2 where at2.claim_id=c.id and at.new_status in ('InvoiceEscalatedToHandler','ManualInvoiceBRERejected') and at2.created_date > at.created_date)) then 'Passed' else 'Failed' end,
           i.full_total_to_pay, i.hire_net,
           case when hire_net_does_not_exceed_band_hire_net_ceiling then bre.hire_net_ceiling else null end,
           case when (hire_net_does_not_exceed_band_hire_net_ceiling and i.hire_net <= bre.hire_net_ceiling and c.claim_type not in (7,8,9,11,12,13,18,19,20)) then 'Passed' else case when (hire_net_does_not_exceed_band_hire_net_ceiling and i.hire_net > bre.hire_net_ceiling and c.claim_type not in (7,8,9,11,12,13,18,19,20)) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='040' and is_old=false and type='ERROR') then 'Failed' else
                case when exists (select * from history h where h.claim_id=c.id and h.rule_id='040' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='040' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end,
           case when hire_net_does_not_exceed_vehicle_class_hire_net_ceiling then cvcc.hire_net_ceiling else null end,
           case when (hire_net_does_not_exceed_vehicle_class_hire_net_ceiling and cvcc is not null and i.hire_net <= cvcc.hire_net_ceiling and c.claim_type not in (7,8,9,11,12,13,18,19,20)) then 'Passed' else case when (hire_net_does_not_exceed_vehicle_class_hire_net_ceiling and cvcc is not null and i.hire_net > cvcc.hire_net_ceiling and c.claim_type not in (7,8,9,11,12,13,18,19,20)) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='003' and is_old=false and type='ERROR') then 'Failed' else
                 case when exists (select * from history h where h.claim_id=c.id and h.rule_id='003' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='003' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end,
           i.repair_net,
           case when repair_net_does_not_exceed_band_repair_net_ceiling then bre.max_repair_value else null end,
           case when (repair_net_does_not_exceed_band_repair_net_ceiling and i.repair_net <= bre.max_repair_value and c.claim_type not in (7,8,9,11,12,13,18,19,20)) then 'Passed' else case when (repair_net_does_not_exceed_band_repair_net_ceiling and i.repair_net > bre.max_repair_value and c.claim_type not in (7,8,9,11,12,13,18,19,20)) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='041' and is_old=false and type='ERROR') then 'Failed' else
                 case when exists (select * from history h where h.claim_id=c.id and h.rule_id='041' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='041' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end,
           case when repair_net_does_not_exceed_vehicle_class_repair_net_ceiling then cvcc.repair_net_ceiling else null end,
           case when (repair_net_does_not_exceed_vehicle_class_repair_net_ceiling and cvcc is not null and i.repair_net <= cvcc.repair_net_ceiling and c.claim_type not in (7,8,9,11,12,13,18,19,20)) then 'Passed' else case when (repair_net_does_not_exceed_vehicle_class_repair_net_ceiling and cvcc is not null and i.repair_net > cvcc.repair_net_ceiling and c.claim_type not in (7,8,9,11,12,13,18,19,20)) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='023' and is_old=false and type='ERROR') then 'Failed' else
                 case when exists (select * from history h where h.claim_id=c.id and h.rule_id='023' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='023' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end,
           vh.days,
           case when hire_day_count_does_not_exceed_band_hire_day_ceiling then bre.hire_day_ceiling else null end,
           case when (hire_day_count_does_not_exceed_band_hire_day_ceiling and vh is not null and vh.days <= bre.hire_day_ceiling and c.claim_type not in (7,8,9,18,19,20)) then 'Passed' else case when (repair_net_does_not_exceed_vehicle_class_repair_net_ceiling and vh is not null and vh.days > bre.hire_day_ceiling and c.claim_type not in (7,8,9,18,19,20)) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='004' and is_old=false and type='ERROR') then 'Failed' else
                 case when exists (select * from history h where h.claim_id=c.id and h.rule_id='004' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='004' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end,
           case when storage_recovery_net_ceiling_check then bre.storage_recovery_net_ceiling else null end,
           case when (storage_recovery_net_ceiling_check and i.storage_recovery_net <= bre.storage_recovery_net_ceiling) then 'Passed' else case when (storage_recovery_net_ceiling_check and i.storage_recovery_net > bre.storage_recovery_net_ceiling) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='093' and is_old=false and type='ERROR') then 'Failed' else
                 case when exists (select * from history h where h.claim_id=c.id and h.rule_id='093' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='093' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end,
           case when (hmd is null) then null else hmd.labour_rate end,
           case when (maximum_labour_rate_standard_check and cu.vehicle_class_id in (25,26,27,29,30,31,35,36,44,45,46,48,52,54,55,62,64,65,95,97,98,100,101,102,105,107,108,110,113,114,117,121,122,123,127,140,146,147,148,149,150,151,152,153,154,155,156,157,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,254,255,256,257,258,259,260,261,262,293,294,295,296,297,298,299,300,301,302,303,304,329,330,331,332,333,334,338,339,340,341,342,343)) then bre.max_allowed_labour_standard_rate else null end,
           case when (maximum_labour_rate_standard_check and cu.vehicle_class_id in (25,26,27,29,30,31,35,36,44,45,46,48,52,54,55,62,64,65,95,97,98,100,101,102,105,107,108,110,113,114,117,121,122,123,127,140,146,147,148,149,150,151,152,153,154,155,156,157,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,254,255,256,257,258,259,260,261,262,293,294,295,296,297,298,299,300,301,302,303,304,329,330,331,332,333,334,338,339,340,341,342,343) and hmd is not null and hmd.labour_rate is not null and hmd.labour_rate <= bre.max_allowed_labour_standard_rate) then 'Passed'
                else case when (maximum_labour_rate_standard_check and cu.vehicle_class_id in (25,26,27,29,30,31,35,36,44,45,46,48,52,54,55,62,64,65,95,97,98,100,101,102,105,107,108,110,113,114,117,121,122,123,127,140,146,147,148,149,150,151,152,153,154,155,156,157,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,254,255,256,257,258,259,260,261,262,293,294,295,296,297,298,299,300,301,302,303,304,329,330,331,332,333,334,338,339,340,341,342,343) and hmd is not null and hmd.labour_rate is not null and hmd.labour_rate > bre.max_allowed_labour_standard_rate) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='087' and is_old=false and type='ERROR') then 'Failed' else
                 case when exists (select * from history h where h.claim_id=c.id and h.rule_id='087' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='087' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end,
           case when (maximum_labour_rate_prestige_check and cu.vehicle_class_id in (28,33,34,37,38,39,40,41,42,43,47,48,49,50,51,53,56,57,58,59,60,61,63,66,67,68,69,70,71,72,73,94,96,99,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,141,142,143,182,183,184,185,186,187,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,364,365,366,367,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,384,385,386,387,388,389,390,391,392,393,394,395,396,397,398,399,400,401,402,403,404,405,406,407,408,409,410,411,412)) then bre.max_allowed_labour_prestige_rate else null end,
           case when (maximum_labour_rate_prestige_check and cu.vehicle_class_id in (28,33,34,37,38,39,40,41,42,43,47,48,49,50,51,53,56,57,58,59,60,61,63,66,67,68,69,70,71,72,73,94,96,99,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,141,142,143,182,183,184,185,186,187,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,364,365,366,367,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,384,385,386,387,388,389,390,391,392,393,394,395,396,397,398,399,400,401,402,403,404,405,406,407,408,409,410,411,412) and hmd is not null and hmd.labour_rate is not null and hmd.labour_rate <= bre.max_allowed_labour_prestige_rate) then 'Passed'
                else case when (maximum_labour_rate_prestige_check and cu.vehicle_class_id in (28,33,34,37,38,39,40,41,42,43,47,48,49,50,51,53,56,57,58,59,60,61,63,66,67,68,69,70,71,72,73,94,96,99,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,141,142,143,182,183,184,185,186,187,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,346,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,364,365,366,367,368,369,370,371,372,373,374,375,376,377,378,379,380,381,382,383,384,385,386,387,388,389,390,391,392,393,394,395,396,397,398,399,400,401,402,403,404,405,406,407,408,409,410,411,412) and hmd is not null and hmd.labour_rate is not null and hmd.labour_rate > bre.max_allowed_labour_prestige_rate) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='088' and is_old=false and type='ERROR') then 'Failed' else
                 case when exists (select * from history h where h.claim_id=c.id and h.rule_id='088' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='088' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end,
           case when (c.is_invoice_review_required) then 'Yes' else 'No' end,
           case when (flagged_for_manual_invoice_review and not c.is_invoice_review_required) then 'Passed' else case when (flagged_for_manual_invoice_review and c.is_invoice_review_required) then 'Failed' else 'Skipped' end end,
           case when exists (select * from history h where h.claim_id=c.id and h.rule_id='027' and is_old=false and type='ERROR') then 'Failed' else
                 case when exists (select * from history h where h.claim_id=c.id and h.rule_id='027' and is_old=false and type='INFO' and narrative like '%Passed%') then 'Passed' else case when exists (select * from history h where h.claim_id=c.id and h.rule_id='027' and is_old=false and type='INFO' and narrative like '%Skipped%') then 'Skipped' else 'Passed/Skipped' end end end
    from claim c left outer join vehicle_hire vh on (c.vehicle_hire_id=vh.id) left outer join vehicle_class_ceiling vcc on (vcc.insurer_id=c.insurer_id and vcc.vehicle_class_id=vh.vehicle_class_id)
         left outer join hire_monitoring_detail hmd on (c.hire_monitoring_detail_id=hmd.id) left outer join customer cu on (c.customer_id=cu.id) left outer join vehicle_class_ceiling cvcc on (cvcc.insurer_id=c.insurer_id and cvcc.vehicle_class_id=cu.vehicle_class_id),
         chorganisation cho, invoice i, vehicle_class vhvc, bre_band_organisation bbo, bre_band bre, insurer ins
    where c.chorganisation_id = cho.id and c.invoice_id=i.id and c.vehicle_hire_id=vh.id and vh.vehicle_class_id=vhvc.id
      and bre.insurer_id=c.insurer_id and bbo.chorganisation_id=c.chorganisation_id and bbo.band_id=bre.id
      and c.customer_id = cu.id and c.insurer_id = ins.id
      and (insurerIds is null or c.insurer_id = ANY(insurerIds))
      and (choIds is null or c.chorganisation_id = ANY(choIds))
      and (claimTypes is null or c.claim_type = ANY(claimTypes))
      and i.created_date >= startDate and i.created_date < endDate;

END;
$$ LANGUAGE plpgsql;
--GRANT EXECUTE ON FUNCTION monthly_bre_audit_report(integer[], integer[], text, text, integer[]) TO chox_user;
--GRANT EXECUTE ON FUNCTION monthly_bre_audit_report(integer[], integer[], text, text, integer[]) TO chox_mi;
GRANT EXECUTE ON FUNCTION monthly_bre_audit_report(integer[], integer[], text, text, integer[]) TO #{DB_USER.CHOX_USER};
GRANT EXECUTE ON FUNCTION monthly_bre_audit_report(integer[], integer[], text, text, integer[]) TO #{DB_USER.CHOX_MI};
