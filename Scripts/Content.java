import java.time.LocalDateTime;
import java.util.List;


public class Content
{
    protected int id;
    protected String title, description;
    protected LocalDateTime creationDate, publicationDate;
    protected User author;
    protected State state;
    protected List<Tags> tags;
    protected Category category;

    Content(int id, String title, String description, LocalDateTime creationDate, LocalDateTime publicationDate, User author, State state, List<Tags> tags, Category category)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.publicationDate = publicationDate;
        this.author = author;
        this.state = state;
        this.tags = tags;
        this.category = category;
    }

    public String toString() //to string default
    {
        return "Content ID: " + id + ", Title: " + title + ", Description: " + description + ", Created On: " + creationDate.toString() + ", Published On: " + publicationDate.toString() + ", Author: " + author.getName() + ", State: " + state.toString() + ", Category: " + category.toString();
    }

    public int getId() { //Getters and Setters
        return id;
    }

    public String getTitle() {
        return title;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public Category getCategory() {
        return category;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void publicate() //apartir de aca podes cambiar lo que sea
    {
        this.state = State.PUBLICADO;
        this.publicationDate = LocalDateTime.now();
    }

    public String visualize()
    {
        return toString();
    }

    public void depublicate()
    {
        this.state = State.BORRADOR;
        this.publicationDate = null;
    }

    public boolean isPublished()
    {
        return this.state == State.PUBLICADO;
    }
    





    



}