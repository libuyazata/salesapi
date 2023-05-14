package com.yaz.alind.model;

import java.util.List;

public class DashBoardVariables {
	
	private int noOfNonAllotedCalls;
	private int noOfOngoingCalls;
	private int noOfCompletedCalls;
	private int noPendingCalls;
	private int newCallViewStatus;
	private int completedCallViewStatus;;
	private int pendingCallViewStatus; 
	private List<CallDetail> nonAllottedCalls;
	private List<CallDetail> onGoingCalls;
	private List<CallDetail> completedCalls;
	private List<CallDetail> pendingCalls;
	
	public int getNoOfNonAllotedCalls() {
		return noOfNonAllotedCalls;
	}
	public void setNoOfNonAllotedCalls(int noOfNonAllotedCalls) {
		this.noOfNonAllotedCalls = noOfNonAllotedCalls;
	}
	public int getNoOfOngoingCalls() {
		return noOfOngoingCalls;
	}
	public void setNoOfOngoingCalls(int noOfOngoingCalls) {
		this.noOfOngoingCalls = noOfOngoingCalls;
	}
	public int getNoOfCompletedCalls() {
		return noOfCompletedCalls;
	}
	public void setNoOfCompletedCalls(int noOfCompletedCalls) {
		this.noOfCompletedCalls = noOfCompletedCalls;
	}
	public List<CallDetail> getNonAllottedCalls() {
		return nonAllottedCalls;
	}
	public void setNonAllottedCalls(List<CallDetail> nonAllottedCalls) {
		this.nonAllottedCalls = nonAllottedCalls;
	}
	public List<CallDetail> getOnGoingCalls() {
		return onGoingCalls;
	}
	public void setOnGoingCalls(List<CallDetail> onGoingCalls) {
		this.onGoingCalls = onGoingCalls;
	}
	public List<CallDetail> getCompletedCalls() {
		return completedCalls;
	}
	public void setCompletedCalls(List<CallDetail> completedCalls) {
		this.completedCalls = completedCalls;
	}
	public int getNoPendingCalls() {
		return noPendingCalls;
	}
	public void setNoPendingCalls(int noPendingCalls) {
		this.noPendingCalls = noPendingCalls;
	}
	public List<CallDetail> getPendingCalls() {
		return pendingCalls;
	}
	public void setPendingCalls(List<CallDetail> pendingCalls) {
		this.pendingCalls = pendingCalls;
	}
	public int isNewCallViewStatus() {
		return newCallViewStatus;
	}
	public void setNewCallViewStatus(int newCallViewStatus) {
		this.newCallViewStatus = newCallViewStatus;
	}
	public int isCompletedCallViewStatus() {
		return completedCallViewStatus;
	}
	public void setCompletedCallViewStatus(int completedCallViewStatus) {
		this.completedCallViewStatus = completedCallViewStatus;
	}
	public int isPendingCallViewStatus() {
		return pendingCallViewStatus;
	}
	public void setPendingCallViewStatus(int pendingCallViewStatus) {
		this.pendingCallViewStatus = pendingCallViewStatus;
	}
	public int getNewCallViewStatus() {
		return newCallViewStatus;
	}
	public int getCompletedCallViewStatus() {
		return completedCallViewStatus;
	}
	public int getPendingCallViewStatus() {
		return pendingCallViewStatus;
	}
	

}
