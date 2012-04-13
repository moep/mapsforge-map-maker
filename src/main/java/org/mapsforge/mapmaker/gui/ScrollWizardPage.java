package org.mapsforge.mapmaker.gui;

import java.util.Locale;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class ScrollWizardPage extends WizardPage {

	protected ScrollWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
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
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(lblMapFilePath, 6);
		fd_text.left = new FormAttachment(0, 0);
		tfOutputFilePath.setLayoutData(fd_text);

		// BROWSE FILES BUTTON
		Button btnNewButton = new Button(container, SWT.NONE);
		fd_text.right = new FormAttachment(btnNewButton, -6);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(tfOutputFilePath, 0,
				SWT.CENTER);
		fd_btnNewButton.right = new FormAttachment(100);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("...");

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
		// onInputChanged();
		lblPreferredLang.setText("Preferred language:");

		Combo inpPreferredLang = new Combo(container, SWT.BORDER
				| SWT.DROP_DOWN | SWT.READ_ONLY);
		FormData fd_inpPreferredLang = new FormData();
		fd_inpPreferredLang.left = new FormAttachment(lblPreferredLang, 6);
		fd_inpPreferredLang.bottom = new FormAttachment(lblPreferredLang, 0,
				SWT.BOTTOM);
		inpPreferredLang.setLayoutData(fd_inpPreferredLang);
		populateLanguageDropDownMenu(inpPreferredLang);
		inpPreferredLang.pack();

		// Allow container's content to be scrolled by defining container as
		// scrolledComposite's content
		scrolledComposite.setContent(container);

		// Calculate size
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		System.out.println("Container size: "
				+ container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
	}

	private void populateLanguageDropDownMenu(Combo inpPreferredLang) {
		// See: http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
		String[] countryCodes = Locale.getISOCountries();
		inpPreferredLang.add("(none)");
		for (String code : countryCodes) {
			inpPreferredLang.add(code);
		}

	}

}
