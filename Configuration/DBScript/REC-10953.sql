--turn off rsa scheduler job
UPDATE scheduler_job SET active = false WHERE login_username = 'paidInvoices.rsa';