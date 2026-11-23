package com.vaadin.demo.ui;

import com.vaadin.demo.ui.component.CloneBanner;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Adds the {@link CloneBanner} to every UI of a production (hosted) run, or
 * also in development mode when demo.clone-banner.show-in-development is set,
 * to try it out locally.
 */
@Component
public class CloneBannerInitListener implements VaadinServiceInitListener {

    private final String cloneUrl;
    private final boolean showInDevelopment;

    public CloneBannerInitListener(@Value("${demo.git-clone-url:}") String cloneUrl,
            @Value("${demo.clone-banner.show-in-development:false}") boolean showInDevelopment) {
        this.cloneUrl = cloneUrl;
        this.showInDevelopment = showInDevelopment;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        boolean production = event.getSource().getDeploymentConfiguration().isProductionMode();
        if (cloneUrl.isBlank() || !(production || showInDevelopment)) {
            return;
        }
        event.getSource().addUIInitListener(e -> e.getUI().add(new CloneBanner(cloneUrl)));
    }
}
