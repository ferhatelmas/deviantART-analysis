package deviant;

public class Deviant {

  private int deviant_id;
  private String deviant_name;
  private short deviant_type;

  public Deviant() {
  }

  public int getDeviant_id() {
    return deviant_id;
  }

  public void setDeviant_id(int deviant_id) {
    this.deviant_id = deviant_id;
  }

  public String getDeviant_name() {
    return deviant_name;
  }

  public void setDeviant_name(String deviant_name) {
    this.deviant_name = deviant_name;
  }

  public short getDeviant_type() {
    return deviant_type;
  }

  public void setDeviant_type(short deviant_type) {
    this.deviant_type = deviant_type;
  }
}
