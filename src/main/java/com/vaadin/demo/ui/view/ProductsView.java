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
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataView;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("products")
@PageTitle("Products — Vaadin Demo")
public class ProductsView extends View {

    public ProductsView(SourceService sourceService) {
        addClassNames(Aura.SURFACE_SOLID, "products-view");

        ViewHeader header = createHeader(sourceService);
        Grid<SampleData.Product> grid = createProductsGrid();

        add(header, grid);
    }

    /**
     * Page header with drawer toggle, title, add product button, and source viewer button.
     */
    private ViewHeader createHeader(SourceService sourceService) {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addThemeVariants(ButtonVariant.TERTIARY);

        H1 title = new H1("Products");

        Button addProduct = new Button("Add Product", Lucide.PLUS.create());
        addProduct.addThemeVariants(ButtonVariant.PRIMARY);
        addProduct.addClickListener(e -> openProductDialog(null));

        Button viewSource = new Button(Lucide.CODE.create(), e -> new SourceViewerDialog(ProductsView.class, sourceService).open());
        viewSource.addThemeVariants(ButtonVariant.TERTIARY);
        viewSource.setAriaLabel("View source");
        viewSource.setTooltipText("View source");

        return new ViewHeader(toggle, title, addProduct, viewSource);
    }

    /**
     * Grid listing products with name, category, price, stock, status, and actions columns.
     */
    private Grid<SampleData.Product> createProductsGrid() {
        Grid<SampleData.Product> grid = new Grid<>(SampleData.Product.class, false);
        grid.addThemeVariants(GridVariant.NO_BORDER);
        grid.setSelectionMode(SelectionMode.NONE);

        var nameCol = grid.addColumn(SampleData.Product::name)
                .setHeader("Name").setWidth("200px").setFlexGrow(2).setResizable(true).setSortable(true);
        var categoryCol = grid.addColumn(SampleData.Product::category)
                .setHeader("Category").setWidth("140px").setFlexGrow(1).setSortable(true);
        var priceCol = grid.addColumn(SampleData.Product::price)
                .setHeader("Price").setAutoWidth(true).setFlexGrow(0).setSortable(true);
        var stockCol = grid.addColumn(SampleData.Product::stock)
                .setHeader("Stock").setAutoWidth(true).setFlexGrow(0).setSortable(true);
        var statusCol = grid.addComponentColumn(p -> createStatusBadge(p.status()))
                .setHeader("Status").setAutoWidth(true).setFlexGrow(0);
        var actionsCol = grid.addComponentColumn(product -> {
            Button edit = new Button(Lucide.SQUARE_PEN.create(), e -> openProductDialog(product));
            edit.addThemeVariants(ButtonVariant.TERTIARY);
            edit.setAriaLabel("Edit");
            edit.setTooltipText("Edit");
            return edit;
        }).setAutoWidth(true).setFlexGrow(0).setFrozenToEnd(true);

        var dataView = grid.setItems(SampleData.products());

        appendFilterRow(grid, dataView, nameCol, categoryCol, statusCol, priceCol, stockCol, actionsCol);
        return grid;
    }

