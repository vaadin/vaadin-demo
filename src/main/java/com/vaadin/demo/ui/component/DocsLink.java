package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.flow.component.html.Anchor;

/**
 * Short "Docs ↗" link to a component's documentation on vaadin.com, opened
 * in a new tab. Meant for a card header, next to the component name.
 */
public class DocsLink extends Anchor {

    private static final String TEXT = "Docs";

    public DocsLink(String componentName, String docsUrl) {
        super(docsUrl, TEXT);
        add(Lucide.SQUARE_ARROW_OUT_UP_RIGHT.create());
        setTarget("_blank");
        // Starts with the visible text, so voice control users can say it
        setAriaLabel(TEXT + " for " + componentName);
        addClassName("docs-link");
    }
}
