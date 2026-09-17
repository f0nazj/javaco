package leetcode.topic;

public class topic344 {
    public static void main(String[] args){
        char[] s = {'h', 'e', 'l', 'l', 'o'};
        reverseString(s);
        for (char c : s) {
            System.out.print(c);
        }
    }
    public static void reverseString(char[] s) {
    // 你的程式碼
        int left = 0;
        int right = s.length - 1;
        while (left < right) {
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;
            left++;
            right--;
        }
    }
}
