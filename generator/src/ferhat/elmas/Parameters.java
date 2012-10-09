package ferhat.elmas;

public interface Parameters {

  // number of users and resources
  int USER_NUMBER_OF = 1000;
  int RESOURCE_NUMBER_OF = 3000;

  // simulation is repeated as this number
  int SIMULATION_RUN_NUMBER_OF = 100;

  // simulation length, simulation will span these many days
  int SIMULATION_LENGTH_OF = 500;

  // distribution selecter for generations
  int USER_DISTRIBUTION_TYPE_OF = 8;
  int RESOURCE_DISTRIBUTION_TYPE_OF = 8;

  // scale factors for daily transaction generations
  double AVERAGE_FAVORITE_LIST_SIZE = 2 * 5;//2.0241;
  //double DAILY_TRANSACTIONS_MU_SCALE_FACTOR_OF = 0.02;
  int DAILY_TRANSACTIONS_SIGMA_SCALE_FACTOR_OF = 2;

  // window lengths
  int DERIVATION_WINDOW_LENGTH_OF = 10;
  int SETUP_WINDOW_LENGTH_OF = 15;
}