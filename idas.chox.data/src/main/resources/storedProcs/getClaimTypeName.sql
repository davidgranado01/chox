-- Function: getclaimtype(integer)

DROP FUNCTION getClaimTypeName(integer);

CREATE OR REPLACE FUNCTION getClaimTypeName(claimTypeId integer)
  RETURNS text AS
$BODY$ 
 DECLARE
   resultString text;
 BEGIN  
    IF $1 = 0 THEN       
         resultString = 'GTA';
    ELSIF $1 = 1 THEN       
         resultString = 'GTA (Orig. Invoice)';
    ELSIF $1 = 2 THEN       
         resultString = 'GTA (Supp. Invoice)';
    ELSIF $1 = 3 THEN       
         resultString = 'Third Party Intervention (TPI)';
    ELSIF $1 = 4 THEN       
         resultString = 'Insurer vs. Insurer';
    ELSIF $1 = 5 THEN       
         resultString = 'Insurer vs. Insurer (Orig. Invoice)';
    ELSIF $1 = 6 THEN       
         resultString = 'Insurer vs. Insurer (Supp. Invoice)';
    ELSIF $1 = 7 THEN       
         resultString = 'Subscriber';
    ELSIF $1 = 8 THEN       
         resultString = 'Subscriber (Orig. Invoice)';
    ELSIF $1 = 9 THEN       
         resultString = 'Subscriber (Supp. Invoice)';
    ELSIF $1 = 10 THEN       
         resultString = 'Insurer Invoice';
    ELSIF $1 = 11 THEN       
         resultString = 'Fixed Fee';
    ELSIF $1 = 12 THEN       
         resultString = 'Fixed Fee (Orig. Invoice)';
    ELSIF $1 = 13 THEN       
         resultString = 'Fixed Fee (Supp. Invoice)';
    ELSIF $1 = 14 THEN       
         resultString = 'Insurer Claim';
    ELSIF $1 = 15 THEN       
         resultString = 'Insurer Claim (Orig. Invoice)';
    ELSIF $1 = 16 THEN       
         resultString = 'Insurer Claim (Supp. Invoice)';
    ELSIF $1 = 17 THEN       
         resultString = 'Insurer Upload';
    ELSIF $1 = 18 THEN       
         resultString = 'Collaboration Protocol';
    ELSIF $1 = 19 THEN       
         resultString = 'Collaboration Protocol (Orig. Invoice)';
    ELSIF $1 = 20 THEN       
         resultString = 'Collaboration Protocol (Supp. Invoice)';
    ELSE
         resultString = '';
    END IF;
        RETURN resultString;
 END; 
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION getClaimTypeName(integer)
  OWNER TO chox;
