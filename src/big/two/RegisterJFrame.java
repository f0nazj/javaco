package big.two;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * RegisterJFrame — 帳號註冊視窗
 *
 * 提供新使用者建立帳號的介面，包含帳號、密碼、確認密碼三個欄位。
 *
 * 驗證規則：
 *   1. 帳號不可為空白
 *   2. 密碼長度至少 6 個字元
 *   3. 「密碼」與「確認密碼」必須完全相同
 *   4. 帳號不可與已存在的使用者重複
 *
 * 流程：
 *   - 點擊「確認註冊」→ 驗證通過後加入 LoginJFrame.list → 彈出成功提示 → 返回登入頁
 *   - 點擊「返回登入」→ 直接關閉此視窗並重新開啟登入視窗
 *
 * 背景圖片與登入頁相同（BigTwobackground.png）。
 */
public class RegisterJFrame extends JFrame implements MouseListener {

    // ─────────────────────────────────────────────────────────────────────────
    // 介面元件
    // ─────────────────────────────────────────────────────────────────────────

    /** 帳號輸入框（明文顯示） */
    JTextField     usernameField        = new JTextField();

    /** 密碼輸入框（隱藏字元） */
    JPasswordField passwordField        = new JPasswordField();

    /** 確認密碼輸入框（需與密碼欄一致） */
    JPasswordField confirmPasswordField = new JPasswordField();

    /** 確認註冊按鈕：執行驗證並完成註冊 */
    JButton registerBtn = new JButton("確認註冊");

    /** 返回登入按鈕：放棄註冊，回到登入頁 */
    JButton cancelBtn   = new JButton("返回登入");

