
public class Statistics {

	//gathers the total time customers spent waiting in each queue
	double atmQueueWait;
	double shopQueueWait;
	double checkQueueWait;
	
	//counts number of ATM visits
	int atmVisits;
	
	//counts total number of queue length @ each respective visit 
	double atmQueueLength; 
	double shopQueueLength;
	double checkQueueLength;
	
	//counts total number of fish purchases
	double numFish;
	
	//counts total number of clients that came thru the market
	int totalClients;
	//counts total time spent at the market
	double totalTime;
	
	//constructor
	Statistics ( ){
		atmQueueWait = 0;
		shopQueueWait = 0;
		checkQueueWait = 0;
		
		atmVisits = 0;
		
		atmQueueLength = 0; 
		shopQueueLength = 0; 
		checkQueueLength = 0; 
		
		totalClients = 0;
		totalTime = 0;
		
		numFish = 0;
	}
	
	//returns average ATM queue length
	public double average_ATM_queue_length() {
		if (atmVisits == 0) {
			return 0;
		}
		//divide total length gathered by ATMvisits since not everyone visits ATM
		return atmQueueLength/atmVisits;
	}
	
	//returns average Shopping queue length
	public double average_shop_queue_length() {
		if (totalClients == 0) {
			throw new IllegalArgumentException("Cannot divide by 0. \nNo one came to market.");
		}
		//divide total length gathered by totalClients since everyone goes to shop
		return shopQueueLength/totalClients;
	}
	
	//returns average Checkout queue length
	public double average_check_queue_length() {
		if (totalClients == 0) {
			throw new IllegalArgumentException("Cannot divide by 0. \nNo one came to market.");
		}
		//divide total length gathered by totalClients since everyone goes to checkout
		return checkQueueLength/totalClients;
	}
	
	//returns average ATM queue wait
	public double average_ATM_queue_wait() {
		if (atmVisits == 0) {
			return 0;
		}
		//divide total waitTime gathered by ATMVisits since not everyone visits ATM
		return atmQueueWait/atmVisits;
	}
	
	//returns average Shopping queue wait
	public double average_shop_queue_wait() {
		if (totalClients == 0) {
			throw new IllegalArgumentException("Cannot divide by 0. \nNo one came to market.");
		}
		//divide total waitTime gathered by totalClients since everyone goes to shop
		return shopQueueWait/totalClients;
	}
	
	//returns average Checkout queue wait
	public double average_check_queue_wait() {	
		if (totalClients == 0) {
			throw new IllegalArgumentException("Cannot divide by 0. \nNo one came to market.");
		}
		//divide total waitTime gathered by totalClients since everyone goes to checkout
		return checkQueueWait/totalClients;
	}
	
	//returns average time spent at the market
	public double average_market_time() {
		if (totalClients == 0) {
			throw new IllegalArgumentException("Cannot divide by 0. \nNo one came to market.");
		}
		//divides total time gathered spent @ market by totalClients 
		return totalTime/totalClients;
	}
	
	//prints the stats gathered from the simulation
	public void printStats() {
		//prints the total overall statistics
		System.out.println("\n--Overall Market Statistics gathered--");
		System.out.println("Total customers that came through the Market: " + totalClients + " people");
		System.out.println("Average time spent at the market: " + (int) (average_market_time()) + " minutes " +  (int)((average_market_time()*60)%60) + " seconds ");
		System.out.printf("Average number of fish each customer purchased: %.2f fish\n\n", (numFish/totalClients));
		
		//prints ATM statistics
		System.out.println("\n--ATM Statistics gathered--");
		System.out.println("Total customers that visited the ATM: " + atmVisits);
		System.out.printf("Average ATM queue wait time: %d minutes %.2f seconds\n", Math.round(average_ATM_queue_wait()), ((average_ATM_queue_wait()*60)%60));
		System.out.printf("Average ATM queue length: %.2f people\n\n", average_ATM_queue_length());

		//prints Shopping statistics
		System.out.println("\n--Shopping Statistics gathered--");
		System.out.println("Average shopping queue wait time: " + (int)(average_shop_queue_wait()) + " minutes "  + (int)((average_shop_queue_wait()*60)%60) + " seconds ");
		System.out.printf("Average shopping queue length:  %.2f people\n\n", average_shop_queue_length());
		
		//prints Checkout statistics
		System.out.println("\n--Checkout Statistics gathered--");
		System.out.println("Average checkout queue wait time: " + (int)average_check_queue_wait() + " minutes "  + (int)((average_check_queue_wait()*60)%60) + " seconds ");
		System.out.printf("Average checkout queue length:  %.2f people\n\n", average_check_queue_length());
		
	}
}
