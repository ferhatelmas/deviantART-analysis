package ferhat.elmas;

public class Generator implements Parameters   {

  public static void main(String[] args) throws Exception {
    /*
    Network network = new Network(Parameters.USER_NUMBER_OF, Parameters.RESOURCE_NUMBER_OF);

    for(int i=1; i<21; i++)
      for(int j=1;  j<11; j++) {
        System.out.println("dw:" + i + " sw:" + j);

        network.run(Parameters.SIMULATION_RUN_NUMBER_OF,
          Parameters.SIMULATION_LENGTH_OF,
          Parameters.USER_DISTRIBUTION_TYPE_OF,
          Parameters.RESOURCE_DISTRIBUTION_TYPE_OF,
          Parameters.DAILY_TRANSACTIONS_MU_SCALE_FACTOR_OF,
          Parameters.DAILY_TRANSACTIONS_SIGMA_SCALE_FACTOR_OF,
          i,
          j,
          Parameters.DERIVATION_WINDOW_LENGTH_OF,
          Parameters.SETUP_WINDOW_LENGTH_OF,
      }
    */
    Network n = new Network(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

      n.run(Integer.parseInt(args[2]), Integer.parseInt(args[3]), 
        Integer.parseInt(args[4]), Integer.parseInt(args[5]), 
        Parameters.AVERAGE_FAVORITE_LIST_SIZE, Integer.parseInt(args[6]),
        Integer.parseInt(args[7]), Integer.parseInt(args[8]), Byte.parseByte(args[9]));
    }
}