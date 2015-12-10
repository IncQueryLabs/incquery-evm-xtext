package com.incquerylabs.evm.xtext.ui

import com.incquerylabs.evm.xtext.XtextEventRealm
import com.incquerylabs.evm.xtext.XtextIndexActivationState
import com.incquerylabs.evm.xtext.XtextIndexRules
import org.eclipse.incquery.runtime.evm.api.Activation
import org.eclipse.incquery.runtime.evm.api.Context
import org.eclipse.incquery.runtime.evm.api.EventDrivenVM
import org.eclipse.incquery.runtime.evm.api.RuleEngine
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtext.resource.IResourceDescription.Event
import org.eclipse.xtext.resource.IResourceDescriptions

class XtextIndexLoggerApplication {
    
    public static val INSTANCE = new XtextIndexLoggerApplication
    
    @Accessors(PUBLIC_GETTER)
    var RuleEngine engine
    extension XtextIndexRules rules = new XtextIndexRules
    
    def resource() {
        resourceRule.action(XtextIndexActivationState.APPEARED) [
                println("Resource appeared: " + it.URI)
            ].action(XtextIndexActivationState.UPDATED) [
                println("Resource updated: " + it.URI)
            ].action(XtextIndexActivationState.DISAPPEARED) [
                println("Resource disappeared: " + it.URI)
            ].build
    }
    
    def object() {
        objectRule.action(XtextIndexActivationState.APPEARED) [resource, object |
                println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
            ].action(XtextIndexActivationState.UPDATED) [resource, object |
                println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
            ].action(XtextIndexActivationState.DISAPPEARED) [resource, object |
                println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
            ].build
    }
    
    def initialize(Event.Source source, IResourceDescriptions descriptions) {
        val realm = new XtextEventRealm(source, descriptions)
        engine = EventDrivenVM.createRuleEngine(realm)
        
        val resourceRuleSpecification = resource
        engine.addRule(resourceRuleSpecification, resourceRuleSpecification.createEmptyFilter)
        
        val objectRuleSpecification = object
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