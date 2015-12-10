package com.incquerylabs.evm.xtext;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.incquery.runtime.evm.api.Job;
import org.eclipse.incquery.runtime.evm.api.RuleSpecification;
import org.eclipse.incquery.runtime.evm.api.event.ActivationState;

import com.incquerylabs.evm.xtext.eobject.IXtextObjectProcessor;
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject;
import com.incquerylabs.evm.xtext.eobject.XtextObjectJob;
import com.incquerylabs.evm.xtext.eobject.event.XtextIndexObjectEventSourceSpecification;
import com.incquerylabs.evm.xtext.resource.IXtextResourceProcessor;
import com.incquerylabs.evm.xtext.resource.XtextIndexedResource;
import com.incquerylabs.evm.xtext.resource.XtextResourceJob;
import com.incquerylabs.evm.xtext.resource.events.XtextIndexResourceEventSourceSpecification;

public class XtextIndexRules {

	public static class XtextResourceRuleBuilder {
		
		private final XtextIndexResourceEventSourceSpecification sourceSpecification;
		private final Set<Job<XtextIndexedResource>> jobs = new HashSet<>(); 
		
		public XtextResourceRuleBuilder(XtextIndexResourceEventSourceSpecification sourceSpecification) {
			this.sourceSpecification = sourceSpecification;
		}
		
		public XtextResourceRuleBuilder action(ActivationState activationState, IXtextResourceProcessor resourceProcessor) {
			jobs.add(new XtextResourceJob(activationState, resourceProcessor));
			return this;
		}
		
		public RuleSpecification<XtextIndexedResource> build() {
			return new RuleSpecification<XtextIndexedResource>(sourceSpecification, XtextActivationLifeCycle.INSTANCE, jobs);
		}
	}
	
	public static class XtextObjectRuleBuilder {
		
		private final XtextIndexObjectEventSourceSpecification sourceSpecification;
		private final Set<Job<XtextIndexedObject>> jobs = new HashSet<>(); 
		
		public XtextObjectRuleBuilder(XtextIndexObjectEventSourceSpecification sourceSpecification) {
			this.sourceSpecification = sourceSpecification;
		}
		
		public XtextObjectRuleBuilder action(ActivationState activationState, IXtextObjectProcessor resourceProcessor) {
			jobs.add(new XtextObjectJob(activationState, resourceProcessor));
			return this;
		}
		
		public RuleSpecification<XtextIndexedObject> build() {
			return new RuleSpecification<XtextIndexedObject>(sourceSpecification, XtextActivationLifeCycle.INSTANCE, jobs);
		}
	}
	
	public XtextResourceRuleBuilder resourceRule() {
		return new XtextResourceRuleBuilder(new XtextIndexResourceEventSourceSpecification());
	}
	
	public XtextObjectRuleBuilder objectRule() {
		return new XtextObjectRuleBuilder(new XtextIndexObjectEventSourceSpecification());
	}
}
