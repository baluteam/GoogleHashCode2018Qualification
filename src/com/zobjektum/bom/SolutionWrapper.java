/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zobjektum.bom;

import com.zobjektum.io.FileIO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Balu_ADMIN
 */
public class SolutionWrapper {
    public Map<Integer, List<Integer>> solution;
    
    public SolutionWrapper() {
        solution = new TreeMap<>();
    }
    
    /**
     * Adds the given vehicle id and the rides taken by it to the solution. If the ride was already added, only the new rider IDs are added, the old wont be deleted.
     * 
     * @param vehicleIndex
     * @param riderIdsInOrderAssignedToThisVehicle 
     */
    public void addSolution(Integer vehicleIndex, List<Integer> riderIdsInOrderAssignedToThisVehicle) {
        if(solution.containsKey(vehicleIndex)) {
            //if this car is already added, we only add the new ride IDs
            Set<Integer> ridesAlreadyAdded = new TreeSet<>(solution.get(vehicleIndex));
            ridesAlreadyAdded.addAll(riderIdsInOrderAssignedToThisVehicle);
            solution.replace(vehicleIndex, new ArrayList<>(ridesAlreadyAdded));
            return;
        }
        solution.put(vehicleIndex, riderIdsInOrderAssignedToThisVehicle);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Integer> kIt = solution.keySet().iterator();
        while(kIt.hasNext()) {
            Integer key = kIt.next();
            List<Integer> riderIdsInOrderAssignedToThisVehicle = solution.get(key);
            sb.append(riderIdsInOrderAssignedToThisVehicle.size());
            if(0 < riderIdsInOrderAssignedToThisVehicle.size()) {
                sb.append(FileIO.DATA_SEPARATOR);
                Iterator<Integer> it = riderIdsInOrderAssignedToThisVehicle.iterator();
                while(it.hasNext()) {
                    Integer riderId = it.next();
                    sb.append(riderId);
                    if(it.hasNext()) {
                        sb.append(FileIO.DATA_SEPARATOR);
                    }
                }
            }
            if(kIt.hasNext()) {
                sb.append(FileIO.LINE_SEPARATOR);
            }
        }
        return sb.toString();
    }
}
