--
-- VUL-6842 (CHOX): Replacing email addresses from john Dowson to team email address in the gmail_scheduler_job table


UPDATE gmail_scheduler_job  SET  bcc_receiver='DL-UKBR-Audatex-penguin-reporting@audatex.com' , error_message_receiver='DL-UKBR-Audatex-penguin-reporting@audatex.com';



