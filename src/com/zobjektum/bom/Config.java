/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zobjektum.bom;

import com.zobjektum.io.FileIO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author Balu_ADMIN
 */
public class Config {
    /**
     * Number of rows
     */
    private final int R;
    /**
     * Number of columns
     */
    private final int C;
    /**
     * Number of cars in fleet
     */
    private final int F;
    /**
     * Number of rides
     */
    private final int N;
    /**
     * Bonus points for starting at the earliest allowed time
     */
    private final int B;
    /**
     * Maximum time steps
     */
    private final int T;
    private final List<Ride> RIDES;
    private List<Ride> ridesOrderedByEarliestStart;
    private Map<Integer, List<Ride>> ridesMappedForEarliestStart;
    
    /**
     * Comparator to compare rides organised ascending by the earliest start step time.
     */
    private static final class ComparatorOfRidesByEarliestStartTime implements Comparator<Ride> {
        @Override
        public int compare(Ride r1, Ride r2) {
            return (r1.TIME_OF_EARLIEST_START_TO_MOVE < r2.TIME_OF_EARLIEST_START_TO_MOVE) ? 
                    -1 : 
                    ((r1.TIME_OF_EARLIEST_START_TO_MOVE == r2.TIME_OF_EARLIEST_START_TO_MOVE) ? 
                        0 :
                        +1
                    )
            ;
        }
    }
    
    public Config(int rows, int columns, int numberOfCarsInFleet, int numberOfRides, int bonusForStartingRideOnTime, int numberOfMaxStepsAvailable, List<Ride> rides) {
        R = rows;
        C = columns;
        F = numberOfCarsInFleet;
        N = numberOfRides;
        B = bonusForStartingRideOnTime;
        T = numberOfMaxStepsAvailable;
        RIDES = rides;
        if(1 <= R && R <= 10000) {} else {throw new RuntimeException("Invalid rows: " + rows);}
        if(1 <= C && C <= 10000) {} else {throw new RuntimeException("Invalid columns: " + columns);}
        if(1 <= F && F <= 1000) {} else {throw new RuntimeException("Invalid numberOfCarsInFleet: " + numberOfCarsInFleet);}
        if(1 <= N && N <= 10000) {} else {throw new RuntimeException("Invalid numberOfRides: " + numberOfRides);}
        if(1 <= B && B <= 10000) {} else {throw new RuntimeException("Invalid bonusForStartingRideOnTime: " + bonusForStartingRideOnTime);}
        if(1 <= T && T <= Math.pow(10,9)) {} else {throw new RuntimeException("Invalid numberOfMaxStepsAvailable: " + numberOfMaxStepsAvailable);}
        if(rides.size() != N) {throw new RuntimeException("Invalid number of rides: " + rides.size() + ". " + N + " rides were expected.");}
        for(int i=0; i<rides.size(); i++) {
            Ride ride = rides.get(i);
            if(ride.START_X == ride.END_X && ride.START_Y == ride.END_Y) {
                throw new RuntimeException("Invalid ride(" + ride.getRowIndexInInputFile() + "). Start(" + ride.START_X + "," + ride.START_Y + ") is the same as end(" + ride.END_X + "," + ride.END_Y + ").");
            }
            if(0 <= ride.START_X && ride.START_X < R) {} else {throw new RuntimeException("Invalid startX: " + ride.START_X + " for ride(" + ride.getRowIndexInInputFile() + ").");}
            if(0 <= ride.END_X && ride.END_X < R) {} else {throw new RuntimeException("Invalid endX: " + ride.END_X + " for ride(" + ride.getRowIndexInInputFile() + ").");}
            if(0 <= ride.START_Y && ride.START_Y < C) {} else {throw new RuntimeException("Invalid startY: " + ride.START_Y + " for ride(" + ride.getRowIndexInInputFile() + ").");}
            if(0 <= ride.END_Y && ride.END_Y < C) {} else {throw new RuntimeException("Invalid endY: " + ride.END_Y + " for ride(" + ride.getRowIndexInInputFile() + ").");}
            if(0 <= ride.TIME_OF_EARLIEST_START_TO_MOVE && ride.TIME_OF_EARLIEST_START_TO_MOVE <= T) {} else {throw new RuntimeException("Invalid earliest start time: " + ride.TIME_OF_EARLIEST_START_TO_MOVE + " for ride(" + ride.getRowIndexInInputFile() + ").");}
            if(0 <= ride.TIME_OF_LATEST_FINISH && ride.TIME_OF_LATEST_FINISH <= T) {} else {throw new RuntimeException("Invalid latest finish time: " + ride.TIME_OF_LATEST_FINISH + " for ride(" + ride.getRowIndexInInputFile() + ").");}
            if(ride.TIME_OF_EARLIEST_START_TO_MOVE + ride.getTimeItTakesForDistance() <= ride.TIME_OF_LATEST_FINISH) {} else {throw new RuntimeException("Invalid duration time(" + ride.TIME_OF_EARLIEST_START_TO_MOVE + "+" + ride.getTimeItTakesForDistance() + "<=" + ride.TIME_OF_LATEST_FINISH + ") for ride(" + ride.getRowIndexInInputFile() + ").");}
        }
        //
        ridesOrderedByEarliestStart = new ArrayList<>(rides);
        Collections.sort(ridesOrderedByEarliestStart, new ComparatorOfRidesByEarliestStartTime());
        //
        ridesMappedForEarliestStart = new TreeMap<>();
        for(Ride ride : ridesOrderedByEarliestStart) {
            if(!ridesMappedForEarliestStart.containsKey(ride.TIME_OF_EARLIEST_START_TO_MOVE)) {
                ridesMappedForEarliestStart.put(ride.TIME_OF_EARLIEST_START_TO_MOVE, new ArrayList<>());
            }
            ridesMappedForEarliestStart.get(ride.TIME_OF_EARLIEST_START_TO_MOVE).add(ride);
        }
    }
    
