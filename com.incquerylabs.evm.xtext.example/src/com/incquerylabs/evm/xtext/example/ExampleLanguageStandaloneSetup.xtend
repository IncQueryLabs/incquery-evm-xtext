/*
 * generated by Xtext 2.9.0
 */
package com.incquerylabs.evm.xtext.example


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class ExampleLanguageStandaloneSetup extends ExampleLanguageStandaloneSetupGenerated {

	def static void doSetup() {
		new ExampleLanguageStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
