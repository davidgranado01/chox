drop function last_ecd_update_report(integer, integer[], integer[]);

create or replace function last_ecd_update_report
(
   IN insurerId integer, IN choIds integer[], IN claimTypes integer[]
)
returns table
(
   "Supplier Reference" character varying(128),
   "CHO Name" character varying(128),
   "Insurer Workgroup Name" character varying,
   "Insurer Claim Owner" text,
   "Date Claim Moved to AwaitingCarHireInfo Status" text,
   "Created Date of the Most Recent ECD Update" text,
   "ECD Provided" text,
   "Latest LOU Update" text
)
as $$ DECLARE 

BEGIN 

RETURN QUERY

    SELECT
          c.cho_reference,
          cho.name,
          wg.name,
          (ch.first_name || ' ' || ch.last_name),
          to_char(c.status_modified_date, 'dd/mm/yyyy'),
          to_char(ecd.created_date, 'dd/mm/yyyy'),
          to_char(ecd.ecd_date, 'dd/mm/yyyy'),
          to_char(greatest(hmd.inspection_booked_date_last_modified, hmd.inspection_date_last_modified,
                   hmd.repair_authorised_date_last_modified, hmd.repair_book_in_date_last_modified,
                   hmd.repair_commenced_date_last_modified, hmd.repair_completion_date_last_modified,
                   hmd.total_loss_offer_made_last_modified, hmd.total_loss_offer_accepted_last_modified,
                   hmd.total_loss_check_issued_last_modified, hmd.total_loss_check_received_last_modified      
          ), 'dd/mm/yyyy')
    FROM 
          claim c INNER JOIN chorganisation cho ON c.chorganisation_id = cho.id 
          INNER JOIN insurer ins ON c.insurer_id = ins.id
          LEFT OUTER JOIN workgroup wg ON c.workgroup_id = wg.id
          LEFT OUTER JOIN web_user ch ON c.claim_owner_id = ch.id
          LEFT OUTER JOIN hire_monitoring_detail hmd ON c.hire_monitoring_detail_id = hmd.id
          LEFT OUTER JOIN hire_monitoring_ecd ecd ON (ecd.claim_id = c.id AND ecd.created_date = (SELECT MAX(created_date) FROM hire_monitoring_ecd WHERE claim_id = c.id))
    WHERE
          ins.id = insurerId
          AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
          AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
          AND c.status ilike 'AwaitingCarHireInfo'
          ORDER BY cho.name ASC, c.cho_reference ASC;
END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION last_ecd_update_report(integer, integer[], integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION last_ecd_update_report(integer, integer[], integer[]) TO chox_mi;
-- e.g.
--     select * from last_ecd_update_report(20, array[]::integer[], array[]::integer[]);
