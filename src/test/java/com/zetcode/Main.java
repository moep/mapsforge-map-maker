package com.zetcode;

import org.eclipse.swt.widgets.Display;
import org.mapsforge.mapmaker.logging.ProgressManager;

public class Main {
	static void calc() {
		int i = 0;
		for(;;) {
			i++;
		}
	}
	
	public static void main(String[] args) {
		// TestGUI gui = TestGUI.getInstance();
		//
		// Thread emptyThread = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// ProgressManager pm = TestGUI.getInstance();
		// while(true) {
		// System.out.println("tick");
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// Thread.currentThread().interrupt();
		// }
		// }
		//
		// }
		// });
		// emptyThread.start();
		//
		// gui.show();
		// System.out.println("after show");

		TestGUI gui = new TestGUI();
		Display display = new Display();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 100; i++) {
					System.out.println(i);
					try {
						Thread.sleep(100);
						calc();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}

			}

		});
		t.start();
		
		gui.show(display);
		display.dispose();

	}
}
