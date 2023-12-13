package com.goit;
import lombok.Getter;
import lombok.Setter;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class Equation {
    private String equation;

    public Equation(String equation) {
        this.equation = equation;
    }

    public boolean isValid() {
        return !equation.isEmpty() && checkParentheses() && checkOperators();
    }

    private boolean checkParentheses() {
        int count = 0;
        for (char c : equation.toCharArray()) {
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
                if (count < 0) {
                    return false;
                }
            }
        }
        return count == 0;
    }

    private boolean checkOperators() {
        Pattern pattern = Pattern.compile("[+\\-*/=]+");
        Matcher matcher = pattern.matcher(equation);

        int equalsCount = 0;

        boolean isValid = true;
        while (matcher.find()) {
            if (matcher.group().equals("=")) {
                equalsCount++;
                if (equalsCount > 1) {
                    isValid = false;
                    break;
                }
            } else if (matcher.group().length() > 1) {
                isValid = false;
                break;
            }
        }

        return isValid && equalsCount == 1;
    }

    public boolean hasRoot(double rootToCheck) {
        if (!isValid()) {
            System.out.println("Рівняння недійсне. Неможливо знайти корінь.");
            return false;
        }
        try {
            return Math.abs(evaluateExpression(rootToCheck)) < 1e-10;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double evaluateExpression(double x) {
        String expression = equation.replace("x", String.valueOf(x));
        String[] tokens = expression.split("(?=[-+*/=])|(?<=[^-+*/][-+*/=])");

        if (tokens.length == 1 && tokens[0].equals("x")) {
            return x;  // Если уравнение содержит только 'x', возвращаем значение 'x'
        }

        try {
            double result = evaluateExpressionHelper(tokens);
            return result;
        } catch (NumberFormatException e) {
            System.out.println("Помилка обчислення виразу: " + expression);
            return Double.NaN;
        }
    }


    private double evaluateExpressionHelper(String[] tokens) {
        Stack<Double> stack = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (token.equals("x")) {
                stack.push(0.0);  // Value of 'x' you want to use
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalStateException("Not enough operands for operator " + token);
                }

                double operand2 = stack.pop();
                double operand1 = stack.pop();
                double result;

                switch (token) {
                    case "+":
                        result = operand1 + operand2;
                        break;
                    case "-":
                        result = operand1 - operand2;
                        break;
                    case "*":
                        result = operand1 * operand2;
                        break;
                    case "/":
                        result = operand1 / operand2;
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported operator: " + token);
                }

                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalStateException("Invalid expression: " + equation);
        }

        return stack.pop();
    }


    private String convertToPostfix(String[] tokens) {
        StringBuilder result = new StringBuilder();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token) || token.equals("x")) {
                result.append(token).append(" ");
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(token)) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            result.append(stack.pop()).append(" ");
        }

        return result.toString().trim();
    }

    private int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
                return 2;
            case "/":
                return 2;
            default:
                return 0;  // Возвращаем 0 для оператора "="
        }
    }

    private double evaluatePostfix(String postfixExpression) {
        Stack<Double> stack = new Stack<>();

        String[] tokens = postfixExpression.split("\\s");

        for (String token : tokens) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (token.equals("x")) {
                stack.push(0.0);  // Значение 'x', которое вы хотите использовать
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalStateException("Not enough operands for operator " + token);
                }

                double operand2 = stack.pop();
                double operand1 = stack.pop();
                double result;

                switch (token) {
                    case "+":
                        result = operand1 + operand2;
                        break;
                    case "-":
                        result = operand1 - operand2;
                        break;
                    case "*":
                        result = operand1 * operand2;
                        break;
                    case "/":
                        result = operand1 / operand2;
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported operator: " + token);
                }

                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalStateException("Invalid postfix expression");
        }

        return stack.pop();
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkAndSaveRoot(double rootToCheck) {
        if (!isValid()) {
            System.out.println("Рівняння недійсне. Неможливо знайти корінь.");
            return false;
        }

        boolean hasRoot = hasRoot(rootToCheck);

        if (hasRoot) {
            System.out.println("Знайдено корінь рівняння: " + rootToCheck);
            // Сохраняем корень в базу данных
            DatabaseManager.saveRoot(new Equation(getEquation()), rootToCheck);
        }

        return hasRoot;
    }

    public boolean isRoot(double rootToCheck) {
        if (!isValid()) {
            return false;
        }

        double result = evaluateExpression(rootToCheck);
        return Math.abs(result) < 1e-10;
    }

    public static void findRootInteractive(Equation equation, Scanner scanner) {
        boolean continueInput = true;

        while (continueInput) {
            System.out.println("Введіть число для перевірки як корінь рівнянь (або 'Вийти' для завершення):");
            String input = scanner.next();

            if (input.equalsIgnoreCase("Вийти")) {
                continueInput = false;
            } else {
                try {
                    double rootToCheck = Double.parseDouble(input);

                    if (equation.hasRoot(rootToCheck)) {
                        System.out.println("Знайдено корінь рівняння: " + rootToCheck);
                        // Сохраняем корень в базу данных
                        equation.checkAndSaveRoot(rootToCheck);
                        break;  // Выходим из цикла, так как нашли корень
                    } else {
                        System.out.println("Введене число не є коренем рівняння.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Некоректне число. Спробуйте ще раз.");
                }
            }
        }
    }
}
