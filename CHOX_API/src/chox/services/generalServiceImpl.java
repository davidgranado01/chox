package chox.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class generalServiceImpl {

    public static java.sql.Timestamp getCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(sdf.format(cal.getTime()));
    } 
}
