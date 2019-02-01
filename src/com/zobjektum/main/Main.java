/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zobjektum.main;

import com.zobjektum.algorithm.Algorithm;
import com.zobjektum.bom.Config;
import com.zobjektum.bom.SolutionWrapper;
import com.zobjektum.io.FileIO;
import com.zobjektum.parser.Parser;


/**
 * Main entry point.
 * 
 * @author Balu_ADMIN
 *
 */
public class Main {
	/**
	 * Helper method to print things.
	 * 
	 * @param msg
	 */
	public static void print(String msg) {
            System.out.println(msg);
	}
	/**
	 * Helper method to print things.
	 * 
	 * @param obj
	 */
	public static void print(Object obj) {
            System.out.println(obj == null ? "null" : obj.toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
            FileIO fileIO = new FileIO();
            String fileContent = fileIO.loadUsingGUI();
            //print(fileContent);
            Config config = Parser.parseInput(fileContent);
            print(config);
            //print(config.printRidesToString(config.getRides()));
            print(config.printRidesToString(config.getRidesOrderedByEarliestStart()));
            SolutionWrapper solutionWrapper = Algorithm.doLogic(config);
            fileIO.saveSolution(solutionWrapper);
            print(solutionWrapper.toString());
	}
}