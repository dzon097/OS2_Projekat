package com.etf.os2.project.scheduler;

import com.etf.os2.project.process.Pcb;
import com.etf.os2.project.process.PcbData;

public class PcbPrioritySJFS implements Comparable<PcbPrioritySJFS> {

	private Pcb pcb;

	public PcbPrioritySJFS(Pcb pcb) {
		this.pcb = pcb;
	}

	@Override
	public int compareTo(PcbPrioritySJFS pcb2) {
		PcbData pd1 = pcb.getPcbData(), pd2 = pcb2.pcb.getPcbData();
		if(pd1.getTau() == pd2.getTau()) {
			if (pcb.getPriority() == pcb2.pcb.getPriority())
				return 0;
			else
				if(pcb.getPriority() > pcb2.getPcb().getPriority())
					return 1;
				else
					return -1;
		}
		else if (pd1.getTau() > pd2.getTau())
			return 1;
		else
			return -1;
	}
	
	public Pcb getPcb() {
		return pcb;
	}


}
