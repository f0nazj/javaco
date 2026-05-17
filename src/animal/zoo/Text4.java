package animal.zoo;

public class Text4{
    public static void main(String[] args) {
        notifyUser(new EmailNotification());
        notifyUser(new SMSNotification());
    }
    public static void notifyUser(Notification n){
        n.send("登入成功");
    }

    interface Notification {
        void send(String message);
    }

    static class EmailNotification implements Notification{
        @Override
        public void send(String message){
            System.out.println("Email通知: " + message);
        }
    }

    static class SMSNotification implements Notification{
        @Override
        public void send(String message){
            System.out.println("SMS通知: " + message);
        }
    }
}