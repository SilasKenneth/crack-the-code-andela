import java.sql.SQLOutput;
import java.util.*;

public class SubsequenceRemove {
    public static List<Integer> findSubsequence(List<Integer> arr) {
        int[] cnts = new int[1000002];
        int max = 0;
        for(int a: arr){
            cnts[a - 1]++;
            max =  Math.max(a, max);
        }
        int[] cnts2 = new int[max + 2];
        for(int i = max; i >= 1; i--){
            while(cnts[i - 1] > 1){
                cnts[i - 1]--;
                cnts2[i - 1]++;
            }
        }
        List<Integer> list2 = new ArrayList<>();
        for(int i = 1; i <= max; i++){
            if(cnts2[i - 1] > 0){
                while(cnts2[i - 1] >= 1){
                    cnts2[i - 1]--;
                    list2.add(i);
                }
            }
        }

        int i = 0;
        int j = 0;
        while(i < arr.size() && j < list2.size()){
            if(arr.get(i).compareTo(list2.get(j)) == 0){
                j++;
            }
            i++;
        }
        if(j < list2.size()){
            return Collections.singletonList(-1);
        }
        return list2;
    }

    public static void main(String[] args) {
        System.out.println(findSubsequence(Arrays.asList(1,4,3,2,4,1,1,1)));
    }
}
