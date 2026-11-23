package com.vaadin.demo.ui.component;

import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

/**
 * "View Source" button for a view header, opening the view's source code in
 * a dialog. On phones only its icon shows (see styles.css), leaving room for
 * the view's own actions; the ARIA label keeps it named for screen readers.
 */
public class ViewSourceButton extends Button {

    private static final String TEXT = "View Source";

    public ViewSourceButton(Class<?> viewClass, SourceService sourceService) {
        super(TEXT, Lucide.CODE.create(), e -> new SourceViewerDialog(viewClass, sourceService).open());
        addThemeVariants(ButtonVariant.TERTIARY);
        addClassName("view-source-button");
        setAriaLabel(TEXT);
    }
}
