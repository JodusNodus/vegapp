package be.tabtabstudio.veganapp.data.network.requestBodies;

public class UserLoginBody {
    public final String email;
    public final String password;

    public UserLoginBody(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
