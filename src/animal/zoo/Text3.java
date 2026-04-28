package animal.zoo;

import java.util.HashMap;

class Text3 {
    public static void main(String[] args) {
        int[] arr = {3, 5, 2, 8, 1};
        int target = 10;
        int[] result = twoSum(arr, target);
        System.out.println(result[0] + ", " + result[1]);
    }
    public static int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if(map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        return null;
    }
}