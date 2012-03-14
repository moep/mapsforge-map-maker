package org.mapsforge.mapmaker;

import org.mapsforge.mapmaker.logging.ProgressManager;

public class FakeOsmosis extends Thread {
	private ProgressManager pm;

	public FakeOsmosis(ProgressManager pm) {
		this.pm = pm;
	}

	public void run() {
		
		this.pm.onStart();
		System.out.println("Fake osmosis has been started");
		
		this.pm.onInit(0, 0);
		this.pm.onMessage("Doing some preparations");
		System.out.println("Preparation phase");
		try {
			for (int i = 0; i <= 20; i++) {
				Thread.sleep(100);
				this.pm.onTick();
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
			Thread.currentThread().interrupt();
		}
		
		
		this.pm.onInit(0, 150);
		this.pm.onMessage("Adding 150 items");
		System.out.println("Counting phase");
		try {
			for (int i = 0; i <= 150; i++) {
				Thread.sleep(50);
				this.pm.onTick();
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
			Thread.currentThread().interrupt();
		}
		
		this.pm.onFinish();
		System.out.println("Fake osmosis has been finished");
	}
}
