import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class test {

  public static void main(String[] args) throws InterruptedException, IOException {
//    int[][] b = new int[][]{{1,3},{5,4},{6,6}};
//    Arrays.sort(b, (int[]x,int[]y)-> Integer.compare(x[0]-x[1],y[0]-y[1]));
//    for(int i=0; i<b.length;i++){
//      for(int j=0; j<b[0].length; j++){
//        System.out.print(b[i][j]);
//      }
//      System.out.println();
//    }
//    System.out.println(b);
//    List a = Arrays.asList(4,3,5,2,7);
//    Collections.sort(a);

    List<Integer> c = new ArrayList<Integer>();
    c.add(1);
    c.add(4);
    c.add(3);
    Collections.sort(c);
    System.out.println(c);
  }
}


