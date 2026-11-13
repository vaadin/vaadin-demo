package com.vaadin.demo.ui.view;

import com.vaadin.demo.data.OrderMetrics;
import com.vaadin.demo.data.SampleData;
import com.vaadin.demo.data.SampleData.Order;
import com.vaadin.demo.service.SourceService;
import com.vaadin.demo.ui.component.SourceViewerDialog;
import com.vaadin.demo.ui.component.StatusBadge;
import com.vaadin.demo.ui.component.View;
import com.vaadin.demo.ui.component.ViewHeader;
import com.vaadin.demo.ui.util.Aura;
import com.vaadin.demo.ui.util.Lucide;
import com.vaadin.demo.ui.util.Notifications;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.ScrollerVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Dashboard showing key figures derived from the order data, a status breakdown,
 * and a feature-rich orders grid.
 * <p>
 * Layout is composed from Vaadin layout components; dashboard-view.css only
 * sizes and orders the main areas per screen size.
 */
@Route("")
@RouteAlias("dashboard")
@PageTitle("Dashboard — Vaadin Demo")
public class DashboardView extends View {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy");
    private static final String ROW_GAP = "var(--vaadin-gap-m)";
    private static final String LABEL_GAP = "var(--vaadin-gap-s)";
    private static final List<String> STATUSES = List.of("Completed", "Processing", "Pending", "Failed");

    private final List<Order> orders = SampleData.orders();
    private final OrderMetrics metrics = new OrderMetrics(orders);

    private final Badge selectionBadge = new Badge();
    private final Button bulkArchive = new Button("Archive");
    private final HorizontalLayout selectionBar = new HorizontalLayout(selectionBadge, bulkArchive);
    private final Map<String, Grid.Column<Order>> toggleableColumns = new LinkedHashMap<>();

    private Grid<Order> grid;
    private GridListDataView<Order> dataView;
    private FooterRow.FooterCell countCell;
    private FooterRow.FooterCell totalCell;

    public DashboardView(SourceService sourceService) {
        addClassName("dashboard-view");
        add(createHeader(sourceService), createContent());
    }

    /**
     * Page header with drawer toggle, title, and source viewer button.
     */
    private ViewHeader createHeader(SourceService sourceService) {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addThemeVariants(ButtonVariant.TERTIARY);

        H1 title = new H1("Dashboard");

        Button viewSource = new Button("View source", Lucide.CODE.create(), e -> new SourceViewerDialog(DashboardView.class, sourceService).open());
        viewSource.addThemeVariants(ButtonVariant.TERTIARY);

        return new ViewHeader(toggle, title, viewSource);
    }

    /**
     * Two columns: the orders grid and, beside it, the supporting cards. The
     * stylesheet sizes and orders the columns; everything within them is
     * composed from layout components.
     */
    private Div createContent() {
        Div content = new Div(createOrdersCard(), createSideColumn());
        content.addClassName("dashboard-content");
        return content;
    }

    /**
     * Supporting cards: the status breakdown and the key figures. The
     * stylesheet arranges them per screen size: the figures are hidden on
     * phones, side by side with the breakdown on tablet, and stacked below it
     * on desktop. The scroller only comes into play on very short windows.
     */
    private Scroller createSideColumn() {
        HorizontalLayout cards = new HorizontalLayout(createStatusBreakdownCard(), createFiguresGrid());
        cards.setPadding(false);
        cards.setWidthFull();
        cards.addClassName("dashboard-side-content");

        Scroller scroller = new Scroller(cards);
        scroller.addClassName("dashboard-side");
        scroller.addThemeVariants(ScrollerVariant.OVERFLOW_INDICATORS);
        return scroller;
    }

    /**
     * The four key figures, one per line. Hidden on phones.
     */
    private HorizontalLayout createFiguresGrid() {
        HorizontalLayout figures = new HorizontalLayout();
        figures.setSpacing(ROW_GAP);
        figures.setPadding(false);
        figures.addClassName("status-cards");

        metrics.metrics().forEach(metric -> {
            Card card = createStatCard(metric);
            card.setWidthFull();
            figures.add(card);
        });
        return figures;
    }

    /**
     * Single KPI card showing a figure with its period-over-period trend.
     */
    private Card createStatCard(OrderMetrics.Metric metric) {
        boolean favorable = metric.favorable();
        Badge trend = new Badge(metric.changeText());
        trend.addThemeVariants(favorable ? BadgeVariant.SUCCESS : BadgeVariant.ERROR);
        trend.setIcon(metric.change() >= 0 ? VaadinIcon.TRENDING_UP.create() : VaadinIcon.TRENDING_DOWN.create());
        Tooltip.forComponent(trend).setText("Compared with the previous " + metrics.periodDays() + " days");

        HorizontalLayout figure = new HorizontalLayout(new H5(metric.value()), trend);
        figure.setAlignItems(Alignment.CENTER);
        figure.setJustifyContentMode(JustifyContentMode.BETWEEN);
        figure.setPadding(false);
        // On narrow cards the trend badge drops below the value instead of overflowing
        figure.setWrap(true);
        figure.setSpacing("0.5rem");

        Card card = new Card();
        card.add(figure);
        card.setTitle(metric.label());
        return card;
    }

