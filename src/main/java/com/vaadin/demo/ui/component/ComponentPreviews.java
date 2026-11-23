package com.vaadin.demo.ui.component;

import com.vaadin.demo.ui.util.Aura;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Theme;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
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
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.ScrollerVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.sidenav.SideNavVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.SerializableSupplier;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Live examples of Vaadin components, shared by the Components view gallery
 * and the "Components used here" tab of the source viewer, so both always
 * show the same thing. Each method builds a fresh instance.
 */
public final class ComponentPreviews {

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

    /**
     * Previews keyed by the component's docs page, as used by
     * {@code SourceService.UsedComponent.page()}.
     */
    private static final Map<String, SerializableSupplier<Component>> BY_DOCS_PAGE = Map.ofEntries(
            Map.entry("app-layout", ComponentPreviews::appLayout),
            Map.entry("avatar", ComponentPreviews::avatarGroup),
            Map.entry("badge", ComponentPreviews::badges),
            Map.entry("button", ComponentPreviews::buttons),
            Map.entry("card", ComponentPreviews::card),
            Map.entry("checkbox", ComponentPreviews::checkboxGroup),
            Map.entry("combo-box", ComponentPreviews::comboBox),
            Map.entry("confirm-dialog", ComponentPreviews::confirmDialog),
            Map.entry("context-menu", ComponentPreviews::contextMenu),
            Map.entry("date-time-picker", ComponentPreviews::dateTimePicker),
            Map.entry("dialog", ComponentPreviews::dialog),
            Map.entry("email-field", ComponentPreviews::emailField),
            Map.entry("form-layout", ComponentPreviews::formLayout),
            Map.entry("grid", ComponentPreviews::grid),
            Map.entry("horizontal-layout", ComponentPreviews::horizontalLayout),
            Map.entry("icons", ComponentPreviews::icons),
            Map.entry("markdown", ComponentPreviews::markdown),
            Map.entry("menu-bar", ComponentPreviews::menuBar),
            Map.entry("message-input", ComponentPreviews::messageInput),
            Map.entry("message-list", ComponentPreviews::messageList),
            Map.entry("multi-select-combo-box", ComponentPreviews::multiSelectComboBox),
            Map.entry("notification", ComponentPreviews::notification),
            Map.entry("number-field", ComponentPreviews::numberField),
            Map.entry("password-field", ComponentPreviews::passwordField),
            Map.entry("progress-bar", ComponentPreviews::progressBar),
            Map.entry("radio-button", ComponentPreviews::radioButtonGroup),
            Map.entry("scroller", ComponentPreviews::scroller),
            Map.entry("select", ComponentPreviews::select),
            Map.entry("side-nav", ComponentPreviews::sideNav),
            Map.entry("tabs", ComponentPreviews::tabs),
            Map.entry("text-field", ComponentPreviews::textField),
            Map.entry("tooltip", ComponentPreviews::tooltip),
            Map.entry("vertical-layout", ComponentPreviews::verticalLayout)
    );

    private ComponentPreviews() {}

    /**
     * A fresh preview for the component documented on the given docs page,
     * or empty if there is no preview for it.
     */
    public static Optional<Component> forDocsPage(String page) {
        return Optional.ofNullable(BY_DOCS_PAGE.get(page)).map(SerializableSupplier::get);
    }

    /**
     * Button variants: primary, default, tertiary, icon-only, success, and error.
     */
    public static Component buttons() {
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

        return preview("buttons-grid", primary, secondary, tertiary, iconButton, error, success);
    }

    /**
     * Button opening a rich notification with an inline card and action buttons.
     */
    public static Component notification() {
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
        return preview("card-center", button);
    }

    /**
     * Badge variants in both text and icon-only styles, plain and filled.
     */
    public static Component badges() {
        return preview("badges-row",
                createBadges(),
                createBadges(BadgeVariant.FILLED),
                createIconBadges(),
                createIconBadges(BadgeVariant.FILLED));
    }

