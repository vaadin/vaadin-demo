package com.vaadin.demo.ui.view;

import com.vaadin.demo.data.SampleData;
import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.component.SourceViewerDialog;
import com.vaadin.demo.ui.component.View;
import com.vaadin.demo.ui.component.ViewHeader;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Notifications;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.ScrollerVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("settings")
@PageTitle("Settings — Vaadin Demo")
public class SettingsView extends View {

    public SettingsView(SourceService sourceService) {
        addClassName("settings-view");

        ViewHeader header = createHeader(sourceService);
        Scroller scroller = createScroller();

        add(header, scroller);
    }

    /**
     * Page header with drawer toggle, title, and source viewer button.
     */
    private ViewHeader createHeader(SourceService sourceService) {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addThemeVariants(ButtonVariant.TERTIARY);

        H1 title = new H1("Settings");

        Button viewSource = new Button(Lucide.CODE.create(), e -> new SourceViewerDialog(SettingsView.class, sourceService).open());
        viewSource.addThemeVariants(ButtonVariant.TERTIARY);
        viewSource.setAriaLabel("View source");
        viewSource.setTooltipText("View source");

        return new ViewHeader(toggle, title, viewSource);
    }

    /**
     * Scroller containing a tab bar wired to show/hide the settings panels.
     */
    private Scroller createScroller() {
        Div general = buildGeneralTab();
        Div notifications = buildNotificationsTab();
        Div security = buildSecurityTab();
        Div integrations = buildIntegrationsTab();

        Tab generalTab = new Tab("General");
        Tab notificationsTab = new Tab("Notifications");
        Tab securityTab = new Tab("Security");
        Tab integrationsTab = new Tab("Integrations");

        notifications.setVisible(false);
        security.setVisible(false);
        integrations.setVisible(false);

        Tabs tabs = new Tabs(generalTab, notificationsTab, securityTab, integrationsTab);
        tabs.addClassName("tabs");
        tabs.addSelectedChangeListener(e -> {
            general.setVisible(e.getSelectedTab() == generalTab);
            notifications.setVisible(e.getSelectedTab() == notificationsTab);
            security.setVisible(e.getSelectedTab() == securityTab);
            integrations.setVisible(e.getSelectedTab() == integrationsTab);
        });

        Scroller scroller = new Scroller();
        scroller.getElement().appendChild(
                tabs.getElement(),
                general.getElement(),
                notifications.getElement(),
                security.getElement(),
                integrations.getElement()
        );
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        return scroller;
    }

    /**
     * Tab content for general organization settings.
     */
    private Div buildGeneralTab() {
        TextField orgName = new TextField("Organization Name");
        orgName.setValue("Acme Corporation");

        TextField orgUrl = new TextField("Website URL");
        orgUrl.setValue("https://acme.example.com");

        EmailField supportEmail = new EmailField("Support Email");
        supportEmail.setValue("support@acme.example.com");

        Select<String> timezone = new Select<>("Timezone");
        timezone.setItems("UTC", "US/Eastern", "US/Pacific", "Europe/London", "Europe/Berlin", "Asia/Tokyo");
        timezone.setValue("UTC");

        Select<String> language = new Select<>("Language");
        language.setItems("English", "Finnish", "German", "French", "Spanish");
        language.setValue("English");

        FormLayout form = new FormLayout(orgName, orgUrl, supportEmail, timezone, language);
        form.setAutoResponsive(true);
        form.setColumnWidth("16rem");
        form.setExpandFields(true);

        Button save = new Button("Save Changes", e -> Notifications.show("General settings saved", NotificationVariant.SUCCESS));
        save.addThemeVariants(ButtonVariant.PRIMARY);

        Div layout = new Div(form, save);
        layout.addClassName("panel");
        return layout;
    }

    /**
     * Tab content for notification email preferences.
     */
    private Div buildNotificationsTab() {
        FormLayout form = new FormLayout(
                new Checkbox("Email me on new orders", true),
                new Checkbox("Email me on failed payments", true),
                new Checkbox("Weekly usage digest", true),
                new Checkbox("Security alerts", true),
                new Checkbox("Product updates and announcements", false),
                new Checkbox("Marketing emails", false)
        );
        form.setAutoResponsive(true);
        form.setExpandColumns(true);
        form.setExpandFields(true);

        Button save = new Button("Save Preferences", e -> Notifications.show("Notification preferences saved", NotificationVariant.SUCCESS));
        save.addThemeVariants(ButtonVariant.PRIMARY);

        Div layout = new Div(form, save);
        layout.addClassName("panel");
        return layout;
    }

    /**
     * Tab content for password change and two-factor authentication.
     */
    private Div buildSecurityTab() {
        PasswordField currentPassword = new PasswordField("Current Password");
        PasswordField newPassword = new PasswordField("New Password");
        PasswordField confirmPassword = new PasswordField("Confirm New Password");

        FormLayout form = new FormLayout(currentPassword, newPassword, confirmPassword);
        form.setAutoResponsive(true);
        form.setColumnWidth("16rem");
        form.setExpandFields(true);

        Button changePassword = new Button("Change Password", e -> Notifications.show("Password change is not implemented in this demo"));
        changePassword.addClassName("section-button");
        changePassword.addThemeVariants(ButtonVariant.PRIMARY);

        Hr hr = new Hr();
        hr.addClassName("section-divider");

        H2 twoFactorHeading = new H2("Two-Factor Authentication");
        twoFactorHeading.addClassName("two-factor-heading");

        Paragraph twoFactorDescription = new Paragraph("Add an extra layer of security to your account.");
        twoFactorDescription.addClassName("two-factor-description");

        Button enableTwoFactor = new Button("Enable 2FA", e -> Notifications.show("2FA setup is not implemented in this demo"));
        enableTwoFactor.addClassName("section-button");

        Div layout = new Div(form, changePassword, hr, twoFactorHeading, twoFactorDescription, enableTwoFactor);
        layout.addClassName("security-panel");
        return layout;
    }

    /**
     * Tab content listing available third-party integrations.
     */
    private Div buildIntegrationsTab() {
        Div grid = new Div();
        grid.addClassName("integrations-grid");

        for (SampleData.Integration integration : SampleData.integrations()) {
            grid.add(createIntegrationCard(integration));
        }
        return grid;
    }

    /**
     * Card representing a single third-party integration with a toggle and configure button.
     */
    private Card createIntegrationCard(SampleData.Integration integration) {
        Span name = new Span(integration.name());
        name.addClassName("integration-name");

        Span description = new Span(integration.description());
        description.addClassName("integration-description");

        Div info = new Div(name, description);
        info.addClassName("integration-info");

        Checkbox checkbox = new Checkbox(integration.enabled());
        checkbox.addValueChangeListener(ev -> Notifications.show(
                ev.getValue() ? integration.name() + " enabled" : integration.name() + " disabled"));
        checkbox.setAriaLabel("Enable " + integration.name());
        checkbox.setTooltipText("Enable " + integration.name());

        Button configure = new Button("Configure", e -> Notifications.show("Integration configuration is not yet implemented"));

        Div row = new Div(checkbox, info, configure);
        row.addClassName("integration-row");

        Card card = new Card();
        card.add(row);
        return card;
    }

}
