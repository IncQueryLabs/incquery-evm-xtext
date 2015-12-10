/*******************************************************************************
 * Copyright (c) 2010-2013, Abel Hegedus, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Abel Hegedus - initial API and implementation
 *******************************************************************************/
package com.incquerylabs.evm.xtext;

import org.eclipse.incquery.runtime.evm.api.event.EventType;
import org.eclipse.incquery.runtime.evm.specific.lifecycle.UnmodifiableActivationLifeCycle;

/**
 * This is the default implementation for an activation life cycle.
 * 
 * The following is the summary of the possible transitions, in the form of
 * StateFrom -Event-> StateTo:
 * <ul>
 * <li>Inactive -Match Appears-> Appeared</li>
 * <li>Appeared -Match Disappears-> Inactive</li>
 * <li>Appeared -Activation fires-> Fired</li>
 * <li>Fired -Match Updates-> Updated</li>
 * <li>Updated -Activation fires-> Fired</li>
 * <li>Updated -Match Disappears-> Inactive / Disappeared</li>
 * <li>Fired -Match Disappears-> Inactive / Disappeared</li>
 * <li>Disappeared -Match Appears-> Fired / Updated</li>
 * <li>Disappeared -Activation fires-> Inactive</li>
 * </ul>
 * 
 * @author Abel Hegedus
 * 
 */
public final class XtextActivationLifeCycle extends UnmodifiableActivationLifeCycle {

	public static final XtextActivationLifeCycle INSTANCE = new XtextActivationLifeCycle();
	
	public XtextActivationLifeCycle() {

		super(XtextIndexActivationState.INACTIVE);

		internalAddStateTransition(XtextIndexActivationState.INACTIVE, XtextIndexEventType.CREATED,
				XtextIndexActivationState.APPEARED);

		internalAddStateTransition(XtextIndexActivationState.APPEARED, XtextIndexEventType.REMOVED,
				XtextIndexActivationState.INACTIVE);
		internalAddStateTransition(XtextIndexActivationState.APPEARED, EventType.RuleEngineEventType.FIRE,
				XtextIndexActivationState.FIRED);

		internalAddStateTransition(XtextIndexActivationState.FIRED, XtextIndexEventType.UPDATED,
				XtextIndexActivationState.UPDATED);
		internalAddStateTransition(XtextIndexActivationState.UPDATED, EventType.RuleEngineEventType.FIRE,
				XtextIndexActivationState.FIRED);
		internalAddStateTransition(XtextIndexActivationState.UPDATED, XtextIndexEventType.REMOVED,
				XtextIndexActivationState.DISAPPEARED);

		internalAddStateTransition(XtextIndexActivationState.FIRED, XtextIndexEventType.REMOVED,
				XtextIndexActivationState.DISAPPEARED);
		internalAddStateTransition(XtextIndexActivationState.DISAPPEARED, XtextIndexEventType.CREATED,
				XtextIndexActivationState.UPDATED);
		internalAddStateTransition(XtextIndexActivationState.DISAPPEARED, EventType.RuleEngineEventType.FIRE,
				XtextIndexActivationState.INACTIVE);

	}

}
