package com.vaadin.demo.ui;

import com.vaadin.demo.ui.component.CloneBanner;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Adds the {@link CloneBanner} to every UI of a production (hosted) run.
 */
@Component
public class CloneBannerInitListener implements VaadinServiceInitListener {

    private final String cloneUrl;

    public CloneBannerInitListener(@Value("${demo.git-clone-url:}") String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        if (!event.getSource().getDeploymentConfiguration().isProductionMode() || cloneUrl.isBlank()) {
            return;
        }
        event.getSource().addUIInitListener(e -> e.getUI().add(new CloneBanner(cloneUrl)));
    }
}
