package org.mapsforge.mapmaker.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.mapsforge.mapmaker.logging.ProgressManager;

public class ProgressGUI implements ProgressManager {
	private static ProgressGUI instance;
	private boolean isInitialized = false;
	private boolean isFinished = false;

	final int PADDING = 4;

	private Shell shell;
	private Display display;

	private ProgressBar progressBar;
	protected CLabel lblStatusText;
	private Button btnOK;
	private Button btnCancel;

	private ProgressGUI() {
		System.out.println("GUI constructor");
	}

	public synchronized static ProgressGUI getInstance() {
		if (instance == null) {
			instance = new ProgressGUI();
		}
		return instance;
	}

	public void show(Display display) {
		this.shell = new Shell(display);
		this.display = display;
		this.shell.setText("m³ - mapsforge map maker");

		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {

				boolean doExit = true;

				// Promt quit if the worker thread has not finished yet
				if (!ProgressGUI.this.isFinished) {
					doExit = MessageDialog.openConfirm(ProgressGUI.this.shell,
							"Cancel action?",
							"Do you really want to abort this action?");
				}

				e.doit = doExit;
			}
		});

		shell.setSize(400, 150);
		shell.setLocation(300, 300);
		initUI();
		shell.pack();

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		shutdown();
	}

	private void shutdown() {
		System.out.println("GUI: shutdown");

		if (!this.isFinished) {
			System.out.println("force exit");
			System.exit(0);
		}
	}

	public void initUI() {
		FormLayout layout = new FormLayout();
		this.shell.setLayout(layout);

		// STATUS TEXT
		this.lblStatusText = new CLabel(this.shell, SWT.WRAP);
		this.lblStatusText.setText("Waiting for plugin");
		Point labelSize = this.lblStatusText.computeSize(SWT.DEFAULT,
				SWT.DEFAULT);
		final FormData labelLayout = new FormData(labelSize.x, SWT.DEFAULT);
		labelLayout.left = new FormAttachment(0, PADDING);
		labelLayout.right = new FormAttachment(100, -PADDING);
		this.lblStatusText.setLayoutData(labelLayout);

		// CANCEL BUTTON
		this.btnCancel = new Button(this.shell, SWT.PUSH);
		this.btnCancel.setText("Cancel");
		FormData cancelButtonLayout = new FormData(100, SWT.DEFAULT);
		cancelButtonLayout.right = new FormAttachment(100, -PADDING);
		cancelButtonLayout.bottom = new FormAttachment(100, -PADDING);
		btnCancel.setLayoutData(cancelButtonLayout);

		// OK BUTTON
		this.btnOK = new Button(this.shell, SWT.PUSH);
		this.btnOK.setText("OK");
		this.btnOK.setEnabled(false);
		FormData okButtonLayout = new FormData(100, SWT.DEFAULT);
		okButtonLayout.right = new FormAttachment(btnCancel, -PADDING);
		okButtonLayout.bottom = new FormAttachment(100, -PADDING);
		btnOK.setLayoutData(okButtonLayout);

		// PROGRESS BAR
		ProgressGUI.this.progressBar = new ProgressBar(this.shell,
				SWT.HORIZONTAL);
		FormData progressBarLayout = new FormData(
				ProgressGUI.this.shell.getSize().x - 2 * PADDING, SWT.DEFAULT);
		progressBarLayout.top = new FormAttachment(
				ProgressGUI.this.lblStatusText, PADDING);
		progressBarLayout.left = new FormAttachment(0, PADDING);
		progressBarLayout.right = new FormAttachment(100, -PADDING);
		progressBarLayout.bottom = new FormAttachment(ProgressGUI.this.btnOK,
				-PADDING);
		ProgressGUI.this.progressBar.setLayoutData(progressBarLayout);
		System.out.println("Shell size before initProgressBar(): "
				+ this.shell.getSize());

		// RESIZE ACTIONS
		// this.shell.addListener(SWT.RESIZE, new Listener() {
		//
		// @Override
		// public void handleEvent(Event e) {
		// Rectangle windowDimensions = TestGUI.this.shell.getClientArea();
		// labelLayout.width = windowDimensions.width - PADDING * 2;
		// TestGUI.this.shell.layout();
		// }
		// });

		// BUTTON ACTIONS
		this.btnOK.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ProgressGUI.this.shell.close();
				ProgressGUI.this.shell.dispose();
				System.out.println("OK");
			}
		});

		// REMOVE LOCKS
		System.out.println("GUI: removing locks");
		synchronized (instance) {
			this.isInitialized = true;
			instance.notify();
			System.out.println("GUI: ready");
		}

	}

	@Override
	public void sendMessage(final String message) {
		System.out.println("Message: " + message);
		if (this.display != null && !this.shell.isDisposed()) {
			this.display.asyncExec(new Runnable() {

				@Override
				public void run() {
					ProgressGUI.this.lblStatusText.setText(message);

				}

			});
		}
	}

	@Override
	public void tick() {
		if (this.display != null && !this.shell.isDisposed()) {
			this.display.asyncExec(new Runnable() {

				@Override
				public void run() {
					ProgressGUI.this.progressBar
							.setSelection(ProgressGUI.this.progressBar
									.getSelection() + 1);

				}

			});
		}
	}

	@Override
	public void initProgressBar(final int minVal, final int maxVal) {
		if (this.display != null && !this.shell.isDisposed()) {
			this.display.asyncExec(new Runnable() {

				@Override
				public void run() {

					boolean isInIndeterminateMode = ProgressGUI.this.progressBar != null
							&& ProgressGUI.this.progressBar.getMinimum() == 0
							&& ProgressGUI.this.progressBar.getMaximum() == 0;

					if (minVal == 0 && maxVal == 0) {
						System.out.println("creating indeterminate bar");
						createOrReplaceProgressBar(SWT.HORIZONTAL
								| SWT.INDETERMINATE);
					} else {
						System.out.println("creating determinate bar");
						createOrReplaceProgressBar(SWT.HORIZONTAL);
						ProgressGUI.this.progressBar.setMinimum(minVal);
						ProgressGUI.this.progressBar.setMaximum(maxVal);
					}

				}

			});
		}
	}

	private synchronized void createOrReplaceProgressBar(int mode) {
		if (ProgressGUI.this.progressBar != null) {
			ProgressGUI.this.progressBar.dispose();
		}

		// ProgressGUI.this.progressBar = new
		// ProgressBar(ProgressGUI.this.shell,
		// mode);
		// ProgressGUI.this.progressBar.setSize(500, 10);
		// final FormData progressBarLayout = new FormData(
		// ProgressGUI.this.shell.getSize().x - 2
		// * ProgressGUI.this.PADDING, SWT.DEFAULT);
		// progressBarLayout.top = new FormAttachment(
		// ProgressGUI.this.lblStatusText, ProgressGUI.this.PADDING);
		// progressBarLayout.left = new FormAttachment(0,
		// ProgressGUI.this.PADDING);
		// progressBarLayout.right = new FormAttachment(100,
		// -ProgressGUI.this.PADDING);
		// progressBarLayout.bottom = new FormAttachment(100,
		// -ProgressGUI.this.PADDING);
		// ProgressGUI.this.progressBar.setLayoutData(progressBarLayout);

		System.out
				.println("Shell size in create...(): " + this.shell.getSize());

		ProgressGUI.this.progressBar = new ProgressBar(this.shell, mode);
		FormData progressBarLayout = new FormData(
				ProgressGUI.this.shell.getSize().x - 2 * PADDING, SWT.DEFAULT);
		progressBarLayout.top = new FormAttachment(
				ProgressGUI.this.lblStatusText, PADDING);
		progressBarLayout.left = new FormAttachment(0, PADDING);
		progressBarLayout.right = new FormAttachment(100, -PADDING);
		progressBarLayout.bottom = new FormAttachment(ProgressGUI.this.btnOK,
				-PADDING);
		ProgressGUI.this.progressBar.setLayoutData(progressBarLayout);
		ProgressGUI.this.shell.pack();

	}

	@Override
	public void updateProgressBar(final int newVal) {
		if (this.display != null && !this.shell.isDisposed()) {
			this.display.asyncExec(new Runnable() {

				@Override
				public void run() {
					ProgressGUI.this.progressBar.setSelection(newVal);

				}

			});
		}

	}

	@Override
	public void start() {
		this.isFinished = false;
		// Block until the GUI has been fully initialized
		synchronized (instance) {
			while (!this.isInitialized) {
				System.out.println("GUI: Waiting for initialization to finish");
				try {
					instance.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}

		if (this.display != null && !this.shell.isDisposed()) {
			this.display.asyncExec(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

				}

			});
		}

	}

	@Override
	public void finish() {
		System.out.println("GUI: finish");
		this.display.asyncExec(new Runnable() {

			@Override
			public void run() {
				ProgressGUI.this.createOrReplaceProgressBar(SWT.HORIZONTAL);
				ProgressGUI.this.progressBar.setMinimum(0);
				ProgressGUI.this.progressBar.setMaximum(1);
				ProgressGUI.this.progressBar.setSelection(1);
				ProgressGUI.this.btnCancel.setEnabled(false);
				ProgressGUI.this.btnOK.setEnabled(true);
				ProgressGUI.this.isFinished = true;
			}

		});

	}

}
