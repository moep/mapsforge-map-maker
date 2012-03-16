package org.mapsforge.mapmaker;

import org.mapsforge.mapmaker.gui.ProgressGUI;

public class Main {
	public static void main(String[] args) {
		final ProgressGUI pbt = ProgressGUI.getInstance();
		Thread t = new FakeOsmosis(pbt);
		t.start();
		pbt.show();

		t.interrupt();
		try {
			t.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		

		// TextProgressManager tpm = new TextProgressManager();
		// tpm.onStart();
		// tpm.onMessage("Doing some preparations...");
		// try {
		// for(int i = 0; i < 50; i++) {
		// Thread.sleep(100);
		// tpm.onTick();
		// }
		// } catch (InterruptedException e) {
		// Thread.currentThread().interrupt();
		// }
		//
		// tpm.onFinish();
		//
	}
}
