package org.mapsforge.mapmaker;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mapsforge.mapmaker.TaskConfigurationBuilder.TaskType;
import org.mapsforge.mapmaker.gui.MainWizard;
import org.mapsforge.mapmaker.gui.ProgressGUI;
import org.openstreetmap.osmosis.core.TaskRegistrar;
import org.openstreetmap.osmosis.core.pipeline.common.Pipeline;

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
		
		shell.dispose();
		display.dispose();

		switch (dialogExitCode) {
		case Window.OK:
			System.out.println("Launching Osmosis");
			ProgressGUI progressHandler = ProgressGUI.getInstance();
			invokeOsmosis(w.getDialogSettings());
			progressHandler.show(display);

		case Window.CANCEL:
			//System.out.println("Saving configuration");
			// TODO offer save configuration if osmosis has finished gracefully
			break;
		default:
		}
	}
	
	
	
	private static void invokeOsmosis(IDialogSettings settings) {
		printSettings(settings);
		
		TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		IDialogSettings generalSection = settings.getSection("general");
		IDialogSettings poiSection = settings.getSection("poi");
		
		String inputFilePath = generalSection.get("inputFilePath");
		// TODO handle error if file name does not have an extension defined
		String inputFileType = inputFilePath.split("\\.(?=[^\\.]*$)")[1];
		boolean createPOIs = generalSection.getBoolean("createPOIs");
		
		// Input file task
		if(inputFileType.equalsIgnoreCase("pbf")) {
			builder.createAndAddTask(TaskType.READ_BINARY, inputFilePath);
		} else if(inputFileType.equalsIgnoreCase("osm")) {
			builder.createAndAddTask(TaskType.READ_XML, inputFilePath);
		}
		
		// POI task
		if(createPOIs) {
			System.out.println("Creating pois");
			String categoryConfigPath = poiSection.get("categoryConfigPath");
			builder.createAndAddTask(TaskType.POI_WRITER, "test.poi", "categoryConfigPath=" + categoryConfigPath, "gui-mode=true");
		}
		
		System.out.println("# Tasks: " + builder.getTaskConfigurations().size());
		
		// Launch Osmosis
		TaskRegistrar taskRegistrar = new TaskRegistrar();
		taskRegistrar.initialize(new LinkedList<String>());

		Pipeline pipeline = new Pipeline(taskRegistrar.getFactoryRegister());
		System.out.println("Preparing pipeline");
		pipeline.prepare(builder.getTaskConfigurations());
		System.out.println("Starting pipeline");
		pipeline.execute();
		
		ProgressGUI gui = ProgressGUI.getInstance();		
		gui.show(new Display());
		
		System.out.println("Waiting for pipeline to finish");
		pipeline.waitForCompletion();
		System.out.println("Done");
		
	}



	private static void printSettings(IDialogSettings settings) {
		System.out.println("#Sections: " + settings.getSections().length);
		for(IDialogSettings s : settings.getSections()) {
			System.out.println(" [*] " + s.getName());
		}
		
		System.out.println("[General settings]");
		IDialogSettings generalSection = settings.getSection("general");
		System.out.println("input file path: " + generalSection.get("inputFilePath"));
		System.out.println("create vector map: " + generalSection.getBoolean("createVectorMap"));
		System.out.println("create POIs: " + generalSection.getBoolean("createPOIs"));
		
		System.out.println("[POI settings]");
		IDialogSettings poiSection = settings.getSection("poi");
		System.out.println("category config path: " + poiSection.get("categoryConfigPath"));
		
	}
}
