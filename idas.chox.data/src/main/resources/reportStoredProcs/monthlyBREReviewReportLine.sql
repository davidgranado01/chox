DROP FUNCTION monthlyBREReviewReportLine(IN start_date text, IN end_date text, IN claimTypes integer[], IN choIds  integer[], IN insIds integer[]);

CREATE OR REPLACE FUNCTION monthlyBREReviewReportLine(IN start_date text, IN end_date text, IN claimTypes integer[], IN choIds  integer[], IN insIds integer[])
  RETURNS boolean AS
  $BODY$
DECLARE
    -- total months between given two dates (this produces 1 count less because this do not include the start month)
    total_months INTEGER = ((extract( year FROM end_date::date ) - extract( year FROM start_date::date )) *12) + extract(MONTH FROM end_date::date ) - extract(MONTH FROM start_date::date);
    -- total number of year between given two dates (this produces 1 count less because this do not include the start year)
    total_year INTEGER = extract( year FROM end_date::date ) - extract( year FROM start_date::date );
    -- change the date of the given start date to the 1st day of the month
    exact_start_date timestamp without time zone = (to_date(to_char(start_date::date, 'MM') || '-01-' || to_char(start_date::date, 'yyyy'), 'mm-dd-yyyy'))::timestamp without time zone;
    -- change the date of the given end date to the last day of the month
    exact_end_date timestamp without time zone = date_trunc('month', end_date::date) + '1month'::interval - '1sec'::interval;
    -- Add 2(1 for month and 1 for year).  
    total_number_of_rows  INTEGER = total_months + total_year + 2;

    -- this is the start date of each month
    temp_month_start_date timestamp without time zone;
    -- this is the end date of the each month
    temp_month_end_date timestamp without time zone;
    -- this is the start date of the each year
    temp_year_start_date timestamp without time zone;
--    temp_year_end_date timestamp without time zone;
    -- this is used for temporay date storage 
    temp_date timestamp without time zone;
    -- this array contains single entry of start date and end date which we use to generate report
    temp_month_array   timestamp without time zone[];
    -- this array contain all the months(start date and end date) break down by year and cumulative dates
    temp_cumulative_array timestamp without time zone[];
    -- this variable is used for the logic
    updated boolean = false;
    label text;
BEGIN
      FOR i IN 1..total_number_of_rows LOOP -- loop through each row
      	IF i = 1 THEN -- the below assignment executed only once
              temp_month_start_date = exact_start_date;
      	    temp_month_end_date = temp_month_start_date + INTERVAL '1 month' - INTERVAL '1sec';
      	    temp_year_start_date = temp_month_start_date;
      	ELSE -- the below code is executed for rest of the rows(except first row)
      	     IF extract( year FROM (temp_month_start_date + INTERVAL '1 month') ) > extract( year FROM temp_month_start_date ) AND updated = FALSE THEN -- the below assingment executed for each next year(to get cumulative dates)
      	        updated = TRUE;
      	        temp_date = temp_month_start_date;
      	        temp_month_end_date = temp_month_start_date + INTERVAL '1 month' - INTERVAL '1sec';
      	        temp_month_start_date = temp_year_start_date;
      	        temp_year_start_date = temp_date + INTERVAL '1 month';
      	     ELSE -- if it is not cumulative then the below code executed
      	        updated = FALSE;
      	        IF temp_month_end_date = exact_end_date THEN -- if the given end date is equal to the calculated end date then get final cumulative dates
      	            temp_month_start_date = temp_year_start_date;
      	        ELSE -- get the start date and end date for each month
      	            temp_month_start_date = temp_month_start_date + INTERVAL '1 month';
      	            temp_month_end_date = temp_month_start_date + INTERVAL '1 month' - INTERVAL '1sec';
      	        END IF;
      	     END IF;
      	END IF;
        -- insert calculated start date and end date into an temporary array
        temp_cumulative_array:= array_cat(temp_cumulative_array, array[[temp_month_start_date, temp_month_end_date]]::timestamp without time zone[]);
      	
        IF updated = TRUE THEN -- month start date set to the start date of the year to get cumulative dates. Now change back to the previous value this variable had
      	temp_month_start_date = temp_date; 
        END IF;
      END LOOP;
    FOREACH temp_month_array SLICE 1 IN ARRAY temp_cumulative_array -- Now loop through each month and run the query
    LOOP
        IF temp_month_array[1] + INTERVAL '1 month' - INTERVAL '1sec' = temp_month_array[2] THEN 
            label = to_char(temp_month_array[1], 'TMMonth') || ' - ' || to_char(temp_month_array[1] , 'yyyy');
        ELSE -- the cumulative lable is not working correctly if the month and year of the given start_date and end_date is same(e.g. '2013-02-01', '2013-02-03'). Need a workaround. I did not do the workaround as no time left.
            label = to_char(temp_month_array[1], 'yyyy') || ' - ' || 'Cumulative';
        END IF;

        -- RETURN QUERY
        -- All 27 query need to be written here. Return all the column as table(change the SP return structure at the moment it returns boolean).  
       
        
        RAISE NOTICE 'Label: % From: %, To: %',label, temp_month_array[1], temp_month_array[2];

        
    END LOOP;
    return true;
END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION monthlyBREReviewReportLine(IN start_date text, IN end_date text, IN claimTypes integer[], IN choids  integer[], IN insIds integer[])
  OWNER TO chox;