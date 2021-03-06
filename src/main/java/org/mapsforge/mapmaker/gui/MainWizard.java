package org.mapsforge.mapmaker.gui;

import java.io.IOException;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class MainWizard extends Wizard {
	/**
	 * This should be private. It is only set to public to prevent an
	 * {@link ExceptionInInitializerError} for this class'
	 * {@link #getSettingsSectionName()} method called by JUnit..
	 */
	public static final String SETTINGS_ROOT_SECTION_NAME = "osmosis parameters";
	private static final String SETTINGS_FILE = "lastSession.settings.xml";

	public MainWizard() {
		setWindowTitle("Mapsforge Map Creation Wizard");
		IDialogSettings settings = new DialogSettings(SETTINGS_ROOT_SECTION_NAME);
		try {
			settings.load(SETTINGS_FILE);
			System.out
					.println("[Wizard] Last used settings have been loaded from file.");
		} catch (IOException e) {
			System.out
					.println("[Wizard] Last used settings could not be used.");
		}

		setDialogSettings(settings);
		
		// Add wizard pages
		IWizardPage generalOptionsPage = new OptionSelectionWizardPage(
				"General Settings", settings);
		addPage(generalOptionsPage);

		IWizardPage mapFileOptionsPage = new MapFileWizardPage(
				MapFileWizardPage.getStaticTitle(), settings);
		addPage(mapFileOptionsPage);

		IWizardPage poiOptionsPage = new POIWizardPage(POIWizardPage.getStaticTitle(), settings);
		addPage(poiOptionsPage);
		
		// Create dynamic wizard page configuration
		WizardPageManager.getInstance().initialize(this);
	}

	public static String getSettingsSectionName() {
		return SETTINGS_ROOT_SECTION_NAME;
	}

	@Override
	public boolean performFinish() {
		System.out.println("Performing finish");
		cleanUp();
		return true;
	}

	@Override
	public boolean performCancel() {
		System.out.println("Canceled");
		cleanUp();
		return true;
	}
	
	private void cleanUp() {
		for(IWizardPage p : getPages()) {
			p.dispose();
		}
	}

	@Override
	public IDialogSettings getDialogSettings() {
		return super.getDialogSettings();
	}

}
