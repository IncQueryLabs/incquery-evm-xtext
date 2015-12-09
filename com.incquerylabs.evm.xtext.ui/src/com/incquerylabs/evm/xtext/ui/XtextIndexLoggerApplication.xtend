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
import org.eclipse.incquery.runtime.evm.api.ActivationLifeCycle
import com.incquerylabs.evm.xtext.eobject.event.XtextIndexedObjectEventSourceSpecification
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject
import com.incquerylabs.evm.xtext.eobject.XtextObjectJob

class XtextIndexLoggerApplication {
    
    public static val INSTANCE = new XtextIndexLoggerApplication
    
    var RuleEngine engine
    
    def resourceRule(ActivationLifeCycle lifecycle) {
        val sourceSpecification = new XtextIndexResourceEventSourceSpecification
        
        val jobs = <Job<XtextIndexedResource>>newHashSet(
            new XtextResourceJob(XtextIndexActivationState.APPEARED) [
                println("Resource appeared: " + it.URI)
            ],
            new XtextResourceJob(XtextIndexActivationState.UPDATED) [
                println("Resource updated: " + it.URI)
            ],
            new XtextResourceJob(XtextIndexActivationState.DISAPPEARED) [
                println("Resource disappeared: " + it.URI)
            ]
        )
        new RuleSpecification<XtextIndexedResource>(sourceSpecification, lifecycle, jobs)
    }
    
    def objectRule(ActivationLifeCycle lifecycle) {
        val sourceSpecification = new XtextIndexedObjectEventSourceSpecification
        
        val jobs = <Job<XtextIndexedObject>>newHashSet(
            new XtextObjectJob(XtextIndexActivationState.APPEARED) [resource, object |
                println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
            ],
            new XtextObjectJob(XtextIndexActivationState.UPDATED) [resource, object |
                println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
            ],
            new XtextObjectJob(XtextIndexActivationState.DISAPPEARED) [resource, object |
                println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
            ]
        )
        new RuleSpecification<XtextIndexedObject>(sourceSpecification, lifecycle, jobs)
    }
    
    def initialize(IBuilderState state) {
        val realm = new XtextEventRealm(state)
        engine = EventDrivenVM.createRuleEngine(realm)
        
        val lifecycle = new XtextActivationLifeCycle 
        
        val resourceRuleSpecification = resourceRule(lifecycle)
        engine.addRule(resourceRuleSpecification, resourceRuleSpecification.createEmptyFilter)
        
        val objectRuleSpecification = objectRule(lifecycle)
        engine.addRule(objectRuleSpecification, objectRuleSpecification.createEmptyFilter)
        
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