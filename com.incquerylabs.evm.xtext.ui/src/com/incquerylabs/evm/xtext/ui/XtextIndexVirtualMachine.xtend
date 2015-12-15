package com.incquerylabs.evm.xtext.ui

import com.incquerylabs.evm.xtext.XtextEventRealm
import org.eclipse.incquery.runtime.evm.api.Activation
import org.eclipse.incquery.runtime.evm.api.Context
import org.eclipse.incquery.runtime.evm.api.EventDrivenVM
import org.eclipse.incquery.runtime.evm.api.RuleEngine
import org.eclipse.incquery.runtime.evm.api.RuleSpecification
import org.eclipse.incquery.runtime.evm.api.event.EventFilter
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtext.resource.IResourceDescription.Event
import org.eclipse.xtext.resource.IResourceDescriptions

class XtextIndexVirtualMachine {
    
    public static val INSTANCE = new XtextIndexVirtualMachine
    
    @Accessors(PUBLIC_GETTER)
    var RuleEngine engine
    
    def <EventAtom>registerRule(RuleSpecification<EventAtom> ruleSpecification, EventFilter<EventAtom> filter) {
        engine.addRule(ruleSpecification, filter)
    }
    
    def <EventAtom>unregisterRule(RuleSpecification<EventAtom> ruleSpecification, EventFilter<EventAtom> filter) {
        engine.removeRule(ruleSpecification, filter)
    }
    
    def initialize(Event.Source source, IResourceDescriptions descriptions) {
        if (engine == null) {
            val realm = new XtextEventRealm(source, descriptions)
            engine = EventDrivenVM.createRuleEngine(realm)
        }
    }
    
    def fire() {
        var Activation<?> act 
        while ((act = engine.nextActivation) != null) {
            act.fire(Context.create())
        }  
    }
}