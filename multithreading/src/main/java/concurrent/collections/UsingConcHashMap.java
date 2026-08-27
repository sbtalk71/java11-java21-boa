package concurrent.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UsingConcHashMap {

	public static void main(String[] args) throws Exception{
		//Map<String,String> mapdata=new HashMap<>();
		Map<String,String> mapdata=new ConcurrentHashMap<>();

		Thread t1= new Thread(new MapWriter(mapdata));
		Thread t2= new Thread(new MapWriter(mapdata));
		Thread t3= new Thread(new MapWriter(mapdata));
		Thread t4= new Thread(new MapWriter(mapdata));

		t1.start();
		t2.start();
		t3.start();
		t4.start();

		t1.join();
		t2.join();
		t3.join();
		t4.join();
		System.out.println("The size of Map "+mapdata.size());

	}

}

class MapWriter implements Runnable{
	private Map<String, String> dataMap;
	
	public MapWriter(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}

	@Override
	public void run() {
		String tname=Thread.currentThread().getName();
		System.out.println("Thread in action : "+tname);
		for(int i=0;i<100;i++) {
			dataMap.put(tname+i, tname+i);
			try {
				Thread.sleep(120);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}


