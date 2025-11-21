public class Person {
    protected String firstName;
    protected String lastName;
    protected String contact;

    public Person(String firstName, String lastName, String contact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContact() {
        return contact;
    }
    
    // Method to get the type of person - to be overridden by subclasses
    public String getPersonType() {
        return "Person";
    }

    @Override
    public String toString() {
        return "Person: " + firstName + " " + lastName +
               " | Contact: " + contact;
    }
}
