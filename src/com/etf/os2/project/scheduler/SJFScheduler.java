package com.etf.os2.project.scheduler;

import java.util.PriorityQueue;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;
import com.etf.os2.project.process.Pcb.ProcessState;

public class SJFScheduler extends Scheduler {
	private static long TAUSTART = 10;

	private float alfa;
	private boolean preempty;
	private PriorityQueue<PcbPrioritySJFS> queue;
	private int CPU;
	private long tau;

	public SJFScheduler(String[] args) // prvi argument stepen usredljavanja, drugi tip algoritma 0 nonpreemty !=0
										// preemty
	{
		alfa = Float.parseFloat(args[0]);
		int n = Integer.parseInt(args[1]);
		tau=Long.MAX_VALUE;
		if (n != 0)
			preempty = true;
		else
			preempty = false;
		queue = new PriorityQueue<>();
	}

	@Override
	public Pcb get(int cpuId) {
		
		if (queue.size() != 0) {
		
			PcbPrioritySJFS pp = queue.remove();
			Pcb pcb = pp.getPcb();
			pcb.setTimeslice(0);
			if (preempty) {
				if(pcb.isPreempt())
				pcb.setPreempt(!preempty);
				if(tau>pcb.getPcbData().getTau()) {
					tau = pcb.getPcbData().getTau();
					CPU=cpuId;
				}
			}
			return pcb;
		}
		
		return null;
	}

	@Override
	public void put(Pcb pcb) {
		if (pcb == null)
			return;
		PcbData pd = pcb.getPcbData();
		if (pd == null)
			pcb.setPcbData(new PcbData());

		if (ProcessState.CREATED == pcb.getPreviousState()) {
			pcb.getPcbData().setTau(TAUSTART);
		} else {
			if (ProcessState.BLOCKED == pcb.getPreviousState()) { // Resi ovo Lepse !!!!
				long time=0;
				if(pcb.getPcbData().getvRunTime()==0)
					time=pcb.getExecutionTime();
				else
					time=pcb.getPcbData().getvRunTime();
				pcb.getPcbData().setTau((long) ((time + pcb.getPcbData().getTau()) * alfa));

			} else if (ProcessState.RUNNING == pcb.getPreviousState()) {
				if(preempty)
					pcb.getPcbData().setvRunTime(pcb.getPcbData().getvRunTime()+pcb.getExecutionTime());
			}
		}
		if (preempty && pcb.getPcbData().getTau()<tau)
			Pcb.RUNNING[CPU].setPreempt(preempty);
		queue.add(new PcbPrioritySJFS(pcb));
	}

}