    /**
     * Card breaking all orders down by fulfillment status, as a share of the total.
     */
    private Card createStatusBreakdownCard() {
        VerticalLayout rows = new VerticalLayout();
        rows.setPadding(false);
        rows.setSpacing(ROW_GAP);

        metrics.statusBreakdown().forEach(share -> {
            HorizontalLayout labels = new HorizontalLayout(
                    new StatusBadge(share.status()),
                    new Span(share.count() + " orders - " + share.shareText()));
            labels.setWidthFull();
            labels.setPadding(false);
            labels.setWrap(true);
            labels.setAlignItems(Alignment.CENTER);
            labels.setJustifyContentMode(JustifyContentMode.BETWEEN);

            ProgressBar bar = new ProgressBar(0, 1, share.share());
            bar.getElement().setAttribute("aria-label", share.status() + " share of all orders");

            VerticalLayout row = new VerticalLayout(labels, bar);
            row.setPadding(false);
            row.setSpacing(LABEL_GAP);
            rows.add(row);
        });

        Card card = new Card();
        card.addClassName("status-breakdown");
        card.add(rows);
        card.setTitle("Status breakdown");
        card.setSubtitle(new Span(orders.size() + " orders in total"));
        return card;
    }

    /**
     * Card wrapping the orders grid, with the column and export menu in its header.
     */
    private Card createOrdersCard() {
        Grid<Order> ordersGrid = createOrdersGrid();

        VerticalLayout body = new VerticalLayout(createFilterBar(), createSelectionBar(), ordersGrid);
        body.setSizeFull();
        body.setPadding(false);
        body.setFlexGrow(1, ordersGrid);

        Card card = new Card();
        card.addClassNames(Aura.SURFACE_SOLID, "orders-card");
        card.setSizeFull();
        card.setTitle("Recent Orders");
        card.setHeaderSuffix(createOrdersMenuBar());
        card.add(body);
        return card;
    }

    /**
     * Filters above the grid rather than in a header row, so every column header
     * sits directly above its own data.
     */
    private HorizontalLayout createFilterBar() {
        TextField customerFilter = new TextField("Customer");
        customerFilter.setClearButtonVisible(true);
        customerFilter.setPlaceholder("Filter by name...");
        customerFilter.setPrefixComponent(Lucide.SEARCH.create());
        customerFilter.setValueChangeMode(ValueChangeMode.LAZY);

        Select<String> statusFilter = new Select<>();
        statusFilter.setLabel("Status");
        statusFilter.setItems(STATUSES);
        statusFilter.setEmptySelectionAllowed(true);
        statusFilter.setEmptySelectionCaption("All statuses");
        statusFilter.setPlaceholder("All statuses");

        Runnable applyFilter = () -> {
            String customer = customerFilter.getValue().trim().toLowerCase();
            String status = statusFilter.getValue();
            dataView.setFilter(order ->
                    (customer.isEmpty() || order.customer().toLowerCase().contains(customer))
                    && (status == null || status.equals(order.status())));
        };

        customerFilter.addValueChangeListener(e -> applyFilter.run());
        statusFilter.addValueChangeListener(e -> applyFilter.run());

        HorizontalLayout bar = new HorizontalLayout(customerFilter, statusFilter);
        bar.setPadding(false);
        bar.setWrap(true);
        bar.setAlignItems(Alignment.END);
        bar.setWidthFull();
        bar.addClassName("orders-filters");
        return bar;
    }

    /**
     * Selection count and bulk action, shown above the grid while rows are selected.
     */
    private HorizontalLayout createSelectionBar() {
        selectionBadge.addThemeVariants(BadgeVariant.CONTRAST);

        bulkArchive.setIcon(Lucide.ARCHIVE.create());
        bulkArchive.addThemeVariants(ButtonVariant.TERTIARY);
        bulkArchive.addClickListener(e -> archiveSelection());

        selectionBar.setAlignItems(Alignment.CENTER);
        selectionBar.setPadding(false);
        selectionBar.setVisible(false);
        return selectionBar;
    }

    /**
     * Menu bar toggling column visibility and offering an export action.
     */
    private MenuBar createOrdersMenuBar() {
        MenuBar menuBar = new MenuBar();

        MenuItem columns = menuBar.addItem("Columns");
        toggleableColumns.forEach((label, column) -> {
            MenuItem item = columns.getSubMenu().addItem(label,
                    e -> column.setVisible(e.getSource().isChecked()));
            item.setCheckable(true);
            item.setChecked(true);
        });

        menuBar.addItem("Export", e -> Notifications.show("Export started — your file will be ready shortly"));
        return menuBar;
    }

