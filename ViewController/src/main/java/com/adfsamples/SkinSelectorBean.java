package com.adfsamples;

import java.io.IOError;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "skinSelectorBean")
@SessionScoped
public class SkinSelectorBean implements Serializable {
    
    private static final String DEFAULT_SKIN_FAMILY = "alta";

    private String skinFamily = DEFAULT_SKIN_FAMILY;

    public SkinSelectorBean() {
    }

    public String getSkinFamily() {
        return skinFamily;
    }

    public void setSkinFamily(String skinFamily) {
        this.skinFamily = skinFamily;
    }

    public void skinFamilyValueChanged(ValueChangeEvent valueChangeEvent) 
    throws IOException {
        // ValueChangeEvent is handled in the PROCESS_VALIDATIONS phase. 
        // Calling refreshPage() will skip the UPDATE_MODEL_VALUES phase 
        // which we need to update skinFamily. We have to queue this event 
        // to the INVOKE_APPLICATION phase.
        if (valueChangeEvent.getPhaseId() != PhaseId.INVOKE_APPLICATION) {
            valueChangeEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
            valueChangeEvent.queue();
            return;
        }

        // refresh the whole view
        refreshView();
    }

    private void refreshView() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String viewId = facesContext.getViewRoot().getViewId();
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        UIViewRoot root = viewHandler.createView(facesContext, viewId);
        root.setViewId(viewId);
        facesContext.setViewRoot(root);
    }
}
