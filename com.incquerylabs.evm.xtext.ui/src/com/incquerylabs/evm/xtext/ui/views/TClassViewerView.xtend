package com.incquerylabs.evm.xtext.ui.views

import com.google.common.collect.ImmutableSet
import com.incquerylabs.evm.queries.ExampleModelQueries
import com.incquerylabs.evm.xtext.XtextIndexActivationState
import com.incquerylabs.evm.xtext.XtextIndexRules
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject
import com.incquerylabs.evm.xtext.example.exampleLanguage.ExampleLanguagePackage
import com.incquerylabs.evm.xtext.resource.XtextIndexedResource
import com.incquerylabs.evm.xtext.ui.XtextIndexVirtualMachine
import javax.inject.Inject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.gef4.layout.algorithms.SpringLayoutAlgorithm
import org.eclipse.gef4.zest.core.viewers.GraphViewer
import org.eclipse.gef4.zest.core.widgets.ZestStyles
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
import org.eclipse.xtext.resource.IResourceDescription
import org.eclipse.xtext.resource.IResourceDescriptions
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.ui.notification.IStateChangeEventBroker
import org.eclipse.xtext.ui.resource.IStorage2UriMapper

class TClassViewerView extends ViewPart {
    package ResourceSet set = new XtextResourceSet
    ViewerState state
    AdvancedIncQueryEngine engine
    extension XtextIndexRules rules = new XtextIndexRules
    val IStorage2UriMapper mapper
    var GraphViewer viewer

    val rRule = {
        resourceRule.action(XtextIndexActivationState.APPEARED) [
            println(it.URI + " appeared")
            it.ensureResourceLoaded
            viewer.applyLayout
        ].action(XtextIndexActivationState.UPDATED) [
            println(it.URI + " updated")
            it.unloadResource
            it.ensureResourceLoaded
            viewer.applyLayout
        ].action(XtextIndexActivationState.DISAPPEARED) [
            println(it.URI + " disappeared")
            it.unloadResource
            viewer.applyLayout
        ].build

    }

    val EventFilter<XtextIndexedResource> rFilter = rRule.createEmptyFilter
    val oRule = {
        objectRule.action(XtextIndexActivationState.APPEARED) [ resource, obj |
            println('''«obj.name» appeared in «resource.URI»''')
            resource.ensureResourceLoaded
        ].build

    }
    val EventFilter<XtextIndexedObject> oFilter = [
        it.type != null && ExampleLanguagePackage.Literals.TCLASS.isSuperTypeOf(it.type)
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
        if (!resource.loaded) {
            resource.load(mapper.getStorages(desc.URI).head.first.contents, null)
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
        viewer = new GraphViewer(parent, SWT.H_SCROLL.bitwiseOr(SWT.V_SCROLL))
        viewer.connectionStyle = ZestStyles.CONNECTIONS_DIRECTED
        viewer.layoutAlgorithm = new SpringLayoutAlgorithm
        try {
            val options = new BaseIndexOptions(false, false)
            engine = AdvancedIncQueryEngine.from(IncQueryEngine.on(new EMFScope(set, options)))
            initializeRules
            state = IncQueryViewerDataModel.newViewerState(engine, ExampleModelQueries.instance().getSpecifications(),
                ViewerDataFilter.UNFILTERED, ImmutableSet.of(ViewerStateFeature.EDGE))  
            IncQueryGraphViewers.bind(viewer, state)
        } catch (IncQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    def protected void initializeRules() {
        XtextIndexVirtualMachine.INSTANCE.registerRule(rRule, rFilter, XtextIndexVirtualMachine.PRIORITY_RULE)
        XtextIndexVirtualMachine.INSTANCE.registerRule(oRule, oFilter, XtextIndexVirtualMachine.PRIORITY_OBJECT)
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
