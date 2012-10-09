package effective;

import db.DbConnection;
import deviant.Deviant;
import deviation.Deviation;
import favorite.Favorite;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Finder {
  private static HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

  public static void main(String[] args) {
    DbConnection conn = new DbConnection();

    Date firstDate = conn.getFirstFavoriteDate();
    Date lastDate = conn.getLastFavoriteDate();

    int length = utils.Date.getDifference(firstDate, lastDate);
    if(length == 0) return;
    for(Deviation deviation : conn.getAllDeviations()) {
      List<Favorite> favorites = conn.getFavoritesOfDeviation(deviation);
      int[] times = createFavoriteTimes(favorites, firstDate, lastDate);
      ArrayList<Integer> days = getMaxSubSum(times);
      putEffectiveDeviants(days, favorites, firstDate);
    }
    for(int i : getEffectiveDeviantFromMap()) System.out.println(i);
  }

  private static int[] createFavoriteTimes(List<Favorite> favorites, Date d1, Date d2) {
    int length = utils.Date.getDifference(d1, d2);
    int[] times = new int[length];

    int diff;
    for(Favorite f : favorites) {
      diff = utils.Date.getDifference(d1, f.getFavorite_date().convert());
      if(times[diff] == 0) times[diff]=1;
      else times[diff]++;
    }
    return times;
  }

  private static ArrayList<Integer> getMaxSubSum(int[] times) {
    ArrayList<Integer> list = new ArrayList<Integer>();

    int max = Integer.MIN_VALUE, tmp;
    for(int i=0; i<times.length-2; i++) {
      tmp = times[i] + times[i+1] + times[i+2];
      if(tmp>max) {
        max = tmp;
        list.clear();
        list.add(i);
      } else if(tmp == max) {
        list.add(i);
      }
    }
    return list;
  }

  private static void putEffectiveDeviants(ArrayList<Integer> list, 
    List<Favorite> favorites, Date first) {

    HashMap<Integer, Integer> local = new HashMap<Integer, Integer>();
    for(int day : list) {
      for(Favorite f : favorites) {
        int diff = utils.Date.getDifference(first, f.getAdjustedDate());
        if(diff>=day-10 && diff<day) {
          if(local.containsKey(f.getDeviant_id())) {
            int val = local.get(f.getDeviant_id());
            local.remove(f.getDeviant_id());
            local.put(f.getDeviant_id(), ++val);
          } else {
            local.put(f.getDeviant_id(), 1);
          }
        }
      }
    }
    if(!local.isEmpty()) {
      for(int key : local.keySet()) {
        if(map.containsKey(key)) {
          int val = map.get(key);
          val += local.get(key);
          map.put(key, val);
        } else {
          map.put(key, local.get(key));
        }
      }
    }
  }

  private static ArrayList<Integer> getEffectiveDeviantFromMap() {
    int max = Integer.MIN_VALUE, val;
    ArrayList<Integer> list = new ArrayList<Integer>();

    for(int key : map.keySet()) {
      val = map.get(key);
      if(val > max) {
        max = val;
        list.clear();
        list.add(key);
      } else if (val == max) {
        list.add(key);
      }
    }
    return list;
  }
}