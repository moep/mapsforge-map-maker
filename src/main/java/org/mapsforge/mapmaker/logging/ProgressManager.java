package org.mapsforge.mapmaker.logging;

public interface ProgressManager {
	public void sendMessage(String message);
	public void tick();
	public void initProgressBar(int minVal, int maxVal);
	public void updateProgressBar(int newVal);
	public void start();
	public void finish();
}
