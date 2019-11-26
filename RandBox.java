import java.util.Random;

//class that determines the instance service time using
//the average service time given (randomly)

public class RandBox extends Random {
	
	static double expo(double mean) {
		
      double x = Math.random();
      x = -mean * Math.log(x);
 
      return x;
      
    }
	
}