    /**
     * Appends a header filter row to the grid wired to the given data view.
     */
    private void appendFilterRow(
            Grid<SampleData.Product> grid,
            ListDataView<SampleData.Product, ?> dataView,
            Grid.Column<SampleData.Product> nameCol,
            Grid.Column<SampleData.Product> categoryCol,
            Grid.Column<SampleData.Product> statusCol,
            Grid.Column<SampleData.Product> priceCol,
            Grid.Column<SampleData.Product> stockCol,
            Grid.Column<SampleData.Product> actionsCol) {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField nameFilter = new TextField();
        nameFilter.setClearButtonVisible(true);
        nameFilter.setPlaceholder("Filter name...");
        nameFilter.setWidthFull();
        filterRow.getCell(nameCol).setComponent(nameFilter);

        Select<String> categoryFilter = new Select<>();
        categoryFilter.setItems("", "Software", "Infrastructure", "Marketing", "Sales", "Support",
                "Developer", "Security", "Data", "Compliance", "Enterprise", "Tools");
        categoryFilter.setPlaceholder("All");
        categoryFilter.setWidthFull();
        filterRow.getCell(categoryCol).setComponent(categoryFilter);

        Select<String> statusFilter = new Select<>();
        statusFilter.setItems("", "Active", "Beta", "Deprecated", "Inactive");
        statusFilter.setPlaceholder("All");
        statusFilter.setWidthFull();
        filterRow.getCell(statusCol).setComponent(statusFilter);

        filterRow.getCell(priceCol).setText("");
        filterRow.getCell(stockCol).setText("");
        filterRow.getCell(actionsCol).setText("");

        Runnable applyFilter = () -> dataView.setFilter(p ->
                (nameFilter.getValue().isBlank() || p.name().toLowerCase().contains(nameFilter.getValue().toLowerCase())) &&
                        (categoryFilter.getValue() == null || categoryFilter.getValue().isBlank() || categoryFilter.getValue().equals(p.category())) &&
                        (statusFilter.getValue() == null || statusFilter.getValue().isBlank() || statusFilter.getValue().equals(p.status())));

        nameFilter.addValueChangeListener(e -> applyFilter.run());
        categoryFilter.addValueChangeListener(e -> applyFilter.run());
        statusFilter.addValueChangeListener(e -> applyFilter.run());
    }

    /**
     * Badge indicating the status of a product.
     */
    private Badge createStatusBadge(String status) {
        Badge badge = new Badge(status);
        switch (status) {
            case "Active" -> badge.addThemeVariants(BadgeVariant.SUCCESS);
            case "Beta" -> badge.addThemeVariants(BadgeVariant.CONTRAST);
            case "Deprecated" -> badge.addThemeVariants(BadgeVariant.ERROR);
        }
        return badge;
    }

    /**
     * Opens a dialog to add a new product or edit an existing one.
     */
    private void openProductDialog(SampleData.Product product) {
        FormLayout form = createProductForm(product);

        Dialog dialog = new Dialog(form);
        dialog.setHeaderTitle(product == null ? "Add Product" : "Edit Product");
        dialog.setWidth("400px");

        Button save = new Button("Save", e -> {
            dialog.close();
            Notifications.show(product == null ? "Product created successfully" : "Product updated successfully", NotificationVariant.SUCCESS);
        });
        save.addThemeVariants(ButtonVariant.PRIMARY);

        Button cancel = new Button("Cancel", e -> dialog.close());
        cancel.addThemeVariants(ButtonVariant.TERTIARY);

        dialog.getFooter().add(cancel, save);
        dialog.open();
    }

    /**
     * Form pre-populated with the given product's values, or defaults for a new product.
     */
    private FormLayout createProductForm(SampleData.Product product) {
        TextField name = new TextField("Name");
        name.setValue(product != null ? product.name() : "");

        ComboBox<String> category = new ComboBox<>("Category");
        category.setItems("Software", "Infrastructure", "Marketing", "Sales", "Support",
                "Developer", "Security", "Data", "Compliance", "Enterprise", "Tools");
        category.setValue(product != null ? product.category() : null);

        TextField price = new TextField("Price");
        price.setValue(product != null ? product.price() : "");

        NumberField stock = new NumberField("Stock");
        stock.setValue(product != null ? (double) product.stock() : 0.0);
        stock.setStep(1);
        stock.setMin(0);

        Select<String> status = new Select<>("Status");
        status.setItems("Active", "Beta", "Deprecated", "Inactive");
        status.setValue(product != null ? product.status() : "Active");

        FormLayout form = new FormLayout();
        form.add(name, category, price, stock, status);
        return form;
    }

}
