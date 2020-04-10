import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class StockMarket {

    private static int binarySearch(int low, int high, int target, int[] tree, int n){
        if(low == high){
            int smallest = minimum(low, high, tree, n);
            return smallest < target ? low : -1;
        }
        int mid = (low + high) >> 1;
        if(minimum(low, high, tree, n) >= target){
            return -1;
        }
        int smallestLeft = minimum(low, mid, tree, n);
        int smallestRight = minimum(mid + 1, high, tree, n);
        if(smallestRight < target){
            return binarySearch(mid + 1, high, target, tree, n);
        }
        else if(smallestLeft < target){
            return binarySearch(low, mid, target, tree, n);
        }
        return -1;
    }

    private static int binarySearchRight(int low, int high, int target, int[] tree, int n){
        if(low == high){
            int smallest = minimum(low, high, tree, n);
            return smallest < target ? low : -1;
        }
        int mid = (low + high) >> 1;
        if(minimum(low, high, tree, n) >= target){
            return -1;
        }
        int smallestLeft = minimum(low, mid, tree, n);
        int smallestRight = minimum(mid + 1, high, tree, n);
        if(smallestLeft < target){
            return binarySearchRight(low, mid, target, tree, n);
        } else if(smallestRight < target) {
            return binarySearchRight(mid + 1, high, target, tree, n);
        }
        return -1;
    }

    private static int minimum(int start, int end, int[] tree, int n){
        return minimum(0,n - 1, start, end, tree, 0);
    }

    private static int minimum(int low, int high, int start, int end, int[] tree, int index) {
        if (start <= low && end >= high) {
            return tree[index];
        }

        if(high < start || low > end){
            return Integer.MAX_VALUE;
        }

        int mid = (low + high) >> 1;
        return Math.min(
                minimum(low, mid, start, end, tree, (index << 1) + 1),
                minimum(mid + 1, high, start, end, tree, (index << 1) + 2));
    }

    private static int segmentTreeUtil(int low, int high, int[] arr, int[] tree, int segmentIndex){
        if(low == high){
            tree[segmentIndex] = arr[low];
            return arr[low];
        }
        int mid = (low + high) >> 1;
        tree[segmentIndex] = Math.min(
                segmentTreeUtil(low, mid, arr, tree, (segmentIndex << 1) + 1),
                segmentTreeUtil(mid + 1, high, arr, tree,  (segmentIndex << 1) + 2));
        return tree[segmentIndex];
    }
    private static int[] segmentTreeUtil(int[] arr){
        int size = (int)Math.ceil(Math.log(arr.length) / Math.log(2) + 1e-14);
        int maxSize = 2 * (int)Math.pow(2, size) - 1;
        int[] dataStructure = new int[maxSize];
        segmentTreeUtil(0, arr.length - 1, arr, dataStructure, 0);
        return dataStructure;
    }

    public static List<Integer> predictAnswer(List<Integer> stockData, List<Integer> queries) {
        int[] arr = new int[stockData.size()];
        for(int i = 0; i < stockData.size();i++){
            arr[i] = stockData.get(i);
        }
        int[] tree = segmentTreeUtil(arr);
        List<Integer> res = new ArrayList<>();
        for(int query: queries){
            if(query > stockData.size()){
                res.add(-1);
            }else{
                query--;
                int left = binarySearch(0, query, arr[query], tree, arr.length);
                int right = binarySearchRight(query, arr.length, arr[query], tree, arr.length);
                if(left == -1 && right == -1){
                    res.add(-1);
                }
                else if(left == -1 || right == -1){
                    res.add(Math.max(left, right)+1);
                } else{
                    int diffLeft = query - left;
                    int diffRight = right - query;
                    if(diffLeft <= diffRight){
                        res.add(left + 1);
                    } else{
                        res.add(right + 1);
                    }
                }
            }
        }
        return res;
    }
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("custom.in"));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("custom.out"));

        int stockDataCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> stockData = IntStream.range(0, stockDataCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(toList());

        int queriesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> queries = IntStream.range(0, queriesCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(toList());

        List<Integer> result = predictAnswer(stockData, queries);

        bufferedWriter.write(
                result.stream()
                        .map(Object::toString)
                        .collect(joining("\n"))
                        + "\n"
        );

        bufferedReader.close();
        bufferedWriter.close();
    }
}
