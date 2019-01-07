package com.etf.os2.project.scheduler.visak;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.etf.os2.project.process.*;
import com.etf.os2.project.process.Pcb.ProcessState;
import com.etf.os2.project.scheduler.Scheduler;

public class CFairSchedularSingleCPU extends Scheduler {
	private final static long MINVRUN = 1;
	private final static int INITVEL = 20;

	private PriorityQueue<Pcb> buffer;
	private PcbCompartor comparator;

	class PcbCompartor implements Comparator<Pcb> {

		public PcbCompartor() {
		}

		@Override
		public int compare(Pcb p1, Pcb p2) {
			long VR1 = p1.getPcbData().getvRunTime() + (Pcb.getCurrentTime() - p1.getPcbData().getWaitTime()),
					VR2 = p2.getPcbData().getvRunTime() + (Pcb.getCurrentTime() - p2.getPcbData().getWaitTime());
			if (VR1 == VR2) {

				if (p1.getPriority() == p2.getPriority())
					return 0;
				else if (p1.getPriority() > p1.getPriority())
					return 1;
				else
					return -1;
			} else if (VR1 > VR2)
				return 1;
			else
				return -1;
		}

	}

	public CFairSchedularSingleCPU() {
		comparator = new PcbCompartor();
		buffer = new PriorityQueue<Pcb>(INITVEL, comparator);
	}

	@Override
	public Pcb get(int cpuId) {
		Pcb pcb = null;
		if (buffer.size() != 0) {
			pcb = buffer.remove();
			long time = (Pcb.getCurrentTime() - pcb.getPcbData().getWaitTime()) / Pcb.getProcessCount();
			pcb.setTimeslice(time == 0 ? MINVRUN : time);
//			pcb.setTimeslice(time);
		}
		return pcb;
	}

	@Override
	public void put(Pcb pcb) {
		if (pcb == null || ProcessState.IDLE == pcb.getPreviousState()
				|| ProcessState.FINISHED == pcb.getPreviousState())
			return;
		PcbData pd = pcb.getPcbData();
		if (pd == null)
			pcb.setPcbData(new PcbData());
		pd = pcb.getPcbData();
		if (ProcessState.CREATED == pcb.getPreviousState() || ProcessState.BLOCKED == pcb.getPreviousState()) {
			pd.setvRunTime(0);
		} else if (ProcessState.RUNNING == pcb.getPreviousState()) {
			pd.setvRunTime(pd.getvRunTime() + pcb.getExecutionTime());
		}
		pd.setWaitTime(Pcb.getCurrentTime());
		buffer.add(pcb);
	}
	
	public int getSize() {
		return buffer.size();
	}

}
