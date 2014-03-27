package cityline.peter.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Stateful;

@Stateful
@Remote(RemoteCounter.class)
public class CounterBean implements RemoteCounter {

	   private int count = 0;
	   
	   @PostConstruct
	   public void init() {
	   System.out.println("EJB inited!");
	   }
	   
	    @Override
	    public void increment() {
	        this.count++;
	    }
	 
	    @Override
	    public void decrement() {
	        this.count--;
	    }
	 
	    @Override
	    public int getCount() {
	        return this.count;
	    }
}
