package org.mapsforge.mapmaker.gui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TestWizardPage extends WizardPage {
	private String inputFilePath;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	
	
	public TestWizardPage(String pageName) {
		super(pageName);
		setPageComplete(false);
		setTitle(pageName);
	}

	@Override
	public void createControl(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
        setControl(composite);
        composite.setLayout(new FormLayout());
        
        Group grpTest = new Group(composite, SWT.NONE);
        grpTest.setText("test");
        FormData fd_grpTest = new FormData();
        fd_grpTest.bottom = new FormAttachment(0, 270);
        fd_grpTest.right = new FormAttachment(0, 588);
        fd_grpTest.top = new FormAttachment(0);
        fd_grpTest.left = new FormAttachment(0);
        grpTest.setLayoutData(fd_grpTest);
        
        grpTest.setLayout(new GridLayout(4, false));
        
        Label lblNewLabel = new Label(grpTest, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("New Label");
        
        text = new Text(grpTest, SWT.BORDER);
        GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_text.widthHint = 178;
        text.setLayoutData(gd_text);
        
        Label lblNewLabel_1 = new Label(grpTest, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("New Label");
        
        text_1 = new Text(grpTest, SWT.BORDER);
        text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_2 = new Label(grpTest, SWT.NONE);
        lblNewLabel_2.setText("gsdfgdfg");
        
        text_2 = new Text(grpTest, SWT.BORDER);
        text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label lblNewLabel_3 = new Label(grpTest, SWT.NONE);
        lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_3.setText("New Label");
        
        text_3 = new Text(grpTest, SWT.BORDER);
        text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
	}
}
