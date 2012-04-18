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

import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.junit.Test;
import org.mapsforge.mapmaker.TaskConfigurationBuilder.TaskType;
import org.mapsforge.mapmaker.gui.MainWizard;
import org.mapsforge.mapmaker.gui.OptionSelectionWizardPage;
import org.mapsforge.mapmaker.gui.POIWizardPage;
import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;

/**
 * Unit test for {@link TaskConfigurationBuilder}.
 */
public class TaskConfigurationBuilderTest {
	// TODO Move data to src/test/resources/
	private final static String EMPTY_INPUT_FILE_PATH = "/home/moep/maps/berlin.osm.pbf";
	private final static String MAP_FILE_PATH = "/home/moep/maps/berlin.map";
	private final static String POI_FILE_PATH = "/home/moep/maps/berlin.poi";
	final static String DIALOG_SETTINGS_POI_CONFIG = "src/test/resources/test.txt";

	private final static String POI_CATEGORY_CONFIG_FILE_PATH = "POICategoriesOsmosis.xml";
	private final static String POI_OUTPUT_PATH = "out.poi";
	private final static String POI_CATEGORY_CONFIG_PATH = "POICategoriesOsmosis.xml";

	/**
	 * Creates two tasks using {@link TaskConfigurationBuilder} and checks if
	 * the created {@link TaskConfiguration}s are correct.
	 */
	// @Test
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
				"categoryConfigPath=" + POI_CATEGORY_CONFIG_FILE_PATH);

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
		assertEquals(POI_CATEGORY_CONFIG_FILE_PATH, taskConfigurations.get(3)
				.getConfigArgs().get("categoryConfigPath"));
	}

	/**
	 * Creates one task using {@link TaskConfigurationBuilder} and checks if the
	 * created {@link TaskConfiguration} is correct.
	 */
	// @Test
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
		String rootSectionName = MainWizard.SETTINGS_SECTION_NAME;
		String generalSectionName = OptionSelectionWizardPage.getSettingsSectionName();
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
		String rootSectionName = MainWizard.SETTINGS_SECTION_NAME;
		String generalSectionName = OptionSelectionWizardPage.getSettingsSectionName();
		String poiSectionName = POIWizardPage.getSettingsSectionName();
		
		// General settings
		IDialogSettings settings = new DialogSettings(rootSectionName);
		IDialogSettings generalOptionsSection = settings
				.addNewSection(generalSectionName);
		generalOptionsSection.put("inputFilePath", EMPTY_INPUT_FILE_PATH);
		generalOptionsSection.put("createVectorMap", true);
		generalOptionsSection.put("createPOIs", false);
		
		return settings;
	}

	private TaskConfiguration getPOITaskConfigurationFromIDialogSettings(
			IDialogSettings settings) {
		
		TaskConfigurationBuilder builder = new TaskConfigurationBuilder();
		builder.buildTaskConfigurationFromDialogSettings(settings);
		List<TaskConfiguration> configurations = builder.getTaskConfigurations();
		
		// There should only be two Osmosis tasks: one for reading and one for writing
		assertEquals(2, configurations.size());

		return configurations.get(1);
	}

	private TaskConfiguration getMFWTaskConfigurationFromIDialogSettings(
			IDialogSettings mfwSettings) {
		// TODO Auto-generated method stub
		return null;
	}

	private void validatePOIConfig(TaskConfiguration poiConfig) {
		Map<String, String> configArgs = poiConfig.getConfigArgs();
		String defaultArg = poiConfig.getDefaultArg();
		
		// Check type
		assertEquals("poi-writer", poiConfig.getType());
		
		// Check Osmosis parameters
		assertEquals(POI_CATEGORY_CONFIG_FILE_PATH, configArgs.get("categoryConfigPath"));
		
		if(defaultArg == null) {
			assertEquals(POI_OUTPUT_PATH, configArgs.get("file"));
		} else {
			assertEquals(POI_OUTPUT_PATH, defaultArg);
		}
	}
	
	private void validateMapFileWriterConfig(TaskConfiguration mfwConfig) {
		
	}
}
