drop function invoice_upload_report(integer[], integer[], text, text);

create or replace function invoice_upload_report
(
   IN uploadOrgIds integer[], IN opposingOrgIds integer[], IN startPeriod text, IN endPeriod text
)
returns table
(
   "Supplier Reference" character varying,
   "Claim Status" character varying(128),
   "Process Status"  character varying(128),
   "Remark"  character varying(256),
   "Error Message" character varying,
   "BRE Failure Message" character varying,
   "Date Processed" date
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select ucd.choreference, ucd.claim_status, ucd.process_status, ucd.remark, ucd.message, ucd.bre_failure, ucd.created_date::date as dateProcessed
from uploaded_claims_detail ucd, web_user w
where ucd.created_date >= startDate and ucd.created_date < endDate and ucd.created_by = w.id
  and ((w.insurer_id is null and w.chorganisation_id = ANY(uploadOrgIds) and (opposingOrgIds is null or exists 
                                    (select * from claim where id=ucd.claim_id and insurer_id = ANY(opposingOrgIds))))
         or (w.chorganisation_id is null and w.insurer_id = ANY(uploadOrgIds) and (opposingOrgIds is null or exists 
                                    (select * from claim where id=ucd.claim_id and chorganisation_id = ANY(opposingOrgIds)))))
order by dateProcesse asc;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION invoice_upload_report(integer[], integer[], text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION invoice_upload_report(integer[], integer[], text, text) TO chox_mi;
-- e.g.
--     select * from invoice_upload_report(null, array[6], '2016-04-11','2016-04-18');
--     select * from invoice_upload_report(null, array[3], '2015-04-13','2015-04-15');
--     select * from invoice_upload_report(array[1007], null, '2015-04-13','2015-04-15');
