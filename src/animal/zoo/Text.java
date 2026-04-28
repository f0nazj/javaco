package animal.zoo;

import java.util.Arrays;

public class Text {
    public static void main(String[] args) {
        int[] arr = {3, 7, 1, 9, 4};
        Arrays.sort(arr);

        int index = Arrays.binarySearch(arr, 9);
        System.out.println(index);
    }
}