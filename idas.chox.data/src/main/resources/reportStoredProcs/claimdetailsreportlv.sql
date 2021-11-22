/**
REC-11448
**/

DROP FUNCTION claimdetailsreportlv(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN claimUploadDate VARCHAR,
    IN closedClaimDate VARCHAR,
    IN insOrCHO char(3),
    IN closedClaimStatuses VARCHAR[],
    IN openClaimStatuses VARCHAR[]);

CREATE OR REPLACE FUNCTION public.claimdetailsreportlv(insids integer[], choids integer[], claimtypes integer[], claimuploaddate character varying, closedclaimdate character varying, insorcho character, closedclaimstatuses character varying[], openclaimstatuses character varying[])
 RETURNS TABLE("Claim Status" character varying, "Claim Type" text, "SLA Days Remaining" character varying, "Supplier Reference" character varying, "CHO Name" character varying, "Insurer Name" character varying, "Workgroup" character varying, "Last Review Date" text, "Invoice Review Required?" text, "Invoice Review Reason" character varying, "Status Modified Date" text, "Reserve Value" numeric, "Liability Status" text, "Final Review?" text, "Liability % Agreed (CHO)" numeric, "Liability % Agreed (Insurer)" numeric, "Liability % Applied to Total to Pay" numeric, "Indemnity Stance" character varying, "CHO Managing Repair?" text, "Customer Contact Date" text, "Credit Agreement Signed by Customer Date" text, "GTA 4.1 Notice Date" text, "Claim Number" character varying, "Insurer Claim Owner" text, "Supplier Claim Owner" text, "Customer Policy Usage" character varying, "Customer Insurer" character varying, "Customer Policy Number" character varying, "Customer Reference" character varying, "Comprehensive?" text, "Customer Postcode" character varying, "Customer Vehicle Manufacturer" character varying, "Customer Vehicle Model" character varying, "Customer Vehicle Registration" character varying, "Customer Vehicle Year" character varying, "Customer Vehicle Class" character varying, "Customer Vehicle Location" character varying, "Customer HPI Vehicle Manufacturer" character varying, "Customer HPI Vehicle Model" character varying, "Customer HPI Vehicle Year" character varying, "Customer HPI Vehicle First Registration" text, "Customer HPI Vehicle Capacity" character varying, "Customer HPI Vehicle Doorplan" character varying, "Customer HPI Vehicle Transmission" character varying, "Customer Can Access Other Vehicle?" text, "Customer Other Vehicle Used?" text, "Customer Other Vehicle" character varying, "Customer Courtesy Car?" text, "Customer Specific Vehicle Required" text, "Customer Specific Vehicle Reason" character varying, "Customer Vehicle Type Required" character varying, "Customer Special Requirements" character varying, "Customer Average Daily Milage" character varying, "Third Party Insurer" character varying, "Third Party Policy Number" character varying, "Third Party Postcode" character varying, "Third Party Vehicle Manufacturer" character varying, "Third Party Vehicle Model" character varying, "Third Party Vehicle Registration" character varying, "Third Party Vehicle Class" character varying, "Customer Vehicle Damage" character varying, "Customer Vehicle Is Usable?" text, "Customer Is Total Loss?" text, "Initial ECD" text, "Incident Date/Time" text, "Incident Location" text, "Police Involved?" text, "Incident Description" text, "Engineer Report Labour Amount" numeric, "Engineer Report Repair Amount" numeric, "Engineer Report Estimated Days Under Repair" numeric, "Engineer Report Is Usable?" text, "Engineer Report Name" character varying, "Engineer Report Company" character varying, "Engineer Report Address1" character varying, "Engineer Report Address2" character varying, "Engineer Report Address3" character varying, "Engineer Report Address4" character varying, "Engineer Report Address5" character varying, "Engineer Report Postcode" character varying, "Engineer Report Telephone" character varying, "Engineer Report Email" character varying, "Hire Vehicle Manufacturer" character varying, "Hire Vehicle Model" character varying, "Hire Vehicle Registration" character varying, "Hire Vehicle Class" character varying, "Hire Vehicle Rental Start" text, "Hire Vehicle Rental End" text, "Hire Vehicle Days Hire" numeric, "Hire Vehicle Collection Reason" character varying, "Hire Vehicle HPI Manufacturer" character varying, "Hire Vehicle HPI Model" character varying, "Hire Vehicle HPI Year" character varying, "Hire Vehicle HPI First Registration" date, "Hire Vehicle HPI Capacity" character varying, "Hire Vehicle HPI Door Plan" character varying, "Hire Vehicle HPI Transmission" character varying, "Name of Repairer" character varying, "Repair Booked-in Date" text, "Repair Authorised Date" text, "Repair Commenced Date" text, "Inspection Booked Date" text, "Inspection Date" text, "Name of IME" character varying, "Repair Completion Date" text, "Is Total Loss?" text, "Date Total Loss Offer Made" text, "Date Total Loss Offer Accepted" text, "Date Total Loss Cheque Issued " text, "Date Total Loss Cheque Received " text, "Labour Rate (per Hour)" numeric, "Labour Hours" numeric, "Total Labour Cost" numeric, "Labour Information Non-Provision Reason" character varying, "Is Repair Only (No Hire)?" text, "Is Non-Fault Insurer Managing Repair?" text, "Is The Vehicle Owner VAT Registered?" text, "Next Review Date" text, "Has Copley Offer been made?" text, "Copley Offer Made Date" text)
 LANGUAGE plpgsql
