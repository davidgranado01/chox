delete from audit_trail where id in (5100498, 5100556);
update claim set status='PaymentReceived', previous_status='InvoicePaymentLogged', status_modified_date='2018-02-14 14:27:18', version=version+1, last_modified_date=now() where id=517140;
delete from comment where id in (4856496, 4856497, 4856498);
delete from event_attributes where event_log_id=1565637;
delete from event_log where id=1565637;
update event_log set status='PaymentReceived' where id=1565479;

delete from audit_trail where id in (5101955, 5101956);
update audit_trail set new_status='AwaitingInvoicePayment' where id=5101939;
update claim set status='AwaitingInvoicePayment', previous_status='AwaitingLiabilityResolution', status_modified_date='2018-05-15 15:12:14', version=version+1, last_modified_date=now() where id=542551;
delete from event_attributes where event_log_id in (1568508, 1568509, 1568510);
delete from event_log where id in (1568508, 1568509, 1568510);
update event_log set status='AwaitingLiabilityResolution' where id=1568476;

delete from audit_trail where id in (5100566, 5100830);
update claim set status='AwaitingLiabilityResolution', previous_status='InvoiceApprovedByBRE', status_modified_date='2018-03-12 13:22:05', version=version+1, last_modified_date=now() where id=538905;
delete from event_attributes where event_log_id=1566251;
delete from event_log where id=1566251;
update event_log set status='AwaitingLiabilityResolution' where id=1565663;

delete from audit_trail where id in (5101786, 5101964, 5101965);
update claim set status='AwaitingInvoicePayment', previous_status='ManualInvoiceBREApproved', status_modified_date='2018-05-08 14:40:05', version=version+1, last_modified_date=now() where id=549341;
delete from event_attributes where event_log_id in (1568528, 1568529);
delete from event_log where id in (1568528, 1568529);
update event_log set status='AwaitingInvoicePayment' where id=1568146;

delete from audit_trail where id in (5100924, 5100929, 5101165);
update claim set status='ContestedInvoiceReferredToCHO', previous_status='InvoiceEscalatedToHandler', status_modified_date='2018-05-15 13:03:50', version=version+1, last_modified_date=now() where id=545267;
delete from event_attributes where event_log_id in (1566432, 1566493, 1566827);
delete from event_log where id in (1566432, 1566493, 1566827);
update event_log set status='ContestedInvoiceReferredToCHO' where id=1566424;

delete from audit_trail where id in (5100506, 5100835);
update claim set status='PaymentReceived', previous_status='InvoicePaymentLogged', status_modified_date='2018-02-28 08:59:21', version=version+1, last_modified_date=now() where id=518965;
delete from event_attributes where event_log_id=1566260;
delete from event_log where id=1566260;
update event_log set status='PaymentReceived' where id=1565505;

delete from audit_trail where id in (5099967, 5100665, 5100668, 5100673);
update claim set status='InvoiceEscalatedToHandler', previous_status='AwaitingInvoiceData', status_modified_date='2018-04-20 12:15:22', version=version+1, last_modified_date=now() where id=523275;
delete from event_attributes where event_log_id in (1565906, 1565907, 1565908, 1565912, 1565925);
delete from event_log where id in (1565906, 1565907, 1565908, 1565912, 1565925);
update event_log set status='InvoiceEscalatedToHandler' where id=1564460;

delete from audit_trail where id in (5100827, 5102476, 5102479);
update claim set status='AwaitingLiabilityResolution', previous_status='ManualInvoiceBREApproved', status_modified_date='2017-03-10 09:38:00', version=version+1, last_modified_date=now() where id=415821;
delete from comment where id=4858324;
delete from event_attributes where event_log_id in (1569893, 1569897);
delete from event_log where id in (1569893, 1569897);
update event_log set status='AwaitingLiabilityResolution' where id=1566247;

delete from audit_trail where id = 5095156;
update claim set status='AwaitingLiabilityResolution', previous_status='ManualInvoiceBREApproved', status_modified_date='2018-04-24 14:34:55', version=version+1, last_modified_date=now() where id=545518;
update event_log set status='AwaitingLiabilityResolution' where id in (1554046,1554064);
--
delete from audit_trail where id in (5095485, 5095548, 5096012);
update claim set status='ContestedInvoiceReferredToInsurer', previous_status='ContestedInvoiceReferredToCHO', status_modified_date='2018-04-30 16:51:23', version=version+1, last_modified_date=now() where id=518785;
delete from comment where id in (4851841, 4851842);
delete from event_attributes where event_log_id in (1554831, 1555400, 1555406);
delete from event_log where id in (1554831, 1555400, 1555406);
update event_log set status='ContestedInvoiceReferredToInsurer' where id in (1554753, 1554787);

delete from audit_trail where id in (5097393, 5097403);
update claim set status='AwaitingLiabilityResolution', previous_status='InvoiceApprovedByBRE', status_modified_date='2017-01-13 15:00:37', version=version+1, last_modified_date=now() where id=368240;
update comment set reverted=true where id in (4853350, 4853351);
delete from event_attributes where event_log_id in (1558431);
delete from event_log where id in (1558431);
update event_log set status='AwaitingLiabilityResolution' where id in (1558413);


delete from audit_trail where id in (5100713, 5100716, 5100615, 5100599);
update claim set status='AwaitingInvoicePayment', previous_status='InvoiceApprovedByBRE', status_modified_date='2018-05-04 09:33:01', version=version+1, last_modified_date=now() where id=546695;
delete from event_attributes where event_log_id in (1565791, 1566014);
delete from event_log where id in (1565791, 1566014);
update event_log set status='AwaitingInvoicePayment' where id in (1565750, 1566010);

delete from audit_trail where id in (5100922, 5100920);
update claim set status='ContestedInvoiceReferredToInsurer', previous_status='ContestedInvoiceReferredToCHO', status_modified_date='2017-10-25 12:41:52', version=version+1, last_modified_date=now() where id=485364;
delete from event_attributes where event_log_id in (1566421);
delete from event_log where id in (1566421);
update event_log set status='ContestedInvoiceReferredToInsurer' where id in (1566418);

delete from audit_trail where id in (5099972, 5099975);
update claim set status='AwaitingLiabilityResolution', previous_status='ManualInvoiceBREApproved', status_modified_date='2018-04-17 14:50:13', version=version+1, last_modified_date=now() where id=549340;
delete from event_attributes where event_log_id in (1564479);
delete from event_log where id in (1564479);
update event_log set status='AwaitingLiabilityResolution' where id in (1564474);

delete from audit_trail where id in (5099945);
update claim set status='InvoiceEscalatedToHandler', previous_status='AwaitingInvoiceData', status_modified_date='2018-05-11 08:55:22', version=version+1, last_modified_date=now() where id=557858;
update event_log set status='InvoiceEscalatedToHandler' where id in (1564412,1564415,1564437,1568562,1568662);

delete from audit_trail where id in (5102131, 5102132);
update claim set status='InvoiceApprovedByBRE', previous_status='AwaitingInvoiceData', status_modified_date='2018-02-27 15:55:37', version=version+1, last_modified_date=now() where id=495625;
delete from event_attributes where event_log_id in (1568852, 1568853);
delete from event_log where id in (1568852, 1568853);
update event_log set status='InvoiceApprovedByBRE' where id in (1568851);
