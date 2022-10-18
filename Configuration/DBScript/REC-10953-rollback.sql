DO $pendingrollback$
BEGIN
--turn off rsa scheduler job
UPDATE scheduler_job SET active = true WHERE login_username = 'paidInvoices.rsa';
END $pendingrollback$