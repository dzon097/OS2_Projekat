package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;

public abstract class Scheduler {
    public abstract Pcb get(int cpuId);

    public abstract void put(Pcb pcb);

    public static Scheduler createScheduler(String[] args) {
    	int n = Integer.parseInt(args[0]);
    	String[] constructArgs = new String[args.length - 1];
    	java.lang.System.arraycopy(args, 1, constructArgs, 0, constructArgs.length);
    	switch(n) {
    	case 1:
    		return new SJFScheduler(constructArgs);
		case 2:
			return new MFQScheduler(constructArgs);
    	case 3:
    		return new CFairSchedular(constructArgs);
    	default:
    		java.lang.System.err.println("Invalid arguments for Scheduler");
            java.lang.System.exit(-1);
            return null;
    	}
    }
}
