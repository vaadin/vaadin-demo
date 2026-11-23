package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Theme;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

import java.util.Locale;

/**
 * Button for a toolbar, such as a view header or the dashboard's selection
 * bar. On phones only its icon shows (see components/button.css), so the text
 * is also set as its ARIA label and, in sentence case, as its tooltip.
 * <p>
 * The text is a Title Case label of plain words, such as "Add Product". It
 * mustn't contain names or acronyms, as the tooltip lowercases all but the
 * first letter.
 */
public class ToolbarButton extends Button {

    public ToolbarButton(String text, Component icon) {
        super(text, icon);
        addThemeNames(Theme.BUTTON_ICON_ONLY_ON_PHONE);
        setAriaLabel(text);
        setTooltipText(text.charAt(0) + text.substring(1).toLowerCase(Locale.ROOT));
    }
}
