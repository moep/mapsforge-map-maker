import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class ProgressBarExample {
	Display display = new Display();
	Shell shell = new Shell(display);
	Button button;
	ProgressBar progressBar;

	public ProgressBarExample() {
		GridLayout gridLayout = new GridLayout(1, true);
		shell.setLayout(gridLayout);
		shell.setText("Progress Bar");
		button = new Button(shell, SWT.HORIZONTAL);
		button.setText("Start");
		progressBar = new ProgressBar(shell, SWT.SMOOTH);
		progressBar.setMinimum(0);
		progressBar.setMaximum(10);

		final Thread thread = new Thread() {
			public void run() {
				for (int i = 0; i <= 10; i++) {
					final int value = i;
					try {
						Thread.sleep(800);
					} catch (Exception e) {
					}
					shell.getDisplay().asyncExec(new Runnable() {
						public void run() {
							progressBar.setSelection(value);
						}
					});
				}
			}
		};
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				button.setEnabled(false);
				thread.start();
			}
		});
		
		progressBar.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				String string = (progressBar.getSelection() * 1.0
						/ (progressBar.getMaximum() - progressBar.getMinimum()) * 100)
						+ "%";
				Point point = progressBar.getSize();

				FontMetrics fontMetrics = e.gc.getFontMetrics();
				int width = fontMetrics.getAverageCharWidth() * string.length();
				int height = fontMetrics.getHeight();
				e.gc.setForeground(shell.getDisplay().getSystemColor(
						SWT.COLOR_WHITE));
				e.gc.drawString(string, (point.x - width) / 2,
						(point.y - height) / 2, true);
			}
		});
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		shell.setSize(300, 90);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] args) {
		new ProgressBarExample();
	}
}