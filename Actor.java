public class Actor extends Person {
    private String specialty;

    public Actor(String firstName, String lastName, String contact, String specialty) {
        super(firstName, lastName, contact);
        this.specialty = specialty;
    }

    @Override
    public String getPersonType() {
        return "Actor";
    }

    @Override
    public String toString() {
        return "Actor: " + firstName + " " + lastName +
               " | Contact: " + contact +
               " | Specialty: " + specialty;
    }
}
