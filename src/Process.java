public class Process {
	private double a, b, c;
	private int num, size, currentAddr, timeRun, faults, evictions, timeResident, nextAddr;
	private int[] loadTimes = new int[100];
	
	public Process() {
		
	}
	
	public Process(double a, double b, double c, double d, int num, int size) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.num = num;
		this.size = size;
		currentAddr = -1;
		timeRun = 0;
		faults = 0;
		evictions = 0;
		timeResident = 0;
	}
	
	public void addFault() {
		faults++;
	}
	
	public int getFaults() {
		return faults;
	}
	
	public void load(int time, int frame) {
		loadTimes[frame] = time;
	}
	
	public void evict(int time, int frame) {
		timeResident += (time - loadTimes[frame]);
		evictions++;
	}
	
	public int getEvictions() {
		return evictions;
	}
	
	public int getTimeResident() {
		return timeResident;
	}
	
	public int chooseNextAddress(String ref, int rand) {
		if (currentAddr == -1) {
			currentAddr = ((111 * num) % size);
		} else {
			if (ref.equals("a")) {
				currentAddr = (currentAddr + 1) % size;
			}
			if (ref.equals("b")) {
				currentAddr = (currentAddr - 5 + size) % size;
			}
			if (ref.equals("c")) {
				currentAddr = (currentAddr + 4) % size;
			}
			if (ref.equals("d")) {
				currentAddr = (rand % size);
			}
		}
		return currentAddr;			
	}
	
	public int getCurrentAddress() {
		timeRun ++;
		return currentAddr;
	}
	
	public void storeNextAddress(String ref, int rand) {
		if (ref.equals("a")) {
			currentAddr = (currentAddr + 1) % size;
		}
		if (ref.equals("b")) {
			currentAddr = (currentAddr - 5 + size) % size;
		}
		if (ref.equals("c")) {
			currentAddr = (currentAddr + 4) % size;
		}
		if (ref.equals("d")) {
			currentAddr = (rand % size);
		}
		nextAddr = currentAddr;
	}
	
	public int getNextAddress() {
		return nextAddr;
	}
	
	public int getTimeRun() {
		return timeRun;
	}
	
	public String chooseReference(int rand) {
		double num = (rand / (Integer.MAX_VALUE + 1d));
		if (num < a) {
			return "a";
		} else if (num < (a + b)) {
			return "b";
		} else if (num < (a + b + c)) {
			return "c";
		} else {
			return "d";
		}
	}
	
	public int getNum() {
		return num;
	}
}
