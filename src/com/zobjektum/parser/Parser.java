/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zobjektum.parser;

import com.zobjektum.bom.Config;
import com.zobjektum.bom.Ride;
import com.zobjektum.io.FileIO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Balu_ADMIN
 */
public class Parser {
    
    public static Config parseInput(String inputContent) {
        String[] lines = inputContent.split(FileIO.LINE_SEPARATOR);
        String header = lines[0];
        String[] headerInfo = header.split(FileIO.DATA_SEPARATOR);
        int rows = Integer.parseInt(headerInfo[0]);
        int columns = Integer.parseInt(headerInfo[1]);
        int numberOfCarsInFleet = Integer.parseInt(headerInfo[2]);
        int numberOfRides = Integer.parseInt(headerInfo[3]);
        int bonusForStartingRideOnTime = Integer.parseInt(headerInfo[4]);
        int numberOfMaxStepsAvailable = Integer.parseInt(headerInfo[5]);
        List<Ride> rides = new ArrayList<>();
        for(int rowIndex=1; rowIndex<lines.length; rowIndex++) {
            String[] rideInfo = lines[rowIndex].split(FileIO.DATA_SEPARATOR);
            int startX = Integer.parseInt(rideInfo[0]);
            int startY = Integer.parseInt(rideInfo[1]);
            int endX = Integer.parseInt(rideInfo[2]);
            int endY = Integer.parseInt(rideInfo[3]);
            int timeOfEarliestStart = Integer.parseInt(rideInfo[4]);
            int timeOfLatestFinish = Integer.parseInt(rideInfo[5]);
            rides.add(new Ride(startX, startY, endX, endY, timeOfEarliestStart, timeOfLatestFinish, rowIndex));
        }
        return new Config(rows, columns, numberOfCarsInFleet, numberOfRides, bonusForStartingRideOnTime, numberOfMaxStepsAvailable, rides);
    }
}
