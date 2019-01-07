package com.etf.os2.project.scheduler.visak;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.etf.os2.project.process.*;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.scheduler.Scheduler;

public class SJFSchedulerSingleCPU extends Scheduler {
	private static long TAUSTART = 1;
	private static int INITVEL = 20;

	private float alfa;
	private boolean preempty;
	private PriorityQueue<Pcb> queue;
	private PcbCompartor comparator;
	private int size;

	class PcbCompartor implements Comparator<Pcb> {
		@Override
		public int compare(Pcb p1, Pcb p2) {	
			long tau1 = p1.getPcbData().getTau(), tau2 = p2.getPcbData().getTau();
			if(tau1 == tau2) {
				if (p1.getPriority() < p2.getPriority())
					return -1;
				else if (p1.getPriority() > p2.getPriority())
					return 1;
				else
				return 0;
			}
			else if (tau1 > tau2)
				return 1;
			else
				return -1;
		}
	}

	public SJFSchedulerSingleCPU(String[] args) {
		alfa = Float.parseFloat(args[0]);
		int n = Integer.parseInt(args[1]);

		if (n != 0)
			preempty = true;
		else
			preempty = false;

		comparator = new PcbCompartor();
		queue = new PriorityQueue<>(INITVEL, comparator);
		size=0;
	}

	@Override
	public Pcb get(int cpuId) {
		if (queue.size() != 0) {
			Pcb pcb = queue.remove();
			pcb.setTimeslice(0);

			if (preempty) {
				if (pcb.isPreempt())
					pcb.setPreempt(!preempty);
			}
			size--;
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
			if (ProcessState.BLOCKED == pcb.getPreviousState() || ProcessState.RUNNING == pcb.getPreviousState() )		
				pcb.getPcbData().setTau((long) ((pcb.getExecutionTime() + pcb.getPcbData().getTau()) * alfa));
		}

		if (preempty) {
			for (int k = 0; k < Pcb.RUNNING.length; k++)
				if (Pcb.RUNNING[k] != null && Pcb.RUNNING[k] != Pcb.IDLE
						&& Pcb.RUNNING[k].getPcbData().getTau() > pcb.getPcbData().getTau()) {
					Pcb.RUNNING[k].setPreempt(true);
					break;
				}
		}
		size++;
		queue.add(pcb);
	}
	
	public int getSize() {
		return size;
	}

}
