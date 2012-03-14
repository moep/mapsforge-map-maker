package org.mapsforge.mapmaker.gui;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MainWizard extends Wizard {
	public MainWizard() {
		setWindowTitle("window title");
		//addPage(new TestWizardPage("select file"));
		addPage(new OptionSelectionWizardPage("select file"));
		addPage(new POIWizardPage("POI settings"));
	}
	
	
	@Override
	public boolean performFinish() {
		System.out.println("Performing finish");
		return true;
	}
}
