package carsharing;

import java.util.ArrayList;
import java.util.Scanner;

public class Manager {

    private void printManagerMenu() {
        System.out.println("" +
                "1. Company list \n" +
                "2. Create a company \n" +
                "0. Back");
    }

    static void printCompanies(ArrayList<Company> companies) {
        System.out.println("Company list:");

        int id = 1;

        for (Company company : companies) {
            System.out.println(id + ". " + company.companyName);
            id++;
        }
        System.out.println();
    }

    void startManaging() {
        Scanner scan = new Scanner(System.in);

        while (true) {
            printManagerMenu();
            int option = Integer.parseInt(scan.nextLine());
            System.out.println();

            switch (option) {
                case 1:
                    ArrayList<Company> companies = DB_Handler.getCompanyList();

                    if (companies.size() == 0) {
                        System.out.println("The company list is empty!");
                        System.out.println();
                    } else {
                        printCompanies(companies);
                    }
                    break;
                case 2:
                    System.out.println("Enter the company name:");
                    String companyName = scan.nextLine();
                    DB_Handler.createCompany(companyName);
                    break;
                case 0:
                    return;
            }
        }
    }
}
