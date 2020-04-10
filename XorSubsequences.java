import java.util.*;

public class XorSubsequences {

    public static int maxSubsequenceLength(int n, int k, List<Integer> arr) {
        int[] counts = new int[1048576];
        Set<String> options = new HashSet<>();
        for(int a: arr){
            StringBuilder option = new StringBuilder();
            if(counts[a ^ k] > 0){
                option.append(a).append(" ").append(a ^ k);
                options.add(option.toString());
            }

            counts[a]++;
        }
        int currentMax = 1;
        for(String option: options){
            StringTokenizer tokenizer = new StringTokenizer(option);
            int a = Integer.parseInt(tokenizer.nextToken());
            int b = Integer.parseInt(tokenizer.nextToken());
            int[] two = new int[]{a, b};
            int i_a = 0;
            int j_a = 0;
            int currentLong = 0;
            while(j_a < arr.size()){
                if(arr.get(j_a) == two[i_a]){
                    i_a++;
                    currentLong++;
                }
                if(i_a > 1){
                    i_a = 0;
                }
                j_a++;
            }
            int ans = currentLong;
            currentLong = 0;
            j_a = 0;
            i_a = 0;
            while(j_a < arr.size()){
                if(arr.get(j_a) == two[(i_a + 1) % 2]){
                    i_a++;
                    currentLong++;
                }
                if(i_a > 1){
                    i_a = 0;
                }
                j_a++;
            }
            ans = Math.max(currentLong, ans);
            currentMax = Math.max(currentMax, ans);
        }
        return currentMax;
    }

    public static void main(String[] args) {
        List<Integer> items = Arrays.asList(2,1,3,5,2);
        List<Integer> items2 = Arrays.asList(121,144,121,144,121,144,121,144,121);
        List<Integer> items3 = Arrays.asList(1,1,1);
        List<Integer> items4 = Collections.singletonList(100000);
        List<Integer> items5 = Arrays.asList(4,3,9, 14,4,3, 9);
        int k = 2;
        int k2 = 233;
        int k3 = 0;
        int k4 = 1;
        int k5 = 10;
        int ans = maxSubsequenceLength(items.size(), k, items);
        int ans2 = maxSubsequenceLength(items2.size(), k2, items2);
        int ans3 = maxSubsequenceLength(items3.size(), k3, items3);
        int ans4 = maxSubsequenceLength(items4.size(), k4, items4);
        int ans5 = maxSubsequenceLength(items5.size(), k5, items5);
        System.out.println(ans5);
    }
}
