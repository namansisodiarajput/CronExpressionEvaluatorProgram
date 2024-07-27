/**
 * Enum representing the different types of cron sub-expressions.
 */
public enum ExpressionType {
    WILDCARD, STEP, RANGE, SINGLE;

    /**
     * Determines the type of the given sub-expression.
     *
     * @param subExpr The sub-expression to analyze
     * @return The ExpressionType enum representing the type of the sub-expression
     */
    public static ExpressionType getExpressionType(String subExpr) {
        if (subExpr.equals("*")) return ExpressionType.WILDCARD;
        if (subExpr.contains("/")) return ExpressionType.STEP;
        if (subExpr.contains("-")) return ExpressionType.RANGE;
        return ExpressionType.SINGLE;
    }

}