package devprocess;

import db.DbConnection;
import deviation.Deviation;
import favorite.Favorite;
import utils.Date;

import java.util.List;

public class FavoriteRelationProcessor {

  public static void main(String[] args) {
    DbConnection conn = new DbConnection();
    List<Deviation> deviations = conn.getAllDeviations();
    for(Deviation d : deviations) {
      List<Favorite> favorites = conn.getFavoritesOfDeviation(d);
      if(favorites.size() > 0) {
        int max = favorites.size() - 1;
        for(int i=0; i<max; i++) {
          int count = max - i;
          int passedDays = getPassedDays(favorites.get(i).getFavorite_date(),
            favorites.get(max).getFavorite_date());
          conn.putFavoriteIncrease(d.getDeviation_id(), favorites.get(i)
            .getDeviant_id(), passedDays, count);
        }
        conn.putFavoriteIncrease(d.getDeviation_id(), 
          favorites.get(max).getDeviant_id(), 0, 0);   
      }
    }
  }

  private static int getPassedDays(Date first, Date last) {
    int passedDays = (last.getYear() - first.getYear()) * 365;
    passedDays += (last.getMonth() - first.getMonth()) * 30;
    passedDays += last.getDay() - first.getDay();
    return passedDays;
  }
}