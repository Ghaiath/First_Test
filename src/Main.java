import javax.naming.OperationNotSupportedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	static final math__operation mathOperation = new math__operation();
	//Unescaped: ^\s*(\d+\.?\d*)\s*([\+\-\*\/])\s*(\d+\.?\d*)\s*$
	static final Pattern pattern = Pattern.compile("^\\s*(\\d+(?:\\.\\d+)?)\\s*([\\+\\-\\*\\/])\\s*(\\d+(?:\\.\\d+)?)\\s*$");
	static final String USAGE = "Usage is:\n"+
			"<operator> <operand> <operator> (example: \"2.3 + 5.7\")\n"+
			"Type \"quit\" to end the program.";


	private static Double processExpression(String expression) throws OperationNotSupportedException {
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
			}
		}

		// Stoppar man detta i en else så tror compilern att koden inte går att nå så måste göra på detta sätt.
		throw new OperationNotSupportedException("The expression has invalid format");
	}


	private static void interactiveMode() throws IOException{
		try (InputStreamReader inputStreamReader = new InputStreamReader(System.in);
			 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

			while (true) {
				System.out.print("> ");
				String userInput = bufferedReader.readLine().trim();
				if (userInput.equalsIgnoreCase("quit"))
					break;

				try {
					System.out.println(processExpression(userInput));
				} catch (OperationNotSupportedException e) {
					System.out.println(USAGE);
				}
			};
		}
	}

	public static void main(String[] args) throws IOException {
		// Lägg ihop alla argument från commandline till en sträng och se om den är > 0 i längd.
		// Isf utförs en uträkning på det uttrycket. Annars så körs interaktivt läge där användaren kan utföra
		// uträkningar upprepande gånger.
		StringBuilder stringBuilder = new StringBuilder();
		for (String arg : args) {
			stringBuilder.append(arg);
		}
		String concatenatedCommandLineArguments = stringBuilder.toString().trim();

		if (concatenatedCommandLineArguments.length() > 0) {
			try {
				System.out.println(processExpression(concatenatedCommandLineArguments));
			} catch (OperationNotSupportedException e) {
				System.out.println(USAGE);
			}
		} else {
			interactiveMode();
		}
	}
}