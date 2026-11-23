package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Notifications;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.clipboard.Clipboard;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.Code;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Banner pinned above the whole app that shows the git clone command, with a
 * button that copies it to the clipboard. Added only to production (hosted)
 * runs, see {@link com.vaadin.demo.ui.CloneBannerInitListener}.
 * <p>
 * A labelled landmark, so screen reader users can find it. The command is
 * plain text, so it can still be selected and copied by hand when the
 * clipboard isn't available.
 */
public class CloneBanner extends Aside {

    private static final String COPY_TEXT = "Copy Clone Command";

    public CloneBanner(String cloneUrl) {
        addClassName("clone-banner");
        String command = "git clone " + cloneUrl;

        Span label = new Span("Clone this project");
        label.setId("clone-banner-label");
        label.addClassName("label");
        getElement().setAttribute("aria-labelledby", label.getId().orElseThrow());

        Code code = new Code(command);
        code.addClassName("command");

        Button copy = new Button(Lucide.COPY.create());
        copy.addThemeVariants(ButtonVariant.SMALL, ButtonVariant.TERTIARY);
        copy.setAriaLabel(COPY_TEXT);
        copy.setTooltipText(COPY_TEXT);

        Clipboard.onClick(copy).writeText(command,
                copied -> Notifications.show("Clone command copied", NotificationVariant.SUCCESS),
                err -> Notifications.show("Could not copy. Select the command to copy it manually."));

        add(label, code, copy);
    }
}
