package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Aura;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;

/**
 * Badge indicating the fulfillment status of an order. An unrecognized status
 * renders in the default accent color rather than failing.
 */
public class StatusBadge extends Badge {

    public StatusBadge(String status) {
        super(status);
        addClassName(accentClassName(status));
        if ("Processing".equals(status)) {
            addThemeVariants(BadgeVariant.FILLED);
        }
    }

    /**
     * Aura accent class for a status. Add it to anything that should share
     * the status badge's color, such as a progress bar.
     */
    public static String accentClassName(String status) {
        return switch (status) {
            case "Completed" -> Aura.ACCENT_GREEN;
            case "Pending" -> Aura.ACCENT_YELLOW;
            case "Failed" -> Aura.ACCENT_RED;
            default -> Aura.ACCENT_COLOR;
        };
    }
}
