package test.okay;

import test.something.SomethingMain;
import test.yogurt.Yoga;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Halooo." + getSomething("gaga", 2));
	}

	private static double getSomething(String p, int i) {
		Okay o = new Okay();
		SomethingMain m = new SomethingMain(o);
		SomethingMain f = new SomethingMain(o);
		
		Yoga k = new Yoga();
		k.yogaPrint();
		
		returnOkay(o);
		System.out.println(m.toString());
		System.out.println(f.toString() + "sdasds");
		return 15000;
	}
	
	private static void returnOkay(Okay o) {
		System.out.println("o");
	}
	
}
