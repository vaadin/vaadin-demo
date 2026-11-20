package com.vaadin.demo.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SourceService {

    private static final String DOCS_URL = "https://vaadin.com/docs/latest/components/";

    private static final Pattern COMPONENT_IMPORT =
            Pattern.compile("^import com\\.vaadin\\.flow\\.component\\.[\\w.]*\\.(\\w+);", Pattern.MULTILINE);

    /**
     * Vaadin component classes mapped to their docs page. Helper classes map to
     * the component they belong to (e.g. DrawerToggle to App Layout), as do
     * components documented on another's page (TabSheet on Tabs, AvatarGroup
     * on Avatar, IntegerField on Number Field); anything not listed, such as
     * variants and html elements, is ignored.
     */
    private static final Map<String, String> DOCS_PAGES = Map.ofEntries(
            Map.entry("Accordion", "accordion"),
            Map.entry("AppLayout", "app-layout"),
            Map.entry("DrawerToggle", "app-layout"),
            Map.entry("Avatar", "avatar"),
            Map.entry("AvatarGroup", "avatar"),
            Map.entry("Badge", "badge"),
            Map.entry("Button", "button"),
            Map.entry("Card", "card"),
            Map.entry("Checkbox", "checkbox"),
            Map.entry("CheckboxGroup", "checkbox"),
            Map.entry("ComboBox", "combo-box"),
            Map.entry("ConfirmDialog", "confirm-dialog"),
            Map.entry("ContextMenu", "context-menu"),
            Map.entry("GridContextMenu", "context-menu"),
            Map.entry("DatePicker", "date-picker"),
            Map.entry("DateTimePicker", "date-time-picker"),
            Map.entry("Details", "details"),
            Map.entry("Dialog", "dialog"),
            Map.entry("EmailField", "email-field"),
            Map.entry("FormLayout", "form-layout"),
            Map.entry("Grid", "grid"),
            Map.entry("HorizontalLayout", "horizontal-layout"),
            Map.entry("Icon", "icons"),
            Map.entry("SvgIcon", "icons"),
            Map.entry("VaadinIcon", "icons"),
            Map.entry("IntegerField", "number-field"),
            Map.entry("ListBox", "list-box"),
            Map.entry("Markdown", "markdown"),
            Map.entry("MenuBar", "menu-bar"),
            Map.entry("MessageInput", "message-input"),
            Map.entry("MessageList", "message-list"),
            Map.entry("MultiSelectComboBox", "multi-select-combo-box"),
            Map.entry("Notification", "notification"),
            Map.entry("NumberField", "number-field"),
            Map.entry("PasswordField", "password-field"),
            Map.entry("ProgressBar", "progress-bar"),
            Map.entry("RadioButtonGroup", "radio-button"),
            Map.entry("Scroller", "scroller"),
            Map.entry("Select", "select"),
            Map.entry("SideNav", "side-nav"),
            Map.entry("SplitLayout", "split-layout"),
            Map.entry("TabSheet", "tabs"),
            Map.entry("Tabs", "tabs"),
            Map.entry("TextArea", "text-area"),
            Map.entry("TextField", "text-field"),
            Map.entry("TimePicker", "time-picker"),
            Map.entry("Tooltip", "tooltip"),
            Map.entry("TreeGrid", "tree-grid"),
            Map.entry("Upload", "upload"),
            Map.entry("VerticalLayout", "vertical-layout")
    );

    /**
     * A Vaadin component used by a view, with a link to its documentation.
     * {@code page} is the docs page id, e.g. "combo-box".
     */
    public record UsedComponent(String page, String name, String docsUrl) {}

    public String getSource(Class<?> clazz) {
        String path = "source/" + clazz.getName().replace('.', '/') + ".java";
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "// Source not available for " + clazz.getSimpleName() + "\n// Path: " + path;
        }
    }

    public String getGitHubUrl(Class<?> clazz) {
        String path = "src/main/java/" + clazz.getName().replace('.', '/') + ".java";
        return "https://github.com/vaadin/vaadin-demo/blob/main/" + path + "#L1";
    }

    /**
     * Vaadin components imported by the class's source, one entry per docs
     * page, sorted by name.
     */
    public List<UsedComponent> getComponentsUsed(Class<?> clazz) {
        return COMPONENT_IMPORT.matcher(getSource(clazz)).results()
                .map(match -> DOCS_PAGES.get(match.group(1)))
                .filter(Objects::nonNull)
                .distinct()
                .map(SourceService::toUsedComponent)
                .sorted((a, b) -> a.name().compareTo(b.name()))
                .toList();
    }

    /**
     * Documentation link for a single Vaadin component class, if it has one.
     */
    public Optional<UsedComponent> getDocs(Class<?> componentClass) {
        return Optional.ofNullable(DOCS_PAGES.get(componentClass.getSimpleName()))
                .map(SourceService::toUsedComponent);
    }

    private static UsedComponent toUsedComponent(String page) {
        return new UsedComponent(page, displayName(page), DOCS_URL + page);
    }

    private static String displayName(String page) {
        return Arrays.stream(page.split("-"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
