package com.etf.os2.project.scheduler.visak;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.etf.os2.project.process.*;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.scheduler.Scheduler;

public class MFQScheduler2 extends Scheduler {

	private long[] timeCvant;
	private PriorityQueue<Pcb>[] level;
	
	class PcbCompartor implements Comparator<Pcb>{
		@Override
		public int compare(Pcb p1, Pcb p2) {
			if (p1.getPriority() < p2.getPriority()) 
				return 0; 
			else if (p1.getPriority() > p2.getPriority()) 
				return 1; 
			return 0;
		}	
	}

	@SuppressWarnings("unchecked")
	public MFQScheduler2(String[] args) {
		int numLevel = 0;
		if (args.length > 0)
			numLevel = Integer.parseInt(args[0]);
		if (args.length < numLevel + 1)
			java.lang.System.err.println("Invalid arguments\n\nMFQScheduler:\n" + "\t<NUM_LEVEL> <TIME_CVANT... > ");

		timeCvant = new long[numLevel];
		level = new PriorityQueue[numLevel];
		PcbCompartor comparator = new PcbCompartor();
		for (int i = 0; i < numLevel; i++) {
			timeCvant[i] = Long.parseLong(args[i + 1]);
			level[i] = new PriorityQueue<Pcb>(10, comparator);
		}
	}

	@Override
	public Pcb get(int cpuId) {
		for (int i = 0; i < level.length; i++) {
				Pcb pcb =  level[i].poll();
				if(pcb!=null) {
				pcb.setTimeslice(timeCvant[i]);
				return pcb;
			}
		}
		return null;
	}

	@Override
	public void put(Pcb pcb) {
		if (pcb == null || ProcessState.IDLE == pcb.getPreviousState() || ProcessState.FINISHED == pcb.getPreviousState())
			return;
		int i = 0;
		PcbData pd = pcb.getPcbData();
		if (pd == null)
			pcb.setPcbData(new PcbData());
		if (ProcessState.CREATED == pcb.getPreviousState()) {
			i = 0;
		} else if (ProcessState.BLOCKED == pcb.getPreviousState()) {
			int n = pcb.getPcbData().getMFQSLevel();
			i = (n - 1 < 0) ? 0 : n - 1;
		} else if (ProcessState.RUNNING == pcb.getPreviousState()) {
			int n = pcb.getPcbData().getMFQSLevel();
			i = (n + 1 >= level.length) ? level.length - 1 : n + 1;
		}

		pcb.getPcbData().setMFQSLevel(i);
		level[i].add(pcb);
	}

}
