-- Function: getLiabilityStatus(integer)

-- DROP FUNCTION getLiabilityStatus(integer);

CREATE OR REPLACE FUNCTION getLiabilityStatus(liabilityid integer)
  RETURNS text AS
$BODY$ 
 DECLARE
   resultString text;
 BEGIN  
       IF liabilityId = 0 THEN       
         resultString = '';
       ELSIF liabilityId = 1 THEN       
          resultString = 'Full Liability Accepted';
       ELSIF liabilityId = 2 THEN       
          resultString = 'Liability In Negotiation';
       ELSIF liabilityId = 3 THEN       
          resultString = 'Liability Unknown';
       ELSIF liabilityId = 4 THEN       
          resultString = 'Liability Repudiated';
       ELSIF liabilityId = 5 THEN       
          resultString = 'Liability Split';
       ELSIF liabilityId = 6 THEN       
          resultString = 'Proceed Without Prejudice';
       ELSE
          resultString = '';
      END IF;
   RETURN resultString;
 END; 
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getLiabilityStatus(integer)
  OWNER TO chox;
