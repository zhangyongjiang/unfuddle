package com.gaoshin.android;
/**
 * @description Utility class containing static functions for date/time, phone number formatting related functions
 * @author Abbhijeet Deshpande & Ankur Agarwal
 * @date 15-Apr-2011
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DialerUtils {
	
	/**
	 * This function accepts time in long and returns a formatted date depending
	 * upon the difference with current time as given below. Difference less
	 * than 2 seconds --> "Right now" Difference greater than 2 seconds but less
	 * than 2 minutes --> "Just now" Difference greater than 2 minutes but less
	 * than 24 hours --> "Today, 04:51am" Difference greater than 24 hours -->
	 * "Wednesday, 03:51pm" Difference greater than a week -->
	 * "Fri, Apr-15 04:25pm"
	 * 
	 * @param receivedTime
	 *            - The time which will be converted to date format after
	 *            comparison with current time.
	 * @return the formatted String
	 */

	public static final long DAYINSECONDS = 86400l;
    public static final long YEAR = 31536000000l;
	public static final long DAY = 86400000l;
    public static final long FIVE_HOUR = 18000000l;
    public static final long HOUR = 3600000l;
    public static final long HALF_HOUR = 1800000l;
    public static final long TEN_MINUTE = 600000l;
    public static final long FIVE_MINUTE = 300000l;
    public static final long MINUTE = 60000l;
    
	
	public static final String TODAY = "Today";
	public static final String YESTERDAY = "Yesterday";
	
	public static String getDateTimeString(long receivedTime) {
		StringBuilder timeString = new StringBuilder(40);
	
		Calendar systemTimeCal = Calendar.getInstance();
		
		long systemTimeInSec = System.currentTimeMillis() / 1000;
		long receivedTimeInSec = receivedTime / 1000;
		long secondsDiff = systemTimeInSec - receivedTimeInSec;
		
		long todaySeconds =(systemTimeCal.get(Calendar.HOUR_OF_DAY) * 3600) +
						(systemTimeCal.get(Calendar.MINUTE) * 60) +
						(systemTimeCal.get(Calendar.SECOND));
		
		long year = systemTimeInSec % (YEAR/1000);

		systemTimeCal.setTimeInMillis(receivedTime);
		if (secondsDiff < todaySeconds) {
			timeString.append(TODAY+" "+formatTime(systemTimeCal));
		}
		else if (secondsDiff >= todaySeconds && secondsDiff < todaySeconds + DAYINSECONDS) {
			timeString.append(YESTERDAY+" "+formatTime(systemTimeCal));
		}
		else if (secondsDiff >= todaySeconds + DAYINSECONDS && secondsDiff < todaySeconds + 7*DAYINSECONDS) {
			timeString.append(getDayString(systemTimeCal)+formatTime(systemTimeCal));
		}
		else if(secondsDiff >= todaySeconds + 7*DAYINSECONDS && secondsDiff< systemTimeInSec - year){
			timeString.append(formatData(systemTimeCal)+formatTime(systemTimeCal));
		}
		else {
			timeString.append(formatYearData(systemTimeCal));
		}

		return timeString.toString();
	}
	
	private static String formatData(Calendar systemTimeCal){
		long date = systemTimeCal.getTimeInMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM d, ");
		return sdf.format(new Date(date));
	}
	
	private static String formatTime(Calendar systemTimeCal){
		long date = systemTimeCal.getTimeInMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mmaa");
		return sdf.format(new Date(date)).toLowerCase();
	}
	
	private static String formatYearData(Calendar systemTimeCal){
		long date = systemTimeCal.getTimeInMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		return sdf.format(new Date(date));
	}
	

	/**
	 * Private function which takes Calendar object as input and returns the day
	 * string
	 * @param systemTimeCal - the Calendar object
	 * @return - corresponding day String
	 */

	private static String getDayString(Calendar systemTimeCal) {
		String dayString = null;
		int day = systemTimeCal.get(Calendar.DAY_OF_WEEK)-1;
		switch (day) {
		case 0:
			dayString = "Sunday";
			break;
		case 1:
			dayString = "Monday";
			break;
		case 2:
			dayString = "Tuesday";
			break;
		case 3:
			dayString = "Wednesday";
			break;
		case 4:
			dayString = "Thursday";
			break;
		case 5:
			dayString = "Friday";
			break;
		case 6:
			dayString = "Saturday";
			break;
		}
		return (dayString+ " ");
	}
	
	
	private static String getShortDayString(Calendar systemTimeCal) {
		String dayString = null;
		int day = systemTimeCal.get(Calendar.DAY_OF_WEEK)-1;
		switch (day) {
		case 0:
			dayString = "Sun";
			break;
		case 1:
			dayString = "Mon";
			break;
		case 2:
			dayString = "Tues";
			break;
		case 3:
			dayString = "Wed";
			break;
		case 4:
			dayString = "Thu";
			break;
		case 5:
			dayString = "Fri";
			break;
		case 6:
			dayString = "Sat";
			break;
		}
		return (dayString+ " ");
	}

	/**
	 * Private function which takes Calendar object as input and returns
	 * corresponding short month and date string in the format "Apr-15 "
	 * @param systemTimeCal - Calendar object
	 * @return shortMonthDateString - corresponding month date string
	 */

	public static String getShortMonthDateString(Calendar systemTimeCal) {
		String shortMonthDateString = null;
		int month = systemTimeCal.get(Calendar.MONTH);
		switch (month) {
		case 0:
			shortMonthDateString = "Jan";
			break;
		case 1:
			shortMonthDateString = "Feb";
			break;
		case 2:
			shortMonthDateString = "Mar";
			break;
		case 3:
			shortMonthDateString = "Apr";
			break;
		case 4:
			shortMonthDateString = "May";
			break;
		case 5:
			shortMonthDateString = "Jun";
			break;
		case 6:
			shortMonthDateString = "Jul";
			break;
		case 7:
			shortMonthDateString = "Aug";
			break;
		case 8:
			shortMonthDateString = "Sep";
			break;
		case 9:
			shortMonthDateString = "Oct";
			break;
		case 10:
			shortMonthDateString = "Nov";
			break;
		case 11:
			shortMonthDateString = "Dec";
			break;
		}

		return (shortMonthDateString + "-" + systemTimeCal.get(Calendar.DAY_OF_MONTH) + " ");
	}

	/**
	 * Private function which takes Calendar object and returns corresponding
	 * hour-minutes string This function generates the hour minutes string in
	 * the format "04:51am" 
	 * @param systemTimeCal- Calendar object
	 * @return hourTimeString - corresponding hour minutes string
	 */

	public static String getHourTimeString(Calendar systemTimeCal) {

		int hr = systemTimeCal.get(Calendar.HOUR);
		String hour = (hr < 10 ?"0":"") + hr;
		

		int min = systemTimeCal.get(Calendar.MINUTE);
		String minutes = (min < 10 ? "0":"")+min;

		int am_pm = systemTimeCal.get(Calendar.AM_PM);
		String amPm = (am_pm == 1 ? "pm":"am");

		return hour + ":" + minutes + amPm;
	}
	
	
	/**
	 * Private function which takes seconds and returns corresponding
	 * hour-minutes string This function generates the hour minutes string in
	 * the format "00 H: 02 M: 05 Sec"
	 * @param receivedTime
	 * @return hourMinString - corresponding hour minutes string
	 */

	public static String convertSecToTimeString(long secs) {
		String retString = null;
		long mins = secs/60;
		long sec = secs%60;

		if(mins == 0){
			retString = sec +((sec != 0 && sec != 1)?" secs":" sec");
		} else{
			if(secs > 0){
				retString = (mins+1) + " mins";
			}else{
				retString = mins + (mins == 1 ? " min":" mins");
			}
		}
		return retString;
	}
	
	/**
	 * Normalize time to midnight
	 * @param time
	 * @return midnight
	 */
	public static long normalizeDay(long time) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
    public static String formatName(String name){
        String fin = null;
        if(name != null){
            String names[] = name.replace(",\\w", ", " ).split(" ");
            StringBuffer ret = new StringBuffer(name.length());
            if(names.length>1){
                if(names[0].contains(",")){
                    ret.append(names[1]+" ");
                    ret.append(names[0].replace(",", ""));
                }else{
                    ret.append(names[0]+" ");
                    ret.append(names[1]);
                }
                for(int i =2; i<names.length; i++){
                    ret.append(" "+names[i]);
                }
            }else{
                ret.append(name.trim());
            }
            fin = ret.toString();
        }
        return fin;
    }
    public static String getLastName(String name){
        String[] l1 = name.replaceAll("\t"," ").split(" ");
        String ret = null;
        switch(l1.length){
        case 1:
            ret = l1[0];
            break;
        case 2:
            ret = l1[1];
            break;
        default:
            ret = l1[2];
        }
        return ret;
    }
    public static String getMonthDateTimeString(Calendar systemTimeCal) {
        StringBuilder sb = new StringBuilder(10);
        int month = systemTimeCal.get(Calendar.MONTH);
        switch (month) {
        case 0:
            sb.append("January");
            break;
        case 1:
            sb.append("February");
            break;
        case 2:
            sb.append("March");
            break;
        case 3:
            sb.append("April");
            break;
        case 4:
            sb.append("May");
            break;
        case 5:
            sb.append("June");
            break;
        case 6:
            sb.append("July");
            break;
        case 7:
            sb.append("August");
            break;
        case 8:
            sb.append("September");
            break;
        case 9:
            sb.append("October");
            break;
        case 10:
            sb.append("November");
            break;
        case 11:
            sb.append("December");
            break;
        }
        sb.append(" " + systemTimeCal.get(Calendar.DAY_OF_MONTH));
        sb.append(", " + systemTimeCal.get(Calendar.HOUR) + ":");
        int min = systemTimeCal.get(Calendar.MINUTE);
        sb.append(min < 10 ? "0"+min : min);
        sb.append(systemTimeCal.get(Calendar.AM_PM) == Calendar.AM ? " am" : " pm");
        return sb.toString();
    }
    
    public static void main(String[] args){
//        System.out.println(formatName("Buland, Dave"));
//        System.out.println(formatName("Dave Buland"));
//        System.out.println(formatName("Buland the 3rd, Dave")); //<--revisit this case
//        System.out.println(formatName("Buland, Dave the 3rd"));
//        System.out.println("Bob Johnson".toLowerCase().replaceAll("[^a-z]", "").length());
//        System.out.println("1-408-691-1099".toLowerCase().replaceAll("[^a-z]", "").length());
//        System.out.println("1408-6911099".toLowerCase().replaceAll("[^a-z]", "").length());
    	System.out.println(formatRecentsDateTime(1323591331000l));
    }
    
    public static String formatRecentsDateTime(long time){
    	StringBuilder timeString = new StringBuilder(40);
		Calendar systemTimeCal = Calendar.getInstance();
		long systemTimeInSec = System.currentTimeMillis() / 1000;
		long receivedTimeInSec = time / 1000;
		long secondsDiff = systemTimeInSec - receivedTimeInSec;
		long todaySeconds =(systemTimeCal.get(Calendar.HOUR_OF_DAY) * 3600) +
						(systemTimeCal.get(Calendar.MINUTE) * 60) +
						(systemTimeCal.get(Calendar.SECOND));
		
		systemTimeCal.setTimeInMillis(time);
		
		if (secondsDiff < todaySeconds) {
			timeString.append(TODAY+" "+formatTime(systemTimeCal));
		}
		else if (secondsDiff >= todaySeconds && secondsDiff < todaySeconds + 6*DAYINSECONDS) {
			timeString.append(getShortDayString(systemTimeCal)+formatTime(systemTimeCal));
		}
		else {
			timeString.append(formatYearData(systemTimeCal));
		}
    	return timeString.toString();
    }
}