    // ─────────────────────────────────────────────────────────────────────────
    // 建構子
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 建立並顯示註冊視窗，初始化視窗屬性與所有介面元件。
     */
    public RegisterJFrame() {
        initJFrame(); // 設定視窗基本屬性
        initView();   // 加入所有介面元件
        repaint();    // 強制重繪，確保背景圖片正確顯示
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 初始化方法
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 設定視窗基本屬性：尺寸、標題、置頂、置中、關閉行為。
     */
    private void initJFrame() {
        this.setSize(633, 423);
        this.setTitle("大老二遊戲 V1.0 - 帳號註冊");
        this.setVisible(true);
        this.setAlwaysOnTop(true);                 // 置頂，避免被遮蓋
        this.setDefaultCloseOperation(3);          // 關閉時終止程式
        this.setLocationRelativeTo(null);          // 置中顯示
        this.getContentPane().setLayout(null);     // 使用絕對定位
    }

    /**
     * 建立並加入所有介面元件。
     * 元件使用絕對定位，背景圖片必須最後加入（避免遮蓋其他元件）。
     */
    private void initView() {
        Font labelFont = new Font(null, Font.BOLD, 16); // 標籤字體
        Color white    = Color.WHITE;                   // 標籤文字顏色

        // ── 帳號標籤與輸入框 ──────────────────────────────────────────────────
        JLabel usernameLabel = new JLabel("用戶名");
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(white);
        usernameLabel.setBounds(185, 250, 70, 25);
        this.getContentPane().add(usernameLabel);

        usernameField.setBounds(260, 248, 200, 30);
        this.getContentPane().add(usernameField);

        // ── 密碼標籤與輸入框 ──────────────────────────────────────────────────
        JLabel passwordLabel = new JLabel("密碼");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(white);
        passwordLabel.setBounds(200, 295, 55, 25);
        this.getContentPane().add(passwordLabel);

        // JPasswordField：輸入的字元以 * 顯示，保護密碼隱私
        passwordField.setBounds(260, 293, 200, 30);
        this.getContentPane().add(passwordField);

        // ── 確認密碼標籤與輸入框 ──────────────────────────────────────────────
        JLabel confirmLabel = new JLabel("確認密碼");
        confirmLabel.setFont(labelFont);
        confirmLabel.setForeground(white);
        confirmLabel.setBounds(170, 340, 80, 25);
        this.getContentPane().add(confirmLabel);

        confirmPasswordField.setBounds(260, 338, 200, 30);
        this.getContentPane().add(confirmPasswordField);

        // ── 確認註冊按鈕 ──────────────────────────────────────────────────────
        registerBtn.setBounds(185, 380, 110, 32);
        registerBtn.addMouseListener(this);
        this.getContentPane().add(registerBtn);

        // ── 返回登入按鈕 ──────────────────────────────────────────────────────
        cancelBtn.setBounds(340, 380, 110, 32);
        cancelBtn.addMouseListener(this);
        this.getContentPane().add(cancelBtn);

        // ── 背景圖片（最後加入，否則會遮蓋上方元件）─────────────────────────
        // 使用 Common.loadIcon() 自動相容 JAR 打包與開發環境
        JLabel background = new JLabel(Common.loadIcon("/big/two/images/BigTwobackground.png"));
        background.setBounds(0, 0, 633, 423);
        this.getContentPane().add(background);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 滑鼠事件處理
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 滑鼠點擊事件處理器。
     *
     * 點擊「確認註冊」的驗證流程：
     *   1. 帳號不可為空白
     *   2. 密碼不可為空白
     *   3. 密碼長度至少 6 碼
     *   4. 密碼與確認密碼必須一致
     *   5. 帳號不可與 LoginJFrame.list 中的現有帳號重複
     *   → 全部通過後：新增 User 至 LoginJFrame.list，關閉此視窗，返回登入頁
     *
     * 點擊「返回登入」：
     *   → 直接關閉此視窗，重新開啟登入頁
     *
     * @param e 滑鼠事件
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();

        if (src == registerBtn) {
            // ── 確認註冊 ────────────────────────────────────────────────────────
            String username        = usernameField.getText().trim();
            String password        = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // 驗證規則 1：帳號不可為空
            if (username.isEmpty()) {
                showDialog("用戶名不能為空");
                return;
            }
            // 驗證規則 2：密碼不可為空
            if (password.isEmpty()) {
                showDialog("密碼不能為空");
                return;
            }
            // 驗證規則 3：密碼長度至少 6 碼
            if (password.length() < 6) {
                showDialog("密碼長度至少 6 位");
                return;
            }
            // 驗證規則 4：兩次密碼輸入必須相同
            if (!password.equals(confirmPassword)) {
                showDialog("兩次密碼輸入不一致");
                return;
            }
            // 驗證規則 5：帳號不可與已存在的使用者重複
            for (User u : LoginJFrame.list) {
                if (u.getUsername().equals(username)) {
                    showDialog("用戶名已存在，請更換");
                    return;
                }
            }

            // 所有驗證通過：將新使用者加入共用清單
            LoginJFrame.list.add(new User(username, password));
            showDialog("註冊成功！請返回登入");
            // 關閉註冊視窗並重新開啟登入視窗
            this.dispose();
            new LoginJFrame();

        } else if (src == cancelBtn) {
            // ── 返回登入：放棄註冊 ───────────────────────────────────────────
            this.dispose();
            new LoginJFrame();
        }
    }

    /** 滑鼠按下事件（保留空實作） */
    @Override public void mousePressed(MouseEvent e)  {}
    /** 滑鼠放開事件（保留空實作） */
    @Override public void mouseReleased(MouseEvent e) {}
    /** 滑鼠移入事件（保留空實作） */
    @Override public void mouseEntered(MouseEvent e)  {}
    /** 滑鼠移出事件（保留空實作） */
    @Override public void mouseExited(MouseEvent e)   {}

    // ─────────────────────────────────────────────────────────────────────────
    // 工具方法
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 顯示模態提示對話框，使用者確認後才能繼續操作。
     *
     * @param msg 提示訊息文字
     */
    private void showDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
