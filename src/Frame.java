public class Frame {
	int page;
	Process resident;
	int lastUsed;
	int in;
	
	public Frame(int frSize) {
		resident = null;
	}
	
	public void setIn(int time) {
		in = time;
	}
	
	public int getIn() {
		return in;
	}
	
	public void setLastUsed(int time) {
		lastUsed = time;
	}
	
	public int getLastUsed() {
		return lastUsed;
	}
	
	public void setResident(Process p) {
		resident = p;
	}
	
	public Process getResident() {
		return resident;
	}
	
	public void setPage(int page) {
		this.page = page; 
	}
	
	public int getPage() {
		return page;
	}
}
