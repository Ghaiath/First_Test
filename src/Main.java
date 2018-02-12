import javax.naming.OperationNotSupportedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Group assignment - Calculator
 *
 * This application will calculate expressions of the form "operand1 operator operand2", where operator is +,-,', or %.
 * The application supports "interactive mode" as well as "argument mode".
 *
 * Argument mode:
 * 		The application is run with the expression to calculate as argument (java calc 1+2).
 * 		The application will evaluate the expression, output result and terminate.

 * Interactive mode:
 * 		The application is run without arguments passed to it (java calc).
 * 		The application will repeatedly evaluate expressions the user enters until the user enters "quit" to terminate.
 *
 *
 */
public class Main {
	/* This is a compiled regular expression that is used to verify and extract data from an expression of the form
	* "operand1 operator operand2" (where operand is a Double and operator is +,-,*, or /).
	* The regex has 3 capture groups (operand1, operator, operand2).
	* For example:
	* 1.3 * 4.3 is a valid match for this expression, the capture groups will hold:
	* group 1: 1.3
	* group 2: *
	* group 3: 4.3
	*
	* Unescaped form of the regex is: ^\s*(\d+\.?\d*)\s*([\+\-\*\/])\s*(\d+\.?\d*)\s*$
	*/
	private static final Pattern pattern = Pattern.compile("^\\s*(\\d+(?:\\.\\d+)?)\\s*([\\+\\-\\*\\/])\\s*(\\d+(?:\\.\\d+)?)\\s*$");
	private static final math__operation mathOperation = new math__operation();
	private static final String INTERACTIVE_MODE_USAGE = "Enter expression you wish to calculate or \"quit\" to terminate the application.\n" +
														 "Supported expression are those of the form \"operand operator operand\" where\n" +
													     "operand is any of the four basic math operations (+,-,% and *).";

	private static final String COMMAND_LINE_MODE_USAGE = "USAGE:\n\tjava -jar calc.jar <expression>\n\nExample:\n\t> java -jar calc.jar 1 + 3\n\n" +
														  "Supported expression are those of the form \"operand operator operand\" where\n" +
														  "operand is any of the four basic math operations (+,-,% and *).";


	/**
	 * Evaluates a expression of the form: "operand1 operator operand2" (where operand is a Double and operator is +,-,*, or /).
	 * The evaluation is done using regex and capture groups, see the comment for the static field "pattern" for more
	 * details.
	 *
	 * @param expression A string representing the expression to evaluate
	 * @return The result of the evaluated expression
	 * @throws OperationNotSupportedException Is thrown if the expression is not of valid format.
	 */
	private static Double evaluateExpression(String expression) throws OperationNotSupportedException {
		Matcher matcher = pattern.matcher(expression);

		if (matcher.matches()){
			double operand1 = Double.parseDouble(matcher.group(1));
			char operator = matcher.group(2).charAt(0);
			double operand2 = Double.parseDouble(matcher.group(3));

			switch (operator) {
				case '-':
					return mathOperation.subtract(operand1,operand2);
				case '+':
					return mathOperation.add(operand1,operand2);
				case '/':
					return mathOperation.divide(operand1,operand2);
				case '*':
					return mathOperation.multiply(operand1,operand2);
				default:
					throw new OperationNotSupportedException("The expression has invalid format");
			}
		}
		else {
			throw new OperationNotSupportedException("The expression has invalid format");
		}
	}


	/**
	 * A mode that repeatedly asks the user for an expression to evaluate.
	 * This continues until the user enters "quit". If the user inputs an invalid expression a message about
	 * usage will be displayed (defined by the static field INTERACTIVE_MODE_USAGE).
	 * @throws IOException
	 */
	private static void interactiveMode() throws IOException{
		try (InputStreamReader inputStreamReader = new InputStreamReader(System.in);
			 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

			while (true) {
				System.out.print("> ");
				String userInput = bufferedReader.readLine();

				if (userInput == null || userInput.trim().equalsIgnoreCase("quit")) // can occur if EOF is (example: CTRL+Z/CTRL+C in windows)
					return;

				try {
					System.out.println(evaluateExpression(userInput));
				} catch (OperationNotSupportedException e) {
					System.out.println(INTERACTIVE_MODE_USAGE);
				}
			}
		}
	}

	/**
	 * Entry-point for the application.
	 * @param args Arguments from commandline
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Build a expression from the arguments
		StringJoiner stringJoiner = new StringJoiner(" ");
		for (String arg : args) {
			stringJoiner.add(arg);
		}
		String concatenatedCommandLineArguments = stringJoiner.toString().trim();

		// If there was an expression passed as argument, evaluate that expression, display result and terminate.
		// Otherwise the application enters interactive mode.
		if (concatenatedCommandLineArguments.length() > 0) {
			try {
				System.out.println(evaluateExpression(concatenatedCommandLineArguments));
			} catch (OperationNotSupportedException e) {
				System.out.println(COMMAND_LINE_MODE_USAGE);
			}
		} else {
			System.out.println(INTERACTIVE_MODE_USAGE);
			interactiveMode();
		}
	}
}