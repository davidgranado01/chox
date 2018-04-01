--GDPR getHash function
--    Need to install crypto extension: CREATE EXTENSION pgcrypto;

CREATE OR REPLACE FUNCTION getHash(stringToHash text, size int)
  RETURNS text AS
$BODY$
 DECLARE
    normalisedString text;
    fullHashedString text;
    annotatedHashedString text;
    startString text;

 BEGIN
    -- if string already hashed, just return it
    startString = substring(stringToHash from 1 for 2);
    IF startString = '~~' THEN
      RETURN stringToHash;
    END IF;
    -- NORMALIZE: make all characters capitals and replace spaces with underscores
    normalisedString = upper(replace(stringToHash, ' ', '_'));

    -- HASH: use sha256
    fullHashedString = encode(digest(normalisedString, 'sha256'::text), 'hex');

    -- truncate to size-2 and annotate/prepend with marker '~~'
    annotatedHashedString = '~~' || substring(fullHashedString from 1 for size-2);

   RETURN annotatedHashedString;
 END;
 $BODY$
  LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION getHash(text, int) to chox_user;
