package carsharing;

import java.util.Scanner;

public class Main {

    static void printMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
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
                case 2:
                    new Customer().startServing();
                    break;
                case 3:
                    System.out.println("Enter the customer name:");
                    String customerName = scan.nextLine();
                    DB_Handler.createCustomer(customerName);
                    break;
                case 0:
                    return;
            }
        }
    }
}