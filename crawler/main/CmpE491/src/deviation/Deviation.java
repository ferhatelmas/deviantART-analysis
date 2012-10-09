package deviation;

import utils.Date;

import java.net.MalformedURLException;
import java.net.URL;

public class Deviation {

  private int deviation_id;
  private long deviation_deviantart_id;
  private String deviation_name;
  private String deviation_url;
  private int deviation_deviant_id;
  private Date deviation_date;

  public Deviation() {
  }

  public int getDeviation_id() {
    return deviation_id;
  }

  public void setDeviation_id(int deviation_id) {
    this.deviation_id = deviation_id;
  }

  public long getDeviation_deviantart_id() {
    return deviation_deviantart_id;
  }

  public void setDeviation_deviantart_id(long deviation_deviantart_id) {
    this.deviation_deviantart_id = deviation_deviantart_id;
  }

  public String getDeviation_name() {
    return deviation_name;
  }

  public void setDeviation_name(String deviation_name) {
    this.deviation_name = deviation_name;
  }

  public String getDeviation_url() {
    try {
      URL url = new URL(deviation_url);
      return "http://" + url.getHost().toLowerCase() + url.getPath();
    } catch (MalformedURLException me) {
      return deviation_url;
    }
  }

  public void setDeviation_url(String deviation_url) {
    this.deviation_url = deviation_url;
  }

  public int getDeviation_deviant_id() {
    return deviation_deviant_id;
  }

  public void setDeviation_deviant_id(int deviation_deviant_id) {
    this.deviation_deviant_id = deviation_deviant_id;
  }

  public Date getDeviation_date() {
    return deviation_date;
  }

  public void setDeviation_date(Date deviation_date) {
    this.deviation_date = deviation_date;
  }

  public java.sql.Date getAdjustedDate() {
    return deviation_date.convert();
  }
}