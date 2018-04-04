package be.tabtabstudio.veganapp.data.entities;

public class User {
    public static User getMock() {
        User u = new User();
        u.userid = 2;
        u.firstname = "Bob";
        u.lastname = "De Bouwer";
        return u;
    }

    public long userid;
    public String firstname;
    public String lastname;

    public String getName() {
        return firstname + " " + lastname.substring(0, 1);
    }
}
