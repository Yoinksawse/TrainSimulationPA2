package com.example.trainstation_pa2.Model;

import java.io.IOException;
import java.util.ArrayList;

public class Simulation {
    private Line line;
    private ArrayList<Train> trains;

    public Simulation(Line ln) {
        this.line = ln;
        trains = new ArrayList<>();
    }

    // NEWLY ADDED FOR PA2, no modifications needed
    public Simulation(String filename) throws IOException {
        this(new Line(filename));
    }

    public void addTrain(Train t) {
        trains.add(new Train(t));
    }

    // NEWLY ADDED FOR PA2, no modifications needed
    public void removeTrain(String trainID) {
        for (Train t : trains) {
            if (t.getTrainID().equals(trainID)) {
                trains.remove(t);
                break;
            }
        }
    }

    // NEWLY ADDED FOR PA2, no modifications needed
    public Train getTrain(String trainID) {
        for (Train t : trains) {
            if (t.getTrainID().equals(trainID)) {
                return t;
            }
        }
        return null;
    }

    public Train[] getTrains() {
        return trains.toArray(new Train[trains.size()]);
    }

    public Line getLine() {
        return line;
    }

    public void tick() {
        // Reset delay status on all trains
        for (Train t : trains) {
            t.setDelayed(false);
        }

        for (int i=0; i<trains.size(); i++) {
            Train t1 = trains.get(i);
            if (t1.isServiceEnded()) continue;

            if (!t1.isDelayed()) {
                t1.tick();
            }

            // Check if the subsequent trains in service are too close. If so, delay them.
            for (int j=i+1; j<trains.size(); j++) {
                Train t2 = trains.get(j);
                if (t1 != t2 && (t1.getStationIndex()-1) == t2.getStationIndex() && !t2.isServiceEnded() && t2.isStopped()) {
                    t2.setDelayed(true);
                    break;
                }
            }
        }
    }

    public String toString() {
        String result = "";

        for (int i=0; i<line.countStations(); i++) {
            String left = " ";
            for (Train t : trains) {
                if (t.isServiceEnded()) continue;

                if (t.getStationIndex() == i) {
                    if (t.isDelayed()) left = "D";
                    else if (t.isStopped()) left = "~";
                    else left = "v";
                }
            }
            result += String.format("%-3s%-30s\n", left, line.getStation(i));
        }

        return result;
    }
}
