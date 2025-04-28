package com.example.trainstation_pa2.Model;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

// Models a train line
public class Line {
    private String name;
    private String code;
    private ArrayList<Station> stations;
    private ArrayList<Integer> travelTime;

    public Line(String name, String code, Station firstStation) {
        this.name = name;
        this.code = code;
        stations = new ArrayList<>();
        travelTime = new ArrayList<>();

        stations.add(new Station(firstStation));
    }

    public Line(String filename) throws IOException {
        // NEWLY ADDED FOR PA2
        // TODO: Write your code here
        File file = new File(filename);
        Scanner input = new Scanner(file);

        this.stations = new ArrayList<>();
        this.travelTime = new ArrayList<>();

        //collecting line info (first line of csv file)
        String lineinfo = input.nextLine();
        String[] partsLineinfo = lineinfo.split("[,]");
        this.name = partsLineinfo[0];
        this.code = partsLineinfo[1];

        int i = 0;
        while (input.hasNext()) {
            String curline = input.nextLine();
            String[] parts = curline.split("[,]");
            String stationCode  = parts[0];
            String stationName = parts[1];
            int stationTravelTime = 0;
            if (i > 0) stationTravelTime = Integer.parseInt(parts[2]);

            this.appendStation(new Station(stationCode, stationName), stationTravelTime);
            i++;
        }
    }

    // Adds a new station
    // The time parameter represents the time required to travel from the last added station to this one
    public void appendStation(Station station, int time) {
        if (!station.getCode().substring(0, 2).equals(this.code)) {
            throw new IllegalArgumentException("Station codes must start with " + this.code);
        }

        stations.add(new Station(station));
        travelTime.add(time);
    }

    // Returns the number of stations on the line
    public int countStations() {
        return stations.size();
    }

    public Station[] getStations() {
        return stations.toArray(new Station[stations.size()]);
    }

    // Returns the station at the given index
    public Station getStation(int index) {
        return new Station(stations.get(index));
    }

    // Returns the travel time to the next station at the given index
    public int getTravelTime(int index) {
        return travelTime.get(index-1);
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    //for visualisation
    public ArrayList<Station> getStationsARL() {
        return new ArrayList<>(this.stations);
    }
    public ArrayList<Integer> getTravelTimeARL() {
        return new ArrayList<>(this.travelTime);
    }


    public String toString() {
        String result = stations.get(0).toString() + "\n";
        for (int i = 1; i < stations.size(); i++) {
            result += "  :  " + travelTime.get(i-1) + "mins\n";
            result += stations.get(i).toString() + "\n";
        }
        return result;
    }
}
