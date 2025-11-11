import java.time.LocalDateTime;
import java.util.List;

public class Image extends Content 
{
    private String url;
    private double size;
    private String format;

    public Image(int id, String title, String description, LocalDateTime creationDate, LocalDateTime publicationDate, User author, State state, List<Tags> tags, Category category, String url, double size, String format) 
    {
        super(id, title, description, creationDate, publicationDate, author, state, tags, category);
        this.url = url;
        this.size = size;
        this.format = format;
    }

    public String getUrl() 
    {
        return url;
    }

    public void setUrl(String url) 
    {
        this.url = url;
    }

    public double getSize() 
    {
        return size;
    }

    public void setSize(double size) 
    {
        this.size = size;
    }

    public String getFormat() 
    {
        return format;
    }

    public void setFormat(String format) 
    {
        this.format = format;
    }

    public String getSizeFormatted() 
    {
        if (size < 1024) 
        {
            return String.format("%.2f KB", size);
        } 
        else if (size < 1024 * 1024) 
        {
            return String.format("%.2f MB", size / 1024);
        } 
        else 
        {
            return String.format("%.2f GB", size / (1024 * 1024));
        }
    }

    @Override
    public String visualize() 
    {
        return super.toString() + "\nURL: " + url + "\nSize: " + getSizeFormatted() + 
            "\nFormat: " + format;
    }

    @Override
    public String toString() 
    {
        return super.toString() + ", Size: " + getSizeFormatted() + ", Format: " + format;
    }
}