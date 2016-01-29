-- select * from weekly_dashboard(26, array[1007, 1123, 1341], '2015-11-16', '2015-11-21');

drop function weekly_dashboard(IN insurerids integer, IN choids integer[], IN startdate text, IN enddate text);
create or replace function weekly_dashboard
(
   IN insurerid integer, IN choids integer[], IN startdate text, IN enddate text
)
returns table
(
   	"CHO Name" character varying(128),
   	"Total No. Claims Uploaded" bigint,
   	"Total No. Invoices Uploaded" bigint,
   	"Total No. Invoices Paid" bigint,
   	"Total No. GTA Invoices Paid" bigint,
   	"Total No. Protocol Invoices Paid" bigint
)
as $$ DECLARE 
BEGIN 
    FOR i IN array_lower(choids, 1) .. array_upper(choids, 1) + 1
        LOOP
            IF choids[i] IS NOT NULL THEN

                    RETURN QUERY

                            select

                            (select name from chorganisation cho where cho.id IN (choids[i]::int)) as "CHO Name",

                            (select 
                                    count(*) 
                            from 
                                    claim c
                            where 
                                    c.chorganisation_id IN (choids[i]::int)
                                    and c.insurer_id = insurerid
                                    and c.created_date BETWEEN startdate::date AND enddate::date) as "Total No. Claims Uploaded",

                            (select 
                                    count(*) 
                            from 
                                    claim c,
                                    invoice inv
                            where 
                                    c.invoice_id = inv.id
                                    and c.chorganisation_id IN (choids[i]::int)
                                    and c.insurer_id = insurerid
                                    and inv.created_date BETWEEN startdate::date AND enddate::date) as "Total No. Invoices Uploaded",

                            (select 
                                    count(*) 
                            from 
                                    claim c ,
                                    audit_trail a
                            where
                                    a.claim_id = c.id
                                    and c.chorganisation_id IN (choids[i]::int)
                                    and c.insurer_id = insurerid
                                    and a.new_status in ('PaymentReceived', 'InvoicePaymentLogged', 'ManualInvoicePaid')
                                    and a.reverted = false
                                    and a.created_date BETWEEN startdate::date AND enddate::date) as "Total No. Invoices Paid",

                            (select 
                                    count(*) 
                            from 
                                    claim c ,
                                    audit_trail a
                            where
                                    a.claim_id = c.id
                                    and c.chorganisation_id IN (choids[i]::int)
                                    and c.insurer_id = insurerid
                                    and c.claim_type in (0,1)
                                    and a.new_status in ('PaymentReceived', 'InvoicePaymentLogged', 'ManualInvoicePaid')
                                    and a.reverted = false
                                    and a.created_date BETWEEN startdate::date AND enddate::date) as "Total No. GTA Invoices Paid",

                            (select 
                                    count(*) 
                            from 
                                    claim c ,
                                    audit_trail a
                            where
                                    a.claim_id = c.id
                                    and c.chorganisation_id IN (choids[i]::int)
                                    and c.insurer_id = insurerid
                                    and c.claim_type not in (0,1,2,6,9,10,13,14,15,16,17,20)
                                    and a.new_status in ('PaymentReceived', 'InvoicePaymentLogged', 'ManualInvoicePaid')
                                    and a.reverted = false
                                    and a.created_date BETWEEN startdate::date AND enddate::date) as "Total No. Protocol Invoices Paid";
            ELSE

                    RETURN QUERY

                            select

                            (select 'ALL'::character varying(128)) as "CHO Name",

                            (select 
                                    count(*) 
                            from 
                                    claim c
                            where 
                                    c.chorganisation_id = ANY(choids)
                                    and c.insurer_id = insurerid
                                    and c.created_date BETWEEN startdate::date AND enddate::date) as "Total No. Claims Uploaded",

                            (select 
                                    count(*) 
                            from 
                                    claim c,
                                    invoice inv
                            where 
                                    c.invoice_id = inv.id
                                    and c.chorganisation_id = ANY(choids)
                                    and c.insurer_id = insurerid
                                    and inv.created_date BETWEEN startdate::date AND enddate::date) as "Total No. Invoices Uploaded",

                            (select 
                                    count(*) 
                            from 
                                    claim c ,
                                    audit_trail a
                            where
                                    a.claim_id = c.id
                                    and c.chorganisation_id = ANY(choids)
                                    and c.insurer_id = insurerid
                                    and a.new_status in ('PaymentReceived', 'InvoicePaymentLogged', 'ManualInvoicePaid')
                                    and a.reverted = false
                                    and a.created_date BETWEEN startdate::date AND enddate::date) as "Total No. Invoices Paid",

                            (select 
                                    count(*) 
                            from 
                                    claim c ,
                                    audit_trail a
                            where
                                    a.claim_id = c.id
                                    and c.chorganisation_id = ANY(choids)
                                    and c.insurer_id = insurerid
                                    and c.claim_type in (0,1)
                                    and a.new_status in ('PaymentReceived', 'InvoicePaymentLogged', 'ManualInvoicePaid')
                                    and a.reverted = false
                                    and a.created_date BETWEEN startdate::date AND enddate::date) as "Total No. GTA Invoices Paid",

                            (select 
                                    count(*) 
                            from 
                                    claim c ,
                                    audit_trail a
                            where
                                    a.claim_id = c.id
                                    and c.chorganisation_id = ANY(choids)
                                    and c.insurer_id = insurerid
                                    and c.claim_type not in (0,1,2,6,9,10,13,14,15,16,17,20)
                                    and a.new_status in ('PaymentReceived', 'InvoicePaymentLogged', 'ManualInvoicePaid')
                                    and a.reverted = false
                                    and a.created_date BETWEEN startdate::date AND enddate::date) as "Total No. Protocol Invoices Paid";

            END IF;


        END LOOP;
END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION weekly_dashboard(IN insurerids integer, IN choids integer[], IN startdate text, IN enddate text) TO chox_user;
GRANT EXECUTE ON FUNCTION weekly_dashboard(IN insurerids integer, IN choids integer[], IN startdate text, IN enddate text) TO chox_mi;




