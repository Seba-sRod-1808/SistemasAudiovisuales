public class Video extends Content {
    private String url;
    private int durationSec;

    public Video(int id, String title, String author, String body, String url, int durationSec) {
        super(id, title, author, body);
        this.durationSec = Math.max(0, durationSec);
    }

    public String getUrl() { return url; }
    public int getDurationSec() { return durationSec; }
    public void setUrl(String url) { this.url = (url == null) ? "" : url; } // ternario para evitar null
    public void setDurationSec(int s) { this.durationSec = Math.max(0, s); }

    @Override public String preview() {
        return "[Video] " + getTitle() + " (" + durationSec + "s)";
    }
}
