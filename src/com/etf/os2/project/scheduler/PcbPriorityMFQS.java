package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;

public class PcbPriorityMFQS implements Comparable<PcbPriorityMFQS> {

	private Pcb pcb;

	public PcbPriorityMFQS(Pcb pcb) {
		this.pcb = pcb;
	}

	@Override
	public int compareTo(PcbPriorityMFQS pcb2) {
		if (pcb.getPriority() == pcb2.getPcb().getPriority())
			return 0;
		else if (pcb.getPriority() > pcb2.getPcb().getPriority())
			return 1;
		else
			return -1;
	}

	public Pcb getPcb() {
		return pcb;
	}

	public void setPcb(Pcb pcb) {
		this.pcb = pcb;
	}

}
