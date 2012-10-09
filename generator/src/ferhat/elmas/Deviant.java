package ferhat.elmas;

import java.util.ArrayList;

public class Deviant {
  private int id;
  private double effect;
  private int favSize;

  public Deviant(int id, double effect) {
    this.id = id;
    this.effect = effect;
    this.favSize = 0;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public double getEffect() {
    return effect;
  }

  public void setProb(double effect) {
    this.effect = effect;
  }

  public void increaseFavSize() {
    this.favSize++;
  }

  public int getFavSize() {
    return favSize;
  }
}