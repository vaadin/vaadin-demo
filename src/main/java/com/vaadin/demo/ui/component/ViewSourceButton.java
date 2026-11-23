package com.vaadin.demo.ui.component;

import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.flow.component.button.ButtonVariant;

/**
 * "View Source" button for a view header, opening the view's source code in
 * a dialog.
 */
public class ViewSourceButton extends ToolbarButton {

    public ViewSourceButton(Class<?> viewClass, SourceService sourceService) {
        super("View Source", Lucide.CODE.create());
        addThemeVariants(ButtonVariant.TERTIARY);
        addClickListener(e -> new SourceViewerDialog(viewClass, sourceService).open());
    }
}
