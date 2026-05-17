package animal.zoo;

public class Text3 {

    public static void main(String[] args) {

        // 使用不同付款方式
        checkout(new LinePay());

        checkout(new CashPay());
    }

    // 統一結帳方法
    public static void checkout(Payment payment) {
        payment.pay(500);
    }

    // 付款規則
    interface Payment {
        void pay(int money);
    }

    // LinePay
    static class LinePay implements Payment {

        @Override
        public void pay(int money) {
            System.out.println("LinePay支付了 " + money + " 元");
        }
    }

    // 現金支付
    static class CashPay implements Payment {

        @Override
        public void pay(int money) {
            System.out.println("CashPay支付了 " + money + " 元");
        }
    }
}