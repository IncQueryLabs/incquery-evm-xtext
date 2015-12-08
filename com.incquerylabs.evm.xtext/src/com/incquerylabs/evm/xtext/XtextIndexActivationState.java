package com.incquerylabs.evm.xtext;

import org.eclipse.incquery.runtime.evm.api.event.ActivationState;

public enum XtextIndexActivationState implements ActivationState {
    
    INACTIVE, APPEARED, FIRED, UPDATED, DISAPPEARED;
    
    @Override
    public boolean isInactive() {
        return (this == INACTIVE);
    }
    
}
