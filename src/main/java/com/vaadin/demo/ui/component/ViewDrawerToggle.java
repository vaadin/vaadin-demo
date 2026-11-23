package com.vaadin.demo.ui.component;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;

/**
 * Drawer toggle for a view or drawer header, with a tooltip matching its
 * ARIA label.
 */
public class ViewDrawerToggle extends DrawerToggle {

    private static final String TEXT = "Toggle navigation panel";

    public ViewDrawerToggle() {
        addThemeVariants(ButtonVariant.TERTIARY);
        setAriaLabel(TEXT);
        setTooltipText(TEXT);
    }
}
