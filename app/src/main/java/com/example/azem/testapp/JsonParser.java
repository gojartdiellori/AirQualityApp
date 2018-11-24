package com.example.azem.testapp;



import android.graphics.Color;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonParser {
    String value="0";
    String location;

    public String getDesStud() {
        return desStud;
    }

    String dominentpol;

    public String getBackground() {
        return background;
    }

    public String getDesOut() {
        return desOut;
    }

    public String getDesIn() {
        return desIn;
    }

    public String getDesSens() {
        return desSens;
    }

    public String getDesSpor() {
        return desSpor;
    }

    String latitude;
    String city;
    String country;
    String desChild;
    String desOut,desIn,desSens,desSpor;
    String desStud;
String background;



    public String getDesEld() {
        return desEld;
    }

    public String getDesChild() {
        return desChild;
    }

    String desEld;
    String desChil;
    String desPeoHar;
    String yourCity;

    public String getYourCity() {
        return yourCity;
    }

    String yourCountry;
    String date;
    public String getCountry() {
        return country;
    };
    public String getYourCountry(){return yourCountry;};

    public String getCity() {
        return city;
    }

    String yourLocation;

    public String getDate() {
        return date;
    }

    String longitude;
    String backgroundColor;
    public String getBackgroundColor() {
        return backgroundColor;
    }

    private static DecimalFormat df2 = new DecimalFormat(".##");
    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getYourLocation() {
        return yourLocation;
    }




    public String getParameter()
    {
        return dominentpol;
    }

    public String getValue() {
        return value;
    }

    public String getLocation() {
        return location;
    }
public String checkAQI(String value)
{
    int doubleValue=Integer.parseInt(value);
    if(doubleValue>=0&&doubleValue<50)
    {
        desChild="healthy";

        backgroundColor="back";
        background="36AE67";
        desStud="Enjoy your usual outdoor activities.";
        desSens="Enjoy your usual outdoor activities.";
        return "GOOD";
    }
    else if(doubleValue>=50&&doubleValue<100)
    {
        desIn="It is a good idea to stay in closed & air conditioned places";
        desOut="In these conditions you should minimize your time outside as much as possible";
        desSens="Don't take any chances - keep your prescription medication(s) nearby & limit your physical activity";
        desSpor="You don’t want to do your workout in this environment. Find a cleaner area";
        backgroundColor="orange";
        background="FBE952";
        desStud="Enjoy your usual outdoor activities.";

        return "MODERATE";
    }
    else if(doubleValue>=100&&doubleValue<150)
    {
        desIn="If we were you – we would stay in a closed environment, with the A/C powered on";
       desOut="In these conditions you should minimize your time outside as much as possible";
       desSens="Suffering from respiratory difficulties? Please refrain from physical activity. Keep your prescription medication within reach";
       desSpor="You don’t want to do your workout in this environment. Find a cleaner area";
        backgroundColor="orange1";
        background="FF9933";
        desStud="It’s ok to be active outside, but are recommended to reduce prolonged strenuous exercise.";
        return "UNHEALTHY FOR \n SENSITIVE GROUP";
    }
    else if(doubleValue>=150&&doubleValue<200)
    { desIn ="It is a good idea to stay in closed & air conditioned places";
        desOut="Unless you really have to, our recommendation is to minimize your time outdoors";
        desSens="Don't take any chances - keep your prescription medication(s) nearby & limit your physical activity";
        desSpor="With this air quality level, you should check the area for cleaner places to engage in outdoor activity";
        backgroundColor="orange2";
        background="CC0033";

        desStud="Students should avoid prolonged strenuous exercise, and take more breaks during outdoor activities.";
        return "UNHEALTHY";
    }
    else if(doubleValue>=200&&doubleValue<300)
    {
        desChild="healthy";
       // desPreg="not healthy";
        backgroundColor="orange3";
        background="660099";
       desStud=" Students should stop outdoor activities and move all activities and classes indoors.";
        desPeoHar="People with heart, respiratory and cardiovascular problems, children, teenagers and older adults should stay indoors and reduce physical exertion. If it is necessary to go out, please wear a mask.";
        return "VERY UNHEALTHY";
    }
    else  if(doubleValue>=300)
    {
        desIn="Do us a favor: stay inside, turn on the A/C, and get comfortable, OK?";
        desOut="You don't want to breathe the air outside. Take our word for it and stay indoors";
        desSens="Why risk it? Refrain from any outdoor activities and be aware of any health issues";
        desSpor="We recommend not to perform any strenuous physical activity in the open air";
        background="7E0023";
        backgroundColor="orange4";
        desStud=" Students should stop outdoor activities and move all activities and classes indoors.";
       return "HAZARDOUS";

    }
    else
    { desChild="-";
        //desPreg="-";
        return "-";
    }


}



    public JsonParser(String result,String type)
    {
        try {
            JSONObject obj2 = new JSONObject(result);
            JSONArray data = obj2.getJSONArray("geonames");
            JSONObject g = data.getJSONObject(0);

            yourLocation = g.getString("name");
            yourCity=g.getString("adminName1");
            yourCountry =g.getString("countryName");

        }
        catch(Exception e)
        {
                Log.e("error1",e.getMessage()) ;
        }

    }


    public JsonParser(String result) {
        try {

                      JSONObject object = new JSONObject(result);
                      JSONObject results = object.getJSONObject("data");


                       value = results.getString("aqi");
                       JSONObject citydata=results.getJSONObject("city");
                       city=citydata.getString("name");
                       dominentpol=results.getString("dominentpol").toUpperCase();
                       Log.e("dominent",dominentpol);
                       JSONArray loglat=citydata.getJSONArray("geo");
                       latitude=loglat.getString(0);
                       longitude=loglat.getString(1);

                       JSONObject time=results.getJSONObject("time");
                       date=time.getString("s");
                        SimpleDateFormat oldDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date d=oldDate.parse(date);
                        oldDate.applyPattern("MMMM dd ',' yyyy   hh:mm");
                        date=oldDate.format(d);






        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error",e.getMessage());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
