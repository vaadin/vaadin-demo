package com.vaadin.demo.ui.view;

import com.vaadin.demo.data.SampleData;
import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.component.SourceViewerDialog;
import com.vaadin.demo.ui.component.View;
import com.vaadin.demo.ui.component.ViewHeader;
import com.vaadin.demo.ui.util.Aura;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Notifications;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataView;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route("users")
@PageTitle("Users — Vaadin Demo")
public class UsersView extends View {

    public UsersView(SourceService sourceService) {
        addClassNames(Aura.SURFACE_SOLID, "users-view");

        ViewHeader header = createHeader(sourceService);
        Grid<SampleData.User> grid = createUsersGrid();

        add(header, grid);
    }

    /**
     * Page header with drawer toggle, title, invite user button, and source viewer button.
     */
    private ViewHeader createHeader(SourceService sourceService) {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addThemeVariants(ButtonVariant.TERTIARY);

        H1 title = new H1("Users");

        Button inviteUser = new Button("Invite User", Lucide.MAIL_PLUS.create(), e -> Notifications.show("Invite not yet implemented"));
        inviteUser.addThemeVariants(ButtonVariant.PRIMARY);

        Button viewSource = new Button(Lucide.CODE.create(), e -> new SourceViewerDialog(UsersView.class, sourceService).open());
        viewSource.addThemeVariants(ButtonVariant.TERTIARY);
        viewSource.setAriaLabel("View source");
        viewSource.setTooltipText("View source");

        return new ViewHeader(toggle, title, inviteUser, viewSource);
    }

    /**
     * Grid listing users with name, email, role, last login, and actions columns.
     */
    private Grid<SampleData.User> createUsersGrid() {
        Grid<SampleData.User> grid = new Grid<>(SampleData.User.class, false);
        grid.addThemeVariants(GridVariant.NO_BORDER);
        grid.setSelectionMode(SelectionMode.NONE);

        var nameCol = grid.addComponentColumn(user -> {
                    Avatar avatar = new Avatar(user.name());
                    avatar.addThemeVariants(AvatarVariant.LARGE);
                    avatar.setAbbreviation(user.initials());

                    Div div = new Div(avatar, new Text(user.name()));
                    div.addClassName("user-name-cell");
                    return div;
                }).setComparator(u -> u.name())
                .setHeader("Name")
                .setResizable(true);

        var emailCol = grid.addColumn(SampleData.User::email)
                .setHeader("Email")
                .setResizable(true)
                .setSortable(true);

        var roleCol = grid.addComponentColumn(user -> createRoleBadge(user.role()))
                .setAutoWidth(true)
                .setComparator(u -> u.role())
                .setFlexGrow(0)
                .setHeader("Role");

        var lastLoginCol = grid.addColumn(SampleData.User::lastLogin)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setHeader("Last Login")
                .setSortable(true);

        var actionsCol = grid.addComponentColumn(user -> {
                    Button edit = new Button(Lucide.SQUARE_PEN.create(), e -> openUserDialog(user));
                    edit.addThemeVariants(ButtonVariant.TERTIARY);
                    edit.setAriaLabel("Edit");
                    edit.setTooltipText("Edit");
                    return edit;
                })
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setFrozenToEnd(true);

        var dataView = grid.setItems(SampleData.users());

        appendFilterRow(grid, dataView, nameCol, emailCol, roleCol, lastLoginCol, actionsCol);
        return grid;
    }

