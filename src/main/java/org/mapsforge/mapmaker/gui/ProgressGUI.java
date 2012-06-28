package org.mapsforge.mapmaker.gui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
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

	private final static int PADDING = 4;
	private final static String NL = "\r\n";

	private Shell shell;
	private Display display;

	private ProgressBar progressBar;
	private CLabel lblStatusText;
	private StyledText log;
	private FormData fd_log;
	private Button btnOK;
	private Button btnCancel;

	private int logCharacterCount = 0;

	// DON'T CHANGE THIS VARIABLE (consider it being final)
	private Color ERROR_COLOR;

	private ProgressGUI() {
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

		this.shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {

				boolean doExit = true;

				// Promt quit if the worker thread has not finished yet
				if (!ProgressGUI.this.isFinished) {
					doExit = MessageDialog.openConfirm(ProgressGUI.this.shell, "Cancel action?",
							"Do you really want to abort this action?");
				}

				e.doit = doExit;
			}
		});

		this.shell.setSize(400, 150);
		this.shell.setLocation(300, 300);
		initUI();
		this.shell.pack();

		this.shell.open();

		while (!this.shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		shutdown();
	}

	private void shutdown() {
		System.out.println("[GUI] shutdown");

		if (!this.isFinished) {
			System.out.println("force exit");
			System.exit(0);
		}
	}

	private void initUI() {
		FormLayout layout = new FormLayout();
		this.shell.setLayout(layout);

		// STATUS TEXT
		this.lblStatusText = new CLabel(this.shell, SWT.WRAP);
		this.lblStatusText.setText("Waiting for plugin");
		Point labelSize = this.lblStatusText.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		final FormData fd_lblStatusText = new FormData(labelSize.x, SWT.DEFAULT);
		fd_lblStatusText.left = new FormAttachment(0, PADDING);
		fd_lblStatusText.right = new FormAttachment(100, -PADDING);
		this.lblStatusText.setLayoutData(fd_lblStatusText);

		// PROGRESS BAR
		this.progressBar = new ProgressBar(this.shell, SWT.HORIZONTAL);
		FormData fd_progressBar = new FormData(ProgressGUI.this.shell.getSize().x - 2 * PADDING, SWT.DEFAULT);
		fd_progressBar.top = new FormAttachment(ProgressGUI.this.lblStatusText, PADDING);
		fd_progressBar.left = new FormAttachment(0, PADDING);
		fd_progressBar.right = new FormAttachment(100, -PADDING);
		ProgressGUI.this.progressBar.setLayoutData(fd_progressBar);

		this.log = new StyledText(this.shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.MULTI);
		this.fd_log = new FormData(SWT.DEFAULT, 50);
		this.fd_log.top = new FormAttachment(this.progressBar, PADDING);
		this.fd_log.left = new FormAttachment(0, PADDING);
		this.fd_log.right = new FormAttachment(100, -PADDING);
		this.log.setLayoutData(this.fd_log);
		this.log.setEditable(false);

		// CANCEL BUTTON
		this.btnCancel = new Button(this.shell, SWT.PUSH);
		this.fd_log.bottom = new FormAttachment(this.btnCancel, -PADDING);
		FormData fd_btnCancel = new FormData(100, SWT.DEFAULT);
		fd_btnCancel.right = new FormAttachment(100, -PADDING);
		fd_btnCancel.bottom = new FormAttachment(100, -PADDING);
		this.btnCancel.setLayoutData(fd_btnCancel);
		this.btnCancel.setText("Cancel");

		// OK BUTTON
		this.btnOK = new Button(this.shell, SWT.PUSH);
		FormData okButtonLayout = new FormData(100, SWT.DEFAULT);
		okButtonLayout.right = new FormAttachment(this.btnCancel, -PADDING);
		okButtonLayout.bottom = new FormAttachment(100, -PADDING);
		this.btnOK.setLayoutData(okButtonLayout);
		this.btnOK.setText("OK");
		this.btnOK.setEnabled(false);

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
			}
		});

		this.btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ProgressGUI.this.shell.close();
			}
		});

		// REMOVE LOCKS
		System.out.println("[GUI] removing locks");
		synchronized (instance) {
			this.isInitialized = true;
			instance.notify();
			System.out.println("[GUI] ready");
		}

		// COLORS AND FONTS
		this.ERROR_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		this.log.setFont(new Font(Display.getCurrent(), "Monospace", 10, SWT.NORMAL));
	}

	@Override
	public void setMessage(final String message) {
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
	public void appendLogMessage(final String message, final boolean isErrorMessage) {

		final StyleRange style = new StyleRange();
		style.start = this.logCharacterCount;
		style.length = message.length();
		if (isErrorMessage) {
			style.foreground = this.ERROR_COLOR;
		}

		this.logCharacterCount += message.length() + NL.length();

		if (this.display != null && !this.shell.isDisposed()) {
			this.display.asyncExec(new Runnable() {

				@Override
				public void run() {
					synchronized (ProgressGUI.this.log) {
						ProgressGUI.this.log.append(message + NL);
						ProgressGUI.this.log.setStyleRange(style);
					}
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
					ProgressGUI.this.progressBar.setSelection(ProgressGUI.this.progressBar.getSelection() + 1);

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

					if (minVal == 0 && maxVal == 0) {
						System.out.println("[GUI] creating indeterminate bar");
						ProgressGUI.this.createOrReplaceProgressBar(SWT.HORIZONTAL | SWT.INDETERMINATE);
					} else {
						System.out.println("[GUI] creating determinate bar");
						ProgressGUI.this.createOrReplaceProgressBar(SWT.HORIZONTAL);
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

		this.progressBar = new ProgressBar(this.shell, mode);
		FormData fd_progressBar = new FormData(ProgressGUI.this.shell.getSize().x - 2 * this.PADDING, SWT.DEFAULT);
		fd_progressBar.top = new FormAttachment(ProgressGUI.this.lblStatusText, PADDING);
		fd_progressBar.left = new FormAttachment(0, PADDING);
		fd_progressBar.right = new FormAttachment(100, -PADDING);

		this.fd_log.top = new FormAttachment(this.progressBar, PADDING);

		ProgressGUI.this.progressBar.setLayoutData(fd_progressBar);
		ProgressGUI.this.shell.layout(true);

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
		synchronized (this.instance) {
			while (!this.isInitialized) {
				System.out.println("[GUI] Waiting for initialization to finish");
				try {
					this.instance.wait();
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
		System.out.println("[GUI] finish");
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
