import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class main {
    public static void main (String[] args){
        //Siteden gelen veriler
        String startDate = "02-01-2020";
        String endDate = "31-01-2020";
        String startTime = "9:00";
        String endTime = "18:00";
        String doctor = "Emre Karakuz";
        //        String startDate = "10-01-2020";
//        String endDate = "15-01-2020";
//        String startTime = "10:00";
//        String endTime = "14:00";
//        String doctor = "Dogukan Duduoglu";
        //

        ///////////////////////////
        ///////////////////////////
        if(startTime.length()==4){
            startTime = '0' + startTime;
        }
        if(endTime.length()==4){
            endTime = '0' + endTime;
        }

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:ss");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
        int howManyDays = 0;
        int howManyHours = 0;
        //finding howManyDays and howManyHours to run in a for loop
        try {
            Date countDayStart = format.parse(startDate);
            Date countDayEnd = format.parse(endDate);
            long howManyDaysMilli = countDayEnd.getTime()-countDayStart.getTime();
            howManyDays = (int) (howManyDaysMilli/86400000);
            Date countTimeStart = format2.parse(startTime);
            Date countTimeEnd = format2.parse(endTime);
            long howManyHoursMilli = countTimeEnd.getTime()-countTimeStart.getTime();
            howManyHours = (int) (howManyHoursMilli/3600000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //////////////////////////
        /////////////////
        Date queryDate = null;
        try {
            queryDate = format.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar gc = new GregorianCalendar();
        gc.setTime(queryDate);

        Date queryTime = null;
        try {
            queryTime = format2.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar gct = new GregorianCalendar();
        gct.setTime(queryTime);
        Date temp = queryTime;

        ArrayList<String> unavailableTimes = new ArrayList<>();
        DB_Handler handler = new DB_Handler();
        handler.init();
        int dId = 0;
        try {
            Statement stmt = handler.getConn().createStatement();
            ResultSet nameToId = stmt.executeQuery("select u_id from users where name = '"+doctor+"'");
            while (nameToId.next()){
                dId = nameToId.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<Timestamp> offdayEnd = new ArrayList<>();
        try {
            Statement stmt = handler.getConn().createStatement();
            ResultSet offCheck = stmt.executeQuery("select start,end from offdays where d_id = " + dId + "");
            while (offCheck.next()) {
                offdayEnd.add(offCheck.getTimestamp(2));
            }
        }
        catch (SQLException e){

        }
        System.out.println("offdayEnd:\n" + offdayEnd);

        for(int k = 0;k<=howManyDays;k++){
            queryDate = gc.getTime();
            String queryRunDate = format3.format(queryDate);
            for (int j = 0;j<howManyHours;j++){
                queryTime = gct.getTime();
                String queryRunTime = format2.format(queryTime);
                try {
                    Statement stmt = handler.getConn().createStatement();
                    ResultSet appCheck = stmt.executeQuery("select datetime from appointments " +
                            "where d_id ="+dId+" and datetime ='"+queryRunDate+" "+queryRunTime+"'");
                    while(appCheck.next()){
                        unavailableTimes.add(appCheck.getString(1).substring(0, 16));
                    }
                    ResultSet offDayCheck = stmt.executeQuery("select start from offdays " +
                            "where '"+queryRunDate+" "+queryRunTime+"' between start and end");
                    while(offDayCheck.next()){
                        boolean isLast = false;
                        for(int i=0; i<offdayEnd.size(); i++){
                            if(offdayEnd.get(i).toString().substring(0,10).equals(queryRunDate)
                                    && offdayEnd.get(i).toString().substring(11,16).equals(queryRunTime))
                                isLast = true;
                        }
                        if(!isLast)
                            unavailableTimes.add(queryRunDate + " " + queryRunTime);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                gct.add(Calendar.HOUR,1);
            }
            gct.setTime(temp);
            gc.add(Calendar.DAY_OF_WEEK,1);
        }
        handler.close();

        System.out.println(unavailableTimes);

        String html = "<p style=\"text-align: center; color: black; border-bottom: 1px solid black\">" +
                "Available times for Doctor " + doctor + " at " + "date" + "</p>" +
                "<div style=\"width: 80%; margin: 0 auto;\">\n" +
                "                <form action=\"setAppointment\"  method=\"post\" style=\"width: 70%; margin: 0 auto\">\n";

        Date date1 = null;
        try {
            date1 = format.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c1 = new GregorianCalendar();
        c1.setTime(date1);

        Date time = null;
        try {
            time = format2.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c2 = new GregorianCalendar();
        c2.setTime(time);
        Date temp1 = time;

        for(int k = 0;k<=howManyDays;k++){
            date1 = c1.getTime();
            String date = format3.format(date1);
            //date = date.split("-")[2] + "-" + date.split("-")[1] + "-" + date.split("-")[0];
            System.out.println(date);
            for (int j = 0;j<howManyHours;j++){
                time = c2.getTime();
                String times = format2.format(time);
                c2.add(Calendar.HOUR,1);
                System.out.println(date + " " + times);
            }
            c2.setTime(temp1);
            c1.add(Calendar.DAY_OF_WEEK,1);
        }


    }
}
