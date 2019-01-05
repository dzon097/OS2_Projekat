package com.etf.os2.project.scheduler.visak;

import com.etf.os2.project.process.Pcb;

public class PcbPriorityCFS implements Comparable<PcbPriorityCFS> {

	private Pcb pcb;

	public PcbPriorityCFS(Pcb pcb) {
		this.pcb = pcb;
	}

	@Override
	public int compareTo(PcbPriorityCFS pcb2) {
		long VR1 = pcb.getPcbData().getvRunTime() +(Pcb.getCurrentTime() - pcb.getPcbData().getWaitTime()), 
			VR2 = pcb2.getPcb().getPcbData().getvRunTime() + (Pcb.getCurrentTime() - pcb2.getPcb().getPcbData().getWaitTime());
		if (VR1 == VR2) {
			if (pcb.getPriority() == pcb2.getPcb().getPriority())
				return 0;
			else if (pcb.getPriority() > pcb2.getPcb().getPriority())
				return 1;
			else
				return -1;
		}
		else if(VR1 > VR2)
			return -1;
		else
			return 1;
	}

	public Pcb getPcb() {
		return pcb;
	}

	public void setPcb(Pcb pcb) {
		this.pcb = pcb;
	}

}
