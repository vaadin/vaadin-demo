package com.vaadin.demo.ui.view;

import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.component.SourceViewerDialog;
import com.vaadin.demo.ui.component.View;
import com.vaadin.demo.ui.component.ViewHeader;
import com.vaadin.demo.ui.util.Aura;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Theme;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.avatar.AvatarGroup.AvatarGroupItem;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.ScrollerVariant;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.sidenav.SideNavVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Route("components")
@PageTitle("Components — Vaadin Demo")
public class ComponentsView extends View {

    private static final BadgeVariant[] BADGE_VARIANTS = {BadgeVariant.SUCCESS, BadgeVariant.ERROR, BadgeVariant.WARNING};
    private static final String[] COUNTRIES = {
            "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda",
            "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
            "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan",
            "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria",
            "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada",
            "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros",
            "Congo", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark",
            "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador",
            "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji",
            "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece",
            "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras",
            "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel",
            "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
            "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia",
            "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi",
            "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania",
            "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
            "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal",
            "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea",
            "North Macedonia", "Norway", "Oman", "Pakistan", "Palau", "Panama",
            "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal",
            "Qatar", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
            "Saint Vincent and the Grenadines", "Samoa", "San Marino", "São Tomé and Príncipe",
            "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore",
            "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
            "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname",
            "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
            "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
            "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates",
            "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu",
            "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
    };
    private static final List<Person> PEOPLE = List.of(
            new Person("Alice Johnson", "alice@example.com", "Active"),
            new Person("Bob Smith", "bob@example.com", "Inactive"),
            new Person("Carol White", "carol@example.com", "Active"),
            new Person("David Brown", "david@example.com", "Pending"),
            new Person("Eva Martinez", "eva@example.com", "Active"),
            new Person("Frank Lee", "frank@example.com", "Active"),
            new Person("Grace Kim", "grace@example.com", "Inactive"),
            new Person("Henry Chen", "henry@example.com", "Active"),
            new Person("Isla Scott", "isla@example.com", "Pending"),
            new Person("James Walker", "james@example.com", "Active"),
            new Person("Karen Hall", "karen@example.com", "Active"),
            new Person("Liam Young", "liam@example.com", "Inactive"),
            new Person("Mia Harris", "mia@example.com", "Active"),
            new Person("Noah Clark", "noah@example.com", "Pending"),
            new Person("Olivia Lewis", "olivia@example.com", "Active"),
            new Person("Peter Robinson", "peter@example.com", "Active"),
            new Person("Quinn Turner", "quinn@example.com", "Inactive"),
            new Person("Rachel Adams", "rachel@example.com", "Active"),
            new Person("Sam Mitchell", "sam@example.com", "Pending"),
            new Person("Tina Nelson", "tina@example.com", "Active")
    );

    public ComponentsView(SourceService sourceService) {
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

        Button viewSource = new Button(Lucide.CODE.create(), e -> new SourceViewerDialog(ComponentsView.class, sourceService).open());
        viewSource.addThemeVariants(ButtonVariant.TERTIARY);
        viewSource.setAriaLabel("View source");
        viewSource.setTooltipText("View source");

        return new ViewHeader(toggle, title, viewSource);
    }

    /**
     * Grid of component showcase cards.
     */
    private Div createCardGrid() {
        Div grid = new Div();
        grid.addClassName("components-card-grid");
        grid.add(
                createButtonsCard(),
                createNotificationCard(),
                createBadgesCard(),
                createRadioButtonGroupCard(),
                createSideNavCard(),
                createMenuBarCard(),
                createTabsCard(),
                createDialogCard(),
                createDateTimePickerCard(),
                createCheckboxGroupCard(),
                createComboBoxCard(),
                createTypographyCard(),
                createGridCard(),
                createSelectCard(),
                createAvatarGroupCard(),
                createMessageListCard(),
                createTextFieldCard(),
                createProgressBarCard(),
                createMultiSelectComboBoxCard()
        );
        return grid;
    }

    // ── Individual cards ──────────────────────────────────────────────────

