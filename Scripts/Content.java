import java.time.LocalDateTime;
import java.util.*;

public abstract class Content implements Comparable<Content> {
    private final int id;
    private String title;
    final String author;
    private String body;
    private State state;
    private Category category;
    private final List<Tags> tags = new ArrayList<>();
    private final LocalDateTime createdAt;
    private LocalDateTime publishedAt;
    private int views;
    private int likes;

    protected Content(int id, String title, String author, String body) {
        this.id = id;
        this.title = Objects.requireNonNull(title);
        this.author = Objects.requireNonNull(author);
        this.body = body == null ? "" : body;
        this.state = State.BORRADOR;
        this.createdAt = LocalDateTime.now();
    }

    // Getters básicos
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getBody() { return body; }
    public State getState() { return state; }
    public Category getCategory() { return category; }
    public List<Tags> getTags() { return Collections.unmodifiableList(tags); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public int getViews() { return views; }
    public int getLikes() { return likes; }

    // Metodos de edicion
    public void editTitle(String newTitle) { this.title = Objects.requireNonNull(newTitle); }
    public void editBody(String newBody)   { this.body  = Objects.requireNonNullElse(newBody, ""); }

    public void assignCategory(Category c) { this.category = Objects.requireNonNull(c); }

    public void addTag(Tags t) {
        if (t == null) return;
        for (Tags e : tags) if (e.getName().equals(t.getName())) return;
        tags.add(t);
    }

    public void removeTag(String name) {
        tags.removeIf(t -> t.getName().equalsIgnoreCase(name));
    }

    public boolean publish() {
        if (state == State.ELIMINADO) return false;
        if (!validate()) return false;
        state = State.PUBLICADO;
        publishedAt = LocalDateTime.now();
        return true;
    }

    public boolean markDeleted() {
        if (state == State.ELIMINADO) return false;
        state = State.ELIMINADO;
        return true;
    }

    public boolean isPublished() { return state == State.PUBLICADO; }

    public void addView() { views++; }
    public void like()    { likes++; }

    // Validación minima para el contenido
    public boolean validate() {
        return title != null && !title.isBlank() && author != null && !author.isBlank();
    }

    public abstract String preview();

    @Override public int compareTo(Content o) {
        return this.title.compareToIgnoreCase(o.title);
    }

    @Override public String toString() {
        return "#" + id + " [" + getClass().getSimpleName() + "] " + title + " (" + state + ")";
    }
}
