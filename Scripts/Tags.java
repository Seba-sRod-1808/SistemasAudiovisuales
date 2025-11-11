import java.util.Objects;

// clase simplificada para representar etiquetas los articulos del estudio
public class Tags {
    private final String name;

    public Tags(String name) {
        this.name = Objects.requireNonNull(name).trim().toLowerCase();
    }

    public String getName() { return name; }

    @Override public String toString() { return "#" + name; }
}