    /**
     * Appends a header filter row to the grid wired to the given data view.
     */
    private void appendFilterRow(
            Grid<SampleData.User> grid,
            ListDataView<SampleData.User, ?> dataView,
            Grid.Column<SampleData.User> nameCol,
            Grid.Column<SampleData.User> emailCol,
            Grid.Column<SampleData.User> roleCol,
            Grid.Column<SampleData.User> lastLoginCol,
            Grid.Column<SampleData.User> actionsCol) {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField nameFilter = new TextField();
        nameFilter.setClearButtonVisible(true);
        nameFilter.setPlaceholder("Filter name...");
        nameFilter.setWidthFull();
        filterRow.getCell(nameCol).setComponent(nameFilter);

        TextField emailFilter = new TextField();
        emailFilter.setClearButtonVisible(true);
        emailFilter.setPlaceholder("Filter email...");
        emailFilter.setWidthFull();
        filterRow.getCell(emailCol).setComponent(emailFilter);

        Select<String> roleFilter = new Select<>();
        roleFilter.setItems("", "Admin", "Billing", "Developer", "Viewer");
        roleFilter.setPlaceholder("All roles");
        roleFilter.setWidthFull();
        filterRow.getCell(roleCol).setComponent(roleFilter);

        filterRow.getCell(lastLoginCol).setText("");
        filterRow.getCell(actionsCol).setText("");

        Runnable applyFilter = () -> dataView.setFilter(u ->
                (nameFilter.getValue().isBlank() || u.name().toLowerCase().contains(nameFilter.getValue().toLowerCase())) &&
                        (emailFilter.getValue().isBlank() || u.email().toLowerCase().contains(emailFilter.getValue().toLowerCase())) &&
                        (roleFilter.getValue() == null || roleFilter.getValue().isBlank() || roleFilter.getValue().equals(u.role())));

        nameFilter.addValueChangeListener(e -> applyFilter.run());
        emailFilter.addValueChangeListener(e -> applyFilter.run());
        roleFilter.addValueChangeListener(e -> applyFilter.run());
    }

    /**
     * Badge indicating the role of a user.
     */
    private Badge createRoleBadge(String role) {
        Badge badge = new Badge(role);
        switch (role) {
            case "Admin" -> badge.addThemeVariants(BadgeVariant.ERROR);
            case "Billing" -> badge.addThemeVariants(BadgeVariant.CONTRAST);
            case "Developer" -> badge.addThemeVariants(BadgeVariant.SUCCESS);
        }
        return badge;
    }

    /**
     * Opens a dialog to edit an existing user's role.
     */
    private void openUserDialog(SampleData.User user) {
        Div avatarRow = createAvatarRow(user);

        Select<String> role = new Select<>("Role");
        role.setItems("Admin", "Billing", "Developer", "Viewer");
        role.setValue(user.role());

        Span lastLogin = new Span("Last login: " + user.lastLogin());
        lastLogin.addClassName("user-last-login");

        Div content = new Div(avatarRow, role, lastLogin);
        content.addClassName("user-dialog-content");

        Dialog dialog = new Dialog(content);
        dialog.addClassName("users-view");
        dialog.setHeaderTitle("Edit User");
        dialog.setWidth("400px");

        Button save = new Button("Save Changes", e -> {
            dialog.close();
            Notifications.show(user.name() + "'s role updated to " + role.getValue(), NotificationVariant.SUCCESS);
        });
        save.addThemeVariants(ButtonVariant.PRIMARY);

        Button cancel = new Button("Cancel", e -> dialog.close());
        cancel.addThemeVariants(ButtonVariant.TERTIARY);

        dialog.getFooter().add(cancel, save);
        dialog.open();
    }

    /**
     * Avatar, name, and email row shown at the top of the user dialog.
     */
    private Div createAvatarRow(SampleData.User user) {
        Avatar avatar = new Avatar(user.name());
        avatar.addThemeVariants(AvatarVariant.XLARGE);
        avatar.setAbbreviation(user.initials());

        H3 nameSpan = new H3(user.name());

        Span emailSpan = new Span(user.email());
        emailSpan.addClassName("user-dialog-email");

        Div userInfo = new Div(nameSpan, emailSpan);
        userInfo.addClassName("user-dialog-info");

        Div avatarRow = new Div(avatar, userInfo);
        avatarRow.addClassName("user-dialog-avatar-row");
        return avatarRow;
    }

}
