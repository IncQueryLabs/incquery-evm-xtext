package com.incquerylabs.evm.xtext.ui.util;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.eclipse.xtext.ui.shared.SharedStateModule;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class XtextIntegrationExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return FrameworkUtil.getBundle(this.getClass());
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new SharedStateModule());
	}
}
