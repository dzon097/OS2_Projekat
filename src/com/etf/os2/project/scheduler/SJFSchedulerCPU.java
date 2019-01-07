package com.etf.os2.project.scheduler;


import com.etf.os2.project.process.*;
import com.etf.os2.project.process.Pcb.ProcessState;

public class SJFSchedulerCPU extends Scheduler {

	private SJFScheduler[] QueueCPU;


	public SJFSchedulerCPU(String[] args) {
		int n = Integer.parseInt(args[0]);
		QueueCPU = new  SJFScheduler[n];
		
		String[] constructArgs = new String[args.length - 1];
    	java.lang.System.arraycopy(args, 1, constructArgs, 0, constructArgs.length);
		
		for (int i = 0; i < n; i++) {
			QueueCPU[i] = new SJFScheduler(constructArgs);
		}

	}

	@Override
	public Pcb get(int cpuId) {
		Pcb pcb= QueueCPU[cpuId].get(cpuId);
		if(pcb==null) {
			int size=1, i=0;
			for(int k=0; k<QueueCPU.length;k++)
				if(k!=cpuId && size<QueueCPU[k].getSize()) {
					size=QueueCPU[k].getSize();
					i=k;
				}
			pcb = QueueCPU[i].get(i);
			if(pcb!=null)
				pcb.getPcbData().setCPU(cpuId);
		}
		return pcb;
	}

	@Override
	public void put(Pcb pcb) {
		if (pcb == null)
			return;
		PcbData pd = pcb.getPcbData();
		if (pd == null)
			pcb.setPcbData(new PcbData());
		int i=0;
		if (ProcessState.CREATED == pcb.getPreviousState()) {
			
			int size=Integer.MAX_VALUE;
			for(int k=0; k<QueueCPU.length;k++)
				if(size>QueueCPU[k].getSize()) {
					size=QueueCPU[k].getSize();
					i=k;
				}
			
		} else if( ProcessState.BLOCKED == pcb.getPreviousState() ) {
			i =  pcb.getPcbData().getCPU();
			for(int k=0; k<QueueCPU.length;k++)
				if(QueueCPU[k].getSize()==0 ) {
					i=k;
					break;
				}
		}
			
		else if (ProcessState.RUNNING == pcb.getPreviousState()) {
			i =  pcb.getPcbData().getCPU();
		}
		
		pcb.getPcbData().setCPU(i);
		QueueCPU[i].put(pcb);
	}

}
