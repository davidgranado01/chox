DROP FUNCTION unacknowledgedClaimsReport(IN insId integer, IN choIds  integer[], IN claimStatus character varying[], IN claimTypes integer[]);

CREATE OR REPLACE FUNCTION unacknowledgedClaimsReport(
    IN insId integer,
    IN choIds  integer[],
    IN claimStatus varchar[],
    IN claimTypes integer[])
RETURNS TABLE(
    "Supplier Reference" varchar,
    "CHO Name" varchar,
    "Third Party Insurer" varchar,
    "Third Party Insurer Claim Number" varchar,
    "Workgroup" varchar,
    "CHO Managing Repair?" varchar,
    "Customer Contact Date" varchar,
    "Insurer Owner Username" varchar,
    "Customer Title" varchar,
    "Customer First Name(s)" varchar,
    "Customer Surname" varchar,
    "Customer Address 1" varchar,
    "Customer Address 2" varchar,
    "Customer Address 3" varchar,
    "Customer Address 4" varchar,
    "Customer Address 5" varchar,
    "Customer Postcode" varchar,
    "Customer Telephone day" varchar,
    "Customer Telephone evening" varchar,
    "Customer Vehicle Registration Number" varchar,
    "Customer Vehicle Class" varchar,
    "Customer Vehicle Location" varchar,
    "Customer Vehicle Manufacturer" varchar,
    "Customer Vehicle Model" varchar,
    "Customer Vehicle Year Of Manufacture" varchar,
    "Customer Vehicle Date Of First Registration" varchar,
    "Customer Vehicle Capacity" varchar,
    "Vehicle Door Plan" varchar,
    "Vehicle Transmission" varchar,
    "Customer vehicle damage Description" varchar,
    "Customer Vehicle Usable?" varchar,
    "Customer Vehicle Total Loss?" varchar,
    "Third Party Policy Number" varchar,
    "Third Party Title" varchar,
    "Third Party First Name(s)" varchar,
    "Third Party Surname" varchar,
    "Third Party Vehicle Registration Number" varchar,
    "Incident Date/Time" varchar,
    "Location" varchar,
    "Description" varchar,
    "Name of repairer" varchar,
    "Non-Fault Insurer Managing Repair?" varchar,
    "Repair Book in Date" varchar,
    "Date Repair Authorised" varchar,
    "Date Repair Commenced" varchar,
    "Repair Completion Date" varchar
)
AS $BODY$
    BEGIN
        RETURN QUERY
            select c.cho_reference, cho.name, ins.name, c.claim_number, w.name, case when c.managing_repair then 'Yes'::varchar else 'No'::varchar end,
                   to_char(c.policy_holder_contact_date, 'dd/mm/yyyy hh:mm:ss')::varchar, wu.user_name,
                   cu.title, cu.first_name, cu.last_name, cu.address1, cu.address2, cu.address3, cu.address4, cu.address5,
                   cu.postcode, cu.telephone_day, cu.telephone_evening, cu.vehicle_registration, customer_vc.name, cu.location,
                   cu.hpi_vehicle_manufacturer, cu.hpi_vehicle_model, cu.hpi_vehicle_year,  to_char(cu.hpi_first_registration, 'dd/mm/yyyy')::varchar, 
                   cu.hpi_vehicle_capacity, cu.hpi_vehicle_doorplan, cu.hpi_vehicle_transmission, cu.damage,
                   case when cu.is_usable then 'Yes'::varchar else 'No'::varchar end, case when cu.is_total_loss then 'Yes' else 'No' end::varchar,
                   tp.policy_number, tp.title, tp.first_name, tp.last_name, tp.vehicle_registration, to_char(inc.date, 'dd/mm/yyyy')::varchar,
                   inc.location::varchar, inc.incident_description::varchar, hmd.name_of_repairer, case when hmd.is_non_fault_insurer_managing_repair then 'Yes'::varchar else 'No'::varchar end,
                   to_char(hmd.repair_book_in_date, 'dd/mm/yyyy hh:mm:ss')::varchar, to_char(hmd.repair_authorised_date, 'dd/mm/yyyy hh:mm:ss')::varchar,
                   to_char(hmd.repair_commenced_date, 'dd/mm/yyyy hh:mm:ss')::varchar, to_char(hmd.repair_completion_date, 'dd/mm/yyyy hh:mm:ss')::varchar
            from claim c left outer join workgroup w on (w.id=c.workgroup_id)
                         left outer join web_user wu on (wu.id=c.claim_owner_id),
                 customer cu, third_party tp, chorganisation cho, insurer ins, vehicle_class customer_vc, incident inc, hire_monitoring_detail hmd
            where c.third_party_id=tp.id and c.customer_id=cu.id and c.chorganisation_id=cho.id and c.insurer_id = ins.id
              and customer_vc.id=cu.vehicle_class_id and c.incident_id = inc.id and c.hire_monitoring_detail_id = hmd.id
              and c.status= ANY(claimStatus) and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))
              and c.insurer_id = insId and (choIds is null or array_length(choIds, 1) < 1 or c.chorganisation_id = ANY(choIds));
    END;
$BODY$
    LANGUAGE plpgsql VOLATILE COST 100;

GRANT EXECUTE ON FUNCTION unacknowledgedClaimsReport(IN insId integer, IN choIds  integer[], IN claimStatus character varying[], IN claimTypes integer[]) TO #{DB_USER.CHOX_USER};
GRANT EXECUTE ON FUNCTION unacknowledgedClaimsReport(IN insId integer, IN choIds  integer[], IN claimStatus character varying[], IN claimTypes integer[]) TO #{DB_USER.MI_USER};

-- select * from unacknowledgedClaimsReport(6, null::integer[] , array['ClaimUnacknowledgedRouted'], null::integer[]);
