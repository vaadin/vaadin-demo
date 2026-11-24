package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Aura;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Notifications;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.clipboard.Clipboard;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.Code;
import com.vaadin.flow.component.html.Span;

/**
 * Banner floating above the whole app that shows the git clone command, with a
 * button that copies it to the clipboard. Added on production (hosted) runs,
 * or in development mode when enabled, see
 * {@link com.vaadin.demo.ui.CloneBannerInitListener}.
 * <p>
 * It covers the content below it, so it can be closed to a small chevron that
 * opens it again; the browser remembers which. Escape also closes it while it
 * has focus.
 * <p>
 * A labelled landmark, so screen reader users can find it. The command is
 * plain text, so it can still be selected and copied by hand when the
 * clipboard isn't available.
 */
public class CloneBanner extends Aside {

    private static final String COPY_TEXT = "Copy clone command";
    private static final String COPIED_TEXT = "Clone command copied";
    private static final String CLOSE_TEXT = "Hide clone banner";
    private static final String OPEN_TEXT = "Show clone banner";

    public CloneBanner(String cloneUrl) {
        addClassNames("clone-banner", Aura.SURFACE);
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

        Button close = createToggle(Lucide.X, CLOSE_TEXT, "close", true);
        Button open = createToggle(Lucide.CHEVRON_DOWN, OPEN_TEXT, "open", false);

        Span divider = new Span();
        divider.addClassName("divider");
        divider.getElement().setAttribute("aria-hidden", "true");

        add(createPart("details", label, code, copy, divider, close),
                createPart("opener", open),
                status);
        addAttachListener(e -> initCollapsing(close, open, copy));
    }

    /**
     * One of the banner's two states: the open banner's details, or the
     * chevron shown when it's closed. Opening and closing animate each one's
     * width between its content and nothing (see misc/clone-banner.css).
     */
    private static Span createPart(String className, Component... content) {
        Span part = new Span(content);
        part.addClassName(className);
        return part;
    }

    /**
     * Button that closes or opens the banner. aria-expanded tells screen
     * readers which; only one of the two is shown at a time.
     */
    private static Button createToggle(Lucide icon, String text, String className, boolean expanded) {
        Button button = new Button(icon.create());
        button.addClassName(className);
        button.addThemeVariants(ButtonVariant.SMALL, ButtonVariant.TERTIARY);
        button.setAriaLabel(text);
        button.setTooltipText(text);
        button.getElement().setAttribute("aria-expanded", String.valueOf(expanded));
        return button;
    }

    /**
     * Closing and opening run in the browser, which remembers the choice in
     * localStorage; misc/clone-banner.css animates the change. Focus moves to
     * the button that takes the place of the one pressed, so keyboard users
     * don't lose their place.
     */
    private void initCollapsing(Button close, Button open, Button copy) {
        getElement().executeJs("""
                const key = 'clone-banner-collapsed';
                const setCollapsed = collapsed => {
                    this.toggleAttribute('collapsed', collapsed);
                    try {
                        collapsed ? localStorage.setItem(key, 'true') : localStorage.removeItem(key);
                    } catch (e) {
                        // Storage unavailable: the choice lasts until reload
                    }
                    const target = collapsed ? $1 : $2;
                    target.focus();
                    // Vaadin judges keyboard use itself and would show a focus
                    // ring after a mouse click too; follow the browser instead
                    if (!target.matches(':focus-visible')) {
                        target.removeAttribute('focus-ring');
                    }
                };
                try {
                    this.toggleAttribute('collapsed', localStorage.getItem(key) === 'true');
                } catch (e) {
                    // Storage unavailable: start open
                }
                $0.addEventListener('click', () => setCollapsed(true));
                $1.addEventListener('click', () => setCollapsed(false));
                this.addEventListener('keydown', e => {
                    if (e.key === 'Escape' && !this.hasAttribute('collapsed')) {
                        setCollapsed(true);
                    }
                });
                """, close.getElement(), open.getElement(), copy.getElement());
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
