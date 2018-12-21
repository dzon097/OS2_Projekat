package mn160304;

public abstract class Scheduler {

	public abstract Pcb get(int cpuId);
	
	public abstract void put(Pcb pcb);
	
	public static Scheduler createScheduler(String[] args) {
		
	}
}
