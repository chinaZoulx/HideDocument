package com.chris.hidedocument.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/3/25.
 */
public class FormatUtils {

    /**
     * format date 1277106667 => 2010-06-21 15:51:07
     *
     * @param l
     * @return
     */
    public static String getStandardDate(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(l * 1000));
        return date;
    }

    public static String getStandardDate(long l,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String sd = sdf.format(new Date(l));
        return sd;
    }

    public static Date getStandardDate(String dateStr,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date date=new Date();
        try {
            date=sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
