public class Tags 
{
    private int id;
    private String name;

    public Tags(int id, String name) 
    {
        this.id = id;
        this.name = name;
    }

    public int getId() 
    {
        return id;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    @Override
    public String toString() 
    {
        return "Tag ID: " + id + ", Name: " + name;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tags tag = (Tags) obj;
        return id == tag.id;
    }

    @Override
    public int hashCode() 
    {
        return Integer.hashCode(id);
    }
}

