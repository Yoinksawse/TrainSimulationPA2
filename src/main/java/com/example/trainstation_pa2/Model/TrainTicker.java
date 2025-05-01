package com.example.trainstation_pa2.Model;

public class TrainTicker {
    private Train trainName;
    private int ticker = 0;

    public TrainTicker(Train train, int t){
        trainName = train;
        ticker = t;
    }

    public Train getTrain() { return this.trainName; }
    public int getTrainTicker() { return this.ticker; }
    public void inc() { this.ticker++; }
    public void dec() { this.ticker--; }
    public void setTrain(Train t) { this.trainName = t; }
}
