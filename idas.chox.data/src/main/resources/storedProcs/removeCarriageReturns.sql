-- Drop removeCarriageReturns(string varchar)

CREATE OR REPLACE FUNCTION removeCarriageReturns(string varchar)
    RETURNS varchar AS
$BODY$
DECLARE
    resultString varchar;
BEGIN
    resultString = regexp_replace(string, '[\n\r\s]+', ' ', 'g' )::varchar;
    RETURN resultString;
END;
$BODY$
    LANGUAGE plpgsql VOLATILE
                     COST 100;
ALTER FUNCTION removeCarriageReturns(varchar)
    OWNER TO chox;