    /**
     * Column of text badges (default + success/error/warning) with optional extra variants applied.
     */
    private static Div createBadges(BadgeVariant... variants) {
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
    private static Div createIconBadges(BadgeVariant... variants) {
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

    public static Component radioButtonGroup() {
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>("Options");
        radioButtonGroup.setItems("Option 1", "Option 2", "Option 3");
        radioButtonGroup.setValue("Option 1");
        return preview("card-center", radioButtonGroup);
    }

    /**
     * Side navigation with nested items and a badge counter. The items have
     * no routes, so clicking them in a preview doesn't navigate away.
     */
    public static Component sideNav() {
        SideNav nav = new SideNav();
        nav.addThemeVariants(SideNavVariant.AURA_FILLED);

        SideNavItem components = createSideNavItem("Components", Lucide.HOUSE);
        Badge badge = new Badge();
        badge.setNumber(2);
        components.setSuffixComponent(badge);
        nav.addItem(components);

        nav.addItem(createSideNavItem("Grid View", Lucide.CHART_COLUMN_BIG));
        nav.addItem(createSideNavItem("Reporting", Lucide.CHART_PIE));

        SideNavItem settings = createSideNavItem("Settings", Lucide.SETTINGS);
        settings.addItem(new SideNavItem("Account"));
        settings.addItem(new SideNavItem("Preferences"));
        settings.addItem(new SideNavItem("Subscription"));
        nav.addItem(settings);

        nav.addItem(createSideNavItem("Support", Lucide.MESSAGES_SQUARE));

        return preview("card-center", nav);
    }

    private static SideNavItem createSideNavItem(String label, Lucide icon) {
        SideNavItem item = new SideNavItem(label);
        item.setPrefixComponent(icon.create());
        return item;
    }

    /**
     * Deeply nested menu bar with separators, badges, and checkable items.
     */
    public static Component menuBar() {
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

        return preview("card-center", menuBar);
    }

    /**
     * Tabs with a badge counter on the first tab.
     */
    public static Component tabs() {
        Badge badge = new Badge();
        badge.setNumber(2);

        Tab details = new Tab(new Span("Details"), badge);
        Tab preferences = new Tab("Preferences");
        Tab settings = new Tab("Settings");

        return preview("card-center", new Tabs(details, preferences, settings));
    }

    /**
     * Button opening a confirm dialog.
     */
    public static Component confirmDialog() {
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
        return preview("card-center", button);
    }

    public static Component dateTimePicker() {
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setWidthFull();
        dateTimePicker.setMaxWidth("20em");
        return preview("card-center", dateTimePicker);
    }

    /**
     * Combo box pre-filled with a country list.
     */
    public static Component comboBox() {
        ComboBox<String> comboBox = new ComboBox<>("Country");
        comboBox.setItems(COUNTRIES);
        comboBox.setValue("Finland");
        return preview("card-center", comboBox);
    }

    public static Component checkboxGroup() {
        CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>("Options");
        checkboxGroup.setItems("Option 1", "Option 2", "Option 3");
        checkboxGroup.setValue(Collections.singleton("Option 1"));
        return preview("card-center", checkboxGroup);
    }

    /**
     * Data grid with multi-row selection and a status badge column.
     */
    public static Component grid() {
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

        return preview("grid-preview", grid);
    }

    public static Component select() {
        Select<String> select = new Select<>("Options");
        select.setItems("Option 1", "Option 2", "Option 3");
        select.setValue("Option 1");
        return preview("card-center", select);
    }

    public static Component avatarGroup() {
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
        // A fixed count lets the group shrink to its avatars so it can be
        // centered, instead of filling the width and aligning them left
        avatarGroup.setMaxItemsVisible(6);
        avatarGroup.addClassName("fit-content");
        return preview("card-center", avatarGroup);
    }

    /**
     * Message list with a message input below it.
     */
    public static Component messageList() {
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

        return preview("message-list-card-content", new MessageList(matt, lindsey), new MessageInput());
    }

    public static Component messageInput() {
        return preview("card-center", new MessageInput());
    }

    public static Component progressBar() {
        return preview("card-center", new ProgressBar(0, 100, 50));
    }

    /**
     * Multi-select combo box pre-filled with a country list.
     */
    public static Component multiSelectComboBox() {
        MultiSelectComboBox<String> comboBox = new MultiSelectComboBox<>("Country");
        comboBox.setItems(COUNTRIES);
        comboBox.setValue(Set.of("Finland", "Sweden"));
        comboBox.setWidthFull();
        return preview("card-center", comboBox);
    }

    /**
     * Text field with a clear button and icon prefix.
     */
    public static Component textField() {
        TextField textField = new TextField();
        textField.setClearButtonVisible(true);
        textField.setPrefixComponent(Lucide.FOLDER.create());
        textField.setValue("Projects");
        return preview("card-center", textField);
    }

    /**
     * App Layout can't be nested inside a preview, so this points at the
     * real one around the page instead.
     */
    public static Component appLayout() {
        Span hint = new Span("You're looking at it: the drawer, navigation and page area around this dialog are an App Layout.");
        hint.addClassName("preview-hint");
        return preview("card-center", hint);
    }

    public static Component card() {
        Card card = new Card();
        card.setTitle("Lapland");
        card.setSubtitle(new Span("The Exotic North"));
        card.add(new Text("The northern-most region of Finland and an active outdoor destination."));
        card.setMaxWidth("18rem");
        return preview("card-center", card);
    }

    /**
     * Button opening a context menu on click rather than only on right-click.
     */
    public static Component contextMenu() {
        Button target = new Button("Open Menu", Lucide.ELLIPSIS_VERTICAL.create());
        target.setIconAfterText(true);

        ContextMenu menu = new ContextMenu(target);
        menu.setOpenOnClick(true);
        menu.addItem("Edit");
        menu.addItem("Duplicate");
        menu.addItem("Share");
        return preview("card-center", target);
    }

    /**
     * Button opening a dialog with a header, a field, and footer actions.
     */
    public static Component dialog() {
        Button button = new Button("Open Dialog", e -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("New project");
            dialog.add(new TextField("Name"));

            Button cancel = new Button("Cancel", ev -> dialog.close());
            Button create = new Button("Create", ev -> dialog.close());
            create.addThemeVariants(ButtonVariant.PRIMARY);
            dialog.getFooter().add(cancel, create);
            dialog.open();
        });
        return preview("card-center", button);
    }

    public static Component emailField() {
        EmailField email = new EmailField("Email");
        email.setValue("alice@example.com");
        return preview("card-center", email);
    }

    /**
     * Form layout that fits as many columns as the available width allows.
     */
    public static Component formLayout() {
        FormLayout form = new FormLayout(
                new TextField("First name"),
                new TextField("Last name"),
                new EmailField("Email"));
        form.setAutoResponsive(true);
        return preview("block", form);
    }

    public static Component horizontalLayout() {
        HorizontalLayout layout = new HorizontalLayout(layoutBox("1"), layoutBox("2"), layoutBox("3"));
        layout.setWrap(true);
        return preview("card-center", layout);
    }

    public static Component verticalLayout() {
        VerticalLayout layout = new VerticalLayout(layoutBox("1"), layoutBox("2"), layoutBox("3"));
        layout.setPadding(false);
        layout.setWidth(null);
        return preview("card-center", layout);
    }

    /**
     * Numbered placeholder block showing how a layout arranges its children.
     */
    private static Div layoutBox(String label) {
        Div box = new Div(label);
        box.addClassName("layout-box");
        return box;
    }

    /**
     * Scroller with a fixed height and more content than fits.
     */
    public static Component scroller() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        for (int i = 1; i <= 12; i++) {
            content.add(new Span("Item " + i));
        }

        Scroller scroller = new Scroller(content);
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        scroller.setHeight("8rem");
        scroller.setWidthFull();
        return preview("block", scroller);
    }

