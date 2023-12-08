import java.util.*;

public class ExpressionCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
// Ввод выражения
        System.out.println("Введите выражение:");
        String expression = scanner.nextLine();

// Разбор выражения и вычисление его значения
        try {
            double result = evaluate(expression);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public static double evaluate(String expression) throws Exception {
        Stack<Double> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

// Пропустить пробелы
            if (currentChar == ' ') {
                continue;
            }

// Обработка чисел
            if (Character.isDigit(currentChar)) {
                StringBuilder sb = new StringBuilder();

// Считываем все символы числа
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }

                double number = Double.parseDouble(sb.toString());
                operandStack.push(number);
                i--;
            }
// Обработка открывающей скобки
            else if (currentChar == '(') {
                operatorStack.push(currentChar);
            }
// Обработка закрывающей скобки
            else if (currentChar == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    double result = applyOperator(operandStack, operatorStack);
                    operandStack.push(result);
                }

                if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
                    operatorStack.pop();
                } else {
                    throw new Exception("Некорректное выражение: непарная закрывающая скобка");
                }
            }
// Обработка операторов
            else if (isOperator(currentChar)) {
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(currentChar)) {
                    double result = applyOperator(operandStack, operatorStack);
                    operandStack.push(result);
                }
                operatorStack.push(currentChar);
            } else {
                throw new Exception("Некорректное выражение: недопустимый символ");
            }
        }

        while (!operatorStack.isEmpty()) {
            double result = applyOperator(operandStack, operatorStack);
            operandStack.push(result);
        }

        if (operandStack.size() != 1) {
            throw new Exception("Некорректное выражение");
        }

        return operandStack.pop();
    }

    public static double applyOperator(Stack<Double> operandStack, Stack<Character> operatorStack) throws Exception {
        char operator = operatorStack.pop();
        double operand2 = operandStack.pop();
        double operand1 = operandStack.pop();

        if (operator == '+') {
            return operand1 + operand2;
        } else if (operator == '-') {
            return operand1 - operand2;
        } else if (operator == '*') {
            return operand1 * operand2;
        } else if (operator == '/') {
            if (operand2 == 0) {
                throw new Exception("Деление на ноль");
            }
            return operand1 / operand2;
        } else {
            throw new Exception("Недопустимый оператор");
        }
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        } else {
            return 0;
        }
    }
}