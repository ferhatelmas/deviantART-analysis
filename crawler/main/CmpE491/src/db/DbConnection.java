package db;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import deviation.Deviation;
import deviant.Deviant;

import favorite.Favorite;
import utils.Date;

public class DbConnection {

  private Connection conn;

  private PreparedStatement deviationsGet;
  private PreparedStatement deviationGet;
  private PreparedStatement deviationPut;

  private PreparedStatement deviantsGet;
  private PreparedStatement deviantGet;
  private PreparedStatement deviantPut;

  private PreparedStatement favoritePut;

  private PreparedStatement favoriteDeviantsGet;

  private PreparedStatement favoriteIncreasePut;
  private PreparedStatement favoriteIncreaseGet;

  private PreparedStatement effectPut;

  private PreparedStatement favoritesGet;

  public Connection getConn() {
    return conn;
  }

  public void setConn(Connection conn) {
    this.conn = conn;
  }

  public DbConnection() {

    conn = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      String url = "jdbc:mysql://host:port/db";
      String userName = "userName";
      String password = "password";

      conn = (Connection) DriverManager.getConnection(url, userName, password);

      deviationsGet = conn.prepareStatement("SELECT * FROM deviations WHERE deviation_id>?;");
      deviationGet  = conn.prepareStatement("SELECT * FROM deviations WHERE deviation_deviantart_id=?;");
      deviationPut  = conn.prepareStatement("INSERT INTO deviations (deviation_deviantart_id, deviation_name, deviation_url, deviation_deviant_id, deviation_date) VALUES (?, ?, ?, ?, ?);");

      deviantsGet = conn.prepareStatement("SELECT * FROM deviants;");
      deviantGet    = conn.prepareStatement("SELECT * FROM deviants WHERE deviant_name=?");
      deviantPut    = conn.prepareStatement("INSERT INTO deviants (deviant_name, deviant_type) VALUES (?, ?);");

      favoritePut   = conn.prepareStatement("INSERT INTO favorites (deviant_id, deviation_id, favorite_date) VALUES (?, ?, ?);");

      favoriteDeviantsGet = conn.prepareStatement("SELECT * FROM favorites WHERE deviation_id=? ORDER BY favorite_date;");

      favoriteIncreasePut = conn.prepareStatement("INSERT INTO favoriteIncrease (deviation_id, deviant_id, passed_days, favorite_count) VALUES (?, ?, ?, ?);");
      favoriteIncreaseGet = conn.prepareStatement("SELECT * FROM favoriteIncrease WHERE deviant_id=?;");

      effectPut = conn.prepareStatement("INSERT INTO deviantEffect (deviant_id, deviant_effect) VALUES (?, ?);");

      favoritesGet = conn.prepareStatement("SELECT favorite_date FROM favorites ORDER BY favorite_date;");

    } catch (ClassNotFoundException ex) {
      System.out.println("Driver could not be loaded");
      System.exit(1);
    } catch (SQLException sqlEx) {
      System.out.println("Database could not be accessed or queries could not be created");
      System.out.println(sqlEx.getMessage());
      System.exit(2);
    }
  }

  public void closeConn() {
    try {
      deviationsGet.close();
      deviationGet.close();
      deviationPut.close();

      deviantGet.close();
      deviantPut.close();

      favoritePut.close();

      conn.close();
    } catch (SQLException sqlEx) {
      System.out.println("Prepared queries or database connection could not be closed");
    }
  }

  public List<Deviation> getAllDeviations() {
    return getAllDeviations(0);
  }

  public List<Deviation> getAllDeviations(int start) {
    ArrayList<Deviation> deviations = new ArrayList<Deviation>();
    try {
      deviationsGet.setInt(1, start);
      ResultSet rs = deviationsGet.executeQuery();
      while(rs.next()) {
        Deviation d = new Deviation();

        d.setDeviation_id(rs.getInt("deviation_id"));
        d.setDeviation_deviantart_id(rs.getLong("deviation_deviantart_id"));
        d.setDeviation_deviant_id(rs.getInt("deviation_deviant_id"));
        d.setDeviation_name(rs.getString("deviation_name"));
        d.setDeviation_url(rs.getString("deviation_url"));

        utils.Date date = new utils.Date();
        String strDate = rs.getDate("deviation_date").toString();
        date.setDay(Integer.parseInt(strDate.substring(8, 10)));
        date.setMonth(Integer.parseInt(strDate.substring(5, 7)));
        date.setYear(Integer.parseInt(strDate.substring(0, 4)));
        d.setDeviation_date(date);

        deviations.add(d);
      }
      rs.close();
    } catch (SQLException sqlEx) {}
    return deviations;
  }

  public Deviation getDeviation(long deviation_deviantart_id) {
    try {
      deviationGet.setLong(1, deviation_deviantart_id);
      ResultSet rs = deviationGet.executeQuery();
      if(rs.first()) {
        Deviation d = new Deviation();

        d.setDeviation_id(rs.getInt("deviation_id"));
        d.setDeviation_deviantart_id(rs.getLong("deviation_deviantart_id"));
        d.setDeviation_deviant_id(rs.getInt("deviation_deviant_id"));
        d.setDeviation_name(rs.getString("deviation_name"));
        d.setDeviation_url(rs.getString("deviation_url"));

        utils.Date date = new utils.Date();
        String strDate = rs.getDate("deviation_date").toString();
        date.setDay(Integer.parseInt(strDate.substring(8, 10)));
        date.setMonth(Integer.parseInt(strDate.substring(5, 7)));
        date.setYear(Integer.parseInt(strDate.substring(0, 4)));
        d.setDeviation_date(date);

        rs.close();

        return d;
      } else {
        return null;
      }
    } catch (SQLException sqlEx) {
      return null;    
    }
  }

  public void putDeviation(Deviation d) {
    if(getDeviation(d.getDeviation_deviantart_id()) == null) {
      try {
        deviationPut.setLong(1, d.getDeviation_deviantart_id());
        deviationPut.setString(2, d.getDeviation_name());
        deviationPut.setString(3, d.getDeviation_url());
        deviationPut.setLong(4, d.getDeviation_deviant_id());
        deviationPut.setDate(5, d.getAdjustedDate());

        deviationPut.executeUpdate();
      } catch (SQLException sqlEx) {
          System.out.println("Deviation could not be added: " + d.getDeviation_name());
      }
    }
  }

  public List<Deviant> getAllDeviants() {
    List<Deviant> deviants = new ArrayList<Deviant>();
    try {
      ResultSet rs = deviantsGet.executeQuery();
      while(rs.next()) {
        Deviant d = new Deviant();

        d.setDeviant_type(rs.getShort("deviant_type"));
        d.setDeviant_id(rs.getInt("deviant_id"));
        d.setDeviant_name(rs.getString("deviant_name"));

        deviants.add(d);
      }
      rs.close();
    } catch (SQLException sqlEx) {}
    return deviants;
  }

  public Deviant getDeviant(String deviant_name) {
    try {
      deviantGet.setString(1, deviant_name);
      ResultSet rs = deviantGet.executeQuery();
      if(rs.first()) {
        Deviant d = new Deviant();

        d.setDeviant_id(rs.getInt("deviant_id"));
        d.setDeviant_name(deviant_name);
        d.setDeviant_type(rs.getShort("deviant_type"));

        rs.close();

        return d;
      } else {
        return null;
      }
    } catch (SQLException sqlEx) {
      return null;
    }
  }

  public void putDeviant(Deviant d) {
    if(getDeviant(d.getDeviant_name()) == null) {
      try {
        deviantPut.setString(1, d.getDeviant_name());
        deviantPut.setShort(2, d.getDeviant_type());

        deviantPut.executeUpdate();
      } catch (SQLException sqlEx) {
        System.out.println("Deviant could not be added: " + d.getDeviant_name());
      }
    }
  }

  public void putFavorite(Favorite f) {
    try {
      favoritePut.setInt(1, f.getDeviant_id());
      favoritePut.setInt(2, f.getDeviation_id());
      favoritePut.setDate(3, f.getAdjustedDate());

      favoritePut.executeUpdate();
    } catch (SQLException sqlEx) {
      System.out.println("Favorite could not be added=> deviant: " + 
        f.getDeviant_id() + " deviation: " + f.getDeviation_id());
    }
  }

  public List<Favorite> getFavoritesOfDeviation(Deviation deviation) {
    List<Favorite> favorites = new ArrayList<Favorite>();
    try {
      favoriteDeviantsGet.setInt(1, deviation.getDeviation_id()); 
      ResultSet rs = favoriteDeviantsGet.executeQuery();
      while(rs.next()) {
        Favorite favorite = new Favorite();

        favorite.setDeviant_id(rs.getInt("deviant_id"));
        favorite.setDeviation_id(rs.getInt("deviation_id"));
        favorite.setFavorite_date(rs.getDate("favorite_date"));

        favorites.add(favorite);
      }
      rs.close(); 
    } catch (SQLException sqlEx) {}
    return favorites;
  }

  public void putFavoriteIncrease (int deviation_id, int deviant_id, 
    int passedDays, int favoriteCount) {

    try {
      favoriteIncreasePut.setInt(1, deviation_id);
      favoriteIncreasePut.setInt(2, deviant_id);
      favoriteIncreasePut.setInt(3, passedDays);
      favoriteIncreasePut.setInt(4, favoriteCount);

      favoriteIncreasePut.executeUpdate();
    } catch (SQLException sqlEx) {
      System.out.println("FavoriteIncrease could not be added Deviation_id: " + deviation_id);
    }
  }

  public void putEffectOfDeviant(Deviant deviant) {
    try {
      favoriteIncreaseGet.setInt(1, deviant.getDeviant_id());
      ResultSet rs = favoriteIncreaseGet.executeQuery();
      float effectiveCoefficient = 0;
      int count = 0;
      while (rs.next()) {
        count++;
        float favoriteCount = rs.getInt("favorite_count");
        int passedDays = rs.getInt("passed_days");
        effectiveCoefficient += favoriteCount / ++passedDays;
      }

      if(count != 0) effectiveCoefficient /= count;
      rs.close();

      effectPut.setInt(1, deviant.getDeviant_id());
      effectPut.setFloat(2, effectiveCoefficient);

      effectPut.executeUpdate();
    } catch (SQLException sqlEx) {}
  }

  public java.sql.Date getFirstFavoriteDate() {
    try {
      ResultSet rs = favoritesGet.executeQuery();
      while(rs.next()) {
        if(rs.isFirst()) {
          return rs.getDate("favorite_date");
        }
      }
    } catch (SQLException sqlEx) {}
    return null;
  }

  public java.sql.Date getLastFavoriteDate() {
    try {
      ResultSet rs = favoritesGet.executeQuery();
      while(rs.next()) {
        if(rs.isLast()) {
          return rs.getDate("favorite_date");
        }
      }
    } catch (SQLException sqlEx) {}
    return null;
  }

  public int getDataLength() {
    return Date.getDifference(getFirstFavoriteDate(), getLastFavoriteDate());
  }
}