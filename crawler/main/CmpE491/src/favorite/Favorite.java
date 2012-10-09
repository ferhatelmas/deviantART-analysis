package favorite;

import utils.Date;

public class Favorite {

  private int deviant_id;
  private int deviation_id;
  private Date favorite_date;

  public Favorite() {
  }

  public int getDeviant_id() {
    return deviant_id;
  }

  public void setDeviant_id(int deviant_id) {
    this.deviant_id = deviant_id;
  }

  public int getDeviation_id() {
    return deviation_id;
  }

  public void setDeviation_id(int deviation_id) {
    this.deviation_id = deviation_id;
  }

  public Date getFavorite_date() {
    return favorite_date;
  }

  public void setFavorite_date(Date favorite_date) {
    this.favorite_date = favorite_date;
  }

  public void setFavorite_date(java.sql.Date favorite_date) {
    this.favorite_date = new Date(favorite_date);
  }

  public java.sql.Date getAdjustedDate() {
    return favorite_date.convert();
  }
}
