package com.example.trainstation_pa2.Model;

public class Train {
    private String trainID;
    private Line line;
    private int stationIndex;
    private int minutesToNextStop;
    private boolean stopped;
    private boolean delayed;
    private boolean serviceEnded;
    private boolean monitored;

    public Train(String trainID, Line line) {
        if (trainID.length() != 4) {
            // Require train ID to be exactly 4 characters
            throw new IllegalArgumentException("Train ID must be exactly 4 characters long.");
        }

        this.trainID = trainID;
        this.line = line;
        this.minutesToNextStop = 0;
        this.stopped = true;
        this.delayed = false;
        this.serviceEnded = false;
    }

    // Copy constructor
    public Train(Train t) {
        this.trainID = t.trainID;
        this.line = t.line;
        this.stationIndex = t.stationIndex;
        this.minutesToNextStop = t.minutesToNextStop;
        this.stopped = t.stopped;
        this.serviceEnded = t.serviceEnded;
    }

    public String getTrainID() {
        return trainID;
    }

    public int getStationIndex() {
        return stationIndex;
    }

    public boolean isServiceEnded() {
        return serviceEnded;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public boolean isStopped() {
        return stopped;
    }

    public boolean isMonitored() {return monitored;}

    public void setMonitored(boolean monitored) {
        this.monitored = monitored;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public int getMinutesToNextStop() {
        return this.minutesToNextStop;
    }

    // Advances the "progress" of the train by 1 minute
    // If the train is moving and not yet at a stop, keep moving it
    // If the train is moving and reaches a stop, it stops there
    // If the train is stopped, it will move off to the next stop (ie. Stop for 1 min to pick up passengers)
    public void tick() {
        if (serviceEnded) return;   // Don't move the train anymore if the service has ended

        if (!stopped) {
            minutesToNextStop--;
        }

        if (minutesToNextStop <= 0 && !stopped) {
            stopped = true;
            if (stationIndex >= line.countStations() - 1) {
                serviceEnded = true;
            }
        }
        else if (stopped) {
            stationIndex += 1;
            minutesToNextStop = line.getTravelTime(stationIndex);
            stopped = false;
        }
    }

    public String toString() {
        String result = "";
        if (serviceEnded) {
            result = "Service has ended";
        }
        if (stopped) {
            result = "Stopped at " + line.getStation(stationIndex);
        }
        else {
            result = minutesToNextStop + "mins to " + line.getStation(stationIndex);
        }

        if (delayed) {
            result += " (delayed)";
        }

        return result;
    }
}
