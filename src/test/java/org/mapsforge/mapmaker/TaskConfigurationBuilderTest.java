/*
 * Copyright 2010, 2011, 2012 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.mapmaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.junit.Test;
import org.mapsforge.mapmaker.TaskConfigurationBuilder.TaskType;
import org.mapsforge.mapmaker.gui.MainWizard;
import org.mapsforge.mapmaker.gui.MapFileWizardPage;
import org.mapsforge.mapmaker.gui.OptionSelectionWizardPage;
import org.mapsforge.mapmaker.gui.POIWizardPage;
import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;

/**
 * Unit test for {@link TaskConfigurationBuilder}.
 */
public class TaskConfigurationBuilderTest {
	// TODO Move data to src/test/resources/
	private final static String DIALOG_SETTINGS_POI_CONFIG = "src/test/resources/test.txt";
	private final static String EMPTY_INPUT_FILE_PATH = "/home/moep/maps/berlin.osm.pbf";

	private final static String MAP_FILE_PATH = "/home/moep/maps/berlin.map";
	private final static double MAP_START_POS_LAT = 53.083418;
	private final static double MAP_START_POS_LON = 8.81376;
	private final static double MAP_BB_MIN_LAT = 52.4477300;
	private final static double MAP_BB_MIN_LON = 13.2756600;
	private final static double MAP_BB_MAX_LAT = 52.4588200;
	private final static double MAP_BB_MAX_LON = 13.2986600;
	private final static int MAP_START_ZOOM = 11;
	private final static String MAP_PREFERRED_LANGUAGE = "de";
	private final static String MAP_COMMENT = "junit";
	private final static String MAP_TAG_CONFIG_FILE = "todo.xml";
	private final static int MAP_SIMPLIFICATION_FACTOR_AS_INT = 500;
	private final static int MAP_BB_ENLARGEMENT = 20;
	private final static String MAP_ZOOM_INTERVAL_CONF = "5,0,7,10,8,11,14,12,21";
	private final static String POI_FILE_PATH = "/home/moep/maps/berlin.poi";
	private final static String POI_OUTPUT_PATH = "out.poi";
	private final static String POI_CATEGORY_CONFIG_PATH = "POICategoriesOsmosis.xml";

	/**
	 * Creates two tasks using {@link TaskConfigurationBuilder} and checks if
	 * the created {@link TaskConfiguration}s are correct.
	 */
	@Test
	public void testTaskConfigurationBuilderWithTwoTasks() {

		TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		// --rb $FILE_PATH
		builder.createAndAddTask(TaskType.READ_BINARY, EMPTY_INPUT_FILE_PATH);
		// --mw $MAP_FILE_PATH type=hd
		builder.createAndAddTask(TaskType.MAP_WRITER, "file=" + MAP_FILE_PATH,
				"type=hd");
		// --write-poi $POI_FILE_PATH
		// categoryConfigPath=$POI_CATEGORY_CONFIG_FILE_PATH
		builder.createAndAddTask(TaskType.POI_WRITER, POI_FILE_PATH,
				"categoryConfigPath=" + POI_CATEGORY_CONFIG_PATH);

		List<TaskConfiguration> taskConfigurations = builder
				.getTaskConfigurations();

		// read-binary + tee + mapfile-writer + write-poi
		assertEquals(4, taskConfigurations.size());

		// read binary
		assertEquals("rb", taskConfigurations.get(0).getType());
		assertEquals(EMPTY_INPUT_FILE_PATH, taskConfigurations.get(0)
				.getDefaultArg());

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
		assertEquals("poi-writer", taskConfigurations.get(3).getType());
		assertEquals(POI_CATEGORY_CONFIG_PATH, taskConfigurations.get(3)
				.getConfigArgs().get("categoryConfigPath"));
	}

