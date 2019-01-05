package com.etf.os2.project.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.etf.os2.project.process.Pcb;

public class LevelMFQS  {
	
	private List<Pcb> buffer;

	public LevelMFQS() {
		buffer = new ArrayList<>();
	}

	public Pcb remove() {
		return buffer.remove(0);
	}
	
	public void add(Pcb pcb) {
		int p = pcb.getPriority();
		int k =0;
		for(; k<buffer.size();k++)
			if(buffer.get(k).getPriority()>=p)
				break;
		buffer.add(k, pcb);
	}
	
	public int size() {
		return buffer.size();
	}

}
