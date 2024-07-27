import java.util.*;

class CronExpression {
    private final Map<CronField, String> fields = new EnumMap<>(CronField.class);
    private final String command;

    // Only support 5 filed of cron expression + command
    private final static int EXPRESSION_LENGTH = 6;

    // Basic pattern: Allows numbers, commas, hyphens, forward slashes, and wildcards
    private final static String CRON_PATTERN = "^[\\d,\\-\\*\\/]+$";


    /**
     * Constructs a CronExpression object from a cron string.
     * The cron string should contain 5 time fields followed by a command.
     *
     * @param cronString The cron string to parse
     * @throws IllegalArgumentException if the cron string is invalid
     */
    public CronExpression(String cronString) {
        String[] parts = cronString.split("\\s+");
        if (parts.length != EXPRESSION_LENGTH) {
            throw new IllegalArgumentException("Invalid cron string. Expected 6 fields.");
        }

        for (int i = 0; i < EXPRESSION_LENGTH - 1; i++) {
            CronField field = CronField.values()[i];
            String expression = parts[i];
            validateField(field, expression);
            fields.put(field, expression);
        }
        command = parts[5];
    }

    /**
     * Validates a single cron field expression.
     *
     * @param field The CronField enum representing the field type
     * @param expression The expression to validate
     * @throws IllegalArgumentException if the expression is invalid
     */
    private void validateField(CronField field, String expression) {
        if (!isValidExpression(field, expression)) {
            throw new IllegalArgumentException("Invalid expression for " + field.getName() + ": " + expression);
        }
    }

    /**
     * Checks if the given expression is valid for the specified field.
     *
     * @param field The CronField enum representing the field type
     * @param expression The expression to validate
     * @return true if the expression is valid, false otherwise
     */
    private boolean isValidExpression(CronField field, String expression) {
        // Basic pattern: Allows numbers, commas, hyphens, forward slashes, and wildcards
        if (!expression.matches(CRON_PATTERN)) {
            return false;
        }

        // Split the expression into sub-expressions using comma as the delimiter
        String[] subExpressions = expression.split(",");

        // Iterate through each sub-expression
        for (String subExpr : subExpressions) {
            // Validate each sub-expression individually
            if (!isValidSubExpression(field, subExpr)) {
                // If any sub-expression is invalid, the entire expression is invalid
                return false;
            }
        }

        // All sub-expressions are valid
        return true;
    }

    /**
     * Validates a sub-expression based on its type (wildcard, step, range, or single value).
     *
     * @param field The CronField enum representing the field type
     * @param subExpr The sub-expression to validate
     * @return true if the sub-expression is valid, false otherwise
     */
    private boolean isValidSubExpression(CronField field, String subExpr) {

        return switch (ExpressionType.getExpressionType(subExpr)) {
            case ExpressionType.WILDCARD -> true;
            case ExpressionType.STEP -> isValidStepExpression(field, subExpr);
            case ExpressionType.RANGE -> isValidRangeExpression(field, subExpr);
            case ExpressionType.SINGLE -> isValidSingleValue(field, subExpr);
        };
    }

    /**
     * Validates a step expression (e.g., "[wildcard]/15" or "5/15").
     * Note: [wildcard] represents the asterisk symbol in cron expressions.
     *
     * @param field The CronField enum representing the field type
     * @param expression The step expression to validate
     * @return true if the step expression is valid, false otherwise
     */
    private boolean isValidStepExpression(CronField field, String expression) {

        // support only 2 length for increment exp
        String[] parts = expression.split("/");
        if (parts.length != 2) {
            return false;
        }

        // start & step part e.g. */15 run every 15
        String start = parts[0];
        String step = parts[1];

        // start can be only * or valid numeric value
        if (!start.equals("*") && !isValidSingleValue(field, start)) {
            return false;
        }

        // end can only be valid numeric value
        return isValidSingleValue(field, step);
    }

    /**
     * Validates a range expression (e.g., "1-5").
     *
     * @param field The CronField enum representing the field type
     * @param expression The range expression to validate
     * @return true if the range expression is valid, false otherwise
     */
    private boolean isValidRangeExpression(CronField field, String expression) {

        // support only 2 length for range exp
        String[] parts = expression.split("-");
        if (parts.length != 2) {
            return false;
        }

        // both should be a valid numeric value
        return isValidSingleValue(field, parts[0]) && isValidSingleValue(field, parts[1]);
    }

    /**
     * Validates a single numeric value for a field.
     *
     * @param field The CronField enum representing the field type
     * @param value The value to validate
     * @return true if the value is valid for the field, false otherwise
     */
    private boolean isValidSingleValue(CronField field, String value) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue >= 0 && intValue <= field.getMaxValue();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Gets the expression for a specific cron field.
     *
     * @param field The CronField enum representing the field type
     * @return The expression for the specified field
     */
    public String getFieldExpression(CronField field) {
        return fields.get(field);
    }

    /**
     * Gets the command part of the cron expression.
     *
     * @return The command string
     */
    public String getCommand() {
        return command;
    }
}