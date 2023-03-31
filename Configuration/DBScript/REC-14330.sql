DELETE FROM  gmail_scheduler_job WHERE email_subject = 'DLG Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@dlg.com', '', 'reassignClaim', 'DLG Reassign Claim', 'lisa.thomas@directlinegroup.co.uk,Steven.Talbot@directlinegroup.co.uk,Tam.bedford@directlinegroup.co.uk,uac.requests@directlinegroup.co.uk',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());