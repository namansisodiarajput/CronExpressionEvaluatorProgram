import java.util.*;

public class CronExpressionHandler {

    private static boolean TEST_ENABLED = true;

    private static final List<String> TEST_CRON_EXPRESSIONS = Arrays.asList(
            "*/15 0 1,15 * 1-5 /usr/bin/find",
            "* * * * * /usr/bin/run-everytime",
            "0 0 1 1 * /usr/bin/backup",
            "0 12 * * 1-5 /usr/bin/lunchtime",
            "0 0 * * 0 /usr/bin/weekly-report",
            "*/5 8-17 * * 1-5 /usr/bin/workday-check",
            "0 22 * * 1-5 /usr/bin/goodnight",
            "30 9 1 1-6/2 * /usr/bin/bimonthly-task",
            "0 0 1,15,30 * * /usr/bin/important-task",
            "*/10 * * * * /usr/bin/frequent-check",
            "5 0 * 8 * /usr/bin/august-daily"
    );


    public static void main(String[] args) {

        /**
         * RUN Test Input or provide manual input
         *
         * command to run from terminal for user input
         * javac CronExpressionHandler.java
         * java CronExpressionHandler "* 0 1-15 0 1-5 /usr/bin/find"
         */

        if(TEST_ENABLED) {
            runTests();
            return;
        }

        processCronExpression(args);
    }

    private static void processCronExpression(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide a single cron string as an argument.");
            return;
        }

        /**
         *
         * To Extend the code you can tune these parameters config according to need
         *
         * 1. ExpressionType - add a new filed
         * 2. CRON_PATTERN (CronExpression.class)  - configure according to the pattern of cron
         * 3. EXPRESSION_LENGTH (CronExpression.class) - max length of cron
         * 4. isValidSubExpression (CronExpression.class) - add a new validation or update existing validation
         * 5. parse (CronFieldParser.class) - add a new parsing logic or update existing parsing
         */

        try {
            // Step 1: Create and validate CronExpression
            CronExpression cronExpression = new CronExpression(args[0]);

            // Step 2 & 3: Parse the CronExpression
            CronExpressionParser parser = new CronExpressionParser();
            Map<CronField, List<Integer>> parsedData = parser.parse(cronExpression);

            // Step 4 & 5: Print the parsed data
            CronPrinter printer = new CronPrinter();
            printer.print(parsedData, cronExpression.getCommand());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void runTests() {
        CronExpressionParser parser = new CronExpressionParser();
        CronPrinter printer = new CronPrinter();

        for (String cronString : TEST_CRON_EXPRESSIONS) {
            System.out.println("=".repeat(50));
            System.out.println("Input: " + cronString);
            System.out.println("-".repeat(50));

            try {
                CronExpression cronExpression = new CronExpression(cronString);
                Map<CronField, List<Integer>> parsedData = parser.parse(cronExpression);

                System.out.println("Output:");
                printer.print(parsedData, cronExpression.getCommand());

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: Failed to parse expression: " + e.getMessage());
            }

            System.out.println("=".repeat(50));
            System.out.println(); // Add a blank line between expressions
        }
    }
}
