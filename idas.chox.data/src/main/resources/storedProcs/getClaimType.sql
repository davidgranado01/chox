-- Function: getClaimType(integer)

DROP FUNCTION getClaimType(integer);

CREATE OR REPLACE FUNCTION getClaimType(claimTypeId integer)
  RETURNS text AS
$BODY$ 
 DECLARE
   resultString text;
 BEGIN  
    IF $1 IN (0,1,2) THEN       
         resultString = 'GTA';
    ELSIF $1 IN (3) THEN       
         resultString = 'TPI';
    ELSIF $1 IN (4,5,6) THEN       
         resultString = 'INSURER_VS_INSURER';
    ELSIF $1 IN (7,8,9) THEN       
         resultString = 'SUBSCRIBER';
    ELSIF $1 IN (10,14,15,16,17) THEN       
         resultString = 'INSURER';
    ELSIF $1 IN (11,12,13) THEN       
         resultString = 'FIXED_FEE';
    ELSE
         resultString = '';
    END IF;
        RETURN resultString;
 END; 
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

