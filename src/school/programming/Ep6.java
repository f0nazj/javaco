package school.programming;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Ep6 implements ActionListener {
    JButton jb1, jb2, jb3;

    public Ep6() {
        // ✅ 在建構子中直接建立 GUI
        JFrame jf = new JFrame("介面測試");
        jf.setSize(800, 600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLayout(null);
        jf.setLocationRelativeTo(null);

        jb1 = new JButton("按鈕1");
        jb1.setBounds(100, 100, 200, 50);
        jb1.addActionListener(this);
        jf.add(jb1);

        jb2 = new JButton("按鈕2");
        jb2.setBounds(100, 200, 200, 50);
        jb2.addActionListener(this);
        jf.add(jb2);

        jb3 = new JButton("按鈕3");
        jb3.setBounds(100, 300, 200, 50);
        jb3.addActionListener(this);
        jf.add(jb3);

        jf.setVisible(true); // 👈 別忘了這個
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == jb1) {
            JOptionPane.showMessageDialog(null, "按鈕1被點擊了");
        } else if (source == jb2) {
            JOptionPane.showMessageDialog(null, "按鈕2被點擊了");
        } else if (source == jb3) {
            JOptionPane.showMessageDialog(null, "按鈕3被點擊了");
        }
    }

    public static void main(String[] args) {
        new Ep6(); // ✅ 建立物件 → 會自動觸發建構子 → 顯示 GUI
    }
}