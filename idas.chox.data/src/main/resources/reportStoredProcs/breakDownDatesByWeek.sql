CREATE OR REPLACE FUNCTION breakDownDatesByWeek(IN numberOfWeeks integer)
RETURNS TABLE (
    week_label varchar,
    week_start date,
    week_end date)
AS

$BODY$
    DECLARE
    today date;
    dayOfWeek integer;
    dayDifference integer;
    startDate date;
    endDate date;
    temp_cumulative_array date[];
    BEGIN
        -- determine start and end of first week
        today = now()::date;
        dayOfWeek = extract(dow from today);
        dayDifference = case when dayOfWeek=0 then 0 else 7-dayOfWeek end;
        endDate = today + interval '1 day'*dayDifference;
        startDate = endDate - interval '6 days';
        temp_cumulative_array:= array_cat(temp_cumulative_array, array[[startDate, endDate]]::date[]);

        FOR i IN 1 .. (numberOfWeeks-1) LOOP
            startDate = startDate - interval '7 days';
            endDate = endDate - interval '7 days';
            temp_cumulative_array:= array_cat(temp_cumulative_array, array[[startDate, endDate]]::date[]);
        END LOOP;

        FOR i IN 1 .. numberOfWeeks LOOP
            RETURN QUERY
                SELECT ('Week ' || i)::varchar, temp_cumulative_array[i][1], temp_cumulative_array[i][2];
        END LOOP;
    END;

$BODY$
    LANGUAGE plpgsql VOLATILE COST 100;

GRANT EXECUTE ON FUNCTION breakDownDatesByWeek(IN numberOfWeeks integer) TO chox_user;
GRANT EXECUTE ON FUNCTION breakDownDatesByWeek(IN numberOfWeeks integer) TO chox_mi;
