package org.mapsforge.mapmaker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Display;
import org.mapsforge.mapmaker.TaskConfigurationBuilder.TaskType;
import org.mapsforge.mapmaker.gui.ProgressGUI;
import org.mapsforge.mapmaker.logging.ProgressManager;
import org.openstreetmap.osmosis.core.TaskRegistrar;
import org.openstreetmap.osmosis.core.cli.CommandLineParser;
import org.openstreetmap.osmosis.core.pipeline.common.Pipeline;
import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;

/**
 * Hello world!
 * 
 */
public class CLIOsmosisLauncher {
	// --rb /home/moep/maps/berlin.osm.pbf --tee --mw
	// file=/home/moep/maps/berlin.map --poi-writer /home/moep/maps/berlin.poi
	// categoryConfigPath=/home/moep/workspace/mapmaker/POICategoriesOsmosis.xml
	public static void main(String[] args) {
		createAndLaunchUsingTaskConfigurationBuilder();
		// createAndLaunchCustomConfiguration();
		// System.out.println("Starting Osmosis " + OsmosisConstants.VERSION);
		// CommandLineParser commandLineParser;
		// TaskRegistrar taskRegistrar;
		// Pipeline pipeline;
		//
		// commandLineParser = new CommandLineParser();
		// commandLineParser.parse(args);
		//
		// List<TaskConfiguration> tasks = commandLineParser.getTaskInfoList();
		// System.out.println("Task configurations: ");
		//
		// taskRegistrar = new TaskRegistrar();
		// taskRegistrar.initialize(commandLineParser.getPlugins());
		//
		// pipeline = new Pipeline(taskRegistrar.getFactoryRegister());
		// pipeline.prepare(tasks);
		// pipeline.execute();
		// pipeline.waitForCompletion();

		// analyzeTasks(args);
		// createAndLaunchCustomConfiguration();

	}

	private static void createAndLaunchUsingTaskConfigurationBuilder() {
		final TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		builder.createAndAddTask(TaskType.READ_BINARY,
				"/home/moep/maps/berlin.osm.pbf");
		builder.createAndAddTask(TaskType.POI_WRITER,
				"/home/moep/maps/berlin.poi",
				"categoryConfigPath=POICategoriesOsmosis.xml", "gui-mode=true");

		final TaskRegistrar taskRegistrar = new TaskRegistrar();
		taskRegistrar.initialize(new LinkedList<String>());

		final ProgressGUI gui = ProgressGUI.getInstance();
		final Display display = new Display();
		
		// Start Osmosis
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ProgressManager pm = ProgressGUI.getInstance();
				
				
				pm.start();
				System.out.println("Starting pipeline");				
				Pipeline pipeline = new Pipeline(taskRegistrar.getFactoryRegister());
				System.out.println("Preparing pipeline");
				pipeline.prepare(builder.getTaskConfigurations());
				System.out.println("Executing pipeline");
				pipeline.execute();
				System.out.println("Waiting for completion");
				pipeline.waitForCompletion();
				pm.finish();
			}
			
		});
		t.start();
		
		gui.show(display);
		try {
			t.join();			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		System.out.println("Nothing to do here");

	}

	private static void createAndLaunchCustomConfiguration() {
		Map<String, String> configArgs = new HashMap<String, String>();
		Map<String, String> pipeArgs = new HashMap<String, String>();

		TaskConfiguration readTask = new TaskConfiguration("1-rb", "rb",
				configArgs, pipeArgs, "/home/moep/maps/berlin.osm.pbf");
		TaskConfiguration teeTask;
		TaskConfiguration writePOITask = new TaskConfiguration("2-poi-writer",
				"poi-writer", configArgs, pipeArgs, "/home/moep/maps/test.poi");
		LinkedList<TaskConfiguration> taskList = new LinkedList<TaskConfiguration>();
		taskList.add(readTask);
		taskList.add(writePOITask);

		TaskRegistrar taskRegistrar = new TaskRegistrar();
		taskRegistrar.initialize(new LinkedList<String>());

		Pipeline pipeline = new Pipeline(taskRegistrar.getFactoryRegister());
		pipeline.prepare(taskList);

	}

	private static void analyzeTasks(String[] args) {
		CommandLineParser clParser = new CommandLineParser();
		System.out.println("parsing...");
		// clParser.parse(new String[] {"--rb",
		// "/home/moep/maps/berlin.osm.pbf", "--wx",
		// "/home/moep/maps/test.xml"});
		clParser.parse(args);
		List<TaskConfiguration> tasks = clParser.getTaskInfoList();
		for (TaskConfiguration t : tasks) {
			System.out.println("[" + t.getId() + "]");
			System.out.println("  Type: " + t.getType());
			System.out.println("  Default arg: " + t.getDefaultArg());
			System.out.println("  Config args: ");
			for (String key : t.getConfigArgs().keySet()) {
				System.out.println("   [*] " + key + "="
						+ t.getConfigArgs().get(key));
			}
			System.out.println("  Pipe args: ");
			for (String key : t.getPipeArgs().keySet()) {
				System.out.println("   [*] " + key + "="
						+ t.getPipeArgs().get(key));
			}
		}

		System.out.println("Plugins: ");
		for (String l : clParser.getPlugins()) {
			System.out.println("  [*] " + l);
		}
	}

}
