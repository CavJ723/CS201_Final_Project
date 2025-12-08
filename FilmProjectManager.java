import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class FilmProjectManager {
    protected ArrayList<Person> people;
    
    public FilmProjectManager() {
        people = new ArrayList<>();
    }

// PRE: A valid Scanner object is passed in
// POST: A new Person (Actor, Crew, or Volunteer) is created based on user input
public void addPerson(Scanner input) {
    // File selection for writing
    File selectedFile = selectFileForWriting(input);
    if (selectedFile == null) {
        System.out.println("File selection cancelled. Returning to menu.");
        return;
    }
    
    System.out.print("Enter last name: ");
    String lastName = input.nextLine();

    System.out.print("Enter first name: ");
    String firstName = input.nextLine();

    System.out.print("Enter contact information (phone-number or email): ");
    String contact = input.nextLine();

    System.out.println("Select person type:");
    System.out.println("1. Actor");
    System.out.println("2. Crew");
    System.out.println("3. Volunteer");
    System.out.println("0. Return to menu");
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

    // PRE: A valid Scanner object is passed in
    // POST: The specified Person is removed from the selected file and memory
    public void removePerson(Scanner input) {
        // Have user select which file to remove from
        File selectedFile = selectFileForReading(input);
        if (selectedFile == null) {
            System.out.println("File selection cancelled. Returning to menu.");
            return;
        }
        
        System.out.print("Enter the last name of the person to remove: ");
        String lastName = input.nextLine();
        
        // Load people from the selected file and find matches
        ArrayList<Person> filePersons = loadPersonsFromFile(selectedFile);
        ArrayList<Person> matches = new ArrayList<>();
        for (Person person : filePersons) {
            if (person.getLastName().equalsIgnoreCase(lastName)) {
                matches.add(person);
            }
        }
        
        if (matches.isEmpty()) {
            System.out.println("No person found with last name: " + lastName);
            return;
        }
        
        Person toRemove = null;
        
        if (matches.size() == 1) {
            // One match found
            toRemove = matches.get(0);
            System.out.println("Found: " + toRemove.toString());
            System.out.print("Are you sure you want to remove this person? (y/n): ");
            String confirm = input.nextLine().trim().toLowerCase();
            
            if (!confirm.equals("y") && !confirm.equals("yes")) {
                System.out.println("Removal cancelled.");
                return;
            }
        } else {
            // Multiple matches found, then let user choose
            System.out.println("\nMultiple people found with last name '" + lastName + "':");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println((i + 1) + ". " + matches.get(i).toString());
            }
            System.out.println("0. Cancel removal");
            
            System.out.print("Select the number of the person to remove: ");
            int choice = input.nextInt();
            input.nextLine(); // consume newline
            
            if (choice == 0) {
                System.out.println("Removal cancelled.");
                return;
            } else if (choice >= 1 && choice <= matches.size()) {
                toRemove = matches.get(choice - 1);
            } else {
                System.out.println("Invalid selection. Removal cancelled.");
                return;
            }
        }
        
        // Remove from memory (if it exists there)
        people.remove(toRemove);
        
        // Remove from file by rewriting it without the removed person
        removePersonFromFile(toRemove, selectedFile);
        
        System.out.println("Person removed successfully from file!");
    }
    
    // PRE: No preconditions
    // POST: Displays all persons in the system
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
    
    // PRE: A valid Scanner object is passed in
    // POST: Finds and displays persons based on user input
    public void findPerson(Scanner input) {
        System.out.println("Find person functionality not yet implemented.");
    }
    
    // PRE: A valid Scanner object is passed in
    // POST: Counts and displays the number of persons by role based on user input
    public void countRole(Scanner input) {
        System.out.println("Which file do you want to count roles from? ");
        File selectedFile = selectFileForReading(input);
        if (selectedFile == null) {
            System.out.println("File selection cancelled. Returning to menu.");
            return;
        }
        ArrayList<Person> filePersons = loadPersonsFromFile(selectedFile); 
        int actorCount = 0;
        int crewCount = 0;
        int volunteerCount = 0;
        for (Person person : filePersons) {
            if (person instanceof Actor) {
                actorCount++;
            } else if (person instanceof Crew) {
                crewCount++;
            } else if (person instanceof Volunteer) {
                volunteerCount++;
            }
        }

        System.out.println("Role counts in file '" + selectedFile.getName() + "':");
        System.out.println("Actors: " + actorCount);
        System.out.println("Crew: " + crewCount);
        System.out.println("Volunteers: " + volunteerCount);
    }

    public void payrollManagement(Scanner input) {
        System.out.println("Payroll management is currently locked and not available.");
    }
    
    // PRE: A valid Scanner object is passed in
    // POST: Manages file operations such as creating, selecting, and deleting files
    public void manageFiles(Scanner input) {
        System.out.println("\n========== File Management ==========");
        
        // Check for existing .txt files in the current directory
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        
        if (files != null && files.length > 0) {
            System.out.println("Existing text files found:");
            // Display files
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }
            
            System.out.println("\nOptions:");
            System.out.println("1. Write to an existing file");
            System.out.println("2. Create a new file");
            System.out.println("3. Delete a file");
            System.out.println("0. Return to main menu");
            System.out.print("Choose an option: ");
            
            int choice = input.nextInt();
            input.nextLine(); // consume newline
            
            if (choice == 1) {
                selectExistingFile(input, files);
            } else if (choice == 2) {
                createNewFile(input);
            } else if (choice == 3) {
                deleteFile(input, files);
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
    
    // PRE: A valid Scanner object and array of Files are passed in
    // POST: Allows user to select an existing file to write to
    private void selectExistingFile(Scanner input, File[] files) {
        System.out.print("Enter the number of the file you want to write to: ");
        int fileChoice = input.nextInt();
        input.nextLine(); // consume newline
        
        // Validate the file choice
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
    
    // PRE: A valid Scanner object is passed in
    // POST: Creates a new file and writes table headers to it
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
    
    // PRE: A valid Scanner object and array of Files are passed in
    // POST: Deletes a selected file
    //       Use double confirmation to prevent accidental deletions
    private void deleteFile(Scanner input, File[] files) {
        System.out.println("\n========== File Deletion ==========");
        System.out.println("WARNING: This action cannot be undone!");
        System.out.println("Available files to delete:");
        
        // Display files with their sizes
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ". " + files[i].getName() + " (" + files[i].length() + " bytes)");
        }
        System.out.println("0. Cancel deletion");
        
        System.out.print("Select the number of the file you want to delete: ");
        int fileChoice = input.nextInt();
        input.nextLine(); // consume newline
        
        if (fileChoice == 0) {
            System.out.println("File deletion cancelled.");
            return;
        }
        
        // Will show preview of contents inside the file and ask for double confirmation
        if (fileChoice >= 1 && fileChoice <= files.length) {
            File selectedFile = files[fileChoice - 1];
            
            // Show file preview
            System.out.println("\nFile Preview for '" + selectedFile.getName() + "':");
            System.out.println("========================================");
            showFilePreview(selectedFile);
            System.out.println("========================================");
            
            // Double confirmation
            System.out.println("\nPlease review the file content above carefully.");
            System.out.print("Are you absolutely sure you want to delete '" + selectedFile.getName() + "'? (yes/no): ");
            String firstConfirm = input.nextLine().trim().toLowerCase();
            
            // By typing the file name, this ensures user really wants to delete it
            if (firstConfirm.equals("yes")) {
                System.out.print("Type the file name '" + selectedFile.getName() + "' to confirm deletion: ");
                String nameConfirm = input.nextLine().trim();
                
                if (nameConfirm.equals(selectedFile.getName())) {
                    // Attempt to delete the file
                    if (selectedFile.delete()) {
                        System.out.println("SUCCESS: File '" + selectedFile.getName() + "' has been successfully deleted!");
                    } else {
                        System.out.println("ERROR: Could not delete file '" + selectedFile.getName() + "'.");
                        System.out.println("   The file might be in use or you may not have permission to delete it.");
                    }
                } else {
                    System.out.println("ERROR: File name does not match. Deletion cancelled for safety.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("Invalid file number. Deletion cancelled.");
        }
    }
    
    // PRE: A valid File object is passed in
    // POST: Displays a preview of the file's contents (first 20 lines)
    private void showFilePreview(File file) {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            int lineCount = 0;
            int maxLines = 20; // Show first 20 lines
            
            while ((line = reader.readLine()) != null && lineCount < maxLines) {
                System.out.println(line);
                lineCount++;
            }
            
            if (lineCount == maxLines) {
                // Check if there are more lines
                if (reader.readLine() != null) {
                    System.out.println("... (file continues, showing first " + maxLines + " lines only)");
                }
            }
            
            if (lineCount == 0) {
                System.out.println("(File is empty)");
            }
            
        } catch (IOException e) {
            System.out.println("Error reading file preview: " + e.getMessage());
        }
    }
    
    // PRE: A valid Scanner object and File are passed in
    // POST: Writes user-selected content to the specified file
    private void writeToFile(Scanner input, File file) {
        System.out.println("\nWhat would you like to write to the file?");
        System.out.println("1. Current list of all persons");
        System.out.println("2. Custom text");
        System.out.println("0. Return to menu");
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
    
    // PRE: A valid PrintWriter object is passed in
    // POST: Writes the personnel table to the specified file
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
    
    // PRE: A valid Scanner object is passed in
    // POST: Allows the user to select a file for writing or create a new one
    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
    
    // PRE: A valid Scanner object is passed in
    // POST: Allows the user to select a file for writing or create a new one
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
    
    // PRE: A valid Scanner object is passed in
    // POST: Allows the user to select an existing file for writing
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
    
    // PRE: A valid Scanner object is passed in
    // POST: Creates a new file and writes table headers to it
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
    
    // PRE: A valid File object is passed in
    // POST: Writes table headers to the specified file
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
    
    // PRE: A valid Person object and File are passed in
    // POST: Writes the person's details to the specified file
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
    
    // PRE: A valid File object is passed in
    // POST: Ensures the file has proper table headers, adding them if necessary
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
    
    // PRE: A valid Scanner object is passed in
    // POST: Allows the user to select a file for reading
    private File selectFileForReading(Scanner input) {
        System.out.println("\n========== File Selection for Removal ==========");
        
        // Check for existing .txt files in the current directory
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        
        if (files == null || files.length == 0) {
            System.out.println("No text files found in the current directory.");
            return null;
        }
        
        System.out.println("Available text files:");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ". " + files[i].getName());
        }
        System.out.println("0. Cancel");
        
        System.out.print("Select the file to remove person from: ");
        int choice = input.nextInt();
        input.nextLine(); // consume newline
        
        if (choice == 0) {
            return null; // Cancel
        } else if (choice >= 1 && choice <= files.length) {
            File selectedFile = files[choice - 1];
            System.out.println("Selected file: " + selectedFile.getName());
            return selectedFile;
        } else {
            System.out.println("Invalid file number.");
            return null;
        }
    }
    
    // PRE: A valid Person object and File are passed in
    // POST: Removes the specified person from the file by rewriting it without that person
    private void removePersonFromFile(Person personToRemove, File file) {
        try {
            // Read all lines from the file
            java.util.List<String> lines = new java.util.ArrayList<>();
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            
            // Rewrite the file, excluding the person to be removed
            try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
                boolean inDataSection = false;
                
                for (String line : lines) {
                    // Check if we've reached the data section (after the separator line)
                    if (line.startsWith("--------------------")) {
                        writer.println(line);
                        inDataSection = true;
                        continue;
                    }
                    
                    // If we're in the data section, check if this line represents the person to remove
                    if (inDataSection && !line.trim().isEmpty() && !line.contains("=== End of List ===")) {
                        // Parse the line to check if it matches the person to remove
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 2) {
                            String fileLastName = parts[0].trim();
                            String fileFirstName = parts[1].trim();
                            
                            // Compare with the person to remove
                            if (fileLastName.equalsIgnoreCase(personToRemove.getLastName()) && 
                                fileFirstName.equalsIgnoreCase(personToRemove.getFirstName())) {
                                // Skip this line (don't write it back)
                                System.out.println("Removed person from file: " + personToRemove.getFirstName() + " " + personToRemove.getLastName());
                                continue;
                            }
                        }
                    }
                    
                    // Write all other lines back to the file
                    writer.println(line);
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error removing person from file: " + e.getMessage());
        }
    }
    
    // PRE: A valid File object is passed in
    // POST: Loads persons from the file and returns them as an ArrayList
    private ArrayList<Person> loadPersonsFromFile(File file) {
        ArrayList<Person> loadedPersons = new ArrayList<>();
        
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            boolean inDataSection = false;
            
            while ((line = reader.readLine()) != null) {
                // Skip header lines until we reach the data section
                if (line.startsWith("--------------------")) {
                    inDataSection = true;
                    continue;
                }
                
                // If we're in the data section and the line has content
                if (inDataSection && !line.trim().isEmpty() && 
                    !line.contains("=== End of List ===") && 
                    !line.contains("=== Film Project Personnel List ===") &&
                    !line.contains("Generated on:")) {
                    
                    // Parse the line to extract person information
                    // The format is: LastName FirstName Contact Type (fixed width columns)
                    if (line.length() >= 60) { // Minimum expected line length
                        String lastName = line.substring(0, 20).trim();
                        String firstName = line.substring(20, 40).trim();
                        String contact = line.substring(40, 65).trim();
                        String type = line.substring(65).trim();
                        
                        // Create appropriate person object based on type
                        Person person = null;
                        switch (type.toLowerCase()) {
                            case "actor":
                                person = new Actor(firstName, lastName, contact, "Unknown"); // Default specialty
                                break;
                            case "crew":
                                person = new Crew(firstName, lastName, contact, "Unknown"); // Default department
                                break;
                            case "volunteer":
                                person = new Volunteer(firstName, lastName, contact, "Unknown"); // Default task
                                break;
                        }
                        
                        if (person != null) {
                            loadedPersons.add(person);
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        
        return loadedPersons;
    }
}