    /**
     * Card showcasing button variants: primary, default, tertiary, icon-only, success, and error.
     */
    private Card createButtonsCard() {
        Button primary = new Button("Primary");
        primary.addThemeVariants(ButtonVariant.PRIMARY);

        Button secondary = new Button("Default");

        Button tertiary = new Button("Tertiary");
        tertiary.addThemeVariants(ButtonVariant.TERTIARY);

        Button iconButton = new Button(Lucide.BELL_RING.create());
        iconButton.addThemeVariants(ButtonVariant.TERTIARY);
        iconButton.setAriaLabel("Notifications");
        iconButton.setTooltipText("Notifications");

        Button success = new Button("Success");
        success.addThemeVariants(ButtonVariant.PRIMARY, ButtonVariant.SUCCESS);

        Button error = new Button("Error");
        error.addThemeVariants(ButtonVariant.ERROR, ButtonVariant.PRIMARY);

        Div buttons = new Div(primary, secondary, tertiary, iconButton, error, success);
        buttons.addClassName("components-buttons-grid");

        Card card = new Card();
        card.add(buttons);
        card.addClassName("col-span-2");
        return card;
    }

    /**
     * Card demonstrating a rich notification with an inline card and action buttons.
     */
    private Card createNotificationCard() {
        Button button = new Button("Show Notification", Lucide.BELL_RING.create(), e -> {
            Notification notification = new Notification();
            notification.setDuration(0);
            notification.setPosition(Notification.Position.TOP_END);

            Card card = new Card();
            card.addThemeNames(Theme.CARD_FOOTER_END, Theme.CARD_NO_FRAME);
            card.addThemeVariants(CardVariant.HORIZONTAL);
            card.setMedia(Lucide.MESSAGES_SQUARE.create());
            card.setTitle("New Message from Olivia");
            card.add(new Text("The AI chat UI is evolving with the integration of components..."));

            Button show = new Button("Show", ev -> notification.close());
            show.addThemeVariants(ButtonVariant.PRIMARY);
            Button dismiss = new Button("Dismiss", ev -> notification.close());
            card.addToFooter(show, dismiss);

            notification.add(card);
            notification.open();
        });

        Div div = new Div(button);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing badge variants in both text and icon-only styles, plain and filled.
     */
    private Card createBadgesCard() {
        Div badges = new Div();
        badges.addClassName("badges-row");

        badges.add(createBadges());
        badges.add(createBadges(BadgeVariant.FILLED));
        badges.add(createIconBadges());
        badges.add(createIconBadges(BadgeVariant.FILLED));

        Card card = new Card();
        card.add(badges);
        card.addClassNames("col-span-2", "row-span-2");
        return card;
    }

    /**
     * Column of text badges (default + success/error/warning) with optional extra variants applied.
     */
    private Div createBadges(BadgeVariant... variants) {
        Div badges = new Div();
        badges.addClassName("badges-col");

        Badge defaultBadge = new Badge("Default");
        defaultBadge.addThemeVariants(variants);
        badges.add(defaultBadge);

        for (BadgeVariant variant : BADGE_VARIANTS) {
            String name = variant.getVariantName();
            Badge badge = new Badge(Character.toUpperCase(name.charAt(0)) + name.substring(1));
            badge.addThemeVariants(variant);
            badge.addThemeVariants(variants);
            badges.add(badge);
        }
        return badges;
    }

    /**
     * Column of icon-only badges (default + success/error/warning) with optional extra variants applied.
     */
    private Div createIconBadges(BadgeVariant... variants) {
        Div badges = new Div();
        badges.addClassName("badges-col");

        Badge defaultBadge = new Badge(Lucide.BELL_RING.create());
        defaultBadge.addThemeVariants(BadgeVariant.ICON_ONLY);
        defaultBadge.addThemeVariants(variants);
        badges.add(defaultBadge);

        for (BadgeVariant variant : BADGE_VARIANTS) {
            Badge badge = new Badge(Lucide.BELL_RING.create());
            badge.addThemeVariants(variant);
            badge.addThemeVariants(BadgeVariant.ICON_ONLY);
            badge.addThemeVariants(variants);
            badges.add(badge);
        }
        return badges;
    }

    /**
     * Card showcasing a radio button group.
     */
    private Card createRadioButtonGroupCard() {
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>("Options");
        radioButtonGroup.setItems("Option 1", "Option 2", "Option 3");
        radioButtonGroup.setValue("Option 1");

        Div div = new Div(radioButtonGroup);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing a side navigation with nested items and a badge counter.
     */
    private Card createSideNavCard() {
        SideNav nav = new SideNav();
        nav.addThemeVariants(SideNavVariant.AURA_FILLED);

        SideNavItem components = createSideNavItem("Components", getClass(), Lucide.HOUSE);
        Badge badge = new Badge();
        badge.setNumber(2);
        components.setSuffixComponent(badge);
        nav.addItem(components);

        nav.addItem(createSideNavItem("Grid View", Lucide.CHART_COLUMN_BIG));
        nav.addItem(createSideNavItem("Reporting", Lucide.CHART_PIE));

        SideNavItem settings = createSideNavItem("Settings", Lucide.SETTINGS);
        settings.addItem(createSideNavItem("Account"));
        settings.addItem(createSideNavItem("Preferences"));
        settings.addItem(createSideNavItem("Subscription"));
        nav.addItem(settings);

        nav.addItem(createSideNavItem("Support", Lucide.MESSAGES_SQUARE));

        Div div = new Div(nav);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        card.addClassName("row-span-2");
        return card;
    }

    private SideNavItem createSideNavItem(String label) {
        return new SideNavItem(label);
    }

    private SideNavItem createSideNavItem(String label, Lucide icon) {
        SideNavItem item = new SideNavItem(label);
        item.setPrefixComponent(icon.create());
        return item;
    }

    private SideNavItem createSideNavItem(String label, Class<? extends Component> view, Lucide icon) {
        return new SideNavItem(label, view, icon.create());
    }

    /**
     * Card showcasing a deeply nested menu bar with separators, badges, and checkable items.
     */
    private Card createMenuBarCard() {
        MenuBar menuBar = new MenuBar();
        MenuItem actions = menuBar.addItem("Actions");

        actions.getSubMenu().addItem("Edit");
        actions.getSubMenu().addItem("Duplicate");
        actions.getSubMenu().addSeparator();

        MenuItem archive = actions.getSubMenu().addItem("Archive");
        archive.addComponentAsFirst(Lucide.ARCHIVE.create());
        Badge badge = new Badge();
        badge.addClassName(Aura.ACCENT_PURPLE);
        badge.setNumber(2);
        archive.add(badge);

        MenuItem more = actions.getSubMenu().addItem("More");
        more.getSubMenu().addItem("Move to Project...");
        more.getSubMenu().addItem("Move to Folder...");
        more.getSubMenu().addSeparator();

        MenuItem advancedOptions = more.getSubMenu().addItem("Advanced Options");
        advancedOptions.getSubMenu().addItem("Show All").setCheckable(true);
        advancedOptions.getSubMenu().addItem("Show Hidden Items").setCheckable(true);
        advancedOptions.getSubMenu().addSeparator();
        advancedOptions.getSubMenu().addItem("Open...");

        actions.getSubMenu().addSeparator();
        actions.getSubMenu().addItem("Share");
        actions.getSubMenu().addItem("Add to Favorites");
        actions.getSubMenu().addSeparator();
        actions.getSubMenu().addItem("Delete").addClassName(Aura.ACCENT_RED);

        Div div = new Div(menuBar);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing tabs with a badge counter on the first tab.
     */
    private Card createTabsCard() {
        Badge badge = new Badge();
        badge.setNumber(2);

        Tab details = new Tab(new Span("Details"), badge);
        Tab preferences = new Tab("Preferences");
        Tab settings = new Tab("Settings");

        Tabs tabs = new Tabs(details, preferences, settings);

        Div div = new Div(tabs);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        card.addClassName("col-span-2");
        return card;
    }

    /**
     * Card with a button that opens a confirm dialog.
     */
    private Card createDialogCard() {
        Button button = new Button("Open Dialog", e -> {
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Unsaved Changes");
            dialog.setText("Do you want to save or discard the changes? That is the question. But whatever you choose, there will be no consequences.");
            dialog.setCancelable(true);
            dialog.setRejectable(true);
            dialog.setRejectText("Discard");
            dialog.setConfirmText("Save");
            dialog.open();
        });

        Div div = new Div(button);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing a date-time picker.
     */
    private Card createDateTimePickerCard() {
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setWidth("20em");

        Div div = new Div(dateTimePicker);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        card.addClassName("col-span-2");
        return card;
    }

    /**
     * Card showcasing a combo box pre-filled with a country list.
     */
    private Card createComboBoxCard() {
        ComboBox<String> comboBox = new ComboBox<>("Country");
        comboBox.setItems(COUNTRIES);
        comboBox.setValue("Finland");

        Div div = new Div(comboBox);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing a checkbox group.
     */
    private Card createCheckboxGroupCard() {
        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>("Options");
        checkboxGroup.setItems("Option 1", "Option 2", "Option 3");
        checkboxGroup.setValue(Collections.singleton("Option 1"));

        Div div = new Div(checkboxGroup);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
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
        card.add(div);
        return card;
    }

    /**
     * Card showcasing a data grid with multi-row selection and a status badge column.
     */
    private Card createGridCard() {
        Grid<Person> grid = new Grid<>(Person.class, false);
        grid.addThemeVariants(GridVariant.NO_BORDER);
        grid.setItems(PEOPLE);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addComponentColumn(p -> {
            Avatar avatar = new Avatar(p.name());
            avatar.setColorIndex(PEOPLE.indexOf(p) % 7);
            return avatar;
        }).setAutoWidth(true).setFlexGrow(0);

        grid.addColumn(Person::name).setHeader("Name");
        grid.addColumn(Person::email).setHeader("Email");

        grid.addComponentColumn(person -> {
            Badge badge = new Badge(person.status());
            if ("Active".equals(person.status())) badge.addThemeVariants(BadgeVariant.SUCCESS);
            if ("Inactive".equals(person.status())) badge.addThemeVariants(BadgeVariant.ERROR);
            return badge;
        }).setHeader("Status");

        Card card = new Card();
        card.add(grid);
        card.addClassNames(Aura.SURFACE_SOLID, "col-span-3", "row-span-2");
        return card;
    }

    /**
     * Card showcasing a select field.
     */
    private Card createSelectCard() {
        Select<String> select = new Select<>("Options");
        select.setItems("Option 1", "Option 2", "Option 3");
        select.setValue("Option 1");

        Div div = new Div(select);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing an avatar group.
     */
    private Card createAvatarGroupCard() {
        String[] names = {
                "Alice Johnson", "Bob Smith", "Carol White", "David Brown", "Eva Martinez",
                "Frank Lee", "Grace Kim", "Henry Chen", "Isla Scott"
        };

        AvatarGroup avatarGroup = new AvatarGroup();
        int colorIndex = 0;
        for (String name : names) {
            AvatarGroupItem avatar = new AvatarGroupItem(name);
            avatar.setColorIndex(colorIndex++);
            avatarGroup.add(avatar);
        }

        Div div = new Div(avatarGroup);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing a message list with a message input.
     */
    private Card createMessageListCard() {
        MessageListItem matt = new MessageListItem(
                "Nature does not hurry, yet everything gets accomplished.",
                Instant.now().minus(2, ChronoUnit.MINUTES),
                "Matt Mambo");
        matt.setUserColorIndex(1);

        MessageListItem lindsey = new MessageListItem(
                "Using your talent, hobby or profession in a way that makes you contribute with something good to this world is truly the way to go.",
                Instant.now(),
                "Lindsey Listy");
        lindsey.setUserColorIndex(2);

        MessageList list = new MessageList(matt, lindsey);

        MessageInput input = new MessageInput();

        Div div = new Div(list, input);
        div.addClassName("message-list-card-content");

        Card card = new Card();
        card.add(div);
        card.addClassNames("col-span-2", "row-span-2");
        return card;
    }

    /**
     * Card showcasing a progress bar.
     */
    private Card createProgressBarCard() {
        ProgressBar progressBar = new ProgressBar(0, 100, 50);

        Div div = new Div(progressBar);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing a multi-select combo box pre-filled with a country list.
     */
    private Card createMultiSelectComboBoxCard() {
        MultiSelectComboBox<String> comboBox = new MultiSelectComboBox<>("Country");
        comboBox.setItems(COUNTRIES);
        comboBox.setValue(Set.of("Finland", "Sweden"));

        Div div = new Div(comboBox);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    /**
     * Card showcasing a text field with a clear button and icon prefix.
     */
    private Card createTextFieldCard() {
        TextField textField = new TextField();
        textField.setClearButtonVisible(true);
        textField.setPrefixComponent(Lucide.FOLDER.create());
        textField.setValue("Projects");

        Div div = new Div(textField);
        div.addClassName("card-center");

        Card card = new Card();
        card.add(div);
        return card;
    }

    record Person(String name, String email, String status) {
    }
}