	/**
	 * Creates one task using {@link TaskConfigurationBuilder} and checks if the
	 * created {@link TaskConfiguration} is correct.
	 */
	@Test
	public void testTaskConfigurationBuilderWithOneTask() {
		TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		// --rb $FILE_PATH
		builder.createAndAddTask(TaskType.READ_BINARY, EMPTY_INPUT_FILE_PATH);
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

	@Test
	public void executeTaskConfigurationFromIDialogSettingsTest() {
		// Check, if file exists
		// assertTrue(new File(DIALOG_SETTINGS_POI_CONFIG).isFile());

		// Create mock settings (fake GUI input)
		IDialogSettings poiSettings = generatePOISettings();
		IDialogSettings mfwSettings = generateMFWSettings();
		assertNotNull(poiSettings);
		assertNotNull(mfwSettings);

		// Create OSM task configurations
		TaskConfiguration poiConfig = getPOITaskConfigurationFromIDialogSettings(poiSettings);
		TaskConfiguration mfwConfig = getMFWTaskConfigurationFromIDialogSettings(mfwSettings);
		assertNotNull(poiConfig);
		assertNotNull(mfwConfig);

		validatePOIConfig(poiConfig);
		validateMapFileWriterConfig(mfwConfig);
	}

	/** Build a custom configuration for POIs */
	private IDialogSettings generatePOISettings() {
		String rootSectionName = MainWizard.SETTINGS_ROOT_SECTION_NAME;
		String generalSectionName = OptionSelectionWizardPage
				.getSettingsSectionName();
		String poiSectionName = POIWizardPage.getSettingsSectionName();

		IDialogSettings settings = new DialogSettings(rootSectionName);

		// General settings
		IDialogSettings generalOptionsSection = settings
				.addNewSection(generalSectionName);
		generalOptionsSection.put("inputFilePath", EMPTY_INPUT_FILE_PATH);
		generalOptionsSection.put("createVectorMap", false);
		generalOptionsSection.put("createPOIs", true);

		// POI settings
		IDialogSettings poiSection = settings.addNewSection(poiSectionName);
		poiSection.put("categoryConfigPath", POI_CATEGORY_CONFIG_PATH);
		poiSection.put("outputFilePath", POI_OUTPUT_PATH);

		return settings;
	}

	private IDialogSettings generateMFWSettings() {
		String rootSectionName = MainWizard.SETTINGS_ROOT_SECTION_NAME;
		String generalSectionName = OptionSelectionWizardPage
				.getSettingsSectionName();
		String mwfSectionName = MapFileWizardPage.getSettingsSectionName();

		// General settings
		IDialogSettings settings = new DialogSettings(rootSectionName);
		IDialogSettings generalOptionsSection = settings
				.addNewSection(generalSectionName);
		generalOptionsSection.put("inputFilePath", EMPTY_INPUT_FILE_PATH);
		generalOptionsSection.put("createVectorMap", true);
		generalOptionsSection.put("createPOIs", false);

		// MFW settings
		IDialogSettings mfwSection = settings.addNewSection(mwfSectionName);
		mfwSection.put("mapFilePath", MAP_FILE_PATH);
		mfwSection.put("enableHDDCache", true);
		mfwSection.put("enableCustomStartPosition", true);
		mfwSection.put("startPositionLat", MAP_START_POS_LAT);
		mfwSection.put("startPositionLon", MAP_START_POS_LON);
		mfwSection.put("enableCustomBB", true);
		mfwSection.put("BBMinLat", MAP_BB_MIN_LAT);
		mfwSection.put("BBMinLon", MAP_BB_MIN_LON);
		mfwSection.put("BBMaxLat", MAP_BB_MAX_LAT);
		mfwSection.put("BBMaxLon", MAP_BB_MAX_LON);
		mfwSection.put("enableCustomStartZoomLevel", true);
		mfwSection.put("mapZoomLevel", MAP_START_ZOOM);
		mfwSection.put("enableCustomTagConfig", true);
		mfwSection.put("tagConfigurationFilePath", MAP_TAG_CONFIG_FILE);
		mfwSection.put("enablePolygonClipping", true);
		mfwSection.put("enableWayClipping", true);
		mfwSection.put("computeLabelPositions", true);
		mfwSection.put("enableDebugFile", true);
		mfwSection.put("preferredLanguage", MAP_PREFERRED_LANGUAGE);
		mfwSection.put("comment", MAP_COMMENT);
		mfwSection
				.put("simplificationFactor", MAP_SIMPLIFICATION_FACTOR_AS_INT);
		mfwSection.put("BBEnlargement", MAP_BB_ENLARGEMENT);
		mfwSection.put("zoomIntervalConfiguration", MAP_ZOOM_INTERVAL_CONF);

		return settings;
	}

	private TaskConfiguration getPOITaskConfigurationFromIDialogSettings(
			IDialogSettings settings) {

		TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		builder.buildTaskConfigurationFromDialogSettings(settings);
		List<TaskConfiguration> configurations = builder
				.getTaskConfigurations();

		// There should only be two Osmosis tasks: one for reading and one for
		// writing
		assertEquals(2, configurations.size());

		return configurations.get(1);
	}

	private TaskConfiguration getMFWTaskConfigurationFromIDialogSettings(
			IDialogSettings mfwSettings) {
		TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		builder.buildTaskConfigurationFromDialogSettings(mfwSettings);
		List<TaskConfiguration> configurations = builder
				.getTaskConfigurations();

		// There should only be two Osmosis tasks: one for reading and one for
		// writing
		assertEquals(2, configurations.size());

		return configurations.get(1);
	}

	private void validatePOIConfig(TaskConfiguration poiConfig) {
		Map<String, String> configArgs = poiConfig.getConfigArgs();
		String defaultArg = poiConfig.getDefaultArg();

		// Check type
		assertEquals("poi-writer", poiConfig.getType());

		// Check Osmosis parameters
		assertEquals(POI_CATEGORY_CONFIG_PATH,
				configArgs.get("categoryConfigPath"));

		if (defaultArg == null) {
			assertEquals(POI_OUTPUT_PATH, configArgs.get("file"));
		} else {
			assertEquals(POI_OUTPUT_PATH, defaultArg);
		}
	}

	private void validateMapFileWriterConfig(TaskConfiguration mfwConfig) {
		Map<String, String> configArgs = mfwConfig.getConfigArgs();
		String defaultArg = mfwConfig.getDefaultArg();

		// Check task type
		assertEquals("mw", mfwConfig.getType());

		// Output file path is defined as default parameter
		if (defaultArg != null) {
			assertEquals(MAP_FILE_PATH, defaultArg);
			// Output file path should only be declared once
			assertNull(configArgs.get("file"));
		} else {
			assertEquals(MAP_FILE_PATH, configArgs.get("file"));
		}

		// INPUTS
		assertEquals("hd", configArgs.get("type"));
		assertEquals(MAP_START_POS_LAT + "," + MAP_START_POS_LON,
				configArgs.get("map-start-position"));
		assertEquals(MAP_BB_MIN_LAT + "," + MAP_BB_MIN_LON + ","
				+ MAP_BB_MAX_LAT + "," + MAP_BB_MAX_LON, configArgs.get("bbox"));
		assertEquals("" + MAP_START_ZOOM, configArgs.get("map-start-zoom"));
		assertEquals(MAP_TAG_CONFIG_FILE, configArgs.get("tag-conf-file"));
		assertEquals(MAP_PREFERRED_LANGUAGE,
				configArgs.get("preferred-language"));
		assertEquals(MAP_COMMENT, configArgs.get("comment"));
		assertEquals(
				TaskConfigurationBuilder.SIMPLIFICATION_FACTOR_MULTIPLICATOR
						* MAP_SIMPLIFICATION_FACTOR_AS_INT + "",
				configArgs.get("simplification-factor"));
		assertEquals(MAP_BB_ENLARGEMENT + "",
				configArgs.get("bbox-enlargement"));
		assertEquals(MAP_ZOOM_INTERVAL_CONF,
				configArgs.get("zoom-interval-conf"));

		// CHECKBOXES (should all be set to true for all test cases)
		assertEquals("true", configArgs.get("polygon-clipping"));
		assertEquals("true", configArgs.get("way-clipping"));
		assertEquals("true", configArgs.get("label-position"));
		assertEquals("true", configArgs.get("debug-file"));
	}
}
