/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zobjektum.bom;

/**
 *
 * @author Balu_ADMIN
 */
public class Ride {
    public final int START_X;
    public final int START_Y;
    public final int END_X;
    public final int END_Y;
    public final int TIME_OF_EARLIEST_START_TO_MOVE;
    /**
     * Here car is allowed to move with next ride.
     */
    public final int TIME_OF_LATEST_FINISH;
    private final int DISTANCE;
    private final int ROW_INDEX_IN_INPUT_FILE;

    /**
     * 
     * @param START_X
     * @param START_Y
     * @param END_X
     * @param END_Y
     * @param TIME_OF_EARLIEST_START
     * @param TIME_OF_LATEST_FINISH
     * @param ROW_INDEX_IN_INPUT_FILE 
     */
    public Ride(int START_X, int START_Y, int END_X, int END_Y, int TIME_OF_EARLIEST_START, int TIME_OF_LATEST_FINISH, int ROW_INDEX_IN_INPUT_FILE) {
        this.START_X = START_X;
        this.START_Y = START_Y;
        this.END_X = END_X;
        this.END_Y = END_Y;
        this.TIME_OF_EARLIEST_START_TO_MOVE = TIME_OF_EARLIEST_START;
        this.TIME_OF_LATEST_FINISH = TIME_OF_LATEST_FINISH;
        this.ROW_INDEX_IN_INPUT_FILE = ROW_INDEX_IN_INPUT_FILE;
        DISTANCE = Math.abs(START_X - END_X) + Math.abs(START_Y - END_Y);
    }
    
    /**
     * Car has to move to the finish cell latest in this step. The next step is already too late.
     * 
     * @return 
     */
    public int getTimeOfLatestArrival() {
        return TIME_OF_LATEST_FINISH-1;
    }
    
    public int getDistance() {
        return DISTANCE;
    }
    
    public int getTimeItTakesForDistance() {
        return getDistance();
    }
    
    /**
     * Returns the line number in the input file where this ride was defined.
     * 
     * @return 
     */
    protected int getRowIndexInInputFile() {
        return ROW_INDEX_IN_INPUT_FILE;
    }
    
    public int getRiderID() {
        return ROW_INDEX_IN_INPUT_FILE - 1;
    }

    @Override
    public String toString() {
        return "Ride{" + "START_X=" + START_X + ", START_Y=" + START_Y + ", END_X=" + END_X + ", END_Y=" + END_Y + ", TIME_OF_EARLIEST_START_TO_MOVE=" + TIME_OF_EARLIEST_START_TO_MOVE + ", TIME_OF_LATEST_FINISH=" + TIME_OF_LATEST_FINISH + ", DISTANCE=" + DISTANCE + ", RIDER_ID=" + getRiderID() + '}';
    }
    
    
}
