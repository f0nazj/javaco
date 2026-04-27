package leetcode.topic;

public class topic1_1 {
    public static void main(String[] args) {
        // 1. 創建一個二維數組, 用來存放測試數據和預期結果
        int[] result = twoSum(new int[] { 3, 2, 4 }, 6);
        // 2. 印出測試數據和預期結果
        System.out.println("Result: " + result[0] + ", " + result[1]);
    }
    public static int[] twoSum(int[] nums, int target) {
        // 1. 遍歷數組 i = 0
        for(int i = 0; i < nums.length; i++){
            // 2. 遍歷數組 j = i的下一個
            for(int j = i + 1; j < nums.length; j++){
                // 3. 如果 nums[i] + nums[j] = target
                if(nums[i] + nums[j] == target){
                    // 4. 返回 i 和 j
                    return new int[]{i, j};
                }
            }
        }
        // 5. 如果沒有找到符合的數組，返回 null
        return null;
    }
}
