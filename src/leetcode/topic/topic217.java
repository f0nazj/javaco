package leetcode.topic;

import java.util.HashSet;

public class topic217 {
    public static void main(String[] args) {
        System.out.println(containsDuplicate(new int[]{1, 2, 3, 1})); // Output: true
        System.out.println(containsDuplicate(new int[]{1, 2, 3, 4})); // Output: false
        System.out.println(containsDuplicate(new int[]{1, 1, 1, 3, 3, 4, 3, 2, 4, 2})); // Output: true
    }

    public static boolean containsDuplicate(int[] nums) {
    // 你的程式碼
    HashSet<Integer> set = new HashSet<>();
        for(int n : nums){
            if(set.add(n) == false){
                return true;
            }
        }
        return false;
    }
}