AS $function$
BEGIN
RETURN QUERY

select
    c.status,
    getClaimTypeName(c.claim_type),
    c.remaining_sla_days_str,
    c.cho_reference,
    cho.name,
    ins.name,
    w.name,
    to_char(c.last_review_date, 'dd/mm/yyyy hh24:mm'),
    case when insOrCHO = 'INS' then case when c.is_invoice_review_required then 'Yes' else 'No' end else '' end,
    case when insOrCHO = 'INS' then c.invoice_review_reason else '' end,
    to_char(c.status_modified_date, 'dd/mm/yyyy hh:mm'),
    c.indeminty_amount,
    getLiabilityStatus(c.liability_status),
    case when insOrCHO = 'INS' then case when c.final_review_ins then 'Yes' else 'No' end else case when c.final_review_cho then 'Yes' else 'No' end end,
    round(c.percentage_liability_cho,1),
    round(c.percentage_liability_accepted, 1),
    round(c.applied_liability, 1),
    c.indemnity_stance,
    case when c.managing_repair then 'Yes' else 'No' end,
    to_char(c.policy_holder_contact_date, 'dd/mm/yyyy hh24:mm'),
    to_char(c.credit_agreement_date, 'dd/mm/yyyy hh24:mm'),
    to_char(c.gta_notice_date, 'dd/mm/yyyy hh24:mm'),
    c.claim_number,
    case when wu.hashed then 'GDPR: Data Removed' else wu.last_name || ', ' || wu.first_name end as claim_owner,
    case when wuc.hashed then 'GDPR: Data Removed' else wuc.last_name || ', ' || wuc.first_name end as claim_supplier_owner,
    cust.policy_usage as customer_policy_usage,
    cust.insurer_name as customer_insurer_name,
    cust.policy_number as customer_policy_number,
    cust.claim_reference as customer_claim_reference,
    case when cust.comprehensive then 'Yes' else 'No' end as customer_comprehensive,
    cust.postcode,
    replace(cust.vehicle_manufacturer, '|', '-')::varchar as customer_vehicle_manufacturer,
    replace(cust.vehicle_model, '|', '-')::varchar as customer_vehicle_model,
    cust.vehicle_registration as customer_vehicle_registration,
    cust.vehicle_year as customer_vehicle_year,
    cust_vc.name as customer_vehicle_class,
    cust.location as customer_location,
    cust.hpi_vehicle_manufacturer as customer_hpi_vehicle_manufacturer,
    cust.hpi_vehicle_model as customer_hpi_vehicle_model,
    cust.hpi_vehicle_year as customer_hpi_vehicle_year,
    to_char(cust.hpi_first_registration, 'dd/mm/yyyy') as customer_hpi_vehicle_first_registration,
    cust.hpi_vehicle_capacity as customer_hpi_vehicle_capacity,
    cust.hpi_vehicle_doorplan as customer_hpi_vehicle_doorplan,
    cust.hpi_vehicle_transmission as customer_hpi_vehicle_transmission,
    case when cust.access_other_vehicle is null then '' else case when cust.access_other_vehicle then 'Yes' else 'No' end end as customer_access_other_vehicle,
    case when cust.other_vehicle_used is null then '' else case when cust.other_vehicle_used then 'Yes' else 'No' end end as customer_other_vehicle_used,
    cust.other_vehicle as customer_other_vehicle,
    case when cust.courtesy_car is null then '' else case when cust.courtesy_car then 'Yes' else 'No' end end as customer_courtesy_car,
    case when cust.specific_vehicle is null then '' else case when cust.specific_vehicle then 'Yes' else 'No' end end as customer_specific_vehicle,
    cust.specific_vehicle_reason as customer_specific_vehicle_reason,
    cust.vehicle_type_required as customer_vehicle_type_required,
    cust.special_requirements as customer_special_requirements,
    cust.average_daily_mileage as customer_average_daily_mileage,
    tp_insurer.name as tp_insurer_name,
    tp.policy_number as tp_policy_number, tp.postcode,
    tp.vehicle_manufacturer as tp_vehicle_manufacturer,
    tp.vehicle_model as tp_vehicle_model,
    tp.vehicle_registration as tp_vehicle_registration,
    tp_vc.name as tp_vehicle_class,
    cust.damage as customer_damage,
    case when cust.is_usable is null then '' else case when cust.is_usable then 'Yes' else 'No' end end as customer_is_usable,
    case when cust.is_total_loss is null then '' else case when cust.is_total_loss then 'Yes' else 'No' end end as customer_is_total_loss,
    to_char(cust.initial_ecd, 'dd/mm/yyyy hh24:mm') as customer_initial_ecd,
    to_char(inc. date, 'dd/mm/yyyy hh24:mm') as incident_date,
    inc.location as incident_location,
    case when inc.is_police_involved is null then '' else case when inc.is_police_involved then 'Yes' else 'No' end end as incident_is_police_involved,
    regexp_replace(inc.incident_description, E'[\\n\\r]+', ' ', 'g' ) as incident_description,
    er.labour_amount as er_labour_amount,
    er.total_amount as er_repair_amount,
    er.days as er_days,
    case when er.is_usable is null then 'Unknown' else case when er.is_usable then 'Yes' else 'No' end end,
    er.name as er_name,
    er.company as er_company,
    er.address1 as er_address1,
    er.address2 as er_address2,
    er.address3 as er_address3,
    er.address4 as er_address4,
    er.address5 as er_address5,
    er.postcode as er_postcode,
    er.telephone as er_telephone,
    er.email as er_email,
    vh.vehicle_manufacturer as vh_vehicle_manufacturer,
    vh.vehicle_model as vh_vehicle_model,
    vh.vehicle_registration as vh_vehicle_registration,
    vh_vc.name as vh_vehicle_class_name,
    to_char(vh.rental_start, 'dd/mm/yyyy hh24:mm') as vh_rental_start,
    to_char(vh.rental_end, 'dd/mm/yyyy hh24:mm') as vh_rental_end,
    vh.days as vh_days,
    vh.collection_reason as vh_collection_reason,
    vh.hpi_vehicle_manufacturer as vh_hpi_vehicle_manufacturer,
    vh.hpi_vehicle_model as vh_hpi_vehicle_model,
    vh.hpi_vehicle_year as vh_hpi_vehicle_year,
    vh.hpi_first_registration as vh_hpi_vehicle_first_registration,
    vh.hpi_vehicle_capacity as vh_hpi_vehicle_capacity,
    vh.hpi_vehicle_doorplan as vh_hpi_vehicle_doorplan,
    vh.hpi_vehicle_transmission as vh_hpi_vehicle_transmission,
    hmd.name_of_repairer as hmd_name_of_repairer,
    to_char(hmd.repair_book_in_date, 'dd/mm/yyyy hh24:mm') as hmd_repair_book_in_date,
    to_char(hmd.repair_authorised_date, 'dd/mm/yyyy hh24:mm') as hmd_repair_authorised_date,
    to_char(hmd.repair_commenced_date, 'dd/mm/yyyy hh24:mm') as hmd_repair_commenced_date,
    to_char(hmd.inspection_booked_date, 'dd/mm/yyyy hh24:mm') as hmd_inspection_booked_date,
    to_char(hmd.inspection_date, 'dd/mm/yyyy hh24:mm') as hmd_inspection_date,
    hmd.name_of_ime as hmd_name_of_ime,
    to_char(hmd.repair_completion_date, 'dd/mm/yyyy hh24:mm') as hmd_repair_completion_date,
    case when hmd.is_total_lost_check is null then '' else case when hmd.is_total_lost_check then 'Yes' else 'No' end end as hmd_is_total_lost_check,
    to_char(hmd.total_loss_offer_made, 'dd/mm/yyyy hh24:mm') as hmd_total_loss_offer_made,
    to_char(hmd.total_loss_offer_accepted, 'dd/mm/yyyy hh24:mm') as hmd_total_loss_offer_accepted,
    to_char(hmd.total_loss_check_issued, 'dd/mm/yyyy hh24:mm') as hmd_total_loss_check_issued,
    to_char(hmd.total_loss_check_received, 'dd/mm/yyyy hh24:mm') as hmd_total_loss_check_received,
    hmd.labour_rate as hmd_labour_rate,
    hmd.labour_hour as hmd_labour_hour,
    hmd.labour_cost as hmd_labour_cost,
    hmd.non_provision_reason as hmd_non_provision_reason,
    case when hmd.is_repair_only_check is null then '' else case when hmd.is_repair_only_check then 'Yes' else 'No' end end as claim_repair_only_check,
    case when hmd.is_non_fault_insurer_managing_repair is null then '' else case when hmd.is_non_fault_insurer_managing_repair then 'Yes' else 'No' end end as claim_non_fault_insurer_repair,
    case when hmd.client_vat_registered is null then '' else case when hmd.client_vat_registered then 'Yes' else 'No' end end as claim_client_vat_registered,
    to_char(hmd.next_review_date, 'dd/mm/yyyy hh24:mm') as hmd_next_review_date,
    case when c.copley_offer_made is null then '' else case when c.copley_offer_made then 'Yes' else 'No' end end as claim_copley_offer,
    to_char(c.copley_offer_made_date, 'dd/mm/yyyy hh24:mm')
