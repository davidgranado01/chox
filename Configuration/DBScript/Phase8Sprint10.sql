--------------------------------------------------------------------------------
-- bug#3601 - Production - incorrect audit trail for manual invoices
--------------------------------------------------------------------------------
update audit_trail
  set original_status='AwaitingInvoiceData', version=version+1
where original_status='InvoiceApprovedByBRE' and new_status='ManualInvoiceBREApproved';
----------------------
-- End of bug#3601
----------------------
