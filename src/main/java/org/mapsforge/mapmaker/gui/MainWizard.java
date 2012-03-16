package org.mapsforge.mapmaker.gui;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class MainWizard extends Wizard {
	
	public MainWizard() {
		setWindowTitle("Mapsforge Map Creation Wizard");
		IDialogSettings settings = new DialogSettings("osmosis parameters");
		setDialogSettings(settings);
		settings.put("test", "bla");
		settings.addNewSection("sec");
		settings.getSection("sec").put("blubb", "bla");
		IWizardPage generalOptionsPage = new OptionSelectionWizardPage("General Settings", settings);
		addPage(generalOptionsPage);
		
		IWizardPage poiOptionsPage = new POIWizardPage("POI Settings", settings);
		addPage(poiOptionsPage);
	}
		
	@Override
	public boolean performFinish() {
		System.out.println("Performing finish");
		return true;
	}
	
	@Override
	public boolean performCancel() {
		System.out.println("Canceled");
		return true;		
	}
	
	@Override
	public IDialogSettings getDialogSettings() {
		return super.getDialogSettings();
	}
	
}
