import java.util.Objects;

public abstract class User {
    private final int id;
    private final String name;
    private final String email;
    private final String password; // no cifrada, solo para ejemplo

    protected User(int id, String name, String email, String password) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email).toLowerCase();
        this.password = Objects.requireNonNull(password);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public boolean authenticate(String pwd) {
        return password.equals(pwd);
    }

    public abstract boolean canCreate();
    public abstract boolean canEdit();
    public abstract boolean canPublish();
    public abstract boolean canDelete();

    @Override public String toString() {
        return getClass().getSimpleName() + "(" + id + ", " + name + ", " + email + ")";
    }
}
