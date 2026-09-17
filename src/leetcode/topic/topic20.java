package leetcode.topic;

import java.util.Stack;

public class topic20 {
    public static void main(String[] args){
        System.out.println(isValid("()")); // Output: true
        System.out.println(isValid("()[]{}")); // Output: true
        System.out.println(isValid("(]")); // Output: false
    }
    public static boolean isValid(String s) {
    // 你的程式碼
    char[] chars = s.toCharArray();
    Stack<Character> stack = new Stack<>();
    for (int i = 0; i < chars.length; i++) {
        if (chars[i] == '(' || chars[i] == '[' || chars[i] == '{') {
            stack.push(chars[i]);
        } else if (chars[i] == ')' && stack.isEmpty() || chars[i] == ']' && stack.isEmpty() || chars[i] == '}' && stack.isEmpty()) {
            return false;
        } else if (chars[i] == ')' && stack.peek() != '(' || chars[i] == ']' && stack.peek() != '[' || chars[i] == '}' && stack.peek() != '{') {
            return false;
        } else {
            stack.pop();
        }
    }
    if (stack.isEmpty()) {
        return true;
    }
    return false;
    }
}
