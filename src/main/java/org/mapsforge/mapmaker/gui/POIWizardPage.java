package org.mapsforge.mapmaker.gui;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class POIWizardPage extends WizardPage {
	private String categoryConfigPath;
	private IDialogSettings settings;
	
	private Text tfCategoryConfigPath;

	protected POIWizardPage(String pageName, IDialogSettings settings) {
		super(pageName);
		setPageComplete(false);
		setTitle(pageName);
		setImageDescriptor(ImageDescriptor.createFromFile(null, "logo.png"));
		this.settings = settings;
		this.settings.addNewSection("poi");
	}

	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FormLayout());
		
		Label lblSelectFile = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.right = new FormAttachment(0, 588);
		fd_lblNewLabel.top = new FormAttachment(0);
		fd_lblNewLabel.left = new FormAttachment(0);
		lblSelectFile.setLayoutData(fd_lblNewLabel);
		lblSelectFile.setText("Category Mapping Configuration File:");
		
		tfCategoryConfigPath = new Text(container, SWT.BORDER);
		tfCategoryConfigPath.setText("POICategoriesOsmosis.xml");
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(lblSelectFile, 6);
		fd_text.left = new FormAttachment(lblSelectFile, 0, SWT.LEFT);
		tfCategoryConfigPath.setLayoutData(fd_text);
		tfCategoryConfigPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setCategoryConfigPath(((Text) e.widget).getText());
			}
		});
		
		Button btnOpenFile = new Button(container, SWT.NONE);
		btnOpenFile.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog d = new FileDialog(container.getShell());
				d.setFilterPath(System.getProperty("user.dir"));
				d.setFilterExtensions(new String[] { "*.xml" });				
				String selection = d.open();
				setCategoryConfigPath(selection);
			}

		});
		fd_text.right = new FormAttachment(btnOpenFile, -6);
		FormData fd_btnOpenFile = new FormData();
		fd_btnOpenFile.top = new FormAttachment(lblSelectFile);
		fd_btnOpenFile.right = new FormAttachment(100, -10);
		btnOpenFile.setLayoutData(fd_btnOpenFile);
		btnOpenFile.setText("...");
	}
	
	protected void setCategoryConfigPath(String path) {
		this.categoryConfigPath = path;
		
		if (path != null && !path.equals(this.tfCategoryConfigPath.getText())) {
			this.tfCategoryConfigPath.setText(path);
		}
		
		onInputChanged();
	}
	
	void onInputChanged() {
		boolean isValid = isPageComplete();

		// Show / hide error message
		if (isValid) {
			setErrorMessage(null);
			updateSettings();
		}
		
		setPageComplete(isValid);				
	}
	
	private void updateSettings() {
		IDialogSettings section = this.settings.getSections()[0];
		section.put("categoryConfigPath", this.tfCategoryConfigPath.getText());
		
		System.out.println("Settings have been updated ");
	}
	
	@Override
	public boolean isPageComplete() {
		boolean isValid = true;

		// Input file must be set
		if (this.categoryConfigPath == null) {
			super.setErrorMessage("No file selected.");
			isValid = false;
		}

		// Input file must exist be a file and be writable
		if (this.categoryConfigPath != null) {
			File f = new File(this.categoryConfigPath);

			if (isValid && !f.exists()) {
				super.setErrorMessage("The given input file does not exist. ("
						+ f.getAbsolutePath() + ")");
				isValid = false;
			}

			if (isValid && !f.isFile()) {
				super.setErrorMessage("The given input file is not a file.");
				isValid = false;
			}

			if (isValid && !f.canRead()) {
				super.setErrorMessage("Cannot read the specified file.");
				isValid = false;
			}

		}
		
		return isValid;
	}
	
}
