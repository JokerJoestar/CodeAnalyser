package test.something;

import test.okay.Okay;
import test.yogurt.Yoga;

public class SomethingMain {
	private Okay o;
	
	public SomethingMain(Okay o) {
		this.o = o;
		
		if(o != null)
			Yoga.yogaStatic();
	}
	
	@Override
	public String toString() {
		return "Everything is okay." + o.pr();
	}

}
