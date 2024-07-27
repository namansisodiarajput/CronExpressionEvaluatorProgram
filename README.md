# Cron Expression Parser

This project implements a parser for cron expressions, allowing users to input a cron string and receive a detailed breakdown of its schedule.

## Supported Cron Expression Format

This parser supports the standard cron format with five time fields plus a command:

```
┌───────────── minute (0 - 59)
│ ┌───────────── hour (0 - 23)
│ │ ┌───────────── day of month (1 - 31)
│ │ │ ┌───────────── month (1 - 12)
│ │ │ │ ┌───────────── day of week (1 - 7) (Sunday = 7)
│ │ │ │ │
│ │ │ │ │
* * * * *
```

### Supported Special Characters

- `*`: Matches all values in the field
- `,`: Separates multiple values (e.g., `1,3,5`)
- `-`: Specifies a range of values (e.g., `1-5`)
- `/`: Specifies step values (e.g., `*/15` for every 15 units)

Note: Complex combinations like `1-6/2` are not currently supported.

## Project Structure

- `CronExpression`: Represents and validates a cron expression
- `CronExpressionParser`: Parses a `CronExpression` into concrete field values
- `CronFieldParser`: Parses individual cron fields
- `CronPrinter`: Outputs the parsed cron data in a formatted manner
- `CronField`: Enum representing different fields in a cron expression
- `ExpressionType`: Enum for different types of cron field expressions
- `CronExpressionHandler`: Main class for handling user input and running tests

## How to Compile and Run

1. Compile the Java files: javac CronExpressionHandler.java
2. Run the program with a cron expression as an argument: java CronExpressionHandler "* 0 1-15 0 1-5 /usr/bin/find"


## Test Cases and Results

The project includes a set of test cases to verify the parser's functionality. Here are some example outputs:

```
==================================================
Input: */15 0 1,15 * 1-5 /usr/bin/find
--------------------------------------------------
Output:
minute        0 15 30 45
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find
==================================================

==================================================
Input: * * * * * /usr/bin/run-everytime
--------------------------------------------------
Output:
minute        0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59
hour          0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5 6 7
command       /usr/bin/run-everytime
==================================================

==================================================
Input: 0 0 1 1 * /usr/bin/backup
--------------------------------------------------
Output:
minute        0
hour          0
day of month  1
month         1
day of week   1 2 3 4 5 6 7
command       /usr/bin/backup
==================================================

==================================================
Input: 0 12 * * 1-5 /usr/bin/lunchtime
--------------------------------------------------
Output:
minute        0
hour          12
day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/lunchtime
==================================================

==================================================
Input: 0 0 * * 0 /usr/bin/weekly-report
--------------------------------------------------
Output:
minute        0
hour          0
day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   0
command       /usr/bin/weekly-report
==================================================

==================================================
Input: */5 8-17 * * 1-5 /usr/bin/workday-check
--------------------------------------------------
Output:
minute        0 5 10 15 20 25 30 35 40 45 50 55
hour          8 9 10 11 12 13 14 15 16 17
day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/workday-check
==================================================

==================================================
Input: 0 22 * * 1-5 /usr/bin/goodnight
--------------------------------------------------
Output:
minute        0
hour          22
day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/goodnight
==================================================

==================================================
Input: 30 9 1 1-6/2 * /usr/bin/bimonthly-task
--------------------------------------------------
ERROR: Failed to parse expression: Invalid expression for month: 1-6/2
==================================================

==================================================
Input: 0 0 1,15,30 * * /usr/bin/important-task
--------------------------------------------------
Output:
minute        0
hour          0
day of month  1 15 30
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5 6 7
command       /usr/bin/important-task
==================================================

==================================================
Input: */10 * * * * /usr/bin/frequent-check
--------------------------------------------------
Output:
minute        0 10 20 30 40 50
hour          0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5 6 7
command       /usr/bin/frequent-check
==================================================

==================================================
Input: 5 0 * 8 * /usr/bin/august-daily
--------------------------------------------------
Output:
minute        5
hour          0
day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
month         8
day of week   1 2 3 4 5 6 7
command       /usr/bin/august-daily
==================================================
```


## Approach

The parser follows these main steps:

1. Validate the input cron string format
2. Parse each field of the cron expression
3. Generate concrete values for each field based on the parsed expression
4. Format and display the results

The project uses object-oriented principles to separate concerns:
- `CronExpression` handles initial validation
- `CronExpressionParser` orchestrates the parsing process
- `CronFieldParser` handles the detailed parsing of each field
- `CronPrinter` formats and displays the results

## Limitations and Future Improvements

- The parser does not currently support complex combinations like `1-6/2`,...etc
- Doesn't support special character like `#`, `?` .... etc
- Error handling could be improved with more specific error messages
- Support for special strings like `@yearly`, `@monthly`, etc., could be added
