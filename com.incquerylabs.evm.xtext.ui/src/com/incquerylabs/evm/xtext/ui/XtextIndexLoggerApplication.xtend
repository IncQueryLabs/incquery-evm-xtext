package com.incquerylabs.evm.xtext.ui

import com.incquerylabs.evm.xtext.XtextActivationLifeCycle
import com.incquerylabs.evm.xtext.XtextEventRealm
import com.incquerylabs.evm.xtext.XtextIndexActivationState
import com.incquerylabs.evm.xtext.resource.XtextIndexedResource
import com.incquerylabs.evm.xtext.resource.XtextResourceJob
import com.incquerylabs.evm.xtext.resource.events.XtextIndexResourceEventSourceSpecification
import org.eclipse.incquery.runtime.evm.api.EventDrivenVM
import org.eclipse.incquery.runtime.evm.api.Job
import org.eclipse.incquery.runtime.evm.api.RuleSpecification
import org.eclipse.xtext.builder.builderState.IBuilderState
import org.eclipse.incquery.runtime.evm.api.RuleEngine
import org.eclipse.incquery.runtime.evm.api.Context
import org.eclipse.incquery.runtime.evm.api.Activation

class XtextIndexLoggerApplication {
    
    public static val INSTANCE = new XtextIndexLoggerApplication
    
    var RuleEngine engine
    var RuleSpecification<XtextIndexedResource> ruleSpecification
    
    def initialize(IBuilderState state) {
        val realm = new XtextEventRealm(state)
        engine = EventDrivenVM.createRuleEngine(realm)
        
        val lifecycle = new XtextActivationLifeCycle 
        val sourceSpecification = new XtextIndexResourceEventSourceSpecification
        
        val jobs = <Job<XtextIndexedResource>>newHashSet(
            new XtextResourceJob(XtextIndexActivationState.APPEARED) [
                println("Appeared: " + it.URI)
            ],
            new XtextResourceJob(XtextIndexActivationState.UPDATED) [
                println("Updated: " + it.URI)
            ],
            new XtextResourceJob(XtextIndexActivationState.DISAPPEARED) [
                println("Disappeared: " + it.URI)
            ]
        )
        
        ruleSpecification = new RuleSpecification<XtextIndexedResource>(sourceSpecification, lifecycle, jobs)
        engine.addRule(ruleSpecification, ruleSpecification.createEmptyFilter)
        
        //Initial firing
        fire
    }
    
    def fire() {
        var Activation<?> act 
        while ((act = engine.nextActivation) != null) {
            act.fire(Context.create())
        }  
    }
}