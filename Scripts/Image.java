public class Image extends Content {
    private String format;
    private String dimensions; 

    public Image(int id, String title, String author, String body, String format, String dimensions) {
        super(id, title, author, body);
        this.format = (format == null || format.isBlank()) ? "jpg" : format;
        this.dimensions = (dimensions == null || dimensions.isBlank()) ? "0x0" : dimensions;
    }

    public String getFormat() { return format; }
    public String getDimensions() { return dimensions; }
    public void setFormat(String f) { if (f != null && !f.isBlank()) this.format = f; }
    public void setDimensions(String d) { if (d != null && !d.isBlank()) this.dimensions = d; }

    @Override public String preview() {
        return "[Image] " + getTitle() + " " + dimensions + " ." + format;
    }
}
