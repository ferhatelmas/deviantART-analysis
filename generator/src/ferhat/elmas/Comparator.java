package ferhat.elmas;

import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class Comparator {
  public static void main(String[] args) {
    Network network = new Network(10000, 20000);
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("db.txt")));
      for(int i=0; i<10; i++) {
        for(int j=0; j<10; j++) {
          for(int k=1; k<6; k++) {
            for(int l=1; l<11; l++) {
              for(double m=0.1; m<=1; m+=0.1) {
                for(double n=0.0025; n<=0.01; n+=0.0025) {
                  try {
                    pw.write("10\n");
                    pw.write(String.valueOf(i) + "\n");
                    pw.write(String.valueOf(j) + "\n");
                    pw.write(String.valueOf(m) + "\n");
                    pw.write(String.valueOf(m) + "\n");
                    pw.write(String.valueOf(k) + "\n");
                    pw.write(String.valueOf(l) + "\n");
                    //pw.write(String.valueOf(network.run(10, i, j, m, n, k, l, false)) + "\n");
                  } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                  }
                }
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