package com.incquerylabs.evm.xtext.ui.util;

import org.eclipse.incquery.patternlanguage.emf.ui.EMFPatternLanguageExecutableExtensionFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class XtextIntegrationExecutableExtensionFactory extends EMFPatternLanguageExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return FrameworkUtil.getBundle(this.getClass());
	}

}