    public static Component icons() {
        return preview("card-center",
                VaadinIcon.HOME.create(), VaadinIcon.USER.create(), VaadinIcon.COG.create(),
                VaadinIcon.BELL.create(), VaadinIcon.SEARCH.create(), VaadinIcon.STAR.create(),
                Lucide.HOUSE.create(), Lucide.USERS.create(), Lucide.SETTINGS.create());
    }

    public static Component markdown() {
        return preview("block", new Markdown("""
                **Markdown** renders *formatted* text from a string:

                - Lists and `inline code`
                - [Links](https://vaadin.com)
                """));
    }

    public static Component numberField() {
        NumberField quantity = new NumberField("Quantity");
        quantity.setValue(3d);
        quantity.setStepButtonsVisible(true);
        return preview("card-center", quantity);
    }

    public static Component passwordField() {
        PasswordField password = new PasswordField("Password");
        password.setValue("secret123");
        return preview("card-center", password);
    }

    public static Component tooltip() {
        Button button = new Button("Hover or Focus Me");
        button.setTooltipText("Tooltips give short extra information");
        return preview("card-center", button);
    }

    /**
     * Wraps preview content in a container laid out by component-previews.css.
     */
    private static Div preview(String layoutClass, Component... content) {
        Div div = new Div(content);
        div.addClassNames("component-preview", layoutClass);
        return div;
    }

    record Person(String name, String email, String status) {
    }
}
