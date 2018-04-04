package be.tabtabstudio.veganapp.data.network.requestBodies;

public class UserSignupBody extends UserLoginBody {
    public final String firstname;
    public final String lastname;

    public UserSignupBody(String firstname, String lastname, String email, String password) {
        super(email, password);
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
