package com.incquerylabs.evm.xtext.ui.views

import com.google.common.collect.ImmutableSet
import com.incquerylabs.evm.queries.InferredModelQueries
import com.incquerylabs.evm.xtext.XtextIndexActivationState
import com.incquerylabs.evm.xtext.XtextIndexRules
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject
import com.incquerylabs.evm.xtext.resource.XtextIndexedResource
import com.incquerylabs.evm.xtext.ui.XtextIndexVirtualMachine
import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.gef4.zest.core.viewers.GraphViewer
import org.eclipse.incquery.runtime.api.AdvancedIncQueryEngine
import org.eclipse.incquery.runtime.api.IncQueryEngine
import org.eclipse.incquery.runtime.base.api.BaseIndexOptions
import org.eclipse.incquery.runtime.emf.EMFScope
import org.eclipse.incquery.runtime.evm.api.event.EventFilter
import org.eclipse.incquery.runtime.exception.IncQueryException
import org.eclipse.incquery.viewers.runtime.model.IncQueryViewerDataModel
import org.eclipse.incquery.viewers.runtime.model.ViewerDataFilter
import org.eclipse.incquery.viewers.runtime.model.ViewerState
import org.eclipse.incquery.viewers.runtime.model.ViewerState.ViewerStateFeature
import org.eclipse.incquery.viewers.runtime.zest.IncQueryGraphViewers
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.part.ViewPart
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.resource.DerivedStateAwareResource
import org.eclipse.xtext.resource.IResourceDescription
import org.eclipse.xtext.resource.IResourceDescriptions
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.ui.notification.IStateChangeEventBroker
import org.eclipse.xtext.ui.resource.IStorage2UriMapper

class InferredClassViewerView extends ViewPart {
    package ResourceSet set = new XtextResourceSet
    ViewerState state
    AdvancedIncQueryEngine engine
    extension XtextIndexRules rules = new XtextIndexRules
    val IStorage2UriMapper mapper

    val rRule = {
        resourceRule.action(XtextIndexActivationState.APPEARED) [
            it.ensureResourceLoaded
        ].action(XtextIndexActivationState.UPDATED) [
            it.reloadResource
        ].action(XtextIndexActivationState.DISAPPEARED) [
            it.unloadResource
        ].build

    }

    val EventFilter<XtextIndexedResource> rFilter = [
        val uri = it.resource.URI
        uri.isPlatformResource
    ] // rRule.createEmptyFilter
    val oRule = {
        objectRule.action(XtextIndexActivationState.APPEARED) [ resource, obj |
            resource.ensureResourceLoaded
        ].action(XtextIndexActivationState.UPDATED) [ resource, obj |
            resource.ensureResourceLoaded
        ].build

    }
    val EventFilter<XtextIndexedObject> oFilter = [
        it.type != null && TypesPackage.Literals.JVM_TYPE.isSuperTypeOf(it.type)
    ]

    private def ensureResourceLoaded(IResourceDescription desc) {
        var Resource resource
        try {
             resource = set.getResource(desc.URI, false)
            
        } catch (Exception e) {
            e.printStackTrace
        }
        if (resource == null) {
            resource = set.createResource(desc.URI)
            
        }
        if (resource != null && !resource.loaded) {
            resource.load(mapper.getStorages(desc.URI).head.first.contents, null)
            if (resource instanceof DerivedStateAwareResource) {
                resource.installDerivedState(false)
            }
        }
    }

    private def reloadResource(IResourceDescription desc) {
        val res = set.getResource(desc.URI, false)
        if (res != null && res.isLoaded) {
            res.unload
            res.load(null)
        } else {
            ensureResourceLoaded(desc)
        }
    }

    private def unloadResource(IResourceDescription desc) {
        val res = set.getResource(desc.URI, false)
        if (res != null && res.loaded) {
            res.unload
        }
    }

    @Inject
    new(IStateChangeEventBroker source, IResourceDescriptions descriptions, IStorage2UriMapper mapper) {
        XtextIndexVirtualMachine.INSTANCE.initialize(source, descriptions)
        this.mapper = mapper
    }

    override void createPartControl(Composite parent) {
        var GraphViewer viewer = new GraphViewer(parent, SWT.H_SCROLL.bitwiseOr(SWT.V_SCROLL))
        try {
            initializeRules
            val options = new BaseIndexOptions(false, false).withResourceFilterConfiguration [
                !it.URI.toString.startsWith("java")
            ]
            engine = AdvancedIncQueryEngine.from(IncQueryEngine.on(new EMFScope(set, options)))
        } catch (IncQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        state = IncQueryViewerDataModel.newViewerState(engine, InferredModelQueries.instance().getSpecifications(),
            ViewerDataFilter.UNFILTERED, ImmutableSet.of(ViewerStateFeature.EDGE))
        IncQueryGraphViewers.bindWithIsolatedNodes(viewer, state)
    }

    def protected void initializeRules() {
        XtextIndexVirtualMachine.INSTANCE.registerRule(rRule, rFilter)
        XtextIndexVirtualMachine.INSTANCE.registerRule(oRule, oFilter)
        XtextIndexVirtualMachine.INSTANCE.fire
    }

    override void setFocus() { // TODO Auto-generated method stub
    }

    override void dispose() {
        if (state !== null) {
            state.dispose()
        }
        XtextIndexVirtualMachine.INSTANCE.unregisterRule(rRule, rFilter)
        XtextIndexVirtualMachine.INSTANCE.unregisterRule(oRule, oFilter)
        super.dispose()
    }
}
