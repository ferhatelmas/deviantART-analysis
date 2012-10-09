package ferhat.elmas;

import java.util.ArrayList;

public class Deviation {
  private int id;
  private ArrayList<Deviant> favList;
  private ArrayList<Integer> favTimes;
  private double quality;
  private int prioClass;

  public Deviation(int id, double quality) {
    this.id = id;
    this.quality = quality;
    this.favList = new ArrayList<Deviant>();
    this.favTimes = new ArrayList<Integer>();
    this.prioClass = 1;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ArrayList<Deviant> getFavList() {
    return favList;
  }

  public void setFavList(ArrayList<Deviant> favList) {
    this.favList = favList;
  }

  public ArrayList<Integer> getFavTimes() {
    return favTimes;
  }

  public void setFavTimes(ArrayList<Integer> favTimes) {
    this.favTimes = favTimes;
  }

  public double getQuality() {
    return quality;
  }

  public void setProb(double quality) {
    this.quality = quality;
  }

  public void updatePrioClass(int size) {
    if(favList.size() > 0.9 * size) {
      prioClass = 1;
    } else if(favList.size() > 0.8 * size) {
      prioClass = 2;
    } else if(favList.size() > 0.7 * size) {
      prioClass = 3;
    } else if(favList.size() > 0.6 * size) {
      prioClass = 4;
    } else if(favList.size() > 0.5 * size) {
      prioClass = 5;
    } else if(favList.size() > 0.4 * size) {
      prioClass = 6;
    } else if(favList.size() > 0.3 * size) {
      prioClass = 7;
    } else if(favList.size() > 0.2 * size) {
      prioClass = 8;
    } else if(favList.size() > 0.1 * size) {
      prioClass = 9;
    } else {
      prioClass = 10;
    }
  }

  public int getPrioClass() {
    return this.prioClass;
  }
}