    /**
     * Grid of recent orders, demonstrating multi-sort, an aggregating footer,
     * row details, selection, and a context menu.
     */
    private Grid<Order> createOrdersGrid() {
        grid = new Grid<>(Order.class, false);
        grid.addThemeVariants(GridVariant.NO_BORDER, GridVariant.ROW_STRIPES);
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.setColumnReorderingAllowed(true);
        grid.setEmptyStateText("No orders match the current filters.");
        grid.setSizeFull();

        grid.addColumn(Order::id)
                .setHeader("Order ID")
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setSortable(true);

        Grid.Column<Order> customerColumn = grid.addComponentColumn(this::createCustomerCell)
                .setHeader("Customer")
                .setComparator(Order::customer)
                .setWidth("8rem")
                .setResizable(true)
                .setTooltipGenerator(order -> order.customer() + " — " + order.id());

        Grid.Column<Order> amountColumn = grid.addColumn(Order::amount)
                .setHeader("Amount")
                .setComparator(Comparator.comparingDouble(Order::amountValue))
                .setTextAlign(ColumnTextAlign.END)
                .setAutoWidth(true)
                .setFlexGrow(0);

        Grid.Column<Order> statusColumn = grid.addComponentColumn(order -> new StatusBadge(order.status()))
                .setHeader("Status")
                .setComparator(Order::status)
                .setWidth("7rem")
                .setFlexGrow(0);

        Grid.Column<Order> dateColumn = grid.addColumn(order -> order.date().format(DATE_FORMAT))
                .setHeader("Date")
                .setComparator(Order::date)
                .setAutoWidth(true)
                .setFlexGrow(0);

        grid.addComponentColumn(this::createRowActions)
                .setAutoWidth(true)
                .setFlexGrow(0);

        toggleableColumns.put("Amount", amountColumn);
        toggleableColumns.put("Status", statusColumn);
        toggleableColumns.put("Date", dateColumn);

        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::createOrderDetails));
        grid.setDetailsVisibleOnClick(false);

        dataView = grid.setItems(orders);

        addFooterRow(customerColumn, amountColumn);
        addContextMenu();

        grid.asMultiSelect().addSelectionListener(e -> updateSelection(e.getAllSelectedItems()));
        dataView.addItemCountChangeListener(e -> updateFooter());
        updateFooter();
        return grid;
    }

    /**
     * Footer row aggregating whatever the filters currently leave visible.
     */
    private void addFooterRow(Grid.Column<Order> customerColumn, Grid.Column<Order> amountColumn) {
        FooterRow footerRow = grid.appendFooterRow();
        countCell = footerRow.getCell(customerColumn);
        totalCell = footerRow.getCell(amountColumn);
    }

    /**
     * Right-click actions on a single row.
     */
    private void addContextMenu() {
        GridContextMenu<Order> contextMenu = grid.addContextMenu();
        contextMenu.addItem("Toggle details", e -> e.getItem().ifPresent(this::toggleDetails));
        contextMenu.addItem("Mark as completed", e -> e.getItem().ifPresent(
                order -> Notifications.show(order.id() + " marked as completed", NotificationVariant.SUCCESS)));
        contextMenu.addItem("Copy order ID", e -> e.getItem().ifPresent(
                order -> Notifications.show("Copied " + order.id())));
    }

    private HorizontalLayout createCustomerCell(Order order) {
        Span name = new Span(order.customer());
        name.addClassName("customer-name");

        HorizontalLayout cell = new HorizontalLayout(new Avatar(order.customer()), name);
        cell.setAlignItems(Alignment.CENTER);
        cell.setPadding(false);
        cell.setWidthFull();
        cell.addClassName("customer-cell");
        return cell;
    }

    private Button createRowActions(Order order) {
        Button toggle = new Button(Lucide.SQUARE_ARROW_OUT_UP_RIGHT.create(), e -> toggleDetails(order));
        toggle.addThemeVariants(ButtonVariant.TERTIARY);
        toggle.setAriaLabel("Toggle details");
        toggle.setTooltipText("Toggle details");
        return toggle;
    }

    /**
     * Expanded row content for a single order.
     */
    private Component createOrderDetails(Order order) {
        VerticalLayout details = new VerticalLayout(
                new Span("Customer: " + order.customer()),
                new Span("Amount: " + order.amount()),
                new Span("Status: " + order.status()),
                new Span("Ordered: " + order.date().format(DATE_FORMAT)));
        details.setPadding(false);
        details.setSpacing(LABEL_GAP);
        return details;
    }

    /**
     * Shows or hides a row's details. Both the row button and the context menu
     * go through here.
     */
    private void toggleDetails(Order order) {
        grid.setDetailsVisible(order, !grid.isDetailsVisible(order));
    }

    private void updateSelection(Set<Order> selection) {
        selectionBar.setVisible(!selection.isEmpty());
        selectionBadge.setText(selection.size() + " selected");
    }

    private void archiveSelection() {
        int count = grid.getSelectedItems().size();
        grid.deselectAll();
        Notifications.show(count + " orders archived", NotificationVariant.SUCCESS);
    }

    private void updateFooter() {
        int count = dataView.getItemCount();
        double total = dataView.getItems().mapToDouble(Order::amountValue).sum();
        countCell.setText(count + (count == 1 ? " order" : " orders"));
        totalCell.setText(OrderMetrics.money(total));
    }
}
