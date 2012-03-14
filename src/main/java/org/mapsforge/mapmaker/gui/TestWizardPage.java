package org.mapsforge.mapmaker.gui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TestWizardPage extends WizardPage {
	private String inputFilePath;
	
	// SWT eleḿents
	private Text inputFile;
	Link linkHint1;	
	private Composite composite_1;
	
	public TestWizardPage(String pageName) {
		super(pageName);
		setPageComplete(false);
		setTitle(pageName);
	}

	@Override
	public void createControl(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
        setControl(composite);
        composite.setLayout(new GridLayout(2, false));
        
        createInputFileTextField(composite);
        
        Button btnOpenFile = new Button(composite, SWT.NONE);
        GridData gd_btnOpenFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_btnOpenFile.widthHint = 65;
        btnOpenFile.setLayoutData(gd_btnOpenFile);
        btnOpenFile.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseUp(MouseEvent e) {
        		FileDialog d = new FileDialog(parent.getShell());
        		// TODO remove hardcoded file path
        		d.setFilterPath("/home/moep/maps/");
        		d.setFilterExtensions(new String[]{"*.osm.pbf", "*.osm"});
        		d.setFilterNames(new String[]{"OSM Protocol Buffer (*.osm.pbf)", "OSM XML (*.osm)"});
        		String selection = d.open();
        		setInputFile(selection);
        		checkConstraints();
        	}
        });
        btnOpenFile.setText("...");
        
        createHintLinks(composite);
        new Label(composite, SWT.NONE);
        {
        	composite_1 = new Composite(composite, SWT.BORDER);
        	GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        	gd_composite_1.heightHint = 209;
        	composite_1.setLayoutData(gd_composite_1);
        	composite_1.setLayout(new FormLayout());
        	
        	Button ckboxCreateVectorMap = new Button(composite_1, SWT.CHECK);
        	ckboxCreateVectorMap.addSelectionListener(new SelectionAdapter() {
        		@Override
        		public void widgetSelected(SelectionEvent e) {
        		}
        	});
        	FormData fd_ckboxCreateVectorMap = new FormData();
        	fd_ckboxCreateVectorMap.left = new FormAttachment(0);
        	ckboxCreateVectorMap.setLayoutData(fd_ckboxCreateVectorMap);
        	ckboxCreateVectorMap.setText("Create Vector Map");
        }
        new Label(composite, SWT.NONE);
	}

	private void createInputFileTextField(Composite composite) {
		this.inputFile = new Text(composite, SWT.BORDER);
        GridData gd_inputFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_inputFile.widthHint = 505;
        inputFile.setLayoutData(gd_inputFile);
	}

	private void createHintLinks(Composite composite) {
		this.linkHint1 = new Link(composite, SWT.NONE);
        this.linkHint1.setText("You can download raw data from <a href=\"http://download.geofabrik.de/osm/\">Geofabrik</a> or <a href=\"http://downloads.cloudmade.com/\">CloudMade</a>.");
        this.linkHint1.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				Program.launch(e.text);
			}
        });
	}
	
	private void setInputFile(String path) {
		this.inputFilePath = path;
	}
	
	private void checkConstraints() {
		if(this.inputFilePath != null) {
			this.setPageComplete(true);
			setErrorMessage(null);
		} else {
			setErrorMessage("No input file has been selected");
		}
	}
}
