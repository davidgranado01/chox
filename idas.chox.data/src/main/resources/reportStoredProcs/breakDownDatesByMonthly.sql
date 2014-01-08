-- DROP FUNCTION breakDownDatesByMonthly(IN start_date text, IN end_date text);


CREATE OR REPLACE FUNCTION breakDownDatesByMonthly(
    IN start_date text, 
    IN end_date text)

RETURNS TABLE (
    month_label text, 
    month_start_date timestamp without time zone, 
    month_end_date timestamp without time zone) 
AS

$BODY$
DECLARE
    -- total months between given two dates (this produces 1 count less)
    total_months INTEGER = ((extract( year FROM end_date::date ) - extract( year FROM start_date::date )) *12) + extract(MONTH FROM end_date::date ) - extract(MONTH FROM start_date::date);
    -- total number of year between given two dates (this produces 1 count less)
    total_year INTEGER = extract( year FROM end_date::date ) - extract( year FROM start_date::date );
    -- change the date of the given start_date to 1st of the month
    exact_start_date timestamp without time zone = (to_date(to_char(start_date::date, 'MM') || '-01-' || to_char(start_date::date, 'yyyy'), 'mm-dd-yyyy'))::timestamp without time zone;
    -- change the date of the given end_date to last day of the month
    exact_end_date timestamp without time zone = date_trunc('month', end_date::date) + '1month'::interval - '1sec'::interval;
    -- Add 2(1 for month and 1 for year).  
    total_number_of_rows  INTEGER = total_months + total_year + 2;

    -- this is the start date of each month
    temp_month_start_date timestamp without time zone;
    -- this is the end date of the each month
    temp_month_end_date timestamp without time zone;
    -- this is used to store the cumulative start date which is used to differenciate between cumulative lable and months label.
    temp_cumulative_start_date timestamp without time zone = NULL;
    -- this is the start date of the each year
    temp_year_start_date timestamp without time zone;
    -- this is used for temporay date storage 
    temp_date timestamp without time zone;
    -- this array contains single entry of start date and end date which is used to generate the report
    temp_month_array   timestamp without time zone[];
    -- this array stores all the break down dates(start date and end date)
    temp_cumulative_array timestamp without time zone[];
    label text;
    
BEGIN
      IF age(exact_end_date, exact_start_date) > interval '0 sec' THEN
       
	      FOR i IN 1..total_number_of_rows LOOP -- loop through each row
	      	IF i = 1 THEN -- the below assignment executed only once
	              temp_month_start_date = exact_start_date;
	      	    temp_month_end_date = temp_month_start_date + INTERVAL '1 month' - INTERVAL '1sec';
	      	    temp_year_start_date = temp_month_start_date;
	      	ELSE -- the below code is executed for rest of the rows(except first row)
	      	     IF extract( year FROM (temp_month_start_date + INTERVAL '1 month') ) > extract( year FROM temp_month_start_date ) AND temp_cumulative_start_date IS NULL THEN -- the below assingment executed for each next year(to get cumulative dates)
	      	        temp_date = temp_month_start_date;
	      	        temp_month_end_date = temp_month_start_date + INTERVAL '1 month' - INTERVAL '1sec';
	      	        temp_month_start_date = temp_year_start_date;
	      	        temp_cumulative_start_date = temp_month_start_date;
	      	        temp_year_start_date = temp_date + INTERVAL '1 month';
	      	     ELSE -- if it is not cumulative then the below code executed
	                  temp_cumulative_start_date = NULL;
	      	        IF temp_month_end_date = exact_end_date THEN -- if the given end date is equal to the calculated end date then get final cumulative dates
	      	            temp_month_start_date = temp_year_start_date;
	      	            temp_cumulative_start_date = temp_month_start_date;
	      	        ELSE -- get the start date and end date for each month
	      	            temp_month_start_date = temp_month_start_date + INTERVAL '1 month';
	      	            temp_month_end_date = temp_month_start_date + INTERVAL '1 month' - INTERVAL '1sec';
	      	        END IF;
	      	     END IF;
	      	END IF;
	        -- insert calculated start date and end date into an temporary array
	        temp_cumulative_array:= array_cat(temp_cumulative_array, array[[temp_month_start_date, temp_month_end_date, temp_cumulative_start_date]]::timestamp without time zone[]);
	      	
	        IF temp_cumulative_start_date IS NOT NULL THEN -- month start date set to the start date of the year to get cumulative dates. Now change back to the previous value this variable had
	      	temp_month_start_date = temp_date; 
	        END IF;
	      END LOOP;

	      FOREACH temp_month_array SLICE 1 IN ARRAY temp_cumulative_array LOOP -- Now loop through each month and run the query
	        IF temp_month_array[3] IS NULL OR temp_month_array[1] != temp_month_array[3] THEN 
	            label = to_char(temp_month_array[1], 'TMMonth') || ' - ' || to_char(temp_month_array[1] , 'yyyy');
	        ELSE 
	            label = to_char(temp_month_array[1], 'yyyy') || ' - ' || 'Cumulative';
	        END IF;

	        RETURN QUERY
	        	SELECT label , temp_month_array[1], temp_month_array[2];     
	       
	      END LOOP;
      ELSE
          RAISE NOTICE 'START_DATE: % CAN NOT BE AFTER THE END_DATE: %. PLEASE CHECK THE DATES.', start_date, end_date;
          RETURN;
      END IF;
END;

$BODY$

LANGUAGE plpgsql VOLATILE COST 100;

GRANT EXECUTE ON FUNCTION breakDownDatesByMonthly(IN start_date text, IN end_date text) TO chox_user;
GRANT EXECUTE ON FUNCTION breakDownDatesByMonthly(IN start_date text, IN end_date text) TO chox_mi;
