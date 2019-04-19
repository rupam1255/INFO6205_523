/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.edu.psa;

import com.neu.edu.psa.pojo.Location;
import com.neu.edu.psa.pojo.Team;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class Data 
{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
    
    private ArrayList<Date> date;
    private ArrayList<Team> team;
    private ArrayList<Location> location;
    private HashMap<String, HashMap<Location, Integer>> weather;
    
    public Data()
    {
        date = new ArrayList<>();
        team=new ArrayList<>();
        location=new ArrayList<>();
        weather=new HashMap<>();
        
    }

    public ArrayList<Date> getDate() {
        return date;
    }

    public ArrayList<Team> getTeam() {
        return team;
    }

    public ArrayList<Location> getLocation() {
        return location;
    }

    public HashMap<String, HashMap<Location, Integer>> getWeather() {
        return weather;
    }
    
    void initializeData()
    {
        Location l1 = new Location("Chennai");
        Location l2 = new Location("Banglore");
        Location l3 = new Location("Punjab");
        Location l4 = new Location("Mumbai");
        
        location.clear();
        location.addAll(Arrays.asList(l1,l2,l3,l4));
        
        Team t1 = new Team("Chennai Super Kings", l1);
        Team t2 = new Team("Royal Chanllengers Bangalore",l2);
        Team t3 = new Team("Kings XI Punjab", l3);
        Team t4 = new Team("Mumbai Indians",l4);
        
        team.clear();
        team.addAll(Arrays.asList(t1,t2,t3,t4));
        
        int count = 0;
        Date currentDate = new Date();
        Calendar todaysDate = Calendar.getInstance();
        todaysDate.setTime(currentDate);
        date.clear();
        
        while(count<=(team.size()*team.size()))
        {
            date.add(todaysDate.getTime());
            todaysDate.add(Calendar.DAY_OF_YEAR, 1);
            count++;
        }
        
        weather.clear();
        for(Date d: date)
        {
            HashMap<Location, Integer> map = new HashMap<>();
            weather.put(dateFormat.format(d), map);
            for(Location l : location)
            {
                map.put(l, (new Random().nextInt(100)));
            }
        }
        
    }
    
}
