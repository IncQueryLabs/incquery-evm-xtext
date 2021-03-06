package com.incquerylabs.evm.xtext.ui.views

import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject
import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.xtext.resource.IEObjectDescription

class XtextIndexedObjectLabelProvider extends ColumnLabelProvider {
    
    override String getText(Object element) {
        if (element instanceof XtextIndexedObject) {
            var XtextIndexedObject indexedObject = (element as XtextIndexedObject)
            var IEObjectDescription description = indexedObject.getEObject()
            return '''«description.getEClass().getName()» : «description.name.lastSegment» («indexedObject.getResource().getURI().path»)''' 
        } else if (element instanceof IEObjectDescription) {
            val IEObjectDescription description = element
            return '''«description.getEClass().getName()» : «description.name.lastSegment» («description.EObjectURI.path»)'''
        }
        return super.getText(element)
    }

    override String getToolTipText(Object element) {
        if (element instanceof XtextIndexedObject) {
            var XtextIndexedObject indexedObject = (element as XtextIndexedObject)
            var IEObjectDescription description = indexedObject.getEObject()
            for (String key : description.getUserDataKeys()) {
            }
            return '''
                «description.qualifiedName.lastSegment» : «description.EClass.name»
                -------------------------------------------------
                Qualified name: «description.qualifiedName»
                Resource: «indexedObject.resource.URI»
                User Data:
                «FOR key : description.userDataKeys SEPARATOR "\\n"» «key» = «description.getUserData(key)» «ENDFOR»
            '''
        }
        return super.getToolTipText(element)
    }
}
