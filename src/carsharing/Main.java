package carsharing;

import java.util.Scanner;

public class Main {

    static void printMenu() {
        System.out.println("1. Log in as a manager\n" +
                "0. Exit"
        );
    }

    public static void main(String[] args)  {
        Scanner scan =  new Scanner(System.in);
        DB_Handler.createTables();

        while (true) {
            printMenu();
            int option = Integer.parseInt(scan.nextLine());
            System.out.println();

            switch (option) {
                case 1:
                    new Manager().startManaging();
                    break;
                case 0:
                    return;
            }
        }
    }
}