package leetcode.topic;

public class topic26 {
    public static void main(String[] args){
        System.out.println(removeDuplicates(new int[]{1,1,2})); // Output: 2
        System.out.println(removeDuplicates(new int[]{0,0,1,1,1,2,2,3,3,4})); // Output: 5
    }
    public static int removeDuplicates(int[] nums) {
    // 你的程式碼
    int count = 0;
    for (int i = 0; i < nums.length; i++) {
        if (nums[i] != nums[count]) {
            count++;
            nums[count] = nums[i];
        }
    }
    System.out.println("nums: " + java.util.Arrays.toString(nums));
    return count + 1;
    }
}
