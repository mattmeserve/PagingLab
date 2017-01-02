import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		/*String[] args1 = new String[6];
		args1[0] = "20";
		args1[1] = "10";
		args1[2] = "10";
		args1[3] = "2";
		args1[4] = "10";
		args1[5] = "random";*/
		System.out.println();
		int mSize = Integer.parseInt(args[0]);
		int pgSize = Integer.parseInt(args[1]);
		int prSize = Integer.parseInt(args[2]);
		int j = Integer.parseInt(args[3]);
		int numRefs = Integer.parseInt(args[4]);
		String alg = args[5];
		
		System.out.println("The machine size is " + mSize);
		System.out.println("The page size is " + pgSize);
		System.out.println("The process size is " + prSize);
		System.out.println("The job mix number is " + j);
		System.out.println("The number of references per process is " + numRefs);
		System.out.println("The replacement algorithm is " + alg + "\n");
		
		int numFrames = mSize / pgSize;

		
		try {
			File file = new File("RANDOMNUMBERS.txt");
			Scanner sc = new Scanner(file);
			
			ArrayList<Process> processes = new ArrayList<Process>();
			processes.add(new Process());
			if (j == 1) {
				processes.add(new Process(1, 0, 0, 0, 1, prSize));
			}
			
			
			if (j == 2) {
				for (int i = 1; i < 5; i ++) {
					processes.add(new Process(1, 0, 0, 0, i, prSize));
				}
			}
			
			
			if (j == 3) {
				for (int i = 1; i < 5; i ++) {
					processes.add(new Process(0, 0, 0, 1, i, prSize));
				}
			}
			
			
			if (j == 4) {
				processes.add(new Process(0.75, 0.25, 0, 0, 1, prSize));
				processes.add(new Process(0.75, 0, 0.25, 0, 2, prSize));
				processes.add(new Process(0.75, 0.125, 0.125, 0, 3, prSize));
				processes.add(new Process(0.5, 0.125, 0.125, 0.25, 4, prSize));
			}
			
			ArrayList<Frame> frameTable = new ArrayList<Frame>();
			for (int i = 0; i < numFrames; i ++) {
				frameTable.add(new Frame((prSize / pgSize)));
			}
			
			int refRand, dRand, algRand, addr, page;
			int time = 1;
			String ref;
			while (time <= (numRefs * (processes.size() - 1))) {
				for (int i = 1; i < processes.size(); i ++) {
					for (int k = 0; k < 3; k ++) {
						if (processes.get(i).getTimeRun() < numRefs) {
							dRand = 0;
							refRand = sc.nextInt();
							//System.out.println("Ref: " + refRand);
							ref = processes.get(i).chooseReference(refRand);
							if (processes.get(i).getCurrentAddress() == -1) {
								addr = processes.get(i).chooseNextAddress(ref, dRand);
								if (ref.equals("d")) {
									dRand = sc.nextInt();
								}
								processes.get(i).storeNextAddress(ref, dRand);
							} else {
								addr = processes.get(i).getNextAddress();
								if (ref.equals("d")) {
									dRand = sc.nextInt();
								}
								processes.get(i).storeNextAddress(ref, dRand);
							}
							
							page = addr / pgSize;
							//System.out.print("At time " + time + " Process " + i + " references word " + addr + " (page " + page + "): ");
							boolean foundHit = false;
							int firstFree = -1;
							
							
							for (int x = frameTable.size() - 1; x > -1; x --) {
								if (frameTable.get(x).getResident() == processes.get(i) && !foundHit && frameTable.get(x).getPage() == (addr / pgSize)) {
									//System.out.println("Hit in frame " + x);
									frameTable.get(x).setLastUsed(time);
									foundHit = true;
								}
								if (frameTable.get(x).getResident() == null && firstFree == -1) {
									firstFree = x;
								}
							}
							if (!foundHit && firstFree != -1) {
								processes.get(i).load(time, firstFree);
								processes.get(i).addFault();
								//System.out.println("Fault, using free frame " + firstFree);
								frameTable.get(firstFree).setPage((addr / pgSize));
								frameTable.get(firstFree).setIn(time);
								frameTable.get(firstFree).setLastUsed(time);
								frameTable.get(firstFree).setResident(processes.get(i));
							}
							if (!foundHit && firstFree == -1) {
								processes.get(i).addFault();
								if (alg.equals("lru")) {
									int lru = frameTable.get(0).getLastUsed();
									int lruFrame = 0;
									for (int x = 1; x < frameTable.size(); x ++) {
										if (frameTable.get(x).getLastUsed() < lru) {
											lruFrame = x;
											lru = frameTable.get(x).getLastUsed();
										}
									}
									//System.out.println("Fault, evicting page " + frameTable.get(lruFrame).getPage() + " of process " + frameTable.get(lruFrame).getResident().getNum() + " from frame " + lruFrame);
									frameTable.get(lruFrame).setPage((addr / pgSize));
									frameTable.get(lruFrame).getResident().evict(time, lruFrame);
									frameTable.get(lruFrame).setLastUsed(time);
									frameTable.get(lruFrame).setResident(processes.get(i));
									processes.get(i).load(time, lruFrame);
								} else if (alg.equals("lifo")) {
									int lastIn = frameTable.get(0).getIn();
									int lastFrame = 0;
									for (int x = 1; x < frameTable.size(); x ++) {
										if (frameTable.get(x).getIn() > lastIn) {
											lastFrame = x;
											lastIn = frameTable.get(x).getIn();
										}
									}
									//System.out.println("Fault, evicting page " + frameTable.get(lastFrame).getPage() + " of process " + frameTable.get(lastFrame).getResident().getNum() + " from frame " + lastFrame);
									frameTable.get(lastFrame).setPage((addr / pgSize));
									frameTable.get(lastFrame).getResident().evict(time, lastFrame);
									frameTable.get(lastFrame).setIn(time);
									frameTable.get(lastFrame).setResident(processes.get(i));
									processes.get(i).load(time, lastFrame);
								} else if (alg.equals("random")) {
									algRand = sc.nextInt();
									algRand = refRand;
									//System.out.print("algRand: " + algRand);
									int randFrame = algRand % frameTable.size();
									//System.out.println("Fault, evicting page " + frameTable.get(randFrame).getPage() + " of process " + frameTable.get(randFrame).getResident().getNum() + " from frame " + randFrame);
									frameTable.get(randFrame).setPage((addr / pgSize));
									frameTable.get(randFrame).getResident().evict(time, randFrame);
									frameTable.get(randFrame).setIn(time);
									frameTable.get(randFrame).setResident(processes.get(i));
									processes.get(i).load(time, randFrame);
								}
								
							}
							time ++;
						} else {
							k = 3;
						}
					}
					
					
				}
			}
			
			int totalFaults = 0;
			int totalRes = 0;
			int totalEvic = 0;
			for (int i = 1; i < processes.size(); i ++) {
				int faults = processes.get(i).getFaults();
				double res = (double)processes.get(i).getTimeResident() / processes.get(i).getEvictions();
				totalRes += processes.get(i).getTimeResident();
				totalEvic += processes.get(i).getEvictions();
				if (processes.get(i).getEvictions() == 0) {
					System.out.println("Process " + i + " had " + faults + " faults.\n\tWith no evictions, the average residence is undefined.");
				} else {
					System.out.println("Process " + i + " had " + faults + " faults and " + res + " average residency.");
				}
				totalFaults += faults;
			}
			System.out.println();
			double avgRes = (double)totalRes / totalEvic;
			if (totalEvic == 0) {
				System.out.println("The total number of faults is " + totalFaults + ".\n\tWith no evictions, the overall average residence is undefined.\n");
			} else {
				System.out.println("The total number of faults is " + totalFaults + " and the overall average residency is " + avgRes + ".\n");
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
		}
		
	}
	
	
}
