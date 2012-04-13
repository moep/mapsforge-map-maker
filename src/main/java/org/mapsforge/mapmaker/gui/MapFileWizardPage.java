package org.mapsforge.mapmaker.gui;

import java.util.Locale;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class MapFileWizardPage extends WizardPage {
	private IDialogSettings settings;
	private final static String SETTINGS_SECTION_NAME = "mapfile";
	private Table inpZoomIntervalConfiguration;

	protected MapFileWizardPage(String pageName, IDialogSettings settings) {
		super(pageName);
		setPageComplete(false);
		setTitle(pageName);
		setImageDescriptor(ImageDescriptor.createFromFile(null, "logo.png"));
		this.settings = settings;
		this.settings.addNewSection(SETTINGS_SECTION_NAME);
	}

	@Override
	public void createControl(final Composite parent) {
		final ScrolledComposite scrolledComposite = new ScrolledComposite(
				parent, SWT.H_SCROLL | SWT.V_SCROLL);
		setControl(scrolledComposite);

		// Child composite for hiding controls
		final Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new FormLayout());

		// LABEL
		Label lblMapFilePath = new Label(container, SWT.NONE);
		FormData fd_lblMapFilePath = new FormData();
		fd_lblMapFilePath.top = new FormAttachment(0);
		fd_lblMapFilePath.left = new FormAttachment(0);
		lblMapFilePath.setLayoutData(fd_lblMapFilePath);
		lblMapFilePath.setText("Save as...");

		// OUTPUT FILE TEXT FIELD
		Text tfOutputFilePath = new Text(container, SWT.BORDER);
		FormData fd_tfOutputFilePath = new FormData();
		fd_tfOutputFilePath.top = new FormAttachment(lblMapFilePath, 6);
		fd_tfOutputFilePath.left = new FormAttachment(0, 0);
		tfOutputFilePath.setLayoutData(fd_tfOutputFilePath);

		// BROWSE FILES BUTTON
		Button btnBrowseMapFile = new Button(container, SWT.NONE);
		fd_tfOutputFilePath.right = new FormAttachment(btnBrowseMapFile, -6);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(tfOutputFilePath, 0,
				SWT.CENTER);
		fd_btnNewButton.right = new FormAttachment(100);
		btnBrowseMapFile.setLayoutData(fd_btnNewButton);
		btnBrowseMapFile.setText("...");

		// HDD MODE CHECKBOX
		Button chkEnableHDDCache = new Button(container, SWT.CHECK);
		FormData fd_chkEnableHDDCache = new FormData();
		fd_chkEnableHDDCache.top = new FormAttachment(tfOutputFilePath, 6);
		chkEnableHDDCache.setLayoutData(fd_chkEnableHDDCache);
		chkEnableHDDCache
				.setText("Cache tiles to HDD (slower but required for bigger map files)");

		// CUSTOM START POSITION CHECKBOX
		Button chkEnableCustomStartPosition = new Button(container, SWT.CHECK);
		FormData fd_chkEnableCustomStartPosition = new FormData();
		fd_chkEnableCustomStartPosition.top = new FormAttachment(
				chkEnableHDDCache, 6);
		chkEnableCustomStartPosition
				.setLayoutData(fd_chkEnableCustomStartPosition);
		chkEnableCustomStartPosition.setText("Use custom start position");

		// MAP START POSITION GROUP
		Group grpMapStartPositions = new Group(container, SWT.NONE);
		FormData fd_grpMapStartPositions = new FormData();
		fd_grpMapStartPositions.top = new FormAttachment(
				chkEnableCustomStartPosition, 6);
		fd_grpMapStartPositions.left = new FormAttachment(0, 0);
		fd_grpMapStartPositions.right = new FormAttachment(100, 0);
		grpMapStartPositions.setLayoutData(fd_grpMapStartPositions);
		grpMapStartPositions.setLayout(new FormLayout());

		// Latitude
		Label lblMapStartLat = new Label(grpMapStartPositions, SWT.NONE);
		FormData fd_lblMapStartLat = new FormData();
		fd_lblMapStartLat.left = new FormAttachment(0);
		lblMapStartLat.setLayoutData(fd_lblMapStartLat);
		lblMapStartLat.setText("Latitude: ");

		Text tfMapStartLat = new Text(grpMapStartPositions, SWT.NONE);
		FormData fd_tfMapStartLat = new FormData();
		fd_tfMapStartLat.bottom = new FormAttachment(lblMapStartLat, 0,
				SWT.BOTTOM);
		tfMapStartLat.setLayoutData(fd_tfMapStartLat);

		// Longitude
		Label lblMapStartLon = new Label(grpMapStartPositions, SWT.NONE);
		FormData fd_lblMapStartLon = new FormData();
		fd_lblMapStartLon.left = new FormAttachment(0);
		fd_lblMapStartLon.top = new FormAttachment(lblMapStartLat, 6);
		lblMapStartLon.setLayoutData(fd_lblMapStartLon);
		lblMapStartLon.setText("Longitude: ");

		Text tfMapStartLon = new Text(grpMapStartPositions, SWT.NONE);
		fd_tfMapStartLat.left = new FormAttachment(tfMapStartLon, 0, SWT.LEFT);
		FormData fd_tfMapStartLon = new FormData();
		fd_tfMapStartLon.top = new FormAttachment(tfMapStartLat, 6);
		fd_tfMapStartLon.left = new FormAttachment(lblMapStartLon, 6);
		tfMapStartLon.setLayoutData(fd_tfMapStartLon);

		// MAP START ZOOM LEVEL
		Button chkEnableCustomMapStartZoom = new Button(container, SWT.CHECK);
		FormData fd_EnableCustomMapStartZoom = new FormData();
		fd_EnableCustomMapStartZoom.top = new FormAttachment(
				grpMapStartPositions, 8);
		chkEnableCustomMapStartZoom.setLayoutData(fd_EnableCustomMapStartZoom);
		chkEnableCustomMapStartZoom
				.setText("Use custom map start zoom level: ");

		Spinner inpMapStartZoom = new Spinner(container, SWT.BORDER);
		FormData fd_inpMapStartZoom = new FormData();
		fd_inpMapStartZoom.left = new FormAttachment(
				chkEnableCustomMapStartZoom, 6);
		fd_inpMapStartZoom.bottom = new FormAttachment(
				chkEnableCustomMapStartZoom, 0, SWT.BOTTOM);
		inpMapStartZoom.setLayoutData(fd_inpMapStartZoom);
		inpMapStartZoom.setMinimum(0);
		inpMapStartZoom.setMaximum(21);
		inpMapStartZoom.setIncrement(1);
		inpMapStartZoom.setSelection(14);

		// PREFFERED LANGUAGE
		Label lblPreferredLang = new Label(container, SWT.NONE);
		FormData fd_lblPreferredLang = new FormData();
		fd_lblPreferredLang.top = new FormAttachment(
				chkEnableCustomMapStartZoom, 6);
		lblPreferredLang.setLayoutData(fd_lblPreferredLang);
		onInputChanged();
		lblPreferredLang.setText("Preferred language:");

		Text tfPreferredLang = new Text(container, SWT.NONE);
		FormData fd_tfPreferredLang = new FormData();
		fd_tfPreferredLang.left = new FormAttachment(lblPreferredLang, 6);
		fd_tfPreferredLang.bottom = new FormAttachment(lblPreferredLang, 0,
				SWT.BOTTOM);
		tfPreferredLang.setLayoutData(fd_tfPreferredLang);
		tfPreferredLang.setTextLimit(2);
		// TODO Set optimal textfield width
		tfPreferredLang.pack();

		Link lblPreferredLangHelpText = new Link(container, SWT.NONE);
		FormData fd_lblPreferredLangHelpText = new FormData();
		fd_lblPreferredLangHelpText.left = new FormAttachment(tfPreferredLang,
				6);
		fd_lblPreferredLangHelpText.bottom = new FormAttachment(
				tfPreferredLang, 0, SWT.BOTTOM);
		lblPreferredLangHelpText.setLayoutData(fd_lblPreferredLangHelpText);
		lblPreferredLangHelpText
				.setText("(2-letter code according to <a href=\"http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2\">ISO 3166-1</a>)");

		// COMMENT
		Label lblComment = new Label(container, SWT.NONE);
		FormData fd_lblComment = new FormData();
		fd_lblComment.left = new FormAttachment(0);
		fd_lblComment.top = new FormAttachment(lblPreferredLang, 6);
		lblComment.setLayoutData(fd_lblComment);
		lblComment.setText("Comment:");

		Text tfComment = new Text(container, SWT.NONE);
		FormData fd_tfComment = new FormData();
		fd_tfComment.left = new FormAttachment(tfPreferredLang, 0, SWT.LEFT);
		fd_tfComment.bottom = new FormAttachment(lblComment, 0, SWT.BOTTOM);
		fd_tfComment.right = new FormAttachment(100, -6);
		tfComment.setLayoutData(fd_tfComment);

		//
		// ADVANCED SETTINGS
		//

		final ExpandBar bar = new ExpandBar(container, SWT.NONE);
		// TODO remove hard-coded dimensions
		FormData tmp = new FormData(SWT.DEFAULT, 500);
		tmp.left = new FormAttachment(0);
		tmp.right = new FormAttachment(100);
		tmp.top = new FormAttachment(lblComment, 6);
		// tmp.bottom = new FormAttachment(100);
		bar.setLayoutData(tmp);

		final Composite advancedOptions = new Composite(bar, SWT.NONE);
		advancedOptions.setLayout(new FormLayout());
		ExpandItem expandItem = new ExpandItem(bar, SWT.NONE, 0);
		expandItem
				.setText("Advanced Options (only use when you know what you are doing)");
		expandItem.setControl(advancedOptions);
		expandItem.setExpanded(true);

		// TAG CONFIGURATION FILE
		Button chkUseCustomTagConfigFile = new Button(advancedOptions,
				SWT.CHECK);
		FormData td_chkUseCustomTagConfigFile = new FormData();
		td_chkUseCustomTagConfigFile.left = new FormAttachment(0);
		td_chkUseCustomTagConfigFile.top = new FormAttachment(0, 6);
		chkUseCustomTagConfigFile.setLayoutData(td_chkUseCustomTagConfigFile);
		chkUseCustomTagConfigFile.setText("Use custom tag configuration:");

		Text tfTagConfigurationFilePath = new Text(advancedOptions, SWT.NONE);
		FormData fd_tfTagConfigurationFilePath = new FormData();
		fd_tfTagConfigurationFilePath.left = new FormAttachment(
				chkUseCustomTagConfigFile, 6);
		fd_tfTagConfigurationFilePath.bottom = new FormAttachment(
				chkUseCustomTagConfigFile, 0, SWT.BOTTOM);
		tfTagConfigurationFilePath.setLayoutData(fd_tfTagConfigurationFilePath);
		tfTagConfigurationFilePath.setText("fgsdlkfndklj");

		Button btnBrowseTagConfigurationFile = new Button(advancedOptions,
				SWT.PUSH);
		fd_tfTagConfigurationFilePath.right = new FormAttachment(
				btnBrowseTagConfigurationFile, -6);
		FormData fd_btnBrowseTagConfigurationFile = new FormData();
		fd_btnBrowseTagConfigurationFile.top = new FormAttachment(
				tfTagConfigurationFilePath, 0, SWT.CENTER);
		fd_btnBrowseTagConfigurationFile.right = new FormAttachment(100);
		btnBrowseTagConfigurationFile
				.setLayoutData(fd_btnBrowseTagConfigurationFile);
		btnBrowseTagConfigurationFile.setText("...");

		// POLYGON CLIPPING
		Button chkEnablePolygonClipping = new Button(advancedOptions, SWT.CHECK);
		FormData fd_chkEnablePolygonClipping = new FormData();
		fd_chkEnablePolygonClipping.top = new FormAttachment(
				chkUseCustomTagConfigFile, 6);
		chkEnablePolygonClipping.setLayoutData(fd_chkEnablePolygonClipping);
		chkEnablePolygonClipping
				.setText("use polygon clipping to reduce map file size (minimal performance overhead)");
		chkEnablePolygonClipping.setSelection(true);

		// WAY CLIPPING
		Button chkEnableWayClipping = new Button(advancedOptions, SWT.CHECK);
		FormData fd_chkEnableWayClipping = new FormData();
		fd_chkEnableWayClipping.top = new FormAttachment(
				chkEnablePolygonClipping, 6);
		chkEnableWayClipping.setLayoutData(fd_chkEnableWayClipping);
		chkEnableWayClipping
				.setText("Use way clipping to reduce map file size (minimal performance overhead)");
		chkEnableWayClipping.setSelection(true);

		// LABEL POSITION
		Button chkComputeLabelPositions = new Button(advancedOptions, SWT.CHECK);
		FormData fd_chkComputeLabelPositions = new FormData();
		chkComputeLabelPositions.setLayoutData(fd_chkComputeLabelPositions);
		fd_chkComputeLabelPositions.top = new FormAttachment(
				chkEnableWayClipping, 6);
		chkComputeLabelPositions
				.setText("Compute label position for polygons that cover multiple tiles (minimal performance overhead)");
		chkComputeLabelPositions.setSelection(true);

		// DEBUG FILE
		Button chkEnableDebugFile = new Button(advancedOptions, SWT.CHECK);
		FormData fd_chkEnableDebugFile = new FormData();
		fd_chkEnableDebugFile.top = new FormAttachment(
				chkComputeLabelPositions, 6);
		chkEnableDebugFile.setLayoutData(fd_chkEnableDebugFile);
		chkEnableDebugFile
				.setText("Write debug information to file (DO NOT activate this option unless you know, what you are doing.)");
		chkEnableCustomMapStartZoom.setSelection(false);

		// SIMPLIFICATION FACTOR
		Label lblSimplificationFactor = new Label(advancedOptions, SWT.NONE);
		FormData fd_lblSimplificationFactor = new FormData();
		fd_lblSimplificationFactor.top = new FormAttachment(chkEnableDebugFile,
				6);
		lblSimplificationFactor.setLayoutData(fd_lblSimplificationFactor);
		lblSimplificationFactor.setText("Simplification factor: ");

		Spinner inpSimplificationFactor = new Spinner(advancedOptions, SWT.NONE);
		FormData fd_inpSimplificationFactor = new FormData();
		fd_inpSimplificationFactor.bottom = new FormAttachment(
				lblSimplificationFactor, 0, SWT.CENTER);
		fd_inpSimplificationFactor.left = new FormAttachment(
				tfTagConfigurationFilePath, 0, SWT.LEFT);
		inpSimplificationFactor.setLayoutData(fd_inpSimplificationFactor);
		inpSimplificationFactor.setDigits(2);
		inpSimplificationFactor.setMaximum(Integer.MAX_VALUE);
		inpSimplificationFactor.setSelection(500);
		// inpSimplificationFactor.setMinimum(0);

		// BOUNDING BOX ENLARGEMENT
		Label lblBBEnlargement = new Label(advancedOptions, SWT.NONE);
		FormData fd_lblBBEnlargement = new FormData();
		fd_lblBBEnlargement.top = new FormAttachment(lblSimplificationFactor, 6);
		lblBBEnlargement.setLayoutData(fd_lblBBEnlargement);
		lblBBEnlargement.setText("Bounding box enlargement in px:");

		Spinner inpBBEnlargement = new Spinner(advancedOptions, SWT.NONE);
		FormData fd_inpBBEnlargement = new FormData();
		fd_inpBBEnlargement.top = new FormAttachment(lblSimplificationFactor, 6);
		fd_inpBBEnlargement.left = new FormAttachment(
				tfTagConfigurationFilePath, 0, SWT.LEFT);
		fd_inpBBEnlargement.right = new FormAttachment(inpSimplificationFactor,
				0, SWT.RIGHT);
		inpBBEnlargement.setLayoutData(fd_inpBBEnlargement);
		inpBBEnlargement.setMinimum(0);
		inpBBEnlargement.setMaximum(Integer.MAX_VALUE);
		inpBBEnlargement.setSelection(20);

		// ZOOM INTERVAL CONFIGURATION
		Label lblZoomIntervalConfig = new Label(advancedOptions, SWT.NONE);
		FormData fd_lblZoomIntervalConfig = new FormData();
		fd_lblZoomIntervalConfig.top = new FormAttachment(lblBBEnlargement, 6);
		lblZoomIntervalConfig.setLayoutData(fd_lblZoomIntervalConfig);
		lblZoomIntervalConfig.setText("Zoom interval configuration:");

		Button btnAddZoomInterval = new Button(advancedOptions, SWT.PUSH);
		FormData fd_btnAddZoomInterval = new FormData();
		fd_btnAddZoomInterval.top = new FormAttachment(lblZoomIntervalConfig, 6);
		btnAddZoomInterval.setLayoutData(fd_btnAddZoomInterval);
		btnAddZoomInterval.setImage(new Image(Display.getCurrent(),
				"list-add.png"));
		btnAddZoomInterval.setToolTipText("Add a zoom interval");

		Button btnRemoveZoomInterval = new Button(advancedOptions, SWT.PUSH);
		FormData fd_btnRemoveZoomInterval = new FormData();
		fd_btnRemoveZoomInterval.bottom = new FormAttachment(
				btnAddZoomInterval, 0, SWT.BOTTOM);
		fd_btnRemoveZoomInterval.left = new FormAttachment(btnAddZoomInterval,
				6);
		btnRemoveZoomInterval.setLayoutData(fd_btnRemoveZoomInterval);
		btnRemoveZoomInterval.setImage(new Image(Display.getCurrent(),
				"list-remove.png"));

		inpZoomIntervalConfiguration = new Table(advancedOptions, SWT.MULTI
				| SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		FormData fd_inpZoomIntervalConfiguration = new FormData(SWT.DEFAULT, 80);
		fd_inpZoomIntervalConfiguration.top = new FormAttachment(
				btnAddZoomInterval, 3);
		fd_inpZoomIntervalConfiguration.left = new FormAttachment(0);
		fd_inpZoomIntervalConfiguration.right = new FormAttachment(100);
		inpZoomIntervalConfiguration
				.setLayoutData(fd_inpZoomIntervalConfiguration);
		initializeZoomIntervalTable();

		// RESTORE DEFAULT SETTINGS
		Button btnDefaultSettings = new Button(container, SWT.PUSH);
		FormData fd_btnDefaultSettings = new FormData();
		fd_btnDefaultSettings.bottom = new FormAttachment(100);
		btnDefaultSettings.setLayoutData(fd_btnDefaultSettings);
		btnDefaultSettings.setText("Restore default settings");

		// Set correct height
		expandItem.setHeight(advancedOptions.computeSize(SWT.DEFAULT,
				SWT.DEFAULT).y);

		// SCROLLBARS
		// Allow container's content to be scrolled by defining container as
		// scrolledComposite's content
		scrolledComposite.setContent(container);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		// Calculate size
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		scrolledComposite.setMinSize(
				container.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, 800);
		System.out.println("Container size: "
				+ container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// EVENT LISTENERS

		setValuesFromSettings();

	}

	/**
	 * Reads last used values from settings object and sets form elements'
	 * values accordingly.
	 */
	private void setValuesFromSettings() {
		System.out.println("[WizardPage] Applying setting values");
		IDialogSettings section = this.settings
				.getSection(SETTINGS_SECTION_NAME);

		// Text field
		// if(section.get("inputFilePath") != null) {
		// this.inputFilePath = section.get("inputFilePath");
		// this.tfInputFilePath.setText(section.get("inputFilePath"));
		// }

		// Checkboxes
		// this.ckboxCreatePOIs.setSelection(section.getBoolean("createPOIs"));

		// Zoom interval configuration
		this.inpZoomIntervalConfiguration.setRedraw(false);
		new TableItem(this.inpZoomIntervalConfiguration, SWT.NONE)
				.setText(new String[] { "5", "0", "7" });
		new TableItem(this.inpZoomIntervalConfiguration, SWT.NONE)
				.setText(new String[] { "10", "8", "11" });
		new TableItem(this.inpZoomIntervalConfiguration, SWT.NONE)
				.setText(new String[] { "14", "12", "21" });
		this.inpZoomIntervalConfiguration.setRedraw(true);

		// Validate inputs
		onInputChanged();
	}

	private void populateLanguageDropDownMenu(Combo inpPreferredLang) {
		// See: http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
		String[] countryCodes = Locale.getISOCountries();
		inpPreferredLang.add("(none)");
		for (String code : countryCodes) {
			inpPreferredLang.add(code);
		}

	}

	private void initializeZoomIntervalTable() {
		this.inpZoomIntervalConfiguration.setLinesVisible(true);
		this.inpZoomIntervalConfiguration.setHeaderVisible(true);

		TableColumn cols[] = new TableColumn[3];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = new TableColumn(this.inpZoomIntervalConfiguration,
					SWT.NONE);
		}

		cols[0].setText("Base zoom level");
		cols[1].setText("Minimal zoom level");
		cols[2].setText("Maximal zoom level");

		for (int i = 0; i < cols.length; i++) {
			cols[i].pack();
		}
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
		IDialogSettings section = this.settings
				.getSection(SETTINGS_SECTION_NAME);

		System.out.println("Settings have been updated ");
	}

	@Override
	public boolean isPageComplete() {
		boolean isValid = true;

		return isValid;
	}
}