public class SUM3{
  public static void main(String[] args){
    check3(args);
  }
  private static boolean check3(String[] args){
    for (int i=0; i<args.length; i++){
      for(int j=0; j<args.length; j++){
        for(int k=0; k<args.length; k++){
          if(Integer.parseInt(args[i])+Integer.parseInt(args[j])+Integer.parseInt(args[k])==0){
            System.out.println("true");return true;}
          }
        }
      }
    System.out.println("false");return false;
  }
}
