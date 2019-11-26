
public class Client {
	
	//time client arrives at market
	int marketArrivalTime;
	//how much money client has 
	int money;
	//how many fish the client can purchase with their money
	int numFish;
	//the times at which they arrived at the respective queues
	int atmQueueArrival;
	int shopQueueArrival;
	int checkQueueArrival;

	//default constructor
	Client(){
		//set all variables to 0
		marketArrivalTime = 0;
		money = 0;
		numFish = 0;
		
		atmQueueArrival = 0;
		shopQueueArrival = 0;
		checkQueueArrival = 0;
	}
	
	//constructor
	Client(int time){
		//arrival time is time provided
		marketArrivalTime = time;
		
		//money ammount determined randomly by by constructor (between 1-100)
		//randomly assign a number between 1 and 100
		money = (int) ((100 * Math.random()));
		
		//calculate the number fish the customer wants to buy
		numFish = money/10;
		
		//sets their queue arrival times to 0
		atmQueueArrival = 0;
		shopQueueArrival = 0;
		checkQueueArrival = 0;
		
	}
	
	//increases client's money at their visit to the ATM
	public void increaseMoney() {
		// incremenet their money randomly (amount between $10-40)
		money = money + ((int) (30 * Math.random() + 10));
		
		//update the clients number of fish they need
		updateFish();
	}
	
	//updates the number of fish after ATM visit
	public void updateFish() {
		numFish = money/10;
	}
	
}
