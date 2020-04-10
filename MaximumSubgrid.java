import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MaximumSubgrid {

    private static int subSum(int i1, int j1, int i2, int j2, int[][] sum){
        int total = sum[i2][j2];

        if (j1 - 1 >= 0)
            total -= sum[i2][j1 - 1];

        if (i1 - 1 >= 0)
            total -= sum[i1 - 1][j2];

        if (i1 - 1 >= 0 && j1 - 1 >= 0)
            total += sum[i1 - 1][j1 - 1];
        return total;
    }

    public static void printSubtract(int i, int j, int i1, int j2, List<List<Integer>> grid){
        for(int kk = i; kk <= i1; kk++){
            for(int k = j; k <= j2; k++){
                System.out.print(grid.get(kk).get(k)+" ");
            }
            System.out.println();
        }
    }
    private static int linearized(int k, int[][] sum, int maxSum){
        int n = sum.length;
        int thisSum = 0;
        int ans = -1;
        boolean spoilt = false;
        for(int i = 0; i < (n * n); i++){
            int row = (i / n);
            int column = (i % n);
            int end = row + k - 1;
            int endColumn = column + k - 1;
            if(end < n && endColumn < n){
                thisSum = subSum(row, column, end, endColumn, sum);
                if(thisSum <= maxSum){
                    ans = thisSum;
                } else{
                    spoilt = true;
                    break;
                }
            }
        }
        return spoilt ? -1 : ans;
    }

    private static int doSearch(int low, int high, int[][] sum, int maxSum, int currentMax, List<List<Integer>> grid) throws IOException{
        if(low >= high){
            return currentMax;
        }

        int mid = (low + high) >> 1;
        int containsValida = linearized(mid,sum, maxSum);
        if(containsValida == -1){
            return doSearch(low, mid, sum, maxSum, currentMax, grid);
        } else {
            return doSearch(mid + 1, high, sum, maxSum, mid, grid);
        }
    }

    public static int largestSubgrid(List<List<Integer>> grid, int maxSum) throws IOException{
        int[][] sum = new int[grid.size()][grid.size()];
        sum[0][0] = grid.get(0).get(0);
        boolean hasPossibility = grid.get(0).get(0) <= maxSum;
        for(int i = 1; i < sum.length; i++){
            int current_column = grid.get(0).get(i);
            int current_row = grid.get(i).get(0);
            sum[0][i] = current_column + sum[0][i-1];
            sum[i][0] = current_row + sum[i - 1][0];
            if(Math.min(current_row, current_column) > maxSum){
                hasPossibility = false;
            }
        }

        for(int i = 1; i < sum.length; i++){
            for(int j = 1; j < sum.length;j++){
                sum[i][j] = sum[i - 1][j] + sum[i][j - 1] + grid.get(i).get(j) - sum[i - 1][j - 1];
                if(grid.get(i).get(j) > maxSum){
                    hasPossibility = false;
                }
            }
        }

        if(sum[sum.length - 1][sum.length - 1] <= maxSum){
            return sum.length;
        }
        int valid = doSearch(2, sum.length  , sum, maxSum, 0, grid);
        return hasPossibility ? (valid == 0 ? 1 : valid) : 0;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("matrix.in"));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("matrix.out"));

        int gridRows = Integer.parseInt(bufferedReader.readLine().trim());
        int gridColumns = Integer.parseInt(bufferedReader.readLine().trim());

        List<List<Integer>> grid = new ArrayList<>();

        IntStream.range(0, gridRows).forEach(i -> {
            try {
                grid.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        int maxSum = Integer.parseInt(bufferedReader.readLine().trim());

        int result = largestSubgrid(grid, maxSum);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
