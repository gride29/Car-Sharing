package carsharing;

import java.util.Scanner;

public class Account {

    int id;
    String name;
    int rentedCarId;

    public Account(int id, String name, int rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public Account(Account account) {
        this.id = account.id;
        this.name = account.name;
        this.rentedCarId = account.rentedCarId;
    }

    private void printAccountMenu() {

        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
    }

    void controlAccount() {
        Scanner scan = new Scanner(System.in);

        while (true) {
            printAccountMenu();

            int option = Integer.parseInt(scan.nextLine());
            System.out.println();

            switch (option) {
                case 1:
                    DB_Handler.rentCar(this);
                    break;
                case 2:
                    DB_Handler.returnCar(this);
                    break;
                case 3:
                    DB_Handler.getCar(this);
                    break;
                case 0:
                    return;
            }
        }
    }
}
