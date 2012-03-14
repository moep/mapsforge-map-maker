package org.mapsforge.mapmaker.gui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class POIWizardPage extends WizardPage {

	protected POIWizardPage(String pageName) {
		super(pageName);
		setPageComplete(true);
		setTitle(pageName);
		setImageDescriptor(ImageDescriptor.createFromFile(null, "logo.png"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
	}

}
