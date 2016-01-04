package com.incquerylabs.evm.xtext.ui

import com.google.common.collect.Sets
import com.incquerylabs.evm.xtext.XtextEventRealm
import org.eclipse.incquery.runtime.evm.api.Activation
import org.eclipse.incquery.runtime.evm.api.Context
import org.eclipse.incquery.runtime.evm.api.EventDrivenVM
import org.eclipse.incquery.runtime.evm.api.ExecutionSchema
import org.eclipse.incquery.runtime.evm.api.RuleSpecification
import org.eclipse.incquery.runtime.evm.api.event.EventFilter
import org.eclipse.incquery.runtime.evm.specific.resolver.FixedPriorityConflictResolver
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtext.resource.IResourceDescription.Event
import org.eclipse.xtext.resource.IResourceDescriptions

class XtextIndexVirtualMachine {
    
    public static val INSTANCE = new XtextIndexVirtualMachine
    public static int PRIORITY_UNDEFINED = 10;
    public static int PRIORITY_RULE = 2;
    public static int PRIORITY_OBJECT = 1;
    
    @Accessors(PUBLIC_GETTER)
    var ExecutionSchema engine
    val FixedPriorityConflictResolver resolver = new FixedPriorityConflictResolver
    
    def <EventAtom>registerRule(RuleSpecification<EventAtom> ruleSpecification, EventFilter<EventAtom> filter) {
        registerRule(ruleSpecification, filter, PRIORITY_UNDEFINED)
    }
    
    def <EventAtom>registerRule(RuleSpecification<EventAtom> ruleSpecification, EventFilter<EventAtom> filter, int priority) {
        engine.addRule(ruleSpecification, filter)
        resolver.setPriority(ruleSpecification, priority)
    }
    
    def <EventAtom>unregisterRule(RuleSpecification<EventAtom> ruleSpecification, EventFilter<EventAtom> filter) {
        engine.removeRule(ruleSpecification, filter)
    }
    
    def initialize(Event.Source source, IResourceDescriptions descriptions) {
        if (engine == null) {
            val realm = new XtextEventRealm(source, descriptions)
            engine = EventDrivenVM.createExecutionSchema(realm, WorkspaceBuildCompletedScheduler.FACTORY, Sets.newHashSet)
            engine.conflictResolver = resolver
        }
    }
    
    def fire() {
        var Activation<?> act 
        while ((act = engine.nextActivation) != null) {
            act.fire(Context.create())
        }  
    }
}