package animal.zoo;

public class Text {
    public static void main(String[] args) {
        System.out.println(multiply(5));
    }

    public static int multiply(int n) {
        if (n == 1){
            return 1; // 終止條件
        }
        return n * multiply(n - 1); // 呼叫自己
    }
}