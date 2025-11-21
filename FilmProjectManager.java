import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class FilmProjectManager {
    protected ArrayList<Person> people;
    
    public FilmProjectManager() {
        people = new ArrayList<>();
    }

public void addPerson(Scanner input) {
    // Handle file selection for writing
    File selectedFile = selectFileForWriting(input);
    if (selectedFile == null) {
        System.out.println("File selection cancelled. Returning to menu.");
        return;
    }
    
    System.out.print("Enter last name: ");
    String lastName = input.nextLine();

    System.out.print("Enter first name: ");
    String firstName = input.nextLine();

    System.out.print("Enter contact information (phonenumber or email): ");
    String contact = input.nextLine();

    System.out.println("Select person type:");
    System.out.println("1. Actor");
    System.out.println("2. Crew");
    System.out.println("3. Volunteer");
    System.out.print("Choice: ");

    int typeChoice = input.nextInt();
    input.nextLine();

    Person person;

    switch (typeChoice) {
        case 1:
            System.out.print("Enter actor specialty: ");
            String specialty = input.nextLine();
            person = new Actor(firstName, lastName, contact, specialty);
            break;

        case 2:
            System.out.print("Enter crew department: ");
            String department = input.nextLine();
            person = new Crew(firstName, lastName, contact, department);
            break;

        case 3:
            System.out.print("Enter volunteer task: ");
            String task = input.nextLine();
            person = new Volunteer(firstName, lastName, contact, task);
            break;

        default:
            System.out.println("Invalid type option. Returning to menu.");
            return;
    }

    people.add(person);
    System.out.println("Person added successfully!");
    
    // Write the person to the selected file
    writePersonToFile(person, selectedFile);
}

    // Add placeholder methods for the ones called in Main.java
    public void removePerson(Scanner input) {
        System.out.println("Remove person functionality not yet implemented.");
    }
    
    public void displayPersons() {
        if (people.isEmpty()) {
            System.out.println("No people in the system.");
        } else {
            System.out.println("\nAll People:");
            for (Person person : people) {
                System.out.println(person.toString());
            }
        }
    }
    
    public void findPerson(Scanner input) {
        System.out.println("Find person functionality not yet implemented.");
    }
    
    public void countRole(Scanner input) {
        System.out.println("Count role functionality not yet implemented.");
    }
    
    // File management methods
    public void manageFiles(Scanner input) {
        System.out.println("\n========== File Management ==========");
        
        // Check for existing .txt files in the current directory
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        
        if (files != null && files.length > 0) {
            System.out.println("Existing text files found:");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }
            
            System.out.println("\nOptions:");
            System.out.println("1. Write to an existing file");
            System.out.println("2. Create a new file");
            System.out.print("Choose an option: ");
            
            int choice = input.nextInt();
            input.nextLine(); // consume newline
            
            if (choice == 1) {
                selectExistingFile(input, files);
            } else if (choice == 2) {
                createNewFile(input);
            } else {
                System.out.println("Invalid choice. Returning to main menu.");
            }
        } else {
            System.out.println("No existing text files found in the current directory.");
            System.out.print("Would you like to create a new file? (y/n): ");
            String response = input.nextLine().trim().toLowerCase();
            
            if (response.equals("y") || response.equals("yes")) {
                createNewFile(input);
            } else {
                System.out.println("Returning to main menu.");
            }
        }
    }
    
    private void selectExistingFile(Scanner input, File[] files) {
        System.out.print("Enter the number of the file you want to write to: ");
        int fileChoice = input.nextInt();
        input.nextLine(); // consume newline
        
        if (fileChoice >= 1 && fileChoice <= files.length) {
            File selectedFile = files[fileChoice - 1];
            System.out.println("Selected file: " + selectedFile.getName());
            // Ensure the file has proper table headers
            ensureFileHasHeaders(selectedFile);
            writeToFile(input, selectedFile);
        } else {
            System.out.println("Invalid file number. Returning to main menu.");
        }
    }
    
    private void createNewFile(Scanner input) {
        System.out.print("Enter the name for the new file (without .txt extension): ");
        String fileName = input.nextLine().trim();
        
        if (!fileName.isEmpty()) {
            File newFile = new File(fileName + ".txt");
            
            try {
                if (newFile.createNewFile()) {
                    System.out.println("File '" + newFile.getName() + "' created successfully!");
                    // Write table headers to the new file
                    writeTableHeadersToFile(newFile);
                    writeToFile(input, newFile);
                } else {
                    System.out.println("File '" + newFile.getName() + "' already exists!");
                    System.out.print("Do you want to write to it anyway? (y/n): ");
                    String response = input.nextLine().trim().toLowerCase();
                    
                    if (response.equals("y") || response.equals("yes")) {
                        // Ensure existing file has headers before writing
                        ensureFileHasHeaders(newFile);
                        writeToFile(input, newFile);
                    } else {
                        System.out.println("Returning to main menu.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid file name. Returning to main menu.");
        }
    }
    
    private void writeToFile(Scanner input, File file) {
        System.out.println("\nWhat would you like to write to the file?");
        System.out.println("1. Current list of all persons");
        System.out.println("2. Custom text");
        System.out.print("Choose an option: ");
        
        int choice = input.nextInt();
        input.nextLine(); // consume newline
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            if (choice == 1) {
                writePersonnelTableToFile(writer);
                System.out.println("Personnel list written to '" + file.getName() + "' successfully!");
                
            } else if (choice == 2) {
                System.out.print("Enter the text you want to write: ");
                String customText = input.nextLine();
                writer.println(customText);
                System.out.println("Custom text written to '" + file.getName() + "' successfully!");
                
            } else {
                System.out.println("Invalid choice. Nothing was written to the file.");
                return;
            }
            
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    private void writePersonnelTableToFile(PrintWriter writer) {
        writer.println("=== Film Project Personnel List ===");
        writer.println("Generated on: " + java.time.LocalDateTime.now());
        writer.println();
        
        if (people.isEmpty()) {
            writer.println("No people in the system.");
        } else {
            // Create table headers
            String headerFormat = "%-20s %-20s %-25s %-15s%n";
            String rowFormat = "%-20s %-20s %-25s %-15s%n";
            
            // Write headers
            writer.printf(headerFormat, "Last Name", "First Name", "Contact", "Type");
            
            // Write separator line
            writer.println("-------------------- -------------------- ------------------------- ---------------");
            
            // Write data rows
            for (Person person : people) {
                writer.printf(rowFormat, 
                    truncateString(person.getLastName(), 20),
                    truncateString(person.getFirstName(), 20),
                    truncateString(person.getContact(), 25),
                    truncateString(person.getPersonType(), 15)
                );
            }
        }
        writer.println();
        writer.println("=== End of List ===");
        writer.println();
    }
    
    // Helper method to truncate strings that are too long for the table
    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
    
    // Method to select file for writing at the beginning of add person process
    private File selectFileForWriting(Scanner input) {
        System.out.println("\n========== File Selection ==========");
        
        // Check for existing .txt files in the current directory
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        
        if (files != null && files.length > 0) {
            System.out.println("Existing text files found:");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }
            
            System.out.println("\nOptions:");
            System.out.println("1. Write to an existing file");
            System.out.println("2. Create a new file");
            System.out.println("0. Cancel and return to menu");
            System.out.print("Choose an option: ");
            
            int choice = input.nextInt();
            input.nextLine(); // consume newline
            
            if (choice == 1) {
                return selectExistingFileForWriting(input, files);
            } else if (choice == 2) {
                return createNewFileForWriting(input);
            } else {
                return null; // Cancel
            }
        } else {
            System.out.println("No existing text files found in the current directory.");
            System.out.print("Would you like to create a new file? (y/n): ");
            String response = input.nextLine().trim().toLowerCase();
            
            if (response.equals("y") || response.equals("yes")) {
                return createNewFileForWriting(input);
            } else {
                return null; // Cancel
            }
        }
    }
    
    private File selectExistingFileForWriting(Scanner input, File[] files) {
        System.out.print("Enter the number of the file you want to write to: ");
        int fileChoice = input.nextInt();
        input.nextLine(); // consume newline
        
        if (fileChoice >= 1 && fileChoice <= files.length) {
            File selectedFile = files[fileChoice - 1];
            System.out.println("Selected file: " + selectedFile.getName());
            
            // Ensure the file has proper table headers
            ensureFileHasHeaders(selectedFile);
            
            return selectedFile;
        } else {
            System.out.println("Invalid file number.");
            return null;
        }
    }
    
    private File createNewFileForWriting(Scanner input) {
        System.out.print("Enter the name for the new file (without .txt extension): ");
        String fileName = input.nextLine().trim();
        
        if (!fileName.isEmpty()) {
            File newFile = new File(fileName + ".txt");
            
            try {
                if (newFile.createNewFile()) {
                    System.out.println("File '" + newFile.getName() + "' created successfully!");
                    // Write table headers to the new file
                    writeTableHeadersToFile(newFile);
                    return newFile;
                } else {
                    System.out.println("File '" + newFile.getName() + "' already exists!");
                    System.out.print("Do you want to write to it anyway? (y/n): ");
                    String response = input.nextLine().trim().toLowerCase();
                    
                    if (response.equals("y") || response.equals("yes")) {
                        return newFile;
                    } else {
                        return null;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Invalid file name.");
            return null;
        }
    }
    
    private void writeTableHeadersToFile(File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            writer.println("=== Film Project Personnel List ===");
            writer.println("Generated on: " + java.time.LocalDateTime.now());
            writer.println();
            
            String headerFormat = "%-20s %-20s %-25s %-15s%n";
            writer.printf(headerFormat, "Last Name", "First Name", "Contact", "Type");
            writer.println("-------------------- -------------------- ------------------------- ---------------");
            
        } catch (IOException e) {
            System.out.println("Error writing headers to file: " + e.getMessage());
        }
    }
    
    private void writePersonToFile(Person person, File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            String rowFormat = "%-20s %-20s %-25s %-15s%n";
            writer.printf(rowFormat, 
                truncateString(person.getLastName(), 20),
                truncateString(person.getFirstName(), 20),
                truncateString(person.getContact(), 25),
                truncateString(person.getPersonType(), 15)
            );
            
            System.out.println("Person written to '" + file.getName() + "' successfully!");
            
        } catch (IOException e) {
            System.out.println("Error writing person to file: " + e.getMessage());
        }
    }
    
    /**
     * Ensures that a file has proper table headers. If the file is empty or doesn't have headers,
     * this method will add them.
     */
    private void ensureFileHasHeaders(File file) {
        try {
            // Check if file exists and has content
            if (!file.exists() || file.length() == 0) {
                // File is empty, add headers
                writeTableHeadersToFile(file);
                return;
            }
            
            // Read the first few lines to check if headers exist
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                boolean hasFilmProjectHeader = false;
                boolean hasColumnHeaders = false;
                
                // Read up to the first 10 lines to look for headers
                for (int i = 0; i < 10 && (line = reader.readLine()) != null; i++) {
                    if (line.contains("=== Film Project Personnel List ===")) {
                        hasFilmProjectHeader = true;
                    }
                    if (line.contains("Last Name") && line.contains("First Name") && 
                        line.contains("Contact") && line.contains("Type")) {
                        hasColumnHeaders = true;
                    }
                }
                
                // If headers are missing, add them
                if (!hasFilmProjectHeader || !hasColumnHeaders) {
                    // Read existing content
                    java.util.List<String> existingLines = new java.util.ArrayList<>();
                    try (java.io.BufferedReader fullReader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                        String existingLine;
                        while ((existingLine = fullReader.readLine()) != null) {
                            existingLines.add(existingLine);
                        }
                    }
                    
                    // Rewrite file with headers first, then existing content
                    try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
                        // Write headers
                        writer.println("=== Film Project Personnel List ===");
                        writer.println("Generated on: " + java.time.LocalDateTime.now());
                        writer.println();
                        
                        String headerFormat = "%-20s %-20s %-25s %-15s%n";
                        writer.printf(headerFormat, "Last Name", "First Name", "Contact", "Type");
                        writer.println("-------------------- -------------------- ------------------------- ---------------");
                        
                        // Write existing content (skip any old headers that might exist)
                        for (String existingLine : existingLines) {
                            // Skip old header lines to avoid duplication
                            if (!existingLine.contains("=== Film Project Personnel List ===") &&
                                !existingLine.contains("Generated on:") &&
                                !existingLine.contains("Last Name") &&
                                !existingLine.startsWith("--------------------") &&
                                !existingLine.trim().isEmpty()) {
                                writer.println(existingLine);
                            }
                        }
                    }
                    
                    System.out.println("Added table headers to '" + file.getName() + "'");
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error checking/adding headers to file: " + e.getMessage());
        }
    }
}
