import java.util.*;

public class Cans {
    public static int minCans(List<Integer> used, List<Integer> total) {
        int sum = used.stream().reduce(0, Integer::sum);
        total.sort((o1, o2) -> -(o1 - o2));
        int containers = 0;
        while(sum > 0){
            if(containers >= total.size()) break;
            sum -= total.get(containers);
            containers++;
        }
        return sum <= 0 ? containers : 0;
    }

    public static void main(String[] args) {
        int ans = minCans(Collections.singletonList(4), Collections.singletonList(3));
        System.out.println(ans);
    }
}
