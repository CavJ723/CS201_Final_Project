import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FilmProjectManager manager = new FilmProjectManager();

        int choice = -1;

        while (choice != 0) {
            System.out.println("Hi! Welcome to the Film Project Management System. Choose an option below:");
            System.out.println("\n========================================================================");
            System.out.println("1. Add Person");
            System.out.println("2. Remove Person");
            System.out.println("3. Display All Persons");
            System.out.println("4. Search for a Person (Locked)");
            System.out.println("5. Count Persons by Role");
            System.out.println("6. File Management");
            System.out.println("7. Payroll Management (Locked)");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    manager.addPerson(input);
                    break;
                case 2:
                    manager.removePerson(input);
                    break;
                case 3:
                    manager.displayPersons();
                    break;
                case 4:
                    manager.findPerson(input);
                    break;
                case 5:
                    manager.countRole(input);
                    break;
                case 6:
                    manager.manageFiles(input);
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        input.close();
    }
}
