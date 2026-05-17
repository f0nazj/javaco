package animal.zoo;

import tool.coding.ListNode;

public class Text {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);  // 先建立 head
        head.next = new ListNode('a');
        head.next.next = new ListNode('9');

    ListNode current = head;
        while (current != null) {
            System.out.println(current.val);
            current = current.next;
        }
    }
}
