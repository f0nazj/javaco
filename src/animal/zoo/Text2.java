package animal.zoo;

public class Text2 {
    public static void main(String[] args) throws Exception {
        Payment p1 = new LinePay();
        p1.pay(100);

        Payment p2 = new CashPay();
        p2.pay(200);
    }
    interface Payment {
        void pay(int money);
    }
    static class LinePay implements Payment {
        @Override
        public void pay(int money) {
            System.out.println("LinePay支付了" + money + "元");
        }
    }
    static class CashPay implements Payment {
        @Override
        public void pay(int money) {
            System.out.println("CashPay支付了" + money + "元");
        }
    }
}