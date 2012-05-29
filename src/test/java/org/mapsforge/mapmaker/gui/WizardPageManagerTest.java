package org.mapsforge.mapmaker.gui;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WizardPageManagerTest {
	private Display display;
	private Shell shell;

	private Wizard wizard;
	private WizardPageManager wpm;
	private IWizardPage p1;
	private IWizardPage p2;
	private IWizardPage p3;
	private IWizardPage p4;

	@Before
	public void setup() {
		this.display = new Display();
		this.shell = new Shell(this.display);

		// Create wizard pages
		this.p1 = new MockWizardPage("p1");
		this.p2 = new MockWizardPage("p2");
		this.p3 = new MockWizardPage("p3");
		this.p4 = new MockWizardPage("p4");

		// Create wizard
		this.wizard = new MockWizard();

		// Add pages to wizard
		this.wizard.addPage(p1);
		this.wizard.addPage(p2);
		this.wizard.addPage(p3);
		this.wizard.addPage(p4);

		// Create WizardPageManager
		this.wpm = WizardPageManager.getInstance();

		// Initialize WizardPageManager
		this.wpm.initialize(this.wizard);
	}

	@Test
	public void runTestsInOrder() {
		enableAllPages();
		disablePagesTwoToFour();
		reEnablePageFour();
	}

	private void enableAllPages() {
		this.wpm.setWizardPageEnabled("p1", true);
		this.wpm.setWizardPageEnabled("p2", true);
		this.wpm.setWizardPageEnabled("p3", true);
		this.wpm.setWizardPageEnabled("p4", true);

		// There should be 4 enabled pages
		Assert.assertEquals(4, this.wpm.getEnabledPages().size());

		// Pages should be in order
		Assert.assertEquals(this.p1, this.wpm.getEnabledPages().toArray()[0]);
		Assert.assertEquals(this.p2, this.wpm.getEnabledPages().toArray()[1]);
		Assert.assertEquals(this.p3, this.wpm.getEnabledPages().toArray()[2]);
		Assert.assertEquals(this.p4, this.wpm.getEnabledPages().toArray()[3]);
	}

	private void disablePagesTwoToFour() {
		this.wpm.setWizardPageEnabled("p2", false);
		this.wpm.setWizardPageEnabled("p3", false);
		this.wpm.setWizardPageEnabled("p4", false);

		// There should be 1 enabled page
		Assert.assertEquals(1, this.wpm.getEnabledPages().size());

		// Only the remaining page should be "p1"
		Assert.assertEquals(this.p1, this.wpm.getEnabledPages().toArray()[0]);
	}

	private void reEnablePageFour() {
		this.wpm.setWizardPageEnabled("p4", true);

		// There should be 2 enabled pages
		Assert.assertEquals(2, this.wpm.getEnabledPages().size());

		// Pages should be in order
		Assert.assertEquals(this.p1, this.wpm.getEnabledPages().toArray()[0]);
		Assert.assertEquals(this.p4, this.wpm.getEnabledPages().toArray()[1]);
	}

	@After
	public void cleanUp() {
		this.wizard.dispose();
		this.shell.close();
		this.shell.dispose();
		this.display.close();
		this.display.dispose();
	}

	// MOCK CLASSES

	private class MockWizard extends Wizard {

		protected MockWizard() {
		}

		@Override
		public boolean performFinish() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	private class MockWizardPage extends WizardPage {

		protected MockWizardPage(String pageName) {
			super(pageName);
		}

		@Override
		public void createControl(Composite arg0) {

		}
	}

}
