public class Article extends Content {
    private String summary;

    public Article(int id, String title, String author, String body) {
        super(id, title, author, body);
        this.summary = body == null ? "" : body.substring(0, Math.min(100, body.length()));
    }

    public String getSummary() { return summary; }
    public void setSummary(String s) { this.summary = (s == null) ? "" : s; }

    @Override public String preview() {
        return "[Article] " + getTitle() + " — " + getAuthor();
    }
}