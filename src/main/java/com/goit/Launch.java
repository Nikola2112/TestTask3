package com.goit;
import java.util.Scanner;
import static com.goit.Equation.findRootInteractive;

public class Launch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Введіть математичне рівняння:");
        String equationString = scanner.nextLine();

        DatabaseManager databaseManager =new DatabaseManager();
        Equation equation = new Equation(equationString);

        if (equation.isValid()) {
            databaseManager.saveEquation(equation);
            System.out.println("Рівняння валідне.");

            findRootInteractive(equation, scanner);
        } else {
            System.out.println("Некоректне рівняння.");
        }
    }
}
