package leetcode.topic;

public class topic136 {
    public static void main(String[] args) {
        System.out.println(singleNumber(new int[]{2, 2, 1})); // Output: 1
        System.out.println(singleNumber(new int[]{4, 1, 2, 1, 2})); // Output: 4
        System.out.println(singleNumber(new int[]{1})); // Output: 1
    }

    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
}