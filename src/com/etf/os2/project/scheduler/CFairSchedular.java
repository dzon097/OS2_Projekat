package com.etf.os2.project.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;
import com.etf.os2.project.process.Pcb.ProcessState;

public class CFairSchedular extends Scheduler {
	
	private List<Pcb> list;
	private final static long MINVRUN=5;
	
	public CFairSchedular(String[] args) {
		list = new ArrayList<Pcb>();
	}

	@Override
	public Pcb get(int cpuId) {
		long minWait= Long.MAX_VALUE;
		Pcb pcb=null;
		int k= Integer.MAX_VALUE;
		for(int i=0; i<list.size();i++) {
			if(list.get(i).getPcbData().getvRunTime()<minWait) {
				minWait=list.get(i).getPcbData().getvRunTime();
				k=i;
			}
		}
		if(k== Integer.MAX_VALUE)
			return null;
		pcb  = list.remove(k);
		long time = (list.size()==0 || (pcb.getPcbData().getWaitTime() - Pcb.getCurrentTime())/list.size() == 0) ?  MINVRUN  :  (pcb.getPcbData().getWaitTime() - Pcb.getCurrentTime())/list.size();
		pcb.setTimeslice(time);
		return pcb;
	}

	@Override
	public void put(Pcb pcb) {
		if(pcb==null || ProcessState.IDLE == pcb.getPreviousState() || ProcessState.FINISHED == pcb.getPreviousState() ) return;
		PcbData pd = pcb.getPcbData();
		if(pd==null) 
			pcb.setPcbData(new PcbData());
		pd = pcb.getPcbData();
		if(ProcessState.CREATED == pcb.getPreviousState() || ProcessState.BLOCKED == pcb.getPreviousState() ) {
				pd.setvRunTime(MINVRUN);
			}
		else
			if(ProcessState.RUNNING == pcb.getPreviousState()) {
				pd.setvRunTime(pd.getvRunTime()+pcb.getExecutionTime()* pcb.getPriority());
			}
		
		pcb.getPcbData().setWaitTime(Pcb.getCurrentTime());
		list.add(pcb);
	}

}
