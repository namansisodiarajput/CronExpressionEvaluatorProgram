import java.util.*;

/**
 * This class is responsible for parsing individual cron field expressions
 * and generating a list of integer values that match the expression.
 */
public class CronFieldParser {

    /**
     * Parses a cron field expression and returns a sorted list of matching integer values.
     *
     * @param fieldExpression The cron field expression to parse
     * @param field Enum consisting of all the possible character
     * @return A sorted list of integer values matching the expression
     */
    public List<Integer> parse(String fieldExpression, CronField field) {

        List<Integer> values = new ArrayList<>();
        String[] parts = fieldExpression.split(",");

        for (String part : parts) {
            switch (ExpressionType.getExpressionType(part)) {
                case STEP -> addStepValues(values, part, field);
                case RANGE -> addRangeValues(values, part);
                case WILDCARD -> addAllValues(values, field);
                case SINGLE -> values.add(Integer.parseInt(part));
            }
        }

        Collections.sort(values);
        return values;
    }

    /**
     * Adds values to the list based on a step expression.
     *
     * @param values The list to add values to
     * @param part The step expression part (e.g., "[wildcard]/15" or "5/15")
     * @param field Enum consisting of all the possible character
     */
    private void addStepValues(List<Integer> values, String part, CronField field) {

        String[] stepParts = part.split("/");

        // Determine the start value:
        // If it's "*", start from 0; otherwise, parse the start value
        int start = stepParts[0].equals("*") ? 0 : Integer.parseInt(stepParts[0]);

        // Parse the step value
        int step = Integer.parseInt(stepParts[1]);

        // Generate and add values from start to maxValue, incrementing by step
        for (int i = start; i <= field.getMaxValue(); i += step) {
            values.add(i);
        }

    }

    /**
     * Adds values to the list based on a range expression.
     *
     * @param values The list to add values to
     * @param part The range expression part (e.g., "1-5")
     */
    private void addRangeValues(List<Integer> values, String part) {
        // Split the range expression into start and end values
        String[] rangeParts = part.split("-");

        // Parse the start value of the range
        int start = Integer.parseInt(rangeParts[0]);

        // Parse the end value of the range
        int end = Integer.parseInt(rangeParts[1]);

        // Generate and add all values from start to end, inclusive
        for (int i = start; i <= end; i++) {
            values.add(i);
        }
    }

    /**
     * Adds all values from 0 to maxValue to the list.
     *
     * @param values The list to add values to
     * @param field Enum consisting of all the possible character
     */
    private void addAllValues(List<Integer> values, CronField field) {
        for (int i = field.getMinValue(); i <= field.getMaxValue(); i++) {
            values.add(i);
        }
    }
}