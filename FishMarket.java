import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class FishMarket {
	
	//average service times used to calcuate the instance service times
	private double INTERARRIVAL_TIME;
	private double ATM_AVG_SERVICE_TIME;
	private double AVG_SHOPPING_TIME_PER_FISH;
	private double AVG_CHECKOUT_TIME_PER_FISH;
	
	//FIFO Queues for each phase
	Queue<Client> ATMQueue;
	Queue<Client> shoppingQueue;
	Queue<Client> checkoutQueue;
	
	//Servers hold clients 
	Client ATM_Server;
	Client shopping_Server; 
	Client checkout_Server; 

	//Event queue - priority queue
	PriorityQueue<Event> eventQueue;
	
	//clock 
	public int clock = 1;
	//how long simulation runs for
	public int simulationTime;
	
	//use to collect statistics
	Statistics stats; 

	//default constructor
	FishMarket(){
		//initialize the Event Queue queue  
		eventQueue = new PriorityQueue<>();
		
		//initallize the server for each "phase"
		ATM_Server = null;
		shopping_Server = null;
		checkout_Server = null;
		
		//initialize the queue for each "phase"
		ATMQueue = new LinkedList<>();
		shoppingQueue = new LinkedList<>();
		checkoutQueue = new LinkedList<>();
		
		//initialize stats to collect data
		stats = new Statistics();
		
		//initiliaze simulation time to 0
		simulationTime = 0;
		
	}
	
	//constructor
	FishMarket(double arrivalTime, double serviceTime, double shoppingTime, double checkoutTime){
		
		//if any of the AVG times provided are less than 0, throw exception (simulation will not end)
		if (arrivalTime <= 0 || serviceTime < 0 || shoppingTime < 0 || checkoutTime < 0 ) {
			throw new IllegalArgumentException("Time cannot be negative. ");
		}
			
		//initialize average service times;
		INTERARRIVAL_TIME = arrivalTime;
		ATM_AVG_SERVICE_TIME = serviceTime;
		AVG_CHECKOUT_TIME_PER_FISH = shoppingTime;
		AVG_SHOPPING_TIME_PER_FISH = checkoutTime;
		
		//initialize the Event Queue queue  
		eventQueue = new PriorityQueue<>();
		
		//initallize the server for each "phase"
		ATM_Server = null;
		shopping_Server = null;
		checkout_Server = null;
		
		//initialize the queue for each "phase"
		ATMQueue = new LinkedList<>();
		shoppingQueue = new LinkedList<>();
		checkoutQueue = new LinkedList<>();
		
		//initialize stats to collect data
		stats = new Statistics();
		
	}
	
	//function to run the simulation 
	public void run (int simulation_time) {
		
		//if simulation time is negative, throw exception (simulation will not run)
		if (simulation_time <= 0) {
			throw new IllegalArgumentException("Time cannot be negative. ");
		}
		
		//initialize how long the simulation will run for
		simulationTime = simulation_time;
		
		//insert a new arrival (first market arrival)
		MARKET_Arrival mArrival = new MARKET_Arrival();
		//add this market arrival to the Event Queue
		eventQueue.add(mArrival);
		
		//while the clock is running 
		while (!eventQueue.isEmpty() ) {	
			//retrieves & removed first event to occur
			Event X = eventQueue.poll();
		
			//process that event
			X.process();
	
		}//close while loop
		
		//print how long the simulation ran for
		System.out.println("------ FISH MARKET SIMULATION ------\n");
		System.out.println("The simulation ran for " + (simulationTime/(30*24*60*60))%12 + " months " 
				+ (simulationTime/(24*60*60))%30 + " days " + (simulationTime/(60*60))%24 + " hours " 
				+ (simulationTime/(60))%60 + " minutes " + (simulationTime%(60)) + " seconds\n");
		//Prints statistics of the simulation
		stats.printStats();
		
	}//---end of run function
	
	//inner class - market arrival 
	public class MARKET_Arrival extends Event {
		
		//constructor
		MARKET_Arrival(){
			//this creates an arrival time, using current clock + calculated interarrival time
			time = (int) (clock + (RandBox.expo(INTERARRIVAL_TIME)));

		}

		@Override
		//process.. what happens at market arrival
		public void process(){
			//move the clock to the time of the arrival event
			clock = time;

			//create a client object with the current time of the clock as arrival time. 
			//simulates new client arrives at market at time "clock"
			Client client = new Client(clock);
			
			//increment the total number of clients 
			stats.totalClients++;
			
			//if client has less than $20, the client is assigned to the ATM server
			if (client.money < 20) {
				//if the ATM server is busy, the client is inserted into the ATM queue
				if (ATM_Server != null) {
					//set time client arrived at ATM queue to clock
					client.atmQueueArrival = clock;
					//add client to queue
					ATMQueue.add(client);
				}
				
				//otherwise place the a client in the ATM server, and schedule a new ATM_departure.  
				else if (ATM_Server == null) {
					//place the client in ATM Server
					ATM_Server = client;
					//schedule time to depart ATM
					ATM_Departure atmDep = new ATM_Departure();
					//add this event to the event queue
					eventQueue.add(atmDep);
				}
				
			}
			
			//if the customer does not need to get money, send them shopping
			if (client.money >= 20) {
				//if the shopping server is occupied
				if(shopping_Server != null) {
					//place the customer in the shopping queue
					shoppingQueue.add(client);
					//record client queue arrival time
					client.shopQueueArrival = clock;
					
				}
				//is the shopping server is empty
				else if (shopping_Server == null) {
					//place client in shopping server
					shopping_Server = client;
					//schedule a new shopping departure 
					SHOPPING_Departure shopDep = new SHOPPING_Departure(client.numFish);
					//add this event to the event queue
					eventQueue.add(shopDep);
					
				}
			}
			
			//for each market arrival, create a new market arrival that will arrive at time x
			MARKET_Arrival arrival = new MARKET_Arrival();
			if (arrival.time < simulationTime) {
				//add the market arrival to the event queue
				eventQueue.add(arrival);
			}
		}//---end of process
		
	}//---end of inner class MARKET_Arrival
	
	//inner class - ATM departure
	public class ATM_Departure extends Event {
		
		//constructor
		ATM_Departure(){
			//this creates an ATM Departure time, using current clock + calculated service time
			time = (int) (clock + RandBox.expo(ATM_AVG_SERVICE_TIME));
			//increment number of ATM visits
			stats.atmVisits++;
		}
		
		//process.. what happens at the ATM
		public void process() {
			//move the clock to the time of the Departure event
			clock = time;
			
			//intakes stats of ATM Queue length 
			stats.atmQueueLength += (ATMQueue.size());
	
			//if the ATM server is occupied, increase their money and send the client shopping
			if (ATM_Server != null) {
				
				//grab the client inside the server
				Client client = ATM_Server;
		
				// incremenet their money randomly (amount between $10-40)
				client.increaseMoney();

				//if the shopping server is full
				if(shopping_Server != null) {
					//add them to the Shopping queue
					shoppingQueue.add(client);
					//record queue arrival time
					client.shopQueueArrival = clock;
				}
				//otherwise if shopping server is empty
				else if (shopping_Server == null) {
					//set the shopping server to client c
					shopping_Server = client;
					//create a new shopping departure
					SHOPPING_Departure shopDep = new SHOPPING_Departure(client.numFish);
					//add event to event queue
					eventQueue.add(shopDep);
				}
			}
			
			//set the ATM_Server to null
			ATM_Server = null;

			//if  the client queue is NOT empty then remove the first client , set him to the ATM_Server 
			if (!ATMQueue.isEmpty()) {
				//set ATM server to next client, removing the client from the queue
				ATM_Server = ATMQueue.poll();
				//gather how long to customer was waiting for (in minutes)
				stats.atmQueueWait += ((clock - ATM_Server.atmQueueArrival)/60);
				//schedule a ATM_departure 
				ATM_Departure ATM_dep = new ATM_Departure();
				//add ATM dept to Event Queue
				eventQueue.add(ATM_dep);
			}
		}//---end of process
		
	}//---end of inner class - ATM_Dep
		
	//Inner class - SHOPPING_Departure
	public class SHOPPING_Departure extends Event {
		//default constructor -- assumes number of fish purchased is 1
		SHOPPING_Departure(){
			//this creates an shopping Departure time, using current clock + calculated service time per fish
			time = (int) (clock + RandBox.expo(AVG_SHOPPING_TIME_PER_FISH));
		}
		//Constructor
		SHOPPING_Departure(int numFish){
			//this creates an shopping Departure time, using current clock + calculated service time per fish
			time = (int) (clock + RandBox.expo(numFish * AVG_SHOPPING_TIME_PER_FISH));
		}
		
		@Override
		//process... what happens @ Shopping Departure
		public void process() {
			//move the clock to the time of the Departure event
			clock = time;
			
			//intakes stats of Shopping Queue length 
			stats.shopQueueLength += (shoppingQueue.size());
			
			//set the shopping_Server to null
			if (shopping_Server != null) {
				Client client = shopping_Server;
				
				//send client to checkout
				if (checkout_Server == null) {
					//send client to checkout server
					checkout_Server = client;
					//create a new Checkout departure
					CHECKOUT_Departure checkDep = new CHECKOUT_Departure(client.numFish);
					//add depature to event queue
					eventQueue.add(checkDep);
				}
				else if (checkout_Server != null) {
					//add client to checkout queue
					checkoutQueue.add(client);	
					//record queue arrival time
					client.checkQueueArrival = clock;
				}
			}
			
			//set shopping server to null
			shopping_Server = null;
			//if  the client queue is NOT empty then remove the first client , set him to the Shopping_Server 
			if (!shoppingQueue.isEmpty()) {
				//send the firt customer in the queue to the shopping server
				shopping_Server = shoppingQueue.poll();
				//gather how long to customer was waiting for (in minutes)
				stats.shopQueueWait += ((clock - shopping_Server.shopQueueArrival)/60);
				//schedule a ATM_departure 
				SHOPPING_Departure shop_dep = new SHOPPING_Departure(shopping_Server.numFish);
				//add ATM departure to event queue
				eventQueue.add(shop_dep);
			}
		}//---end of process
	
	}//---end of inner class - Shopping Departure

	//Inner class - Checkout_Departure
	public class CHECKOUT_Departure extends Event {
		//default constructor -- assumes number of fish purchased is 1
		CHECKOUT_Departure(){
			//this creates a checkout Departure time, using current clock + calculated service time per fish
			time = (int) (clock + RandBox.expo(AVG_CHECKOUT_TIME_PER_FISH));
		}
		
		//constructor
		CHECKOUT_Departure(int numFish){
			//this creates a checkout Departure time, using current clock + calculated service time per fish
			time = (int) (clock + RandBox.expo(numFish * AVG_CHECKOUT_TIME_PER_FISH));
		}
		
		@Override
		//process.. what happens at checkout
		public void process() {
			//move the clock to the time of the Departure event
			clock = time;
			
			//intakes stats of Shopping Queue length 
			stats.checkQueueLength += (checkoutQueue.size());
			
			//if the checkout server not empty
			if (checkout_Server != null) {
				//gather the number of fish purchases to stats total numFish
				stats.numFish += checkout_Server.numFish;
				//gather the total time spent at the market to stats totalTime (in minutes)
				stats.totalTime += ((clock - checkout_Server.marketArrivalTime)/60);
			}
			
			//set the Checkout server to null
			checkout_Server = null;
			
			//if the client queue is NOT empty 
			if (!checkoutQueue.isEmpty()) {
				//then remove the first client, set him to the server 
				checkout_Server = checkoutQueue.poll();	 
				//gather how long to customer was waiting for
				stats.checkQueueWait += ((clock - checkout_Server.checkQueueArrival)/60);
				//and schedule a departure
				CHECKOUT_Departure check_dep = new CHECKOUT_Departure(checkout_Server.numFish);
				//add departure to event queue
				eventQueue.add(check_dep);
			}
			
		}//---end of process
		
	}//---end of inner class - Checkout Departure

	
}//---end of Fish Market Class
