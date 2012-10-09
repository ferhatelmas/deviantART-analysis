package devparser;

public class Main {

  public static void main(String[] args) {
    Parse p = new Parse();

    if(args.length == 0) System.out.println("Wrong parameters");
    else {
      if(Integer.parseInt(args[0]) == 1) {
        if(args.length == 2) p.parse(args[1] ,0);
        else if(args.length == 3) p.parse(args[1], Integer.parseInt(args[2]));
        else System.out.println("Wrong parameters");
      } else if(Integer.parseInt(args[0]) == 2){
        if(args.length == 1) p.parse(0);
        else if (args.length == 2) p.parse(Integer.parseInt(args[1]));
        else System.out.println("Wrong parameters");
      } else System.out.println("Wrong parameters");
    }
    //if(args.length == 0) new Parse().parse();
    //else if(args.length == 1) new Parse().parse(args[0], 0);
    //else if(args.length == 2) new Parse().parse(args[0], Integer.parseInt(args[1]));
    //else System.out.println("Unnecessary parameters");

    //Parse.test();
    }
}