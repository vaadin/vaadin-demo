package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Notifications;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.clipboard.Clipboard;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.Code;
import com.vaadin.flow.component.html.Span;

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

    private static final String COPY_TEXT = "Copy clone command";
    private static final String COPIED_TEXT = "Clone command copied";

    public CloneBanner(String cloneUrl) {
        addClassName("clone-banner");
        String command = "git clone " + cloneUrl;

        Span label = new Span("Clone this project");
        label.setId("clone-banner-label");
        label.addClassName("label");
        getElement().setAttribute("aria-labelledby", label.getId().orElseThrow());

        Code code = new Code(command);
        code.addClassName("command");

        // Announces the copy to screen readers, as the icon change is visual only
        Span status = new Span();
        status.addClassName("status");
        status.getElement().setAttribute("role", "status");

        Button copy = new Button(createCopyIcons());
        copy.addClassName("copy");
        copy.addThemeVariants(ButtonVariant.SMALL, ButtonVariant.TERTIARY);
        copy.setAriaLabel(COPY_TEXT);
        copy.setTooltipText(COPY_TEXT);

        Clipboard.onClick(copy).writeText(command,
                copied -> showCopied(copy, status),
                err -> Notifications.show("Could not copy. Select the command to copy it manually."));

        add(label, code, copy, status);
    }

    /**
     * The copy icon, with a check mark stacked on it that replaces it while
     * the button shows the copied state (see misc/clone-banner.css).
     */
    private static Span createCopyIcons() {
        Span check = new Span(Lucide.CHECK.create());
        check.addClassName("check-icon");

        Span copy = new Span(Lucide.COPY.create());
        copy.addClassName("copy-icon");

        Span icons = new Span(check, copy);
        icons.addClassName("icons");
        return icons;
    }

    /**
     * Swaps the copy icon for a check mark and announces the copy, both for
     * two seconds. Runs in the browser, so repeated clicks restart the timer.
     */
    private static void showCopied(Button copy, Span status) {
        copy.getElement().executeJs("""
                this.setAttribute('data-copied', '');
                $0.textContent = $1;
                clearTimeout(this._copiedTimeout);
                this._copiedTimeout = setTimeout(() => {
                    this.removeAttribute('data-copied');
                    $0.textContent = '';
                }, 2000);
                """, status.getElement(), COPIED_TEXT);
    }
}
