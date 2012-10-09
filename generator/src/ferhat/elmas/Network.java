package ferhat.elmas;

import jsc.distributions.Normal;

import java.util.*;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class Network {

  // random number generator
  private java.util.Random random;

  // the number of the deviants and deviations
  private int deviantCount;
  private int deviationCount;

  // the lists of the deviants and deviations
  private ArrayList<Deviant> deviants;
  private ArrayList<Deviation> deviations;

  // the id of the most effective user
  private int authorityID;

  // the length of the simulation
  private int simulationLength;

  private static int fileIndex=0;

  // constructor
  public Network(int deviantCount, int deviationCount) {
    this.deviantCount = deviantCount;
    this.deviationCount = deviationCount;
    this.deviants = new ArrayList<Deviant>();
    this.deviations = new ArrayList<Deviation>();
    this.random = new Random(System.currentTimeMillis());
  }

  //simulation length getter and setter
  private void setSimulationLength(int simulationLength) {
    this.simulationLength = simulationLength;
  }

  private int getSimulationLength() {
    return this.simulationLength;
  }

  public void run(int runNumber, // the number of how many times simulation will run
                  int simulationLength, // the number of days in wihch simulation runs
                  int deviantDistributionSelect, // which distribution is used for deviants
                  int deviationDistributionSelect, // which distribution is used for deviations
                  double alfa,   // average favorite list size of the deviants
                  int beta,   // a constant to calculate std of daily operations
                  int derivationWindow, // the length of the derivation window
                  int setupWindow, // the length of the setup window
                  byte normalizationFlag //flag to choose the normalization method
  ) {
    double mostEffectiveUser; // the effect value of the most effective user
    setSimulationLength(simulationLength);
    try {
      PrintWriter pw = new PrintWriter(new FileOutputStream("f0.txt"));
      pw.write(deviantCount + " " +
               deviationCount + " " +
               simulationLength + " " +
               deviantDistributionSelect + " " +
               deviationDistributionSelect + " " +
               alfa + " " +
               derivationWindow + " " +
               setupWindow + " " +
               normalizationFlag + "\n");

      // main simulation loop
      for(int i=0; i<runNumber; i++) {
        clear(); //clears old mappings from previous simulation

        //generates deviants and deviations with selected distributions
        generateDeviants(deviantDistributionSelect);
        generateDeviations(deviationDistributionSelect);

        System.out.println("Deviants");
        for(Deviant d : deviants) {
          System.out.println(d.getId() + "->" + d.getEffect());
        }

        System.out.println("Deviations");
        for(Deviation d : deviations) {
          System.out.println(d.getId() + "->" + d.getQuality());
        }
  
        //generate the mappings between deviant and deviations
        // transactions are specified with alfa(for all) and dayCnt(for daily)
        generateNetwork(alfa, beta);

        System.out.println("Deviations");
        for(Deviation d : deviations) {
          System.out.println(d.getId() + "->" +  d.getQuality());
          for(int k=0; k<d.getFavList().size(); k++) {
            System.out.print(d.getFavList().get(k).getId() + " ");
          }
          System.out.println("");
          for(int k=0; k<d.getFavList().size(); k++) {
            System.out.print(d.getFavTimes().get(k).intValue() + " ");
          }
          System.out.println("\n---");
        }

        //save the effect value of the most effective user
        mostEffectiveUser = deviants.get(authorityID).getEffect();

        // run the algorithm and get most effective user
        // in the loop, we get the ratio
        //System.out.println("Run:      " + (i+1));
        //System.out.println("Real:     " + mostEffectiveUser);
        List<Integer> list = getMostEffectiveDeviant(derivationWindow, 
          setupWindow, normalizationFlag);

        if(list.size()>1) {
          double total = 0;
          for(int j : list) {
            total += deviants.get(j).getEffect();
          }

          pw.write((total / list.size()) + "\n");
          pw.write(mostEffectiveUser + "\n");
        } else if (list.size() == 1){
          pw.write(deviants.get(list.get(0)).getEffect() + "\n");
          pw.write(mostEffectiveUser + "\n");
        } 
        pw.flush();
        Runtime.getRuntime().gc();
      }
      pw.close();    
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    fileIndex++;
  }

  // empty the list of the deviants and deviations
  private void clear() {
    deviants.clear();
    deviations.clear();
  }

  // generates deviants according to selected distribution
  private void generateDeviants(int deviantDistributionSelect) {

    double maxEffectiveCoefficient = Double.MIN_VALUE, // max effect
           tempEffectiveCoefficient; // temporary generated effect

    // generate deviants as deviantCount
    // generate an effect from one of the cases
    for(int i=0; i<deviantCount; i++) {
      if(deviantDistributionSelect == 0) {
        tempEffectiveCoefficient = 1-Math.sqrt(random.nextDouble());
      } else {
        tempEffectiveCoefficient = -Math.log(random.nextDouble()) / deviantDistributionSelect;
        while(tempEffectiveCoefficient >= 1) 
          tempEffectiveCoefficient = -Math.log(random.nextDouble()) / deviantDistributionSelect;
      }

      // if generated effect is larger than max, max will this generated effect and
      // related deviant(index) will be the most effective user
      if(tempEffectiveCoefficient > maxEffectiveCoefficient) {
        maxEffectiveCoefficient = tempEffectiveCoefficient;
        authorityID = i;
      }

      // create a deviant with id(index) and generated effect, then add deviant into deviant list
      deviants.add(new Deviant(i, tempEffectiveCoefficient));
    }
  }

  // generates deviations according to selected distribution
  private void generateDeviations(int deviationDistributionSelect) {
    double tempQualityCoefficient;  // temporary generated quality

    // generate deviations as deviationCount
    // generate a quality from one of the cases
    for(int i=0; i<deviationCount; i++) {
      if(deviationDistributionSelect == 0) {
        tempQualityCoefficient = 1-Math.sqrt(random.nextDouble());
      } else {
       tempQualityCoefficient = -Math.log(random.nextDouble()) / deviationDistributionSelect;
       while(tempQualityCoefficient >= 1) 
        tempQualityCoefficient = -Math.log(random.nextDouble()) / deviationDistributionSelect;
      }

      // create a deviation with id(index) and generated quality, then add deviation into deviation list
      deviations.add(new Deviation(i, tempQualityCoefficient));
    }
  }

  // generates the mappings between deviants and deviations    
  private void generateNetwork(double alfa, int beta) {
    int day,  // simulation day
        maxFavorite = 0; // the largest size of favorite lists of deviations
    long i,      // index parameter
         todo, // daily transaction number
         mean = (long)(Math.ceil((alfa * deviantCount) / getSimulationLength())); // daily transaction mean

    ArrayList<Integer> prioFirstClass = new ArrayList<Integer>();
    ArrayList<Integer> prioSecondClass = new ArrayList<Integer>();
    ArrayList<Integer> prioThirdClass = new ArrayList<Integer>();
    ArrayList<Integer> prioFourthClass = new ArrayList<Integer>();
    ArrayList<Integer> prioFifthClass = new ArrayList<Integer>();
    ArrayList<Integer> prioSixthClass = new ArrayList<Integer>();
    ArrayList<Integer> prioSeventhClass = new ArrayList<Integer>();
    ArrayList<Integer> prioEighthClass = new ArrayList<Integer>();
    ArrayList<Integer> prioNinethClass = new ArrayList<Integer>();
    ArrayList<Integer> prioTenthClass = new ArrayList<Integer>();

    ArrayList<Deviation> prioClass1 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass2 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass3 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass4 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass5 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass6 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass7 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass8 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass9 = new ArrayList<Deviation>();
    ArrayList<Deviation> prioClass10 = new ArrayList<Deviation>();

    ArrayList<Integer> bucket = new ArrayList<Integer>();
    for(int j=0; j<deviantCount; j++) bucket.add(j);
      int temp;
      for(int j=0; j<deviantCount/10; j++) {

      temp = random.nextInt(bucket.size());
      prioFirstClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioSecondClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioThirdClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioFourthClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioFifthClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioSixthClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioSeventhClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioEighthClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioNinethClass.add(bucket.get(temp));
      bucket.remove(temp);

      temp = random.nextInt(bucket.size());
      prioTenthClass.add(bucket.get(temp));
      bucket.remove(temp);
    }

    for(Deviation d : deviations) {
      prioClass1.add(d);
    }

    // distribution to generate daily transactions
    Normal normal = new Normal(mean, beta*mean);

    // main loop
    // as soon as we process transactions, all transactions is decreased this amount
    Deviant deviant;
    Deviation deviation = null;
    double prio;
    for(day=1; day<=getSimulationLength(); day++) {
      todo = (long)normal.random();
      for(i=0; i<todo; i++) {
        // pick one random deviant and deviation
        prio = random.nextDouble();
        if(prio < 0.18) {
          deviant = deviants.get(prioFirstClass.get(random.nextInt(prioFirstClass.size())));
        } else if(prio < 0.35) {
          deviant = deviants.get(prioSecondClass.get(random.nextInt(prioSecondClass.size())));
        } else if(prio < 0.49) {
          deviant = deviants.get(prioThirdClass.get(random.nextInt(prioThirdClass.size())));
        } else if(prio < 0.62) {
          deviant = deviants.get(prioFourthClass.get(random.nextInt(prioFourthClass.size())));
        } else if(prio < 0.73){
          deviant = deviants.get(prioFifthClass.get(random.nextInt(prioFifthClass.size())));
        } else if(prio < 0.82){
          deviant = deviants.get(prioSixthClass.get(random.nextInt(prioSixthClass.size())));
        } else if(prio < 0.89){
          deviant = deviants.get(prioSeventhClass.get(random.nextInt(prioSeventhClass.size())));
        } else if(prio < 0.95){
          deviant = deviants.get(prioEighthClass.get(random.nextInt(prioEighthClass.size())));
        } else if(prio < 0.98){
          deviant = deviants.get(prioNinethClass.get(random.nextInt(prioNinethClass.size())));
        } else {
          deviant = deviants.get(prioTenthClass.get(random.nextInt(prioTenthClass.size())));
        }

        prio = random.nextDouble();
        if(prio < 0.18) {
          if(prioClass1.size() > 0) deviation = prioClass1.get(random.nextInt(prioClass1.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else if(prio < 0.35) {
          if(prioClass2.size() > 0) deviation = prioClass2.get(random.nextInt(prioClass2.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else if(prio < 0.49) {
          if(prioClass3.size() > 0) deviation = prioClass3.get(random.nextInt(prioClass3.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else if(prio < 0.62) {
          if(prioClass4.size() > 0) deviation = prioClass4.get(random.nextInt(prioClass4.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else if(prio < 0.73){
          if(prioClass5.size() > 0) deviation = prioClass5.get(random.nextInt(prioClass5.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else if(prio < 0.82){
          if(prioClass6.size() > 0) deviation = prioClass6.get(random.nextInt(prioClass6.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else if(prio < 0.89){
          if(prioClass7.size() > 0) deviation = prioClass7.get(random.nextInt(prioClass7.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else if(prio < 0.95){
          if(prioClass8.size() > 0) deviation = prioClass8.get(random.nextInt(prioClass8.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else if(prio < 0.98){
          if(prioClass9.size() > 0) deviation = prioClass9.get(random.nextInt(prioClass9.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        } else {
          if(prioClass10.size() > 0) deviation = prioClass10.get(random.nextInt(prioClass10.size()));
          else deviation = deviations.get(random.nextInt(deviationCount));
        }

        // if selected deviant hasn't added the deviation into his list,
        // add deviation into list with the probability of the quality of the deviation percent
        if(!deviation.getFavList().contains(deviant)) {
          if(random.nextDouble() < deviation.getQuality()) {
            deviant.increaseFavSize();
            deviation.getFavList().add(deviant);
            deviation.getFavTimes().add(day);
            deviation.setProb(Math.max(deviation.getQuality(), deviant.getEffect()));
            temp = Math.max(maxFavorite, deviation.getFavList().size());
            if(temp != maxFavorite) {
              maxFavorite = temp;
              prioClass1.clear();
              prioClass2.clear();
              prioClass3.clear();
              prioClass4.clear();
              prioClass5.clear();
              prioClass6.clear();
              prioClass7.clear();
              prioClass8.clear();
              prioClass9.clear();
              prioClass10.clear();

              for(Deviation d : deviations) {
                  d.updatePrioClass(maxFavorite);
              }
              for(Deviation d : deviations) {
                temp = d.getPrioClass();
                switch (temp) {
                  case 1: prioClass1.add(d); break;
                  case 2: prioClass2.add(d); break;
                  case 3: prioClass3.add(d); break;
                  case 4: prioClass4.add(d); break;
                  case 5: prioClass5.add(d); break;
                  case 6: prioClass6.add(d); break;
                  case 7: prioClass7.add(d); break;
                  case 8: prioClass8.add(d); break;
                  case 9: prioClass9.add(d); break;
                  case 10: prioClass10.add(d); break;
                }
              }
            } else {
              deviation.updatePrioClass(maxFavorite);
            }
          }
        }
      }
    }
  }

  // the core algorithm
  // takes generated network and two window length and then finds the most effective user
  private ArrayList<Integer> getMostEffectiveDeviant(int derivationWindow,
                                                     int setupWindow,
                                                     byte normalizationFlag) {
    // holds the ticks of the users
    // if a user is important and effective in the timeline of any deviation, he takes one tick
    // so the more tick a user has, the more effective user he is
    HashMap<Integer, Double> map = new HashMap<Integer, Double>();

    int maxListSize = getMaxFavListSize();
    // iterate over the all deviations
    for(Deviation deviation : deviations) {
      updateMap(map, deviation.getFavTimes(), deviation.getFavList(), 
        getPeakDays(createFrequencyVector(deviation.getFavTimes()), derivationWindow),
        setupWindow, maxListSize, normalizationFlag);
    }
    //get the keys of the biggest values from map
    return getEffectiveDeviantFromMap(map, normalizationFlag);
  }

  private void writePearson(HashMap<Integer, Double> map, byte normalizationFlag) {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("pearson" + fileIndex + ".txt")));
      for(int i=0; i<deviantCount; i++) {
        if(map.containsKey(i)){
          if(normalizationFlag == 0 || normalizationFlag == 2)
            pw.write(map.get(i) / deviants.get(i).getFavSize() + "\n");
          else
            pw.write(map.get(i) + "\n");
          } else pw.write("0\n");
        pw.write(deviants.get(i).getEffect() + "\n");
        pw.write(deviants.get(i).getFavSize() + "\n");
      }
      fileIndex++;
    } catch (Exception ex) {
          System.out.println(ex.getMessage());
    } finally {
      if(pw != null) {
          pw.close();
      }
    }
  }

  //the method to find the max favorite list size
  private int getMaxFavListSize() {
    int max = Integer.MIN_VALUE, temp;
    for(Deviation d : deviations) {
      temp = d.getFavList().size();
      //System.out.println(temp + "-->" + d.getPrioClass());
      if(temp > max) max = temp;
    }
    return max;
  }

  // creates an array for deviation such that
  // index of the array is the day of the simulation
  // value of this cell is the number of the transactions that
  // deviants has added the deviation into their list
  private int[] createFrequencyVector(ArrayList<Integer> favTimes) {
    //initialization of freq array
    int[] freq = new int[getSimulationLength()+1];

    //initially fill whole frequency array with zero
    Arrays.fill(freq, 0);

    //start from the first day
    int day = 1;

    //calculate frequencies
    for(int nxt : favTimes) {
      if(nxt == day) {
        freq[day]++;
      } else {
        day = nxt;
        freq[day] = 1;
      }
    }   
    return freq;
  }

  //test of network functions 
  public static void main(String[] args) {
      Network n = new Network(10, 20);
      n.setSimulationLength(20);
      System.out.println((((2.0241 * 1000) / 10000)));
  }

  // get the days that most of the transactions have happened
  private ArrayList<Integer> getPeakDays(int[] freq, int derivationWindow) {

    // list to hold the max days if the days are more than one
    ArrayList<Integer> sumOfPeakDays = new ArrayList<Integer>();
    ArrayList<Integer> tempPeakDays = new ArrayList<Integer>();        
    ArrayList<Integer> peakDays = new ArrayList<Integer>();

    int maxPeak = Integer.MIN_VALUE, // overall max
        tempPeak;  // temporary sum of the frequencies of the days

    // iterate over frequency array and compare the subsums
    for(int i=0; i<freq.length-derivationWindow+1; i++) {
      tempPeak = 0;
      for(int j=0; j<derivationWindow; j++) {
        tempPeak += freq[i+j];
      }

      if(tempPeak > maxPeak) {
        maxPeak = tempPeak;
        sumOfPeakDays.clear();
        sumOfPeakDays.add(i);
      } else if (tempPeak == maxPeak) {
        sumOfPeakDays.add(i);
      }
    }

    int index;
    for(int day : sumOfPeakDays) {
      maxPeak = freq[day];
      index = day;
      for(int i=1; i<derivationWindow; i++) {
        if(freq[day + i] > maxPeak) {
          maxPeak = freq[day + i];
          index = day + i;
        }
      }
      if(!tempPeakDays.contains(index)) {
        tempPeakDays.add(index);
      }
    }

    maxPeak = Integer.MIN_VALUE;
    for(int day : tempPeakDays) {
      if(day > 0) {

        tempPeak = freq[day] - freq[day-1];
        if(tempPeak > maxPeak) {
          maxPeak = tempPeak;
          peakDays.clear();
          peakDays.add(day);
        } else if (tempPeak == maxPeak) {
          peakDays.add(day);
        }
      }
    }
    return peakDays;
  }

  private void updateMap(HashMap<Integer, Double> map, //favorite ticks of the users
                         ArrayList<Integer> favTimes,   //deviation favorite times
                         ArrayList<Deviant> favs,       //deviants that added the deviation
                         ArrayList<Integer> peakDays,   //the days in which most transactions have happened
                         int setupWindow,               //the length of window
                         int maxFavListSize,            //the maximum favorite list size
                         byte normalizationFlag         //the flag to choose the normalization method) {

    int nxt, //temporary variable to hold the day of the transaction in which deviant with
             //id added the deviation into his favorite list
        id,  //id of the deviant(user)
        add; //holds the number of the effective users in the setup window
             //if this setup window is empty(add=0), then first day of the
             //derivation window is used
    double val; //temporary variable to hold the value of the user in the map

    for(int dayPtr : peakDays) {
      add = 0;
      for(int i=0; i<favTimes.size(); i++) {
        nxt = favTimes.get(i);

        //here transactions have happened in setup window or in the first day of the derivation window
        if(nxt >= dayPtr-setupWindow && nxt < dayPtr) {
          add++; // transactions in the setup window are counted
          id = favs.get(i).getId();

          //if map contains, increment the value else add the first value
          if(map.containsKey(id)) {
            val = map.get(id);
            map.remove(id);
            if(normalizationFlag == 0 || normalizationFlag == 1) {
              val += favTimes.size() / ((double)maxFavListSize);
            } else {
              val++;
            }
            map.put(id, val);
          } else {
            if(normalizationFlag == 0 || normalizationFlag == 1) {
              map.put(id, favTimes.size() / ((double)maxFavListSize));
            } else{
              map.put(id, (double)1);    
            }
          }
          // here there is no transaction in setup window
          // therefore the transactions of the first day
          // of the derivation window are processed
        } /*else if(nxt == dayPtr && add == 0) {
          id = favs.get(i).getId();

          //if map contains, increment the value else add the first value
          if(map.containsKey(id)) {

            val = map.get(id);
            map.remove(id);
            if(normalizationFlag == 0 || normalizationFlag == 1) {
              val += favTimes.size() / ((double)maxFavListSize);
            } else {
              val++;
            }
            map.put(id, val);
          } else {
            if(normalizationFlag == 0 || normalizationFlag == 1) {
              map.put(id, favTimes.size() / ((double)maxFavListSize));
            } else {
              map.put(id, (double)1);
            }
          }
        // here day pointer is further than setup window and first
        // day of the derivation window, so there is no need to continue
        } else if((nxt == dayPtr && add != 0) || nxt > dayPtr) break;
          */
      else if(nxt >= dayPtr) break;
        //
        //don't forget above code part
        //
        //
      }
    }
  }

  private ArrayList<Integer> getEffectiveDeviantFromMap(HashMap<Integer, Double> map, 
    byte normalizationFlag) {

    double maxTick = Double.MIN_VALUE, // max tick number in the mapping
        tempTick; // temporary tick number to use in updating

    // List of the effective users
    ArrayList<Integer> list = new ArrayList<Integer>();

    // iterate over the mapping get the biggest ticks and related deviants       
    for(int key : map.keySet()) {

      if(normalizationFlag == 0 || normalizationFlag == 2) {
        tempTick = map.get(key) / deviants.get(key).getFavSize();
      } else {
        tempTick = map.get(key);
      }

      if(tempTick > maxTick) {
        maxTick = tempTick;
        list.clear();
        list.add(key);
      } else if (tempTick == maxTick) {
        list.add(key);
      }
    }
    writePearson(map, normalizationFlag);
    return list;
  }

  // writes the given map into file to be plotted by Matlab
  public void writeMap(HashMap<Integer, Double> map) {
    PrintWriter pw = null;
    try {
      // Create file for output
      pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("matlabPlotFileCalculatedDeviants.txt")));

      // print the frequencies
      for(int i : map.keySet()) {
        pw.write(map.get(i) + "\n");
      }
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    } finally {
      //close the file
      if(pw != null) {
        pw.close();
      }
    }
  }

  // writes the generated deviants into file to be plotted by Matlab
  public void writeDeviants() {
    PrintWriter pw = null;
    try {
      // Create file for output
      pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("matlabPlotFileGeneratedDeviants.txt")));

      // create frequency array
      int[] freq = new int[100];
      Arrays.fill(freq, 0);

      for(Deviant deviant : deviants) {
        freq[(int)(deviant.getEffect()/0.01)]++;
      }
      for(int i : freq) {
        pw.write(i + "\n");
      }
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    } finally {
      //close the file
      if(pw != null) {
        pw.close();
      }
    }
  }

  // writes the generated deviants into file to be plotted by Matlab
  public void writeDeviations() {
    PrintWriter pw = null;
    try {
      // Create file for output
      pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("matlabPlotFileGeneratedDeviations.txt")));

      // create frequency array
      int[] freq = new int[100];
      Arrays.fill(freq, 0);

      for(Deviation deviation : deviations) {
        freq[(int)(deviation.getQuality()/0.01)]++;
      }

      for(int i : freq) {
        pw.write(i + "\n");
      }
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    } finally {
      //close the file
      if(pw != null) {
        pw.close();
      }
    }
  }
}