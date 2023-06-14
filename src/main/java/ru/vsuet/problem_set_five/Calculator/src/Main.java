import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList history = new ArrayList();

        // загрузка истории вычислений из файла
        try {
            File file = new File("history.txt");
            if (file.exists()) {
                Scanner fileScanner = new Scanner(file);
                while (fileScanner.hasNextLine()) {
                    String equation = fileScanner.nextLine();
                    history.add(equation);
                }
                fileScanner.close();
                System.out.println("История загружена.");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке истории: " + e.getMessage());
        }

        while (true) {
            // ввод уравнения
            System.out.print("Введите уравнение: ");
            String equation = scanner.nextLine();

            // проверка на выход из программы
            if (equation.equals("exit")) {
                break;
            }

            try {
                // вычисление уравнения
                int result = calculate(equation);
                System.out.println("Результат: " + result);

                // сохранение уравнения в истории
                history.add(equation);

                // запись истории в файл
                PrintWriter writer = new PrintWriter("history.txt");
                for (Object eq : history) {
                    writer.println(eq);
                }
                writer.close();

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    static int calculate(String equation) throws Exception {
        Stack nums = new Stack();
        Stack ops = new Stack();

        int i = 0;
        while (i < equation.length()) {
            char c = equation.charAt(i);
            if (c == ' ' || c == '\t') {
                i++;
                continue;
            }

            if (Character.isDigit(c)) {
                int num = 0;
                while (i < equation.length() && Character.isDigit(equation.charAt(i))) {
                    num = num * 10 + Character.getNumericValue(equation.charAt(i));
                    i++;
                }
                nums.push(num);
            } else if ("+-*/%^(|".indexOf(c) != -1) {
                while (!ops.empty() && priority(ops.peek()) >= priority(c)) {
                    eval(nums, ops);
                }
                ops.push(c);
                i++;
            } else if (c == ')') {
                while (ops.peek() != '(') {
                    eval(nums, ops);
                }
                ops.pop();
                i++;
            } else if (c == '(') {
                ops.push(c);
                i++;
            } else {
                throw new Exception("Неизвестный символ: " + c);
            }
        }

        while (!ops.empty()) {
            eval(nums, ops);
        }

        if (nums.size() != 1) {
            throw new Exception("Ошибка при вычислении.");
        }

        return nums.pop();
    }

    static int priority(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
            case '|':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }

    static void eval(Stack nums, Stack ops) {
        char op = ops.pop();
        int b = nums.pop();
        int a = nums.pop();
        int result = 0;

        switch (op) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                result = a / b;
                break;
            case '%':
                result = a % b;
                break;
            case '^':
                result = (int) Math.pow(a, b);
                break;
            case '|':
                result = Math.abs(b);
                break;
        }

        nums.push(result);
    }
}