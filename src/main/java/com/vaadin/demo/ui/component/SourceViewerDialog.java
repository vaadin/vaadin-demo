package com.vaadin.demo.ui.component;

import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Code;
import com.vaadin.flow.component.html.Pre;

@JavaScript("context://prism.js")
public class SourceViewerDialog extends Dialog {

    public SourceViewerDialog(Class<?> viewClass, SourceService sourceService) {
        setCloseOnEsc(true);
        setHeaderTitle(viewClass.getSimpleName() + ".java");
        setHeight("80vh");
        setMaxWidth("90vw");
        setWidth("900px");

        String source = sourceService.getSource(viewClass);
        String githubUrl = sourceService.getGitHubUrl(viewClass);

        // Close button in header top-right
        Button closeButton = new Button(Lucide.X.create(), e -> close());
        closeButton.addThemeVariants(ButtonVariant.SMALL, ButtonVariant.TERTIARY);
        closeButton.setAriaLabel("Close");
        closeButton.setTooltipText("Close");
        getHeader().add(closeButton);

        // Code block — Prism.js highlights it after render
        Code code = new Code(source);
        code.addClassNames("language-java");

        Pre pre = new Pre(code);
        pre.addClassNames("language-java");
        add(pre);

        // Trigger Prism highlighting after dialog renders
        addOpenedChangeListener(e -> {
            if (e.isOpened()) {
                UI.getCurrent().getPage().executeJs(
                        "setTimeout(() => Prism.highlightElement($0), 50);", code.getElement());
            }
        });

        // Footer: GitHub link with icon
        Anchor gitHub = new Anchor(githubUrl, "View on GitHub");
        gitHub.add(Lucide.SQUARE_ARROW_OUT_UP_RIGHT.create());
        gitHub.addClassName("source-viewer-github-link");
        gitHub.setTarget("_blank");
        getFooter().add(gitHub);
    }
}
