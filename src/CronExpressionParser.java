import java.util.*;

/**
 * This class is responsible for parsing a CronExpression object into a map of
 * concrete integer values for each cron field.
 */
public class CronExpressionParser {

    /** The parser used for individual cron fields */
    private final CronFieldParser fieldParser;

    /**
     * Constructor initializes a new CronFieldParser instance.
     */
    public CronExpressionParser() {
        this.fieldParser = new CronFieldParser();
    }

    /**
     * Parses a CronExpression object into a map of integer values for each field.
     *
     * @param cronExpression The CronExpression object to parse
     * @return A Map where the key is the CronField and the value is a List of
     *         Integer values representing the times for that field
     */
    public Map<CronField, List<Integer>> parse(CronExpression cronExpression) {
        // Using EnumMap for enum keys
        Map<CronField, List<Integer>> parsedFields = new EnumMap<>(CronField.class);

        // Iterate through all fields in the CronExpression
        for (CronField field : CronField.values()) {
            // Skip the COMMAND field as it's not a time field
            if (field != CronField.COMMAND) {
                // Get the expression string for this field
                String expression = cronExpression.getFieldExpression(field);

                // Parse the expression into a list of integer values
                List<Integer> values = fieldParser.parse(expression, field);

                // Store the parsed values in the map
                parsedFields.put(field, values);
            }
        }

        // Return the map of parsed fields
        return parsedFields;
    }
}