package com.vaadin.demo.data;

import com.vaadin.demo.data.SampleData.Order;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Aggregates a list of orders into the figures shown on the dashboard.
 * <p>
 * The seven most recent days present in the data form the current period, and the
 * seven days before that the comparison period, so every trend shown in the UI is
 * derived from the data rather than hard-coded.
 */
public final class OrderMetrics {

    private static final int PERIOD_DAYS = 7;
    private static final List<String> STATUSES = List.of("Completed", "Processing", "Pending", "Failed");

    private final List<Order> all;
    private final List<Order> current;
    private final List<Order> previous;

    public OrderMetrics(List<Order> orders) {
        LocalDate latest = orders.stream()
                .map(Order::date)
                .max(Comparator.naturalOrder())
                .orElseGet(LocalDate::now);
        LocalDate currentStart = latest.minusDays(PERIOD_DAYS - 1L);
        LocalDate previousStart = currentStart.minusDays(PERIOD_DAYS);

        this.all = orders;
        this.current = between(orders, currentStart, latest);
        this.previous = between(orders, previousStart, currentStart.minusDays(1));
    }

    /**
     * Key figures for the current period, each with its change against the previous one.
     */
    public List<Metric> metrics() {
        double revenue = revenue(current);
        double failed = revenue(withStatus(current, "Failed"));

        return List.of(
                new Metric("Revenue", money(revenue), change(revenue, revenue(previous)), true),
                new Metric("Orders", String.valueOf(current.size()), change(current.size(), previous.size()), true),
                new Metric("Average order", money(average(current)), change(average(current), average(previous)), true),
                new Metric("Failed value", money(failed), change(failed, revenue(withStatus(previous, "Failed"))), false)
        );
    }

    /**
     * Share of all orders per fulfillment status, most common first.
     */
    public List<StatusShare> statusBreakdown() {
        return STATUSES.stream()
                .map(status -> {
                    List<Order> matching = withStatus(all, status);
                    double share = all.isEmpty() ? 0 : (double) matching.size() / all.size();
                    return new StatusShare(status, matching.size(), share, revenue(matching));
                })
                .sorted(Comparator.comparingInt(StatusShare::count).reversed())
                .toList();
    }

    /**
     * Number of days each period covers.
     */
    public int periodDays() {
        return PERIOD_DAYS;
    }

    /**
     * Formats an amount the way the sample data spells it out.
     */
    public static String money(double amount) {
        return String.format("$%,.2f", amount);
    }

    private static List<Order> between(List<Order> orders, LocalDate from, LocalDate to) {
        return orders.stream()
                .filter(order -> !order.date().isBefore(from) && !order.date().isAfter(to))
                .toList();
    }

    private static List<Order> withStatus(List<Order> orders, String status) {
        return orders.stream().filter(order -> order.status().equals(status)).toList();
    }

    private static double revenue(List<Order> orders) {
        return orders.stream().mapToDouble(Order::amountValue).sum();
    }

    private static double average(List<Order> orders) {
        return orders.isEmpty() ? 0 : revenue(orders) / orders.size();
    }

    private static double change(double current, double previous) {
        return previous == 0 ? 0 : (current - previous) / previous * 100;
    }

    /**
     * A single key figure with its period-over-period change.
     */
    public record Metric(String label, String value, double change, boolean higherIsBetter) {

        /**
         * Whether the change moves in the direction that is good for this figure.
         */
        public boolean favorable() {
            return change == 0 || change > 0 == higherIsBetter;
        }

        public String changeText() {
            return String.format("%+.1f%%", change);
        }
    }

    /**
     * The portion of all orders carrying a given status.
     */
    public record StatusShare(String status, int count, double share, double value) {

        public String shareText() {
            return String.format("%.0f%%", share * 100);
        }
    }
}
