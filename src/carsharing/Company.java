package carsharing;

import java.util.ArrayList;
import java.util.Scanner;

public class Company {

    int id;
    String companyName;

    public Company(int id, String companyName) {
        this.id = id;
        this.companyName = companyName;
    }

    public Company(Company company) {
        this.id = company.id;
        this.companyName = company.companyName;
    }

    void printCompanyMenu() {

        System.out.println(
                "1. Car list\n" +
                        "2. Create a car\n" +
                        "0. Back");
    }

    static void printCars(ArrayList<Car> carList) {
        int i = 1;

        for (Car car : carList) {
            System.out.println(i + ". " + car.carName);
            i++;
        }
    }

    void controlCompany() {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            printCompanyMenu();
            int option = Integer.parseInt(scanner.nextLine());
            System.out.println();

            switch (option) {
                case 1:
                    ArrayList<Car> carList = DB_Handler.getCarList(this.id);
                    if (carList.size() == 0) {
                        System.out.println("The car list is empty!");
                        System.out.println();
                    } else {
                        printCars(carList);
                        System.out.println();
                    }
                    break;
                case 2:
                    System.out.println("Enter the car name:");
                    String carName = scanner.nextLine();
                    DB_Handler.createCar(carName, this.id);
                    break;
                case 0:
                    return;
            }
        }
    }
}
