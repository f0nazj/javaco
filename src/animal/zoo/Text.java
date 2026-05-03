package animal.zoo;

import tool.coding.ListNode;

public class Text {
    public static void main(String[] args) {
        ListNode current = head;

        while (current != null) {
            System.out.println(current.val);
            current = current.next;
        }
    }
}