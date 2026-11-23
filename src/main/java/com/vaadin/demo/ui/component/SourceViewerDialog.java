package com.vaadin.demo.ui.component;

import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Code;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;

@JavaScript("context://prism.js")
public class SourceViewerDialog extends Dialog {

    public SourceViewerDialog(Class<?> viewClass, SourceService sourceService) {
        addClassName("source-viewer-dialog");
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

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        Tab sourceTab = tabs.add("Source", pre);
        tabs.add("Components used here", createComponentsList(viewClass, sourceService));
        add(tabs);

        // Trigger Prism highlighting after dialog renders, and again when the
        // source tab is shown, in case its content was re-rendered meanwhile
        Runnable highlight = () -> UI.getCurrent().getPage().executeJs(
                "setTimeout(() => Prism.highlightElement($0), 50);", code.getElement());
        addOpenedChangeListener(e -> {
            if (e.isOpened()) {
                highlight.run();
            }
        });
        tabs.addSelectedChangeListener(e -> {
            if (e.getSelectedTab() == sourceTab) {
                highlight.run();
            }
        });

        // Footer: GitHub link with icon
        Anchor gitHub = new Anchor(githubUrl, "View on GitHub");
        gitHub.add(Lucide.SQUARE_ARROW_OUT_UP_RIGHT.create());
        gitHub.addClassName("github-link");
        gitHub.setTarget("_blank");
        getFooter().add(gitHub);
    }

    /**
     * Vaadin components imported by the view, each as a card with a link to
     * its docs page and, where {@link ComponentPreviews} has one, a live
     * preview of the component.
     */
    private Component createComponentsList(Class<?> viewClass, SourceService sourceService) {
        var components = sourceService.getComponentsUsed(viewClass);

        VerticalLayout list = new VerticalLayout();
        list.addClassName("components-used");
        list.setPadding(false);

        if (components.isEmpty()) {
            list.add(new Span("No Vaadin components found in " + viewClass.getSimpleName() + ".java"));
            return list;
        }

        components.forEach(component -> {
            Card card = new Card();
            card.setTitle(component.name());
            ComponentPreviews.forDocsPage(component.page()).ifPresent(card::add);
            card.setHeaderSuffix(new DocsLink(component.name(), component.docsUrl()));
            card.setWidthFull();
            list.add(card);
        });
        return list;
    }
}
