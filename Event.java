//class implements, used for event Priority Queue

abstract class Event implements Comparable {
	
	int time;

	@Override
	//compareTo method, used to place events in priority queue
	//determines if thisevent.time occurs before, after, or same as 
	//each event in the queue
	public int compareTo(Object o) {
		Event e = (Event) o;
		if (this.time > e.time)
			return 1;
		if (this.time < e.time)
			return -1;
		else 
			return 0;
	}
	
	//process method needs to be defined for each subclass
	abstract public void process ();

}
