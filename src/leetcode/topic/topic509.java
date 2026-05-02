package leetcode.topic;
public class topic509 {
    public static void main(String[] args) {
        System.out.println(fib(6));  // 輸出：8
    }

    public static int fib(int n) {
        // 1. 終止條件：n=0 時，費波那契數列第0項為 0
        if (n == 0) return 0;

        // 2. 終止條件：n=1 時，費波那契數列第1項為 1
        if (n == 1) return 1;

        // 3. 遞迴：當前項 = 前一項 + 前兩項
        //    例如：fib(6) = fib(5) + fib(4)
        //                 = 5     + 3
        //                 = 8
        return fib(n - 1) + fib(n - 2);
    }
}