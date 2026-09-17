package leetcode.topic;

import java.util.Arrays;

public class topic242 {
    public static void main(String[] args) {
        System.out.println(isAnagram("anagram", "nagaram")); // Output: true
        System.out.println(isAnagram("rat", "car")); // Output: false
    }

    public static boolean isAnagram(String s, String t) {
        // 你的程式碼
        if(s.length() != t.length()) {
            return false;
        }
        char[] arrS = s.toCharArray();
        char[] arrT = t.toCharArray();
        Arrays.sort(arrS);
        Arrays.sort(arrT);
        return Arrays.equals(arrS, arrT);
    }
}