package gui.ui.itheima;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 註冊視窗。
 * 負責新增使用者資料，並檢查欄位是否為空、密碼是否一致與帳號是否重複。
 */
public class RegisterJFrame extends JFrame implements ActionListener {
    ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/gui/ui/images/background/login_background.jpg"));

    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JPasswordField confirmPassword = new JPasswordField();

    JButton registerBtn = new JButton("註冊");
    JButton backBtn = new JButton("返回");

    public RegisterJFrame() {
        initJFrame();
        initView();
        this.setVisible(true);
    }

    private void initJFrame() {
        this.setTitle("註冊");
        this.setSize(500, 430);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setLayout(null);
    }

    private void initView() {
        JLabel usernameLabel = new JLabel("用戶名:");
        usernameLabel.setBounds(100, 100, 80, 30);
        this.getContentPane().add(usernameLabel);

        username.setBounds(180, 100, 200, 30);
        this.getContentPane().add(username);

        JLabel passwordLabel = new JLabel("密碼:");
        passwordLabel.setBounds(100, 150, 80, 30);
        this.getContentPane().add(passwordLabel);

        password.setBounds(180, 150, 200, 30);
        this.getContentPane().add(password);

        JLabel confirmLabel = new JLabel("確認密碼:");
        confirmLabel.setBounds(100, 200, 80, 30);
        this.getContentPane().add(confirmLabel);

        confirmPassword.setBounds(180, 200, 200, 30);
        this.getContentPane().add(confirmPassword);

        registerBtn.setBounds(120, 260, 80, 30);
        this.getContentPane().add(registerBtn);
        registerBtn.addActionListener(this);

        backBtn.setBounds(290, 260, 80, 30);
        this.getContentPane().add(backBtn);
        backBtn.addActionListener(this);

        JLabel background = new JLabel(backgroundIcon);
        background.setBounds(0, 0, 500, 430);
        this.getContentPane().add(background);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == registerBtn) {
            String user = username.getText().trim();
            String pwd = new String(password.getPassword());
            String confirmPwd = new String(confirmPassword.getPassword());

            if (user.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty()) {
                showDialog("註冊失敗，欄位不能為空");
                return;
            }

            if (!pwd.equals(confirmPwd)) {
                showDialog("註冊失敗，兩次密碼輸入不一致");
                return;
            }

            for (User u : LoginJFrame.list) {
                if (u.getName().equals(user)) {
                    showDialog("註冊失敗，用戶名已存在");
                    return;
                }
            }

            LoginJFrame.list.add(new User(user, pwd));
            showDialog("註冊成功！請返回登入");
        } else if (obj == backBtn) {
            this.dispose();
            new LoginJFrame();
        }
    }

    private void showDialog(String content) {
        JDialog dialog = new JDialog();
        dialog.setSize(260, 120);
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.add(new JLabel(content));
        dialog.setVisible(true);
    }
}
