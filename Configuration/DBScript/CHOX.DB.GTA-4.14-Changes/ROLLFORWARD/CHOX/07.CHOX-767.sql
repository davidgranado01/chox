--
-- CHOX-767: Create gmail scheduler jobs for claim reassignment. If an claim reassignemnt job already exists  with the same email subject, it is replaced.
-- On UAT, authorised user should be replaced with some of the following email addresses, this could be done manually:
-- John.Strawhorne@audatex.co.uk,John.Dowson@audatex.co.uk,robert.hon@audatex.co.uk,Melvin.Lai@audatex.co.uk,Joshua.Kennedy@audatex.co.uk,isabelle.lecoeuche@audatex.co.uk
-- UPDATE gmail_scheduler_job SET authorised_user = 'John.Strawhorne@audatex.co.uk,John.Dowson@audatex.co.uk,robert.hon@audatex.co.uk,Melvin.Lai@audatex.co.uk,Joshua.Kennedy@audatex.co.uk,isabelle.lecoeuche@audatex.co.uk' WHERE email_subject like '%Reassign Claim%'
-- TODO: Replace passwords with correct values for production (FIRST, SEE ALSO VUL-6871)
--


DELETE FROM  gmail_scheduler_job WHERE email_subject = 'DLG Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@dlg.com', '', 'reassignClaim', 'DLG Reassign Claim', 'lisa.thomas@directlinegroup.co.uk,Steven.Talbot@directlinegroup.co.uk,Tam.bedford@directlinegroup.co.uk',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());


DELETE FROM  gmail_scheduler_job WHERE email_subject = 'LV Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@lv.com', '', 'reassignClaim', 'LV Reassign Claim', 'andrew.seedhouse@lv.com',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());

DELETE FROM  gmail_scheduler_job WHERE email_subject = 'QBE Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@qbe.com', '', 'reassignClaim', 'QBE Reassign Claim', 'jacqueline.britton@uk.qbe.com',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());

DELETE FROM  gmail_scheduler_job WHERE email_subject = 'RSA Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@rsa.com', '', 'reassignClaim', 'RSA Reassign Claim', 'stacie.warrington@uk.rsagroup.com',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());

DELETE FROM  gmail_scheduler_job WHERE email_subject = 'Motability Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@motability.com', '', 'reassignClaim', 'Motability Reassign Claim', 'lewis.jardine@uk.rsagroup.co.uk',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());

DELETE FROM  gmail_scheduler_job WHERE email_subject = 'ERS Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@equity.com', '', 'reassignClaim', 'ERS Reassign Claim', 'jon.lee@equitygroup.co.uk',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());

DELETE FROM  gmail_scheduler_job WHERE email_subject = 'Tesco UW Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@tesco.com', '', 'reassignClaim', 'Tesco UW Reassign Claim', 'david.crown@tescobank.com',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());

DELETE FROM  gmail_scheduler_job WHERE email_subject = 'Admin Reassign Claim';
INSERT INTO gmail_scheduler_job (login_username, login_password, job_name, email_subject, authorised_user,
                                 bcc_receiver, processed_label, error_message_receiver, active, reply_to_sender,
                                 version, created_by, created_date, last_modified_by, last_modified_date)
VALUES ('admin@chox.com', '', 'reassignClaim', 'Admin Reassign Claim', 'support@valexa.com,MichaelPaul.Kemp@audatex.co.uk',
        'DL-UKBR-Audatex-penguin-reporting@audatex.com', 'Processed Reassign Claim', 'DL-UKBR-Audatex-penguin-reporting@audatex.com', 't', 't', 1, 999,
        now(), 999, now());
