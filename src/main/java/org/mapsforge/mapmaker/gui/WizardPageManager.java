package org.mapsforge.mapmaker.gui;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

/**
 * This class is a workaround for SWT's inflexible handling of a Wizard's
 * {@link WizardPage}s.
 * 
 * @author Karsten Groll
 */
class WizardPageManager {
	private static WizardPageManager instance = null;
	private Wizard mainWizard = null;
	private boolean enabledPagesMask[];

	private WizardPageManager() {
	}

	static WizardPageManager getInstance() {
		if (instance == null) {
			instance = new WizardPageManager();
		}

		return instance;
	}

	void initialize(Wizard mainWizard) {
		this.mainWizard = mainWizard;
		this.enabledPagesMask = new boolean[mainWizard.getPages().length];
		enabledPagesMask[0] = true;
	}

	IWizardPage getNextWizardPage(IWizardPage currentWizardPage) {
		System.out.println("-----------");

		IWizardPage nextPage = null;

		// Determine the page's position
		IWizardPage pages[] = this.mainWizard.getPages();

		int pos = getPosForPage(currentWizardPage);

		// This should not happen if this method is called correctly
		if (pos == -1) {
			return null;
		}

		// Determine the page's successor (= the first page that is set to true)
		for (++pos; pos < enabledPagesMask.length; pos++) {
			if (this.enabledPagesMask[pos]) {
				break;
			}
		}

		// Current page has no successor
		if (pos > pages.length - 1) {
			return null;
		}

		return pages[pos];
	}

	void setWizardPageEnabled(String title, boolean flag) {
		IWizardPage page = this.mainWizard.getPage(title);
		int pos = getPosForPage(page);

		if (pos != -1 && pos < this.enabledPagesMask.length) {
			this.enabledPagesMask[pos] = flag;
		} else {
			// TODO error handling (exceptions?)
		}
	}

	/**
	 * Returns the page's position within the internal array.
	 * 
	 * @param page
	 *            The page to look for
	 * @return The page's position in the internal array or -1 if the array does
	 *         not contain the page.
	 */
	private int getPosForPage(IWizardPage page) {
		IWizardPage[] pages = this.mainWizard.getPages();
		int pos;
		for (pos = 0; pos < pages.length; pos++) {
			if (page == pages[pos]) {
				break;
			}
		}

		// Page not found
		if (pages[pos] != page) {
			return -1;
		}

		return pos;
	}

	Collection<IWizardPage> getEnabledPages() {
		IWizardPage[] pages = this.mainWizard.getPages();
		Collection<IWizardPage> enabledPages = new LinkedList<IWizardPage>();

		for (int i = 0; i < this.enabledPagesMask.length; i++) {
			if (this.enabledPagesMask[i]) {
				enabledPages.add(pages[i]);
			}
		}

		return enabledPages;

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		IWizardPage pages[] = this.mainWizard.getPages();

		sb.append('[');
		for (int i = 0; i < this.enabledPagesMask.length; i++) {
			if (this.enabledPagesMask[i]) {
				sb.append(pages[i].getTitle());
				sb.append(", ");
			}
		}

		sb.append("]");
		return sb.toString();
	}
}
