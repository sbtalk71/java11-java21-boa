import java.util.Vector;
import java.lang.ref.*;

public class OutOfmemory1 {

	public static void main(String[] args) {
		// Vector<WeakReference<Long>> store= new Vector<WeakReference<Long>>();
		Vector<Long> store = new Vector<Long>();
		for (int i = 0; true; i++) {
			// WeakReference<Long> ref1= new WeakReference<Long>(new
			// Long(9999));
			store.add(new Long(99999));
			// store.add(ref1);
			System.out.println("Size= " + store.size() + "loop = " + i);
			/*
			 * if(i%100==0) { System.gc(); System.out.println("collected.....");
			 * }
			 */
		}

	}

}
