public class Crew extends Person {
    private String department;

    public Crew(String firstName, String lastName, String contact, String department) {
        super(firstName, lastName, contact);
        this.department = department;
    }

    @Override
    public String getPersonType() {
        return "Crew";
    }

    @Override
    public String toString() {
        return "Crew: " + firstName + " " + lastName +
               " | Contact: " + contact +
               " | Department: " + department;
    }
}
