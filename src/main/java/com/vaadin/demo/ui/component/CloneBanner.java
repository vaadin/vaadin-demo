package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Notifications;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.clipboard.Clipboard;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Banner pinned above the whole app that shows the git clone command and
 * copies it to the clipboard on click. Added only to production (hosted)
 * runs, see {@link com.vaadin.demo.ui.CloneBannerInitListener}, and hidden
 * on phones by misc/clone-banner.css.
 */
public class CloneBanner extends Div {

    public CloneBanner(String cloneUrl) {
        addClassName("clone-banner");
        String command = "git clone " + cloneUrl;

        Span label = new Span("Clone this project");
        label.addClassName("label");

        Button copy = new Button(command, Lucide.COPY.create());
        copy.setIconAfterText(true);
        copy.addThemeVariants(ButtonVariant.SMALL, ButtonVariant.TERTIARY);
        copy.addClassName("command");
        copy.setTooltipText("Copy to Clipboard");

        Clipboard.onClick(copy).writeText(command,
                copied -> Notifications.show("Clone command copied", NotificationVariant.SUCCESS),
                err -> Notifications.show("Could not copy, select the command manually"));

        add(label, copy);
    }
}
