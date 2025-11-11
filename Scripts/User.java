
import java.time.LocalDateTime;
import java.util.List;

public abstract class User
{
    protected int id;
    protected String name, email, password;
    protected LocalDateTime registerDate;

    User(int id, String name, String email, String password, LocalDateTime registerDate)//constructor
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.registerDate = registerDate;
    }

    public abstract List<String> getPermissions(); //metodos que deben tener editor y admin
    public abstract boolean canMakeActions();

    public boolean emailValidation() //chamba de sebas
    {
        return true; 
    }


    public boolean passwordValidation() //chamba de sebas
    {
        return true;
    }

    public String toString() //to string default
    {
        return "User ID: " + id + ", Name: " + name + ", Email: " + email + ", Registered On: " + registerDate.toString();
    }

    public int getId() {  //Getters (no se si password debería tener getter)
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }
}