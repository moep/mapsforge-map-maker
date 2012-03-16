package org.mapsforge.mapmaker.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.mapsforge.mapmaker.logging.ProgressManager;

public class ProgressGUI implements ProgressManager {
	private static ProgressGUI instance = null;

	public Display display;
	public Shell shell;

	public ProgressBar progressBar;
	protected CLabel lblStatusText;

	boolean indeterminateMode = false;

	private final int progressBarX = 10;
	private final int progressBarY = 39;
	private final int progressBarWidth = 431;
	private final int progressBarHeight = 20;

	private ProgressGUI() {
		initialize();
	}

	public static ProgressGUI getInstance() {
		System.out.println("** GetInstance");
		
		if (ProgressGUI.instance == null) {
			System.out.println("Creating progress gui");
			ProgressGUI.instance = new ProgressGUI();
			return ProgressGUI.instance;
		} else {
			System.out.println("Returning singleton");
			return ProgressGUI.instance;
		}
	}

	private void initialize() {
		this.display = new Display();
		this.shell = new Shell(this.display);
		this.shell.setText("POI writer GUI (prototype)");
		this.shell.setSize(453, 94);

		this.lblStatusText = new CLabel(this.shell, SWT.NONE);
		this.lblStatusText.setBounds(10, 10, 431, 23);
		this.lblStatusText.setText("status text");

		this.progressBar = new ProgressBar(this.shell, SWT.HORIZONTAL);
		this.progressBar.setBounds(10, 39, 431, 20);
	}

	public void show() {
		this.shell.open();
		while (!this.shell.isDisposed()) {
			if (!this.display.readAndDispatch()) {
				this.display.sleep();
			}
		}
	}

	public void close() {
		this.shell.close();
		this.display.dispose();
	}

	public void onMessage(final String message) {
		this.display.asyncExec(new Runnable() {
			public void run() {
				ProgressGUI.this.lblStatusText.setText(message);
			}
		});
	}

	public void onTick() {
		this.display.asyncExec(new Runnable() {
			public void run() {
				if (!ProgressGUI.this.indeterminateMode) {
						ProgressGUI.this.progressBar.setSelection(ProgressGUI.this.progressBar.getSelection() + 1);
				}
			}
		});
	}

	public void onInit(final int minVal, final int maxVal) {
		this.display.asyncExec(new Runnable() {
			public void run() {
				if (minVal == 0 && maxVal == 0 && !ProgressGUI.this.indeterminateMode) {
					// Change progress bar style to indeterminate mode
					ProgressGUI.this.progressBar.dispose();
					ProgressGUI.this.progressBar = new ProgressBar(ProgressGUI.this.shell, SWT.HORIZONTAL
							| SWT.INDETERMINATE);
					ProgressGUI.this.progressBar.setBounds(ProgressGUI.this.progressBarX, ProgressGUI.this.progressBarY,
							ProgressGUI.this.progressBarWidth, ProgressGUI.this.progressBarHeight);
					ProgressGUI.this.indeterminateMode = true;
				} else {
					ProgressGUI.this.progressBar.dispose();
					ProgressGUI.this.progressBar = new ProgressBar(ProgressGUI.this.shell, SWT.HORIZONTAL);
					ProgressGUI.this.progressBar.setMinimum(minVal);
					ProgressGUI.this.progressBar.setMaximum(maxVal);
					ProgressGUI.this.progressBar.setBounds(ProgressGUI.this.progressBarX, ProgressGUI.this.progressBarY,
							ProgressGUI.this.progressBarWidth, ProgressGUI.this.progressBarHeight);
					ProgressGUI.this.indeterminateMode = false;
				}
			}
		});
	}

	public void onUpdate(final int newVal) {
		this.display.asyncExec(new Runnable() {
			public void run() {
				progressBar.setSelection(newVal);
			}
		});
	}

	public void onStart() {
		// TODO Auto-generated method stub
	}

	public void onFinish() {
		this.display.asyncExec(new Runnable() {
			public void run() {
				lblStatusText.setText("Done.");
				progressBar.setEnabled(false);
			}
		});

	}

}
