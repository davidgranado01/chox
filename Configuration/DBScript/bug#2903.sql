update claim
  set liability_status = orig.liability_status
from claim orig, customer orig_customer, customer supp_customer
where orig.customer_id = orig_customer.id
  and claim.customer_id = supp_customer.id
  and orig_customer.claim_reference = supp_customer.claim_reference
  and ((orig.claim_type = 1 and claim.claim_type = 2)
    or (orig.claim_type = 5 and claim.claim_type = 6)
    or (orig.claim_type = 8 and claim.claim_type = 9)
    or (orig.claim_type = 12 and claim.claim_type = 13)
    or (orig.claim_type = 15 and claim.claim_type = 16)
    or (orig.claim_type = 19 and claim.claim_type = 20))
  and claim.liability_agreed_date = orig.liability_agreed_date
  and claim.liability_status != orig.liability_status
  and claim.liability_status = 0;

