import java.util.Objects;

public class Category {
    private final String name;
    private final String description;

    public Category(String name, String description) {
        this.name = Objects.requireNonNull(name);
        this.description = description == null ? "" : description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override public String toString() { return name; }
}
