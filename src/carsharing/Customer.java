package carsharing;

import java.util.ArrayList;
import java.util.Scanner;

public class Customer {

    private void printCustomers(ArrayList<Account> customers) {
        System.out.println("Customer list:");

        int id = 1;

        for (Account customer : customers) {
            System.out.println(id + ". " + customer.name);
            id++;
        }

        System.out.println("0. Back");
    }

    void startServing() {
        Scanner scan = new Scanner(System.in);

        ArrayList<Account> customers = DB_Handler.getCustomersList();

        if (customers.size() == 0) {
            System.out.println("The customer list is empty!");
            System.out.println();

        } else {
            printCustomers(customers);

            int selectedCustomer = Integer.parseInt(scan.nextLine());
            System.out.println();

            if (selectedCustomer == 0) {
                return;
            }

            Account currentCustomer = new Account(customers.get(selectedCustomer - 1));
            currentCustomer.controlAccount();
        }
    }
}
