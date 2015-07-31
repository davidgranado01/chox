-- Function: getMainClaimType(integer)

DROP FUNCTION getMainClaimType(integer);

CREATE OR REPLACE FUNCTION getMainClaimType(claimTypeId integer)
  RETURNS integer AS
$BODY$
 DECLARE
   resultString integer;
 BEGIN
    IF $1 IN (0,1,2) THEN
         resultString = 0;
    ELSIF $1 IN (3) THEN
         resultString = 3;
    ELSIF $1 IN (4,5,6) THEN
         resultString = 4;
    ELSIF $1 IN (7,8,9) THEN
         resultString = 7;
    ELSIF $1 IN (10,14,15,16,17) THEN
         resultString = 10;
    ELSIF $1 IN (11,12,13) THEN
         resultString = 11;
    ELSIF $1 IN (18,19,20) THEN
         resultString = 18;
    ELSE
         resultString = '';
    END IF;
        RETURN resultString;
 END;
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION getMainClaimType(integer) TO chox_user;
