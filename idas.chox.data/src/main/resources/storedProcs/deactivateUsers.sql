CREATE OR REPLACE FUNCTION deactivateUsers(claimAge integer)
    RETURNS void AS
$BODY$
DECLARE
    cutOff date;
    BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;

        update web_user
            set status = false, deactivated_date = now()
        where status = true and last_login_date < cutOff
          and user_name not like 'admin@%' and user_name not in ('ecdupdate@erac.com','admin@dlg.com','admin@chox.com','erac_scheduler','admin@motability.com','admin@manualhelphire.com','admin@hertz.com','admin@rsa.com','admin@equity.com','admin@qbe.com','admin@tesco.com','easidrive_scheduler','admin@lv.com','ecdupdate@aiclaimssolutions.com','paidInvoices.rsa','paidinvoices.qbe','system','acknowledgeclaims.dlg','erac_scheduler','claimMatcher.lv','paidInvoices.lv');

        update web_user
            set status = false, deactivated_date = now()
        where status = true and last_login_date is null and created_date < cutOff
          and user_name not like 'admin@%' and user_name not in ('ecdupdate@erac.com','admin@dlg.com','admin@chox.com','erac_scheduler','admin@motability.com','admin@manualhelphire.com','admin@hertz.com','admin@rsa.com','admin@equity.com','admin@qbe.com','admin@tesco.com','easidrive_scheduler','admin@lv.com','ecdupdate@aiclaimssolutions.com','paidInvoices.rsa','paidinvoices.qbe','system','acknowledgeclaims.dlg','erac_scheduler','claimMatcher.lv','paidInvoices.lv');
    END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION deactivateUsers(integer) to chox_user;
