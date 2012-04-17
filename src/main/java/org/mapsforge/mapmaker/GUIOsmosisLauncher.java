package org.mapsforge.mapmaker;

import java.io.IOException;
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
import org.mapsforge.mapmaker.logging.ProgressManager;
import org.openstreetmap.osmosis.core.TaskRegistrar;
import org.openstreetmap.osmosis.core.pipeline.common.Pipeline;

public class GUIOsmosisLauncher {
	// This multiplicator transforms a raw spinner value (stored as int) to its
	// float representation.
	private static final float SIMPLIFICATION_FACTOR_MULTIPLICATOR = 0.01f;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		// Launch wizard
		Wizard w = new MainWizard();
		WizardDialog dialog = new WizardDialog(shell, w);
		Window.setDefaultImage(new Image(display, "logo.png"));
		int dialogExitCode = dialog.open();

		// shell.dispose();
		// display.dispose();

		switch (dialogExitCode) {
			case Window.OK:
				System.out.println("[Main] Launching Osmosis");
				ProgressGUI gui = ProgressGUI.getInstance();
				try {
					System.out.println("[Main] Saving settings");
					w.getDialogSettings().save("lastSession.settings");
				} catch (IOException e) {
					// TODO Error message dialog
					e.printStackTrace();
				}
				printSettings(w.getDialogSettings());
				invokeOsmosisAsync(w.getDialogSettings());
				System.out.println("[Main] Opening progress bar window");
				gui.show(display);

			case Window.CANCEL:
				// System.out.println("Saving configuration");
				// TODO offer save configuration if osmosis has finished gracefully
				break;
			default:
		}
	}

	private static void invokeOsmosisAsync(final IDialogSettings settings) {

		final TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		createTasks(settings, builder);

		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("[OsmosisThread] Started");
				System.out.println("[OsmosisThread] # Tasks: "
						+ builder.getTaskConfigurations().size());
				ProgressManager pm = ProgressGUI.getInstance();

				// Launch Osmosis
				pm.start();
				TaskRegistrar taskRegistrar = new TaskRegistrar();
				taskRegistrar.initialize(new LinkedList<String>());

				Pipeline pipeline = new Pipeline(taskRegistrar
						.getFactoryRegister());
				System.out.println("[OsmosisThread] Preparing pipeline");
				pipeline.prepare(builder.getTaskConfigurations());
				System.out.println("[OsmosisThread] Starting pipeline");
				pipeline.execute();

				System.out
						.println("[OsmosisThread] Waiting for pipeline to finish");
				pipeline.waitForCompletion();
				System.out.println("[OsmosisThread] Done");
				pm.finish();
			}

		}).start();

	}

	private static void createTasks(final IDialogSettings settings,
			final TaskConfigurationBuilder builder) {
		IDialogSettings generalSection = settings.getSection("general");
		IDialogSettings poiSection = settings.getSection("poi");
		IDialogSettings mapFileSection = settings.getSection("mapfile");

		// TODO remove hard coded value
		// boolean createPOIs = generalSection.getBoolean("createPOIs");
		boolean createPOIs = true;
		// boolean createMapFile = generalSection.getBoolean("createMapFile");
		boolean createMapFile = false;

		String inputFilePath = generalSection.get("inputFilePath");
		// TODO handle error if file name does not have an extension
		// defined
		String inputFileType = inputFilePath.split("\\.(?=[^\\.]*$)")[1];

		// INPUT FILE TASK
		if (inputFileType.equalsIgnoreCase("pbf")) {
			builder.createAndAddTask(TaskType.READ_BINARY, inputFilePath);
		} else if (inputFileType.equalsIgnoreCase("osm")) {
			builder.createAndAddTask(TaskType.READ_XML, inputFilePath);
		}

		// POI TASK
		if (createPOIs) {
			System.out.println("Creating POI task");
			String categoryConfigPath = poiSection.get("categoryConfigPath");
			builder.createAndAddTask(TaskType.POI_WRITER, "test.poi",
					"categoryConfigPath=" + categoryConfigPath, "gui-mode=true");
		}

		// MAP FILE TASK
		if (createMapFile) {
			System.out.println("Creating mw task");
			builder.createAndAddTask(TaskType.MAP_WRITER,
					mapFileSection.get("mapFilePath"),
					"type=" + (mapFileSection.getBoolean("enableHDDCache") ? "hd" : "ram"),
					"polygon-clipping=" + mapFileSection.getBoolean("enablePolygonClipping"),
					"way-clipping=" + mapFileSection.getBoolean("enableWayClipping"),
					"label-position=" + mapFileSection.getBoolean("computeLabelPositions"),
					"simplification-factor=" + mapFileSection.getInt("simplificationFactor")
							* SIMPLIFICATION_FACTOR_MULTIPLICATOR,
					"bbox-enlargement=" + mapFileSection.getInt("BBEnlargement"),
					"zoom-interval-conf=" + mapFileSection.get("zoomIntervalConfiguration"),
					"debug-file=" + mapFileSection.get("enableDebugFile")
					);
		}

		// optional parameters
		if (mapFileSection.getBoolean("enableCustomStartPosition")) {
			builder.appendParameterToLastAddedTask("map-start-position=" +
					mapFileSection.get("startPositionLat") + "," +
					mapFileSection.get("startPositionLon")
					);
		}

	}

	private static void printSettings(IDialogSettings settings) {
		System.out.println("#Sections: " + settings.getSections().length);
		for (IDialogSettings s : settings.getSections()) {
			System.out.println(" [*] " + s.getName());
		}

		System.out.println("[General settings]");
		IDialogSettings generalSection = settings.getSection("general");
		System.out.println("input file path: "
				+ generalSection.get("inputFilePath"));
		System.out.println("create vector map: "
				+ generalSection.getBoolean("createVectorMap"));
		System.out.println("create POIs: "
				+ generalSection.getBoolean("createPOIs"));

		System.out.println("[POI settings]");
		IDialogSettings poiSection = settings.getSection("poi");
		System.out.println("category config path: "
				+ poiSection.get("categoryConfigPath"));

		System.out.println("[Map File Settings]");
		IDialogSettings mapFileSection = settings.getSection("mapfile");
		System.out.println("Map File path: "
				+ mapFileSection.getBoolean("mapFilePath"));
		System.out.println("Enable HDD cache: "
				+ mapFileSection.getBoolean("enableHDDCache"));
		System.out.println("Enable custom start position: "
				+ mapFileSection.getBoolean("enableCustomStartPosition"));
		if (mapFileSection.getBoolean("enableCustomStartPosition") == true) {
			System.out.println("  [*] Latitude / Longitude: "
					+ mapFileSection.getInt("startPositionLat") + " / "
					+ mapFileSection.getInt("startPositionLat"));
		}
		System.out.println("Enable custom start zoom level: "
				+ mapFileSection.getBoolean("enableCustomStartZoomLevel"));
		System.out.println("Enable custom tag config: "
				+ mapFileSection.getBoolean("enableCustomTagConfig"));
		if (mapFileSection.getBoolean("enableCustomTagConfig")) {
			System.out.println("  [*] Tag configuration file path: "
					+ mapFileSection.get("tagConfigurationFilePath"));
		}
		System.out.println("Enable polygon clipping: "
				+ mapFileSection.getBoolean("enablePolygonClipping"));
		System.out.println("Enable way clipping: "
				+ mapFileSection.getBoolean("enableWayClipping"));
		System.out.println("Compute label positions: "
				+ mapFileSection.getBoolean("computeLabelPositions"));
		System.out.println("Enable debug file: "
				+ mapFileSection.getBoolean("enableDebugFile"));
		
		System.out.println("Enable custom start zoom level: "
				+ mapFileSection.getBoolean("enableCustomStartZoomLevel"));
		if (mapFileSection.getBoolean("enableCustomStartZoomLevel")) {
			System.out.println("" + mapFileSection.getInt("mapZoomLevel"));
		}
		System.out.println("Preferred language: "
				+ mapFileSection.get("preferredLanguage"));
		System.out.println("Comment: " + mapFileSection.getBoolean("comment"));
		System.out
				.println("Simplification factor: "
						+ (mapFileSection.getInt("simplificationFactor") * SIMPLIFICATION_FACTOR_MULTIPLICATOR));
		System.out.println("Bounding box enlargement"
				+ mapFileSection.getBoolean("BBEnlargement"));
		System.out.println("Zoom interval configuration: "
				+ mapFileSection.get("zoomIntervalConfiguration"));

	}
}
