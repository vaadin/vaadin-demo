package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Theme;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

/**
 * Button for a toolbar, such as a view header or the dashboard's selection
 * bar. On phones only its icon shows (see components/button.css), so the text
 * is also set as its ARIA label and tooltip to keep it named.
 */
public class ToolbarButton extends Button {

    public ToolbarButton(String text, Component icon) {
        super(text, icon);
        addThemeNames(Theme.BUTTON_ICON_ONLY_ON_PHONE);
        setAriaLabel(text);
        setTooltipText(text);
    }
}
