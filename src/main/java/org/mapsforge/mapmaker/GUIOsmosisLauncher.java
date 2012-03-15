package org.mapsforge.mapmaker;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mapsforge.mapmaker.gui.MainWizard;
import org.mapsforge.mapmaker.gui.ProgressGUI;

public class GUIOsmosisLauncher {
	// TODO Where are these constants defined in SWT

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		// shell.open ();

		// while (!shell.isDisposed ()) {
		// if (!display.readAndDispatch ()) display.sleep ();
		// }

		// Launch wizard
		Wizard w = new MainWizard();
		WizardDialog dialog = new WizardDialog(shell, w);
		Window.setDefaultImage(new Image(display, "logo.png"));
		int dialogExitCode = dialog.open();
		
		printSettings(w.getDialogSettings());
		
		shell.dispose();
		display.dispose();

		switch (dialogExitCode) {
		case Window.OK:
			System.out.println("Launching Osmosis");
			ProgressGUI progressHandler = ProgressGUI.getInstance();
			new FakeOsmosis(progressHandler).start();
			progressHandler.show();

		case Window.CANCEL:
			//System.out.println("Saving configuration");
			// TODO offer save configuration if osmosis has finished gracefully
			break;
		default:
		}
	}

	private static void printSettings(IDialogSettings dialogSettings) {
		System.out.println("General settings:");
		IDialogSettings generalSection = dialogSettings.getSection("general");
		System.out.println("input file path: " + generalSection.get("inputFilePath"));
		System.out.println("create vector map " + generalSection.getBoolean("createVectorMap"));
		System.out.println("create POIs " + generalSection.getBoolean("createPOIs"));
	}
}
