
DROP FUNCTION getClaimTypeByName(text);

CREATE OR REPLACE FUNCTION getClaimTypeByName(claimTypeName text)
  RETURNS INTEGER[] AS
$BODY$ 
 DECLARE
   resultString INTEGER[];
 BEGIN  
    IF $1 ILIKE 'GTA'  THEN       
         resultString = ARRAY[0,1,2];
    ELSIF $1 ILIKE 'TPI' THEN       
         resultString = ARRAY[3];
    ELSIF $1 ILIKE 'INSURER_VS_INSURER' THEN       
         resultString = ARRAY[4,5,6];
    ELSIF $1 ILIKE 'SUBSCRIBER' THEN       
         resultString = ARRAY[7,8,9];
    ELSIF ($1 ILIKE 'INSURER' OR $1 ILIKE 'MANUAL') THEN       
         resultString = ARRAY[10,14,15,16,17];
    ELSIF $1 ILIKE 'FIXED_FEE' THEN       
         resultString = ARRAY[11,12,13];
    ELSIF $1 ILIKE 'COLLABORATION_PROTOCOL' THEN       
         resultString = ARRAY[18,19,20];
    ELSE
         resultString = ARRAY[]::integer[];
    END IF;
        RETURN resultString;
 END; 
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

