package com.etf.os2.project.scheduler.visak;

import java.util.PriorityQueue;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.scheduler.Scheduler;

public class SJFSchedulerCPU extends Scheduler {
	private static long TAUSTART = 2;

	private float alfa;
	private boolean preempty;
	private int numCPU;
	private PriorityQueue<PcbPrioritySJFS>[] queue;
	private int numProces;

	public SJFSchedulerCPU(String[] args) /**
										 * prvi argument stepen usredljavanja, drugi tip algoritma 0 nonpreemty !=0
										 * preemty
										 */
	{
		alfa = Float.parseFloat(args[0]);
		int n = Integer.parseInt(args[1]);
		if (n != 0)
			preempty = true;
		else
			preempty = false;
		numCPU = Integer.parseInt(args[2]);
		
		queue = new PriorityQueue[numCPU];
		
		for(int i=0; i<numCPU;i++)
			queue[i] = new PriorityQueue<>();
		numProces=0;
	}

	@Override
	public Pcb get(int cpuId) {
		if (numProces != 0) {
			PcbPrioritySJFS pp=null;
			if(queue[cpuId].size()!=0) 
				pp = queue[cpuId].remove();
			else
				pp= queue[get()].remove();
			numProces--;
			
			Pcb pcb = pp.getPcb();
			pcb.setTimeslice(0);
			if (preempty)
				Pcb.RUNNING[cpuId].setPreempt(!preempty);

		
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

		int idCPU=0;
		
		if (ProcessState.CREATED == pcb.getPreviousState()) {
			pcb.getPcbData().setTau(TAUSTART);
			idCPU=put();
		} else {
			if (ProcessState.BLOCKED == pcb.getPreviousState()) {
				pcb.getPcbData().setTau((long) ((pcb.getExecutionTime() + pcb.getPcbData().getTau()) * alfa));

			} else if (ProcessState.RUNNING == pcb.getPreviousState()) {
				for (idCPU = 0;idCPU < Pcb.RUNNING.length; idCPU++)
					if (pcb.getId() == Pcb.RUNNING[idCPU].getId())
						break;
				pcb.getPcbData().setCPU(idCPU);
			}
			if (preempty)
				Pcb.RUNNING[pcb.getPcbData().getCPU()].setPreempt(preempty);
		}

		boolean b = queue[idCPU].add(new PcbPrioritySJFS(pcb));
		numProces++;
		if (!b)
			System.out.println("***** Nije ubacen u red **** ");

	}
	
	private int put() {
		int i=0;
		int minProc= Integer.MAX_VALUE;
		for(int j=0; j<numCPU;j++)
			if(minProc>queue[j].size()) {
				minProc=queue[j].size();
				i=j;
			}
		return i;
	}
	
	private int get() {
		int i=0;
		int minProc= -1;
		for(int j=0; j<numCPU;j++)
			if(minProc<queue[j].size()) {
				minProc=queue[j].size();
				i=j;
			}
		return i;
	}

}
