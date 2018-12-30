package com.etf.os2.project.scheduler;

import java.util.LinkedList;
import java.util.PriorityQueue;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.process.PcbData;

public class MFQScheduler extends Scheduler {

	private long[] timeCvant;
//	private LinkedList<Pcb>[] level;		//Potrebno pratiti prioritet i raspored po procesorima
	private int numLevel;
	private PriorityQueue<PcbPriorityMFQS>[] level;

	@SuppressWarnings("unchecked")
	public MFQScheduler(String[] args) { // args: num of level and tmeCvant for itch
		if (args.length > 0)

			numLevel = Integer.parseInt(args[0]);

		if (args.length < numLevel + 1)
			java.lang.System.err.println("Invalid arguments\n\nMFQScheduler:\n" + "\t<NUM_LEVEL> <TIME_CVANT... > ");

		timeCvant = new long[numLevel];
//		level = new LinkedList[numLevel];
		level = new PriorityQueue[numLevel];
		for (int i = 0; i < numLevel; i++) {
			timeCvant[i] = Long.parseLong(args[i + 1]);
	//		level[i] = new LinkedList<>();					//ovde mi treba neka prioritetna struktura !!
			level[i] = new PriorityQueue<>();
		}
	}

	
	@Override
	public Pcb get(int cpuId) {
		for(int i =0; i<numLevel ; i++) {
			if(level[i].size()!=0) {
	//			Pcb pcb= level[i].removeFirst();
				 PcbPriorityMFQS p= level[i].remove();
				 Pcb pcb= p.getPcb();
				pcb.setTimeslice(timeCvant[i]);
				return pcb;
			}
		}
		return null;
	}

	@Override
	public void put(Pcb pcb) {
		if(pcb==null || ProcessState.IDLE == pcb.getPreviousState() || ProcessState.FINISHED == pcb.getPreviousState() ) return;
		int i=0;
		PcbData pd = pcb.getPcbData();
		if(pd==null) 
			pcb.setPcbData(new PcbData());
		if(ProcessState.CREATED == pcb.getPreviousState()) {
			i=0;
		}
		else
			if(ProcessState.BLOCKED == pcb.getPreviousState()) {
				int n = pcb.getPcbData().getMFQSLevel();
				i = (n-1 < 0) ? 0 :n-1; 
			}
		else
			if(ProcessState.RUNNING == pcb.getPreviousState()) {
				int n = pcb.getPcbData().getMFQSLevel();
				i = (n+1 >= numLevel) ? numLevel-1 : n+1 ; 
//				int k=0;
//				for(k=0; k<Pcb.RUNNING.length;k++)
//					if(pcb.getId() == Pcb.RUNNING[k].getId())
//						break;
//				pcb.getPcbData().setCPU(k);
			}
		
		pcb.getPcbData().setMFQSLevel(i);
//		level[i].add(pcb);
		boolean b = level[i].add(new PcbPriorityMFQS(pcb));
		if(!b) System.out.println("***** Nije ubacen u red **** ");
	}

}
