/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Carlson
 */
public class DateHelper {

    public static Date getDateFromString(String inp) {

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return df.parse(inp);
        } catch (Exception e) {

            return null;
        }

    }
    
    public static int getDayOfWeek(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
        
    }

     public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static Date setDateOnly(Date date){
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }
    
}
