/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zobjektum.algorithm;

import com.zobjektum.bom.Config;
import com.zobjektum.bom.Ride;
import com.zobjektum.bom.SolutionWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Balu_ADMIN
 */
public class Algorithm {
    
    private static class Intersection {
        private final List<Integer> carIds = new ArrayList<>();
        private final List<Integer> riderIds = new ArrayList<>();
    }
    
    public static SolutionWrapper doLogic(Config config) {
        return doLogicRandom(config);
    }
    
    /**
     * Solution for the easy example input.
     * 
     * @param config
     * @return 
     */
    private static SolutionWrapper doLogicForExampleA(Config config) {
        //it was so small, it was manually configured
        SolutionWrapper solutionWrapper = new SolutionWrapper();
        solutionWrapper.addSolution(1, Arrays.asList(new Integer[] {0}));
        solutionWrapper.addSolution(2, Arrays.asList(new Integer[] {1,2}));
        return solutionWrapper;
    }
    
    /**
     * Random solution.
     * 
     * @param config
     * @return 
     */
    private static SolutionWrapper doLogicRandom(Config config) {
        //it was so small, it was manually configured
        SolutionWrapper solutionWrapper = new SolutionWrapper();
        int numberOfCars = config.getFAsNumberOfCarsInFleet();
        List<Integer> rides = config.getRiderIds();
        Collections.shuffle(rides); //make a random order
        final int RIDES_RAND_MAX_STEP = config.getNumberOfRides() / config.getFAsNumberOfCarsInFleet(); //e.g.: 300 / 100 = 3
        int ridesMinIndex = 0; //inclusive
        int ridesMaxIndex = 0; //exclusive
        int howManyRides;
        Random random = new Random();
        for(int i=1; i<=numberOfCars; i++) { //for every car we create a random number, how many rides this car will pick from the randomly ordered list of rides
            howManyRides = random.nextInt(RIDES_RAND_MAX_STEP); //e.g 2
            ridesMaxIndex = (ridesMaxIndex + howManyRides < rides.size()) ? (ridesMaxIndex + howManyRides) : rides.size();
            solutionWrapper.addSolution(i, rides.subList(ridesMinIndex, ridesMaxIndex));
            ridesMinIndex = ridesMaxIndex;
        }
        //add the rest of the riderIds sequentially
        while(ridesMinIndex < rides.size()) {
            int randomVehicle = random.nextInt(numberOfCars) + 1;
            solutionWrapper.addSolution(randomVehicle, rides.subList(ridesMinIndex, ++ridesMinIndex));
        }
        return solutionWrapper;
    }
    
    /**
     * Implementation of a general solution.
     * 
     * @param config
     * @return 
     */
    private static SolutionWrapper doGeneralLogic(Config config) {
        SolutionWrapper solutionWrapper = new SolutionWrapper();
        List<Ride> orderedRides = config.getRidesOrderedByEarliestStart();
        //creating the city and initalizing the intersections
        Intersection[][] city = new Intersection[config.getColumns()][config.getRows()];
        for(int c=0; c<city.length; c++) {
            for(int r=0; r<city[c].length; r++) {
                city[c][r] = new Intersection();
            }
        }
        //setting all cars to 0,0 at first
        for(int i=1; i<=config.getFAsNumberOfCarsInFleet(); i++) {
            city[0][0].carIds.add(i);
        }
        //starting the time steps
        for(int timeStep = 0; timeStep<=config.getTAsNumberOfMaxStepsAvailable(); timeStep++) {
            //ride(s) show up
            List<Ride> ridesStartingInTimestep = config.getRidesStartingInTimestep(timeStep);
            if(!ridesStartingInTimestep.isEmpty()) {
                for(Ride ride : ridesStartingInTimestep) {
                    city[ride.START_Y][ride.START_X].riderIds.add(ride.getRiderID());
                }
            }
        }
        return solutionWrapper;
    }
}
