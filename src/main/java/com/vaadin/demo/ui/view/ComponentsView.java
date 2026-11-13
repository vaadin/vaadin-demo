package com.vaadin.demo.ui.view;

import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.component.ComponentPreviews;
import com.vaadin.demo.ui.component.DocsLink;
import com.vaadin.demo.ui.component.SourceViewerDialog;
import com.vaadin.demo.ui.component.View;
import com.vaadin.demo.ui.component.ViewHeader;
import com.vaadin.demo.ui.util.Aura;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.ScrollerVariant;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Gallery of Vaadin components. Each card shows a live example from
 * {@link ComponentPreviews} and links to the component's documentation.
 */
@Route("components")
@PageTitle("Components — Vaadin Demo")
public class ComponentsView extends View {

    private final SourceService sourceService;

    public ComponentsView(SourceService sourceService) {
        this.sourceService = sourceService;
        addClassName("components-view");

        ViewHeader header = createHeader(sourceService);
        Scroller scroller = createScroller();

        add(header, scroller);
    }

    /**
     * Scroller wrapping the component showcase card grid.
     */
    private Scroller createScroller() {
        Scroller scroller = new Scroller(createCardGrid());
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        return scroller;
    }

    /**
     * Page header with drawer toggle, title, and source viewer button.
     */
    private ViewHeader createHeader(SourceService sourceService) {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addThemeVariants(ButtonVariant.TERTIARY);

        H1 title = new H1("Components");

        Button viewSource = new Button("View source", Lucide.CODE.create(), e -> new SourceViewerDialog(ComponentsView.class, sourceService).open());
        viewSource.addThemeVariants(ButtonVariant.TERTIARY);

        return new ViewHeader(toggle, title, viewSource);
    }

    /**
     * Grid of component showcase cards. The class names size cards within
     * the stylesheet's dense grid.
     */
    private Div createCardGrid() {
        Div grid = new Div();
        grid.addClassName("card-grid");
        grid.add(
                card("Button", Button.class, ComponentPreviews.buttons(), "col-span-2"),
                card("Notification", Notification.class, ComponentPreviews.notification()),
                card("Badge", Badge.class, ComponentPreviews.badges(), "col-span-2", "row-span-2"),
                card("Radio Button Group", RadioButtonGroup.class, ComponentPreviews.radioButtonGroup()),
                card("Side Nav", SideNav.class, ComponentPreviews.sideNav(), "row-span-2"),
                card("Menu Bar", MenuBar.class, ComponentPreviews.menuBar()),
                card("Tabs", Tabs.class, ComponentPreviews.tabs(), "col-span-2"),
                card("Confirm Dialog", ConfirmDialog.class, ComponentPreviews.confirmDialog()),
                card("Date Time Picker", DateTimePicker.class, ComponentPreviews.dateTimePicker(), "col-span-2"),
                card("Checkbox Group", CheckboxGroup.class, ComponentPreviews.checkboxGroup()),
                card("Combo Box", ComboBox.class, ComponentPreviews.comboBox()),
                createTypographyCard(),
                card("Grid", Grid.class, ComponentPreviews.grid(), Aura.SURFACE_SOLID, "col-span-3", "row-span-2"),
                card("Select", Select.class, ComponentPreviews.select()),
                card("Avatar Group", AvatarGroup.class, ComponentPreviews.avatarGroup()),
                card("Message List", MessageList.class, ComponentPreviews.messageList(), "col-span-2", "row-span-2"),
                card("Text Field", TextField.class, ComponentPreviews.textField()),
                card("Progress Bar", ProgressBar.class, ComponentPreviews.progressBar()),
                card("Multi-Select Combo Box", MultiSelectComboBox.class, ComponentPreviews.multiSelectComboBox())
        );
        return grid;
    }

    /**
     * Showcase card with a title, the live preview as content, and a link to
     * the component's documentation on vaadin.com in the footer.
     */
    private Card card(String title, Class<? extends Component> componentClass, Component preview,
                      String... classNames) {
        Card card = new Card();
        card.setTitle(title);
        card.add(preview);
        card.addClassNames(classNames);
        sourceService.getDocs(componentClass)
                .ifPresent(docs -> card.addToFooter(new DocsLink(title, docs.docsUrl())));
        return card;
    }

    /**
     * Card showcasing the full typography size scale.
     */
    private Card createTypographyCard() {
        String[] sizes = {"text-xl", "text-l", "text-m", "text-s", "text-xs"};

        Div div = new Div();
        div.addClassName("typography-card");
        for (String size : sizes) {
            Div heading = new Div("Heading");
            heading.addClassName(size);
            div.add(heading);
        }

        Card card = new Card();
        card.setTitle("Typography");
        card.add(div);
        return card;
    }
}
