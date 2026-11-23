package com.vaadin.demo.ui.util;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

/**
 * Short notifications in the bottom end corner. Each starts with an icon
 * matching its variant, so a success message doesn't rely on color alone.
 */
public class Notifications {

    private Notifications() {}

    public static void show(String message, NotificationVariant... variants) {
        boolean success = List.of(variants).contains(NotificationVariant.SUCCESS);
        SvgIcon icon = (success ? Lucide.CIRCLE_CHECK : Lucide.INFO).create();

        HorizontalLayout content = new HorizontalLayout(icon, new Span(message));
        content.setAlignItems(Alignment.CENTER);
        content.setPadding(false);
        // Long messages wrap instead of squeezing the icon
        content.setFlexShrink(0, icon);

        Notification notification = new Notification(content);
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.BOTTOM_END);
        notification.addThemeVariants(variants);
        notification.open();
    }
}
