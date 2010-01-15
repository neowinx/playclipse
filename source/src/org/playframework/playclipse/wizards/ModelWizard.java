package org.playframework.playclipse.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.playframework.playclipse.CodeTemplates;

public class ModelWizard extends PlayWizard {

	@Override
	public void addPages() {
		page = new ModelWizardPage(selection);
		addPage(page);
	}

	@Override
	protected String getContent(String name) {
		return CodeTemplates.model(name);
	}

	@Override
	protected IFile getTargetFile(IContainer container, String name) {
		return container.getFile(new Path(name + ".java"));
	}

}