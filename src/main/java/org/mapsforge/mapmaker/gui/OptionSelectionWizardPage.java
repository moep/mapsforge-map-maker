package org.mapsforge.mapmaker.gui;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class OptionSelectionWizardPage extends WizardPage {
	private String inputFilePath;
	private IDialogSettings settings; 
	final static String SETTINGS_SECTION_NAME = "general";
	
	protected Text tfInputFilePath;
	private FormData fd_text;
	private Button btnOpenFile;
	private Link link;
	private Button ckboxCreateVectorMap;
	private Button ckboxCreatePOIs;

	public OptionSelectionWizardPage(String pageName, IDialogSettings settings) {
		super(pageName);
		setPageComplete(false);
		setTitle(pageName);
		setImageDescriptor(ImageDescriptor.createFromFile(null, "logo.png"));
		this.settings = settings;
		this.settings.addNewSection("general");
	}

	@Override
	public void createControl(final Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new FormLayout());

		createInputFilePathTextField(container);
		createOpenInputFileButton(container);
		createLink(container);
		createCheckBoxes(container);

	}

	private void createCheckBoxes(Composite container) {
		final SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onInputChanged();
				// TODO update wizard next page list
			}
		};

		// Vector map
		this.ckboxCreateVectorMap = new Button(container, SWT.CHECK);
		FormData fd_chkBoxCreateVectorMap = new FormData();
		fd_chkBoxCreateVectorMap.right = new FormAttachment(
				this.tfInputFilePath, 0, SWT.RIGHT);
		fd_chkBoxCreateVectorMap.top = new FormAttachment(this.link, 6);
		fd_chkBoxCreateVectorMap.left = new FormAttachment(0);
		this.ckboxCreateVectorMap.setLayoutData(fd_chkBoxCreateVectorMap);
		this.ckboxCreateVectorMap.setText("Create Vector Map (unavailable)");
		this.ckboxCreateVectorMap.setEnabled(false);
		this.ckboxCreateVectorMap.addSelectionListener(selectionListener);

		// POIs
		this.ckboxCreatePOIs = new Button(container, SWT.CHECK);
		FormData fd_ckboxCreatePOIs = new FormData();
		fd_ckboxCreatePOIs.right = new FormAttachment(this.tfInputFilePath, 0,
				SWT.RIGHT);
		fd_ckboxCreatePOIs.top = new FormAttachment(this.ckboxCreateVectorMap,
				6);
		fd_ckboxCreatePOIs.left = new FormAttachment(this.tfInputFilePath, 0,
				SWT.LEFT);
		this.ckboxCreatePOIs.setLayoutData(fd_ckboxCreatePOIs);
		this.ckboxCreatePOIs.setText("Create POI Database");
		this.ckboxCreatePOIs.addSelectionListener(selectionListener);
	}

	private void createLink(Composite container) {
		{
			this.link = new Link(container, SWT.NONE);
			FormData fd_link = new FormData();
			fd_link.top = new FormAttachment(this.tfInputFilePath);
			fd_link.left = new FormAttachment(this.tfInputFilePath, 0, SWT.LEFT);
			this.link.setLayoutData(fd_link);
			this.link
					.setText("You can download raw data from "
							+ "<a href=\"http://download.geofabrik.de/osm/\">Geofabrik</a>"
							+ " or <a href=\"http://downloads.cloudmade.com/\">CloudMade</a>.");
			this.link.addListener(SWT.Selection, new Listener() {
				@Override
				public void handleEvent(Event e) {
					Program.launch(e.text);
				}
			});
		}
	}

	private void createInputFilePathTextField(Composite container) {
		this.tfInputFilePath = new Text(container, SWT.BORDER);
		this.tfInputFilePath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setInputFilePath(((Text) e.widget).getText());
			}
		});

		this.fd_text = new FormData();
		this.fd_text.top = new FormAttachment(0);
		this.fd_text.left = new FormAttachment(0);
		this.tfInputFilePath.setLayoutData(this.fd_text);
	}

	private void createOpenInputFileButton(final Composite container) {
		this.btnOpenFile = new Button(container, SWT.NONE);
		this.btnOpenFile.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog d = new FileDialog(container.getShell());
				d.setFilterPath(System.getProperty("user.home"));
				d.setFilterExtensions(new String[] { "*.osm.pbf", "*.osm" });
				d.setFilterNames(new String[] {
						"OSM Protocol Buffer (*.osm.pbf)", "OSM XML (*.osm)" });
				String selection = d.open();
				setInputFilePath(selection);
			}

		});
		this.fd_text.right = new FormAttachment(this.btnOpenFile, -6);
		FormData fd_btnOpenFile = new FormData();
		fd_btnOpenFile.top = new FormAttachment(0);
		fd_btnOpenFile.right = new FormAttachment(100);
		this.btnOpenFile.setLayoutData(fd_btnOpenFile);
		this.btnOpenFile.setText("...");
	}

	protected void setInputFilePath(String path) {
		this.inputFilePath = path;
		if (path != null && !path.equals(this.tfInputFilePath.getText())) {
			this.tfInputFilePath.setText(path);
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
		section.put("inputFilePath", this.tfInputFilePath.getText());
		section.put("createVectorMap", this.ckboxCreateVectorMap.getSelection());
		section.put("createPOIs", this.ckboxCreatePOIs.getSelection());
		
		System.out.println("Settings have been updated ");
	}

	@Override
	public boolean isPageComplete() {
		boolean isValid = true;

		// Input file must be set
		if (this.inputFilePath == null) {
			super.setErrorMessage("No input file selected.");
			isValid = false;
		}

		// Input file must exist be a file and be writable
		if (this.inputFilePath != null) {
			File f = new File(this.inputFilePath);

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

		// At least one checkbox must be ticked
		if (isValid && !this.ckboxCreatePOIs.getSelection()
				&& !this.ckboxCreateVectorMap.getSelection()) {
			super.setErrorMessage("You have to select at least one build target.");
			isValid = false;
		}

		return isValid;
	}
	
	

}
