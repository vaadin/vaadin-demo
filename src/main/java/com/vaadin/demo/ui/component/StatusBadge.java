package com.vaadin.demo.ui.component;

import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;

/**
 * Badge indicating the fulfillment status of an order. An unrecognized status
 * renders as a plain badge rather than failing.
 */
public class StatusBadge extends Badge {

    public StatusBadge(String status) {
        super(status);
        switch (status) {
            case "Completed" -> addThemeVariants(BadgeVariant.SUCCESS);
            case "Processing" -> addThemeVariants(BadgeVariant.FILLED);
            case "Failed" -> addThemeVariants(BadgeVariant.ERROR);
            case "Pending" -> addThemeVariants(BadgeVariant.CONTRAST);
            default -> { }
        }
    }
}
