import java.util.*;

/**
 * This class is responsible for printing the parsed cron expression data
 * in a formatted table-like output.
 */
public class CronPrinter {

    /**
     * Prints the parsed cron expression data in a formatted manner.
     *
     * @param parsedData A map containing the parsed values for each cron field
     * @param command The command string associated with the cron expression
     */
    public void print(Map<CronField, List<Integer>> parsedData, String command) {
        // Iterate through all cron fields
        for (CronField field : CronField.values()) {
            // Print the field name, left-aligned in a 14-character wide column
            System.out.printf("%-14s", field.getName());

            // Check if the current field is not the COMMAND field
            if (field != CronField.COMMAND) {
                // Get the list of values for the current field
                List<Integer> values = parsedData.get(field);

                // Print each value in the list, separated by spaces
                for (int value : values) {
                    System.out.print(value + " ");
                }
            } else {
                // If it's the COMMAND field, print the command string
                System.out.print(command);
            }

            // Move to the next line after printing each field's data
            System.out.println();
        }
    }
}