    public List<Ride> getRides() {
        return RIDES;
    }
    
    public List<Integer> getRiderIds() {
        return getRiderIds(getRides());
    }
    
    public List<Integer> getRiderIds(List<Ride> rides) {
        return rides.stream().map(r -> r.getRiderID()).collect(Collectors.toList());
    }
    
    /**
     * Gets the rides ordered by their earliest start time step.
     * 
     * @return 
     */
    public List<Ride> getRidesOrderedByEarliestStart() {
        return ridesOrderedByEarliestStart;
    }
    
    /**
     * Gets the rides for a given timestep.
     * 
     * @param timestep
     * @return 
     */
    public List<Ride> getRidesStartingInTimestep(int timestep) {
        if(ridesMappedForEarliestStart.containsKey(timestep)) {
            return ridesMappedForEarliestStart.get(timestep);
        }
        return Collections.EMPTY_LIST;
    }
    
    public int getRows() {
        return R;
    }

    public int getColumns() {
        return C;
    }

    public int getFAsNumberOfCarsInFleet() {
        return F;
    }

    public int getNumberOfRides() {
        return N;
    }

    public int getBonusForStartingRideOnTime() {
        return B;
    }

    public int getTAsNumberOfMaxStepsAvailable() {
        return T;
    }

    public int getR() {
        return R;
    }

    public int getC() {
        return C;
    }

    public int getF() {
        return F;
    }

    public int getN() {
        return N;
    }

    public int getB() {
        return B;
    }

    public int getT() {
        return T;
    }

    @Override
    public String toString() {
        return "Config{" + "Rows=" + R + ", Columns=" + C + ", FleetAsNumberOfCars=" + F + ", NumberOfRides=" + N + ", BonusForStartingRideOnTime=" + B + ", TAsNumberOfMaxStepsAvailable=" + T + ", RIDES=" + RIDES.size() + " ride(s)}";
    }
    
    /**
     * Prints the given rides into a formatted string.
     * 
     * @param rides
     * @return 
     */
    public String printRidesToString(List<Ride> rides) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<rides.size(); i++) {
            sb.append(i+1).append(".: ").append(rides.get(i).toString()).append(FileIO.LINE_SEPARATOR);
        }
        return sb.toString();
    }
}
