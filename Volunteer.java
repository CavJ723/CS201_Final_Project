public class Volunteer extends Person {
    private String task;

    public Volunteer(String firstName, String lastName, String contact, String task) {
        super(firstName, lastName, contact);
        this.task = task;
    }

    @Override
    public String getPersonType() {
        return "Volunteer";
    }

    @Override
    public String toString() {
        return "Volunteer: " + firstName + " " + lastName +
               " | Contact: " + contact +
               " | Task: " + task;
    }
}
