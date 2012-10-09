package ferhat.elmas;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class NetworkThread extends Thread {

  private double alfa;
  private double beta;
  private int deviationWindow;
  private int setupWindow;
  private int deviationDistribution;
  private int deviantDistribution;
  private Network network;

  public NetworkThread(double alfa, double beta, int deviationWindow, 
    int setupWindow, int deviationDistribution, int deviantDistribution) {
    this.alfa = alfa;
    this.beta = beta;
    this.deviationWindow = deviationWindow;
    this.setupWindow = setupWindow;
    this.deviationDistribution = deviationDistribution;
    this.deviantDistribution = deviantDistribution;
    this.network = new Network(10000, 20000);
  }

  public void run() {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.getName() + ".txt")));
      for(int i=0; i<deviantDistribution; i++) {
        for(int j=0; j<deviationDistribution; j++) {
          for(int k=1; k<deviationWindow; k++) {
            for(int l=1; l<setupWindow; l++) {
              try {
                pw.write("10\n");
                pw.write(String.valueOf(i) + "\n");
                pw.write(String.valueOf(j) + "\n");
                pw.write(String.valueOf(alfa) + "\n");
                pw.write(String.valueOf(beta) + "\n");
                pw.write(String.valueOf(k) + "\n");
                pw.write(String.valueOf(l) + "\n");
                //pw.write(String.valueOf(network.run(10, i, j, alfa, beta, k, l, false)) + "\n");
              } catch (Exception ex) {
              System.out.println(ex.getMessage());
              }
            }
          }
        }
      }
    } catch(IOException ioe) {
      System.out.println(ioe.getMessage());
    } finally {
      if(pw != null) {
        pw.close();
      }
    }
  }
}