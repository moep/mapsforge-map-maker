package org.mapsforge.mapmaker.logging;

public interface ProgressManager {

	public void setMessage(String message);
	
	public void appendLogMessage(String message, boolean isErrorMessage);

	public void tick();

	public void initProgressBar(int minVal, int maxVal);

	public void updateProgressBar(int newVal);

	public void start();

	public void finish();
}