from claim c
    join chorganisation cho on (c.chorganisation_id = cho.id)
    join insurer ins on (c.insurer_id = ins.id)
    left outer join workgroup w on (c.workgroup_id = w.id)
    left outer join web_user wu on (c.claim_owner_id = wu.id)
    left outer join web_user wuc on (c.cho_claim_owner_id = wuc.id)
    left outer join customer cust on (c.customer_id = cust.id)
    left outer join vehicle_class cust_vc on (cust.vehicle_class_id = cust_vc.id)
    left outer join third_party tp on (c.third_party_id = tp.id)
    left outer join vehicle_class tp_vc on (cust.vehicle_class_id = tp_vc.id)
    left outer join insurer tp_insurer on (tp.insurer_id = tp_insurer.id)
    left outer join incident inc on (c.incident_id = inc.id)
    left outer join witness wit on (inc.id = wit.incident_id)
    left outer join injury inj on (inc.id = inj.incident_id)
    left outer join engineer_report er on (c.engineer_report_id = er.id)
    left outer join vehicle_hire vh on (c.vehicle_hire_id = vh.id)
    left outer join vehicle_class vh_vc on (vh.vehicle_class_id = vh_vc.id)
    left outer join hire_monitoring_detail hmd on (c.hire_monitoring_detail_id = hmd.id)
where (insIds is null or c.insurer_id = ANY(insIds))
    and (choIds is null or c.chorganisation_id = ANY(choIds))
    and (claimTypes is null or c.claim_type = ANY(claimTypes))
    and c.created_date >= claimUploadDate::Date
    and ((c.status!=ALL(closedClaimStatuses) and c.status!=ALL(openClaimStatuses))
            or ((c.status=ANY(closedClaimStatuses) or (openClaimStatuses is not null and c.status=ANY(openClaimStatuses))) and c.status_modified_date >= closedClaimDate::Date))
order by c.created_date, c.cho_reference;

END;
$function$
;
