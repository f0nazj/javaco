package animal.zoo;

import java.util.ArrayList;
import java.util.Arrays;

public class Text {
    
    public static void main(String[] args) {
        ArrayList<Integer> animals = new ArrayList<>(Arrays.asList(10, 3, 7, 1, 5));
        animals.sort((a, b) -> a - b);
        for (Integer animal : animals) {
            System.out.println(animal);
        }
        System.out.println("----------");
        int[] arr = {10, 3, 7, 1, 5};
        Arrays.sort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
        System.out.println("----------");
        int index = Arrays.binarySearch(arr, 7);
        
        System.out.println(index);
    }
}