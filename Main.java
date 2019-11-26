/****************************************************************
 * Name: Manel Ghorbal, Bernard Duchesne						*
 *																*
 * ID: 002235600, 002241777 									*
 * 																*
 * CS321 - Assignment 3											*
 * 																*
 * The Fish Market Simulation									*
 * 																*
 * This Program intakes a simulation time (in seconds), goes 	*
 * through the simualtion, and outputs the statistics of the 	*
 * simulation.													*
 ***************************************************************/


/*
 *  I found that to yield the most realistic results during a 
 * 2 month simulation, the average interarrival time should
 * be 180 seconds rather than 120 seconds. 
 * While speaking to you, we discussed that 120 seconds was calculated 
 * with the assumption that service time is 30 seconds total (not 
 * 30 seconds PER fish). This caused the simualtion to overload 
 * on Market Arrivals, causing the Queue lengths and Queue waiting times
 * to be unusually high.
 * As discussed, I have set the AVG interarrival time to 180. 
 */


public class Main {

	public static void main(String[] args) {
		//creates new fish market object, initializing AVG service times
	    FishMarket F = new FishMarket (180, 30.0, 30.0, 30.0);
	    //runs simulation for this time (seconds in two months)
	    F.run(2 * 30 * 60 * 60 * 24); 
	    
	}

}
