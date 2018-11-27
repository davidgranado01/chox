DROP FUNCTION monthly_insurer_hire_review_report(IN insurerIds integer[], IN choIds integer[],IN startdate text, IN enddate text, IN statusList varchar[], IN vehicleClassIds integer[]);

CREATE OR REPLACE FUNCTION monthly_insurer_hire_review_report(IN insurerIds integer[], IN choIds integer[],IN startdate text, IN enddate text, IN statusList varchar[], IN vehicleClassIds integer[])
    RETURNS TABLE (
        "Month" text,
        "Total No. Hire Invoices Uploaded" bigint,
        "No. Hire Invoices Uploaded (status & vehicle class restricted)" bigint,
        "Original Hire Days Average" numeric(5,1), 
        "Current Hire Days Average" numeric(5,1),
        "Original Hire Gross Average" numeric(8,2),
        "Current Hire Gross Average" numeric(8,2),
        "Original Hire Days (Total Loss, No Repair) Average" numeric(5,1),
        "Current Hire Days (Total Loss, No Repair) Average" numeric(5,1),
        "Original Hire Gross (Total Loss, No Repair) Average" numeric(8,2),
        "Current Hire Gross (Total Loss, No Repair) Average" numeric(8,2),
        "Original Hire Days (with Repair) Average" numeric(5,1),
        "Current Hire Days (with Repair) Average" numeric(5,1),
        "Original Hire Gross (with Repair) Average" numeric(8,2),
        "Current Hire Gross (with Repair) Average" numeric(8,2),
        "Original Hire Days (No Total Loss, No Repair) Average" numeric(5,1),
        "Current Hire Days (No Total Loss, No Repair) Average" numeric(5,1),
        "Original Hire Gross (No Total Loss, No Repair) Average" numeric(8,2),
        "Current Hire Gross (No Total Loss, No Repair) Average" numeric(8,2)
    ) AS
$BODY$

DECLARE

   start_date date;
   end_date date;
   report_end_date date;

BEGIN

   start_date = (SELECT to_date(to_char(startDate::date, 'MM') || '-01-' || to_char(startDate::date, 'yyyy'), 'mm-dd-yyyy'));
   report_end_date = (SELECT to_date(to_char(enddate::date, 'MM') || '-01-' || to_char(enddate::date, 'yyyy'), 'mm-dd-yyyy') + interval '1 month');
   end_date = start_date + interval '1 month';

