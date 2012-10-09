package comment;

public class Comment {

  private String owner="";
  private String timestamp="";
  private String content="";
  private String url="";
  private String mood="";

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMood() {
    return mood;
  }

  public void setMood(String mood) {
    this.mood = mood;
  }
}