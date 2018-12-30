package com.etf.os2.project.process;

public class PcbData {
	
	private int MFQSLevel;
	private long tau;
	private int CPU;
	private long waitTime;
	private long vRunTime;

	public long getWaitTime() {
		return waitTime;
	}

	public long getvRunTime() {
		return vRunTime;
	}

	public void setvRunTime(long vRunTime) {
		this.vRunTime = vRunTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public int getCPU() {
		return CPU;
	}

	public void setCPU(int cPU) {
		CPU = cPU;
	}

	public long getTau() {
		return tau;
	}

	public void setTau(long tau) {
		this.tau = tau;
	}

	public int getMFQSLevel() {
		return MFQSLevel;
	}

	public void setMFQSLevel(int mFQSLevel) {
		MFQSLevel = mFQSLevel;
	}
	
	
}
