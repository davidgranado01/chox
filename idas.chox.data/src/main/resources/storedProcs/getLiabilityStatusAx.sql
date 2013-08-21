-- Function: getLiabilityStatusAx(integer)

-- DROP FUNCTION getLiabilityStatusAx(integer);

CREATE OR REPLACE FUNCTION getLiabilityStatusAx(liabilityid integer)
  RETURNS text AS
$BODY$ 
 DECLARE
   resultString text;
 BEGIN  
       IF liabilityId = 0 THEN       
         resultString = 'Not Report';
       ELSIF liabilityId = 1 THEN       
          resultString = 'Agreed';
       ELSIF liabilityId = 2 THEN       
          resultString = 'Disputed';
       ELSIF liabilityId = 3 THEN       
          resultString = 'Not Known';
       ELSIF liabilityId = 4 THEN       
          resultString = 'Disputed';
       ELSIF liabilityId = 5 THEN       
          resultString = 'Disputed';
       ELSIF liabilityId = 6 THEN       
          resultString = 'Agreed';
       ELSE
          resultString = 'Not Report';
      END IF;
   RETURN resultString;
 END; 
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getLiabilityStatusAx(integer)
  OWNER TO chox;