WHILE start_date < report_end_date LOOP

  RETURN QUERY
    --------------------   SELECT MONTH -----------------
    SELECT to_char(start_date, 'TMMonth') || ' - ' || to_char(start_date , 'yyyy') AS "Month",

    --------------------   SELECT "Total No. Hire Invoices Uploaded" -----------------
    (SELECT 
      COUNT(*)
    FROM 
      claim c, invoice i, vehicle_hire vh
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id
      AND i.hire_gross > i.admin_fee
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "No. Hire Invoices Uploaded (status restricted)" -----------------
    (SELECT 
      COUNT(*)
    FROM 
      claim c, invoice i, vehicle_hire vh
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id
      AND i.hire_gross > i.admin_fee
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Original Hire Days Average" -----------------
    (SELECT 
      avg(vh.days_original)::numeric(5,1)
    FROM 
      claim c, invoice i, vehicle_hire vh, invoice_original io
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND i.invoice_original_id=io.id
      AND io.hire_gross > io.admin_fee
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Current Hire Days Average" -----------------
    (SELECT 
      avg(vh.days)::numeric(5,1)
    FROM 
      claim c, invoice i, vehicle_hire vh
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id
      AND i.hire_gross > i.admin_fee
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Original Hire Gross Average" -----------------
    (SELECT 
      avg(io.hire_gross)::numeric(8,2)
    FROM 
      claim c, invoice i, vehicle_hire vh, invoice_original io
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND i.invoice_original_id=io.id
      AND io.hire_gross > io.admin_fee
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Current Hire Gross Average" -----------------
    (SELECT 
      avg(i.hire_gross)::numeric(8,2)
    FROM 
      claim c, invoice i, vehicle_hire vh
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id
      AND i.hire_gross > i.admin_fee
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Original Hire Days (Total Loss, No Repair) Average" -----------------
    (SELECT 
      avg(vh.days_original)::numeric(5,1)
    FROM 
      claim c, invoice i, vehicle_hire vh, invoice_original io, customer cu
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND i.invoice_original_id=io.id AND c.customer_id=cu.id
      AND io.hire_gross > io.admin_fee AND io.repair_gross = 0 AND cu.is_total_loss = true
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Current Hire Days (Total Loss, No Repair) Average" -----------------
    (SELECT 
      avg(vh.days)::numeric(5,1)
    FROM 
      claim c, invoice i, vehicle_hire vh, customer cu
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND c.customer_id=cu.id
      AND i.hire_gross > i.admin_fee AND i.repair_gross = 0 AND cu.is_total_loss = true
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Original Hire Gross (Total Loss, No Repair) Average" -----------------
    (SELECT 
      avg(io.hire_gross)::numeric(8,2)
    FROM 
      claim c, invoice i, vehicle_hire vh, invoice_original io, customer cu
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND i.invoice_original_id=io.id AND c.customer_id=cu.id
      AND io.hire_gross > io.admin_fee AND i.repair_gross = 0 AND cu.is_total_loss = true
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Current Hire Gross (Total Loss, No Repair) Average" -----------------
    (SELECT 
      avg(i.hire_gross)::numeric(8,2)
    FROM 
      claim c, invoice i, vehicle_hire vh, customer cu
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND c.customer_id=cu.id
      AND i.hire_gross > i.admin_fee AND i.repair_gross = 0 AND cu.is_total_loss = true
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Original Hire Days (with Repair) Average" -----------------
    (SELECT 
      avg(vh.days_original)::numeric(5,1)
    FROM 
      claim c, invoice i, vehicle_hire vh, invoice_original io
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND i.invoice_original_id=io.id
      AND io.hire_gross > io.admin_fee AND io.repair_gross > 0
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Current Hire Days (with Repair) Average" -----------------
    (SELECT 
      avg(vh.days)::numeric(5,1)
    FROM 
      claim c, invoice i, vehicle_hire vh
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id
      AND i.hire_gross > i.admin_fee AND i.repair_gross > 0
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Original Hire Gross (with Repair) Average" -----------------
    (SELECT 
      avg(io.hire_gross)::numeric(8,2)
    FROM 
      claim c, invoice i, vehicle_hire vh, invoice_original io
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND i.invoice_original_id=io.id
      AND io.hire_gross > io.admin_fee AND i.repair_gross > 0
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Current Hire Gross (with Repair) Average" -----------------
    (SELECT 
      avg(i.hire_gross)::numeric(8,2)
    FROM 
      claim c, invoice i, vehicle_hire vh
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id
      AND i.hire_gross > i.admin_fee AND i.repair_gross > 0
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Original Hire Days (No Total Loss, No Repair) Average" -----------------
    (SELECT 
      avg(vh.days_original)::numeric(5,1)
    FROM 
      claim c, invoice i, vehicle_hire vh, invoice_original io, customer cu
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND i.invoice_original_id=io.id AND c.customer_id=cu.id
      AND io.hire_gross > io.admin_fee AND io.repair_gross = 0 AND cu.is_total_loss = false
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Current Hire Days (No Total Loss, No Repair) Average" -----------------
    (SELECT 
      avg(vh.days)::numeric(5,1)
    FROM 
      claim c, invoice i, vehicle_hire vh, customer cu
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND c.customer_id=cu.id
      AND i.hire_gross > i.admin_fee AND i.repair_gross = 0 AND cu.is_total_loss = false
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Original Hire Gross (No Total Loss, No Repair) Average" -----------------
    (SELECT 
      avg(io.hire_gross)::numeric(8,2)
    FROM 
      claim c, invoice i, vehicle_hire vh, invoice_original io, customer cu
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND i.invoice_original_id=io.id AND c.customer_id=cu.id
      AND io.hire_gross > io.admin_fee AND i.repair_gross = 0 AND cu.is_total_loss = false
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date),

    --------------------   SELECT "Current Hire Gross (No Total Loss, No Repair) Average" -----------------
    (SELECT 
      avg(i.hire_gross)::numeric(8,2)
    FROM 
      claim c, invoice i, vehicle_hire vh, customer cu
    WHERE c.invoice_id=i.id AND c.vehicle_hire_id=vh.id AND c.customer_id=cu.id
      AND i.hire_gross > i.admin_fee AND i.repair_gross = 0 AND cu.is_total_loss = false
      AND (insurerIds is null OR c.insurer_id=ANY(insurerIds))
      AND (choIds is null OR c.chorganisation_id=ANY(choIds))
      AND (statusList is null OR c.status=ANY(statusList))
      AND (vehicleClassIds is null OR vh.vehicle_class_id=ANY(vehicleClassIds))
      AND c.created_date BETWEEN start_date AND end_date);

    start_date = end_date;
    end_date = end_date + interval '1 month';

END LOOP;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
GRANT EXECUTE ON FUNCTION monthly_insurer_hire_review_report(IN insurerIds integer[], IN choIds integer[],IN startdate text, IN enddate text, IN statusList varchar[], IN vehicleClassIds integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION monthly_insurer_hire_review_report(IN insurerIds integer[], IN choIds integer[],IN startdate text, IN enddate text, IN statusList varchar[], IN vehicleClassIds integer[]) TO chox_mi;
