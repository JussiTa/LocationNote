package com.example.jti.locationnote.datamanagement;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jti on 3/5/18.
 */

public class TimeManager {
    private String[] splittedFileRow;

    /**
         * @return DD:HH:mm
         */
        public String getCurrentTimeStamp() {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM_dd_HH_mm");
                String currentTimeStamp = dateFormat.format(new Date());
                return currentTimeStamp;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    /*

    On this method checked currenttime and savedtime if they are same day or future  .
     but not yesterday or older.
     */

        public boolean compareTime(String data){
            splittedFileRow = new String[4];
            splittedFileRow = data.split("-");
            String currentTime = getCurrentTimeStamp();
            String fileTime;
            fileTime = splittedFileRow[3];

            String[] cT = new String[4];
            cT= currentTime.split("_");
            String[] fT = new String[4];
            fT =  fileTime.split("_");
            int month;
            int day;
            int hour;
            int min;

            month = Integer.parseInt(cT[0])-Integer.parseInt(fT[0]);
            day = Integer.parseInt(cT[1])-Integer.parseInt(fT[1]);
            hour = Integer.parseInt(cT[2])- Integer.parseInt(fT[2]);
            min = Integer.parseInt(cT[3])-Integer.parseInt(fT[3]);
            if (month ==0 && day>=1 && hour >=1 &&min >=2)
                return true;

            return false;
        }


    public String[] getLatitudeAndLongitude() {
         return splittedFileRow;
        }
}





