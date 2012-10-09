package devprocess;

import db.DbConnection;
import deviant.Deviant;

import java.util.List;

public class FavoriteIncreaseProcessor {
  public static void main(String[] args) {
    DbConnection conn = new DbConnection();
    List<Deviant> deviants = conn.getAllDeviants();
    for(Deviant d : deviants) {
      conn.putEffectOfDeviant(d);
    }
  }
}
