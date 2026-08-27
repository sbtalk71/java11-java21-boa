import java.util.ArrayList;


public class MemoryEater {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		ArrayList list = new ArrayList();
		Thread.sleep(5000); // 5s to allow starting visualvm
		for (long l = 0; l < Long.MAX_VALUE; l++) {
			list.add(new Long(l));
			for (int i = 0; i  < 5000; i++) {
				// busy wait, 'cause 1ms sleep is too long
				if (i == 5000) break;
			}
		}

	}

}
