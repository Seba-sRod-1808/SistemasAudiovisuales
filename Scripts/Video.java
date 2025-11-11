import java.time.LocalDateTime;
import java.util.List;

public class Video extends Content 
{
    private String url;
    private int duration;
    private String resolution;

    public Video(int id, String title, String description, LocalDateTime creationDate, LocalDateTime publicationDate, User author, State state, List<Tags> tags, Category category, String url, int duration, String resolution) 
    {
        super(id, title, description, creationDate, publicationDate, author, state, tags, category);
        this.url = url;
        this.duration = duration;
        this.resolution = resolution;
    }

    public String getUrl() 
    {
        return url;
    }

    public void setUrl(String url) 
    {
        this.url = url;
    }

    public int getDuration() 
    {
        return duration;
    }

    public void setDuration(int duration) 
    {
        this.duration = duration;
    }

    public String getResolution() 
    {
        return resolution;
    }

    public void setResolution(String resolution) 
    {
        this.resolution = resolution;
    }

    public String getDurationFormatted() 
    {
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        
        if (hours > 0) 
        {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } 
        else 
        {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    @Override
    public String visualize() 
    {
        return super.toString() + "\nURL: " + url + "\nDuration: " + getDurationFormatted() + 
               "\nResolution: " + resolution;
    }

    @Override
    public String toString() 
    {
        return super.toString() + ", Duration: " + getDurationFormatted() + ", Resolution: " + resolution;
    }
}