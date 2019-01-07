package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.*;
import com.etf.os2.project.process.Pcb.ProcessState;

public class CFairSchedularCPU extends Scheduler {
	
	private CFairSchedular[] QueueCPU;

	public CFairSchedularCPU(String[] args) {
		int n = Integer.parseInt(args[0]);
		QueueCPU = new  CFairSchedular[n];	
		for (int i = 0; i < n; i++) {
			QueueCPU[i] = new CFairSchedular();
		}
	}

	@Override
	public Pcb get(int cpuId) {
		Pcb pcb= QueueCPU[cpuId].get(cpuId);
		if(pcb==null) {
			int size=0, i=0;
			for(int k=0; k<QueueCPU.length;k++)
				if(k!=cpuId && size<QueueCPU[k].getSize()) {
					size=QueueCPU[k].getSize();
					i=k;
				}
			if(size!=0) {
			pcb = QueueCPU[i].get(i);
				pcb.getPcbData().setCPU(cpuId);
			}
		}
		return pcb;
	}

	@Override
	public void put(Pcb pcb) {
		PcbData pd = pcb.getPcbData();
		if (pd == null)
			pcb.setPcbData(new PcbData());
		pd = pcb.getPcbData();
		
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
//			for(int k=0; k<QueueCPU.length;k++)
//				if(QueueCPU[k].getSize()==0 ) {
//					i=k;
//					break;
//				}
		}
			
		else if (ProcessState.RUNNING == pcb.getPreviousState()) {
			i =  pcb.getPcbData().getCPU();
		}
		
		pcb.getPcbData().setCPU(i);
		QueueCPU[i].put(pcb);
	}	

}
