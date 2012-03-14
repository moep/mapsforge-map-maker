package org.mapsforge.mapmaker.logging;

public interface ProgressManager {
	public void onMessage(String message);
	public void onTick();
	public void onInit(int minVal, int maxVal);
	public void onUpdate(int newVal);
	public void onStart();
	public void onFinish();
}
