import java.time.LocalDateTime;
import java.util.List;

public class Article extends Content 
{
    private String content;
    private int wordCount;

    public Article(int id, String title, String description, LocalDateTime creationDate, LocalDateTime publicationDate, User author, State state, List<Tags> tags, Category category, String content) 
    {
        super(id, title, description, creationDate, publicationDate, author, state, tags, category);
        this.content = content;
        this.wordCount = calculateWordCount();
    }

    public String getContent() 
    {
        return content;
    }

    public void setContent(String content) 
    {
        this.content = content;
        this.wordCount = calculateWordCount();
    }

    public int getWordCount() 
    {
        return wordCount;
    }

    private int calculateWordCount() 
    {
        if (content == null || content.trim().isEmpty()) 
        {
            return 0;
        }
        return content.trim().split("\\s+").length;
    }

    @Override
    public String visualize() 
    {
        return super.toString() + "\nContent: " + content + "\nWord Count: " + wordCount;
    }

    @Override
    public String toString() 
    {
        return super.toString() + ", Word Count: " + wordCount;
    }
}