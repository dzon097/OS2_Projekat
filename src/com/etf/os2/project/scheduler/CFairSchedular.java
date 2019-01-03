package com.etf.os2.project.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;
import com.etf.os2.project.process.Pcb.ProcessState;

public class CFairSchedular extends Scheduler {
	
	private List<Pcb> list;
	private final static long MINVRUN=10;
	
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
		long t =Pcb.getCurrentTime() - pcb.getPcbData().getWaitTime();
//		long time = (list.size()==0 || (t)/Pcb.getProcessCount() == 0) ?  MINVRUN  : (t)/Pcb.getProcessCount();
//		long time = (pcb.getPcbData().getWaitTime() - Pcb.getCurrentTime())/list.size() + MINVRUN;
		long time = t /Pcb.getProcessCount();
		pcb.setTimeslice(time== 0 ? MINVRUN: time);
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
				pd.setvRunTime(0);
			}
		else
			if(ProcessState.RUNNING == pcb.getPreviousState()) {
				pd.setvRunTime(pd.getvRunTime()+pcb.getExecutionTime());
			}
		pd.setWaitTime(Pcb.getCurrentTime());
		list.add(pcb);
	}

}
