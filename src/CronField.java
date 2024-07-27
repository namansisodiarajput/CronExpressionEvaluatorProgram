
/**
 * Enum representing the different fields in a cron expression.
 */
enum CronField {
    MINUTE("minute", 0, 59),
    HOUR("hour", 0, 23),
    DAY_OF_MONTH("day of month", 1, 31),
    MONTH("month", 1, 12),
    DAY_OF_WEEK("day of week", 1, 7),
    COMMAND("command", 0, 0);

    private final String name;
    private final int minValue;
    private final int maxValue;

    CronField(String name, int minValue, int maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getName() {
        return name;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }
}