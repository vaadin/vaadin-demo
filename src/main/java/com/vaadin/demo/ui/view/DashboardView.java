package com.vaadin.demo.ui.view;

import com.vaadin.demo.data.SampleData;
import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.component.SourceViewerDialog;
import com.vaadin.demo.ui.component.View;
import com.vaadin.demo.ui.component.ViewHeader;
import com.vaadin.demo.ui.util.Aura;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Notifications;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.time.format.DateTimeFormatter;


@Route("")
@RouteAlias("dashboard")
@PageTitle("Dashboard — Vaadin Demo")
public class DashboardView extends View {

    public DashboardView(SourceService sourceService) {
        addClassName("dashboard-view");

        ViewHeader header = createHeader(sourceService);
        Div content = createContent();

        add(header, content);
    }

    /**
     * Main content area containing the stat cards and recent orders card.
     */
    private Div createContent() {
        Div content = new Div(createStatCards(), createOrdersCard());
        content.addClassName("dashboard-content");
        return content;
    }

    /**
     * Page header with drawer toggle, title, and source viewer button.
     */
    private ViewHeader createHeader(SourceService sourceService) {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addThemeVariants(ButtonVariant.TERTIARY);

        H1 title = new H1("Dashboard");

        Button viewSource = new Button(Lucide.CODE.create(), e -> new SourceViewerDialog(DashboardView.class, sourceService).open());
        viewSource.addThemeVariants(ButtonVariant.TERTIARY);
        viewSource.setAriaLabel("View source");
        viewSource.setTooltipText("View source");

        return new ViewHeader(toggle, title, viewSource);
    }

    /**
     * Row of KPI stat cards.
     */
    private Div createStatCards() {
        Div cards = new Div(
                createStatCard("Total Revenue", "$128,430", "+12.5%", true),
                createStatCard("Active Users", "3,842", "+4.2%", true),
                createStatCard("New Orders", "284", "+8.1%", true),
                createStatCard("Churn Rate", "2.4%", "-0.3%", false)
        );
        cards.addClassName("dashboard-stat-cards");
        return cards;
    }

    /**
     * Card containing the recent orders grid.
     */
    private Card createOrdersCard() {
        Card card = new Card();
        card.add(createOrdersGrid());
        card.addClassNames(Aura.SURFACE_SOLID, "dashboard-orders-card");
        card.setHeader(createOrdersHeading());
        card.setHeaderSuffix(createOrdersMenuBar());
        return card;
    }

    /**
     * Heading for the orders section.
     */
    private H2 createOrdersHeading() {
        H2 heading = new H2("Recent Orders");
        heading.addClassName("dashboard-orders-heading");
        return heading;
    }

    /**
     * Menu bar with export and refresh actions for the orders card.
     */
    private MenuBar createOrdersMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Export", e -> Notifications.show("Export started — your file will be ready shortly"));
        menuBar.addItem("Refresh", e -> Notifications.show("Data refreshed", NotificationVariant.SUCCESS));
        return menuBar;
    }

    /**
     * Grid listing recent orders with ID, customer, amount, status, and date columns.
     */
    private Grid<SampleData.Order> createOrdersGrid() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        Grid<SampleData.Order> grid = new Grid<>(SampleData.Order.class, false);

        grid.addColumn(SampleData.Order::id).setHeader("Order ID").setAutoWidth(true).setFlexGrow(0).setSortable(true);
        grid.addColumn(SampleData.Order::customer).setHeader("Customer").setFlexGrow(1).setSortable(true);
        grid.addColumn(SampleData.Order::amount).setHeader("Amount").setAutoWidth(true).setFlexGrow(0);
        grid.addComponentColumn(o -> createStatusBadge(o.status())).setHeader("Status").setWidth("7rem").setFlexGrow(0);
        grid.addColumn(o -> o.date().format(dateTimeFormatter)).setHeader("Date").setAutoWidth(true).setFlexGrow(0).setSortable(true);

        grid.addThemeVariants(GridVariant.NO_BORDER);
        grid.setSelectionMode(SelectionMode.NONE);
        grid.setHeightFull();
        grid.setItems(SampleData.orders());
        return grid;
    }

    /**
     * Single KPI card showing a metric value with a trend badge.
     */
    private Card createStatCard(String label, String value, String trend, boolean favorable) {
        Span valueSpan = new Span(value);
        valueSpan.addClassName("dashboard-stat-value");

        Badge trendBadge = new Badge(trend);
        trendBadge.addClassName("dashboard-stat-trend");
        trendBadge.addThemeVariants(favorable ? BadgeVariant.SUCCESS : BadgeVariant.ERROR);
        trendBadge.setIcon(favorable ? VaadinIcon.TRENDING_UP.create() : VaadinIcon.TRENDING_DOWN.create());

        Card card = new Card();
        card.add(valueSpan, trendBadge);
        card.addClassName("dashboard-stat-card");
        card.setTitle(label);
        return card;
    }

    /**
     * Badge indicating the fulfillment status of an order.
     */
    private Badge createStatusBadge(String status) {
        Badge badge = new Badge(status);
        BadgeVariant variant = switch (status) {
            case "Completed" -> BadgeVariant.SUCCESS;
            case "Processing" -> BadgeVariant.FILLED;
            case "Failed" -> BadgeVariant.ERROR;
            case "Pending" -> BadgeVariant.CONTRAST;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
        badge.addThemeVariants(variant);
        return badge;
    }

}
