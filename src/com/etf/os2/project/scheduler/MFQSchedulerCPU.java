package com.etf.os2.project.scheduler;


import com.etf.os2.project.process.*;
import com.etf.os2.project.process.Pcb.ProcessState;

public class MFQSchedulerCPU extends Scheduler {

	private MFQScheduler[] QueueCPU;

	
	public MFQSchedulerCPU(String[] args) { 
		int numCPU = Integer.parseInt(args[0]);
		String[] constructArgs = new String[args.length - 1];
    	java.lang.System.arraycopy(args, 1, constructArgs, 0, constructArgs.length);
		
		QueueCPU = new MFQScheduler[numCPU];
		for (int i = 0; i < numCPU; i++) {
			QueueCPU[i] = new MFQScheduler(constructArgs);
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
		int i = 0;
		PcbData pd = pcb.getPcbData();
		if (pd == null)
			pcb.setPcbData(new PcbData());
		
		if (ProcessState.CREATED == pcb.getPreviousState() ) {
			int size=Integer.MAX_VALUE;
			for(int k=0; k<QueueCPU.length;k++)
				if(size>QueueCPU[k].getSize()) {
					size=QueueCPU[k].getSize();
					i=k;
				}
			
		}
		else if( ProcessState.BLOCKED == pcb.getPreviousState() ) {
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
