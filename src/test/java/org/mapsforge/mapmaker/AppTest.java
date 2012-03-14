package org.mapsforge.mapmaker;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mapsforge.mapmaker.TaskConfigurationBuilder.TaskType;
import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	// TODO relative file path
	final static String FILE_PATH = "/home/moep/maps/berlin.osm.pbf";
	final static String MAP_FILE_PATH = "/home/moep/maps/berlin.map";
	final static String POI_FILE_PATH = "/home/moep/maps/berlin.poi";
	final static String POI_CATEGORY_CONFIG_FILE_PATH = "POICategoriesOsmosis.xml";

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testTaskConfigurationBuilderWithTwoTasks() {

		TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		// --rb $FILE_PATH
		builder.createAndAddTask(TaskType.READ_BINARY, FILE_PATH);
		// --mw $MAP_FILE_PATH type=hd
		builder.createAndAddTask(TaskType.MAP_WRITER, "file=" + MAP_FILE_PATH,
				"type=hd");
		// --write-poi $POI_FILE_PATH
		// categoryConfigPath=$POI_CATEGORY_CONFIG_FILE_PATH
		builder.createAndAddTask(TaskType.POI_WRITER, POI_FILE_PATH,
				"categoryConfigPath=" + POI_CATEGORY_CONFIG_FILE_PATH);

		List<TaskConfiguration> taskConfigurations = builder
				.getTaskConfigurations();

		// read-binary + tee + mapfile-writer + write-poi
		assertEquals(4, taskConfigurations.size());

		// read binary
		assertEquals("rb", taskConfigurations.get(0).getType());
		assertEquals(FILE_PATH, taskConfigurations.get(0).getDefaultArg());

		// tee
		assertEquals("tee", taskConfigurations.get(1).getType());

		// mapfile-writer
		assertEquals("mw", taskConfigurations.get(2).getType());
		assertEquals(null, taskConfigurations.get(2).getDefaultArg());
		assertEquals(MAP_FILE_PATH, taskConfigurations.get(2).getConfigArgs()
				.get("file"));
		assertEquals("hd", taskConfigurations.get(2).getConfigArgs()
				.get("type"));

		// poi-writer
		assertEquals("write-poi", taskConfigurations.get(3).getType());
		assertEquals(POI_CATEGORY_CONFIG_FILE_PATH, taskConfigurations.get(3)
				.getConfigArgs().get("categoryConfigPath"));
	}

	public void testTaskConfigurationBuilderWithOneTask() {
		TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		// --rb $FILE_PATH
		builder.createAndAddTask(TaskType.READ_BINARY, FILE_PATH);
		// --mw $MAP_FILE_PATH type=hd
		builder.createAndAddTask(TaskType.MAP_WRITER, "file=" + MAP_FILE_PATH,
				"type=hd");

		List<TaskConfiguration> taskConfigurations = builder
				.getTaskConfigurations();

		// read-binary + mapfile-writer
		assertEquals(2, taskConfigurations.size());

		// mapfile-writer
		assertEquals("mw", taskConfigurations.get(1).getType());
		assertEquals(null, taskConfigurations.get(1).getDefaultArg());
		assertEquals(MAP_FILE_PATH, taskConfigurations.get(1).getConfigArgs()
				.get("file"));
		assertEquals("hd", taskConfigurations.get(1).getConfigArgs()
				.get("type"));

	}
}
