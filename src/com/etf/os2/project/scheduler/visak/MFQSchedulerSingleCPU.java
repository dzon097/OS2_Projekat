package com.etf.os2.project.scheduler.visak;


import com.etf.os2.project.process.*;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.scheduler.LevelMFQS;

public class MFQSchedulerSingleCPU {

	private long[] timeCvant;
	private LevelMFQS[] level;
	private int size;

	
	public MFQSchedulerSingleCPU(String[] args) { 
		int numLevel = 0;
		size=0;
		if (args.length > 0)
			numLevel = Integer.parseInt(args[0]);
		if (args.length < numLevel + 1)
			java.lang.System.err.println("Invalid arguments\n\nMFQScheduler:\n" + "\t<NUM_LEVEL> <TIME_CVANT... > ");

		timeCvant = new long[numLevel];
		level = new LevelMFQS[numLevel];
		for (int i = 0; i < numLevel; i++) {
			timeCvant[i] = Long.parseLong(args[i + 1]);
			level[i] = new LevelMFQS();
		}
	}

	public Pcb get() {
		for (int i = 0; i < level.length; i++) {
			if (level[i].size() != 0) {
				Pcb pcb =  level[i].remove();
				pcb.setTimeslice(timeCvant[i]);
				size--;
				return pcb;
			}
		}
		return null;
	}

	public void put(Pcb pcb) {
		if (pcb == null || ProcessState.IDLE == pcb.getPreviousState()
				|| ProcessState.FINISHED == pcb.getPreviousState())
			return;
		int i = 0;
		PcbData pd = pcb.getPcbData();
		if (pd == null)
			pcb.setPcbData(new PcbData());
		if (ProcessState.CREATED == pcb.getPreviousState()) {
			i = level.length/2;
		} else if (ProcessState.BLOCKED == pcb.getPreviousState()) {
			int n = pcb.getPcbData().getMFQSLevel();
			i = (n - 1 < 0) ? 0 : n - 1;
		} else if (ProcessState.RUNNING == pcb.getPreviousState()) {
			int n = pcb.getPcbData().getMFQSLevel();
			i = (n + 1 >= level.length) ? level.length - 1 : n + 1;
		}

		pcb.getPcbData().setMFQSLevel(i);
		size++;
		level[i].add(pcb);
	}
	
	public int getSize() {
		return size;
	}

}
