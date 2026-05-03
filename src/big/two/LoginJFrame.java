package big.two;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * LoginJFrame — 登入視窗
 *
 * 遊戲的進入點畫面，提供帳號密碼與驗證碼的登入流程。
 *
 * 功能說明：
 *   - 使用者輸入帳號、密碼、驗證碼後按下「登入」進行驗證
 *   - 驗證碼顯示在畫面上，可點擊驗證碼文字或按 🔃 按鈕刷新
 *   - 按下「註冊」開啟 RegisterJFrame 進行新帳號註冊
 *   - 登入成功後關閉本視窗並開啟 GameJFrame（遊戲主畫面）
 *
 * 使用者資料：
 *   以靜態 ArrayList<User> list 儲存所有已知使用者，
 *   預設有一個帳號 "admin" / 密碼 "123456"，供測試使用。
 *   RegisterJFrame 成功註冊後會將新使用者加入此清單。
 *
 * 背景圖片：BigTwobackground.png（1920×1280 縮放至視窗大小）
 */
public class LoginJFrame extends JFrame implements MouseListener {

    // ─────────────────────────────────────────────────────────────────────────
    // 使用者清單（靜態共享，供 RegisterJFrame 新增使用者）
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 已註冊的使用者清單（靜態，整個應用程式共用）。
     * 初始包含測試帳號 admin / 123456。
     * 登入驗證透過 list.contains(user) 完成（依賴 User.equals()）。
     */
    static ArrayList<User> list = new ArrayList<>();
    static {
        // 預設測試帳號：帳號 admin，密碼 123456
        list.add(new User("admin", "123456"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 介面元件
    // ─────────────────────────────────────────────────────────────────────────

    /** 登入按鈕：點擊後驗證帳號密碼與驗證碼 */
    JButton login = new JButton();

    /** 註冊按鈕：點擊後跳轉至 RegisterJFrame */
    JButton register = new JButton();

    /** 驗證碼刷新按鈕：點擊後重新產生一組驗證碼 */
    JButton codeButton = new JButton();

    /** 帳號輸入框（明文顯示） */
    JTextField username = new JTextField();

    /** 密碼輸入框（隱藏字元，以 * 顯示） */
    JPasswordField userpassword = new JPasswordField();

    /** 驗證碼輸入框（使用者鍵入要驗證的字串） */
    JTextField code = new JTextField();

    /** 正確驗證碼的顯示標籤（顯示在畫面上供使用者對照輸入） */
    JLabel rightCode = new JLabel();

    // ─────────────────────────────────────────────────────────────────────────
    // 建構子
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 建立並顯示登入視窗，初始化視窗屬性與所有介面元件。
     */
    public LoginJFrame() {
        initJFrame(); // 設定視窗基本屬性
        initView();   // 加入所有介面元件
        repaint();    // 強制重繪，確保背景圖片正確顯示
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 初始化方法
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 設定視窗基本屬性：尺寸、標題、顯示位置、關閉行為等。
     */
    public void initJFrame() {
        this.setSize(633, 423);                    // 視窗大小（符合背景圖片比例）
        this.setTitle("大老二遊戲 V1.0 登入");        // 視窗標題列文字
        this.setVisible(true);                     // 顯示視窗
        this.setAlwaysOnTop(true);                 // 視窗置頂，避免被其他視窗遮蓋
        this.setDefaultCloseOperation(3);          // 關閉視窗時終止整個程式（EXIT_ON_CLOSE）
        this.setLocationRelativeTo(null);          // 視窗置中於螢幕
    }

    /**
     * 建立並加入所有介面元件到視窗中。
     * 元件使用絕對定位（null layout），位置與大小均以 setBounds 設定。
     */
    public void initView() {
        Font labelFont = new Font(null, Font.BOLD, 16); // 標籤字體（粗體 16pt）

        // ── 1. 帳號標籤 ───────────────────────────────────────────────────────
        JLabel usernameLabel = new JLabel("用戶名");
        usernameLabel.setForeground(Color.WHITE);   // 白色文字（在深色背景上顯示）
        usernameLabel.setFont(labelFont);
        usernameLabel.setBounds(200, 300, 55, 22);
        this.getContentPane().add(usernameLabel);

        // ── 2. 帳號輸入框 ─────────────────────────────────────────────────────
        username.setBounds(260, 298, 200, 30);
        this.getContentPane().add(username);

        // ── 3. 密碼標籤 ───────────────────────────────────────────────────────
        JLabel passwordLabel = new JLabel("密碼");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(labelFont);
        passwordLabel.setBounds(210, 340, 55, 22);
        this.getContentPane().add(passwordLabel);

        // ── 4. 密碼輸入框（JPasswordField 不顯示明文） ────────────────────────
        userpassword.setBounds(260, 337, 200, 30);
        this.getContentPane().add(userpassword);

        // ── 5. 驗證碼輸入標籤 ─────────────────────────────────────────────────
        JLabel codeText = new JLabel("驗證碼");
        codeText.setForeground(Color.WHITE);
        codeText.setFont(new Font(null, Font.BOLD, 16));
        codeText.setBounds(540, 250, 55, 22);
        this.getContentPane().add(codeText);

        // ── 6. 驗證碼輸入框 ───────────────────────────────────────────────────
        code.setBounds(510, 280, 100, 30);
        this.getContentPane().add(code);

        // ── 7. 產生初始驗證碼並顯示 ──────────────────────────────────────────
        // CodeUtil.getCode() 產生 4 個字母 + 1 個數字的隨機字串
        String codeStr = CodeUtil.getCode();

        // ── 8. 正確驗證碼的顯示標籤（紅色，供使用者對照填入） ──────────────────
        rightCode.setForeground(Color.RED);         // 紅色文字，使驗證碼明顯易讀
        rightCode.setFont(new Font(null, Font.BOLD, 15));
        rightCode.setBounds(535, 310, 100, 30);
        rightCode.setText(codeStr);
        rightCode.addMouseListener(this);           // 點擊驗證碼文字也可刷新
        this.getContentPane().add(rightCode);

        // ── 9. 驗證碼刷新按鈕 ─────────────────────────────────────────────────
        // 點擊後呼叫 CodeUtil.getCode() 產生新驗證碼並更新顯示
        codeButton.setText("🔃");
        codeButton.setBounds(510, 315, 20, 20);
        codeButton.addMouseListener(this);
        this.getContentPane().add(codeButton);

        // ── 10. 登入按鈕 ──────────────────────────────────────────────────────
        login.setText("登入");
        login.setBounds(100, 290, 80, 40);
        login.addMouseListener(this);
        this.getContentPane().add(login);

        // ── 11. 註冊按鈕 ──────────────────────────────────────────────────────
        register.setText("註冊");
        register.setBounds(100, 335, 80, 40);
        register.addMouseListener(this);
        this.getContentPane().add(register);

        // ── 12. 背景圖片（必須最後加入，否則會遮蓋上方的元件）─────────────────
        // 使用 Common.loadIcon() 載入，自動相容 JAR 打包與開發環境
        JLabel background = new JLabel(Common.loadIcon("/big/two/images/BigTwobackground.png"));
        background.setBounds(0, 0, 633, 423);
        this.getContentPane().add(background);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 滑鼠事件處理
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 滑鼠點擊事件分派器，根據點擊的元件執行對應操作。
     *
     * 處理三種情境：
     *   1. 點擊「登入」按鈕 → 驗證輸入並決定是否開啟遊戲
     *   2. 點擊「註冊」按鈕 → 關閉登入視窗並開啟 RegisterJFrame
     *   3. 點擊驗證碼文字或刷新按鈕 → 重新產生驗證碼
     *
     * 登入驗證流程：
     *   ① 驗證碼不可為空
     *   ② 帳號密碼不可為空
     *   ③ 驗證碼必須與畫面上顯示的相符（大小寫敏感）
     *   ④ 以 list.contains(user) 確認帳號密碼組合存在於使用者清單中
     *
     * @param e 滑鼠事件，包含事件來源元件
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Object obj = e.getSource(); // 取得觸發事件的元件

        if (obj == login) {
            // ── 登入按鈕 ──────────────────────────────────────────────────────
            String usernameInput = username.getText();
            String passwordInput = new String(userpassword.getPassword()); // 轉換為明文比對
            String codeInput     = code.getText();

            // 驗證碼不可為空
            if (codeInput.isEmpty()) {
                showJDialog("驗證碼不能為空");
                return;
            }
            // 帳號或密碼不可為空
            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                showJDialog("用戶名和密碼不能為空");
                return;
            }
            // 驗證碼必須與畫面顯示的相符
            if (!codeInput.equals(rightCode.getText())) {
                showJDialog("驗證碼不正確");
                return;
            }
            // 以帳號密碼組合查找使用者（依賴 User.equals()）
            User user = new User(usernameInput, passwordInput);
            if (list.contains(user)) {
                // 登入成功：關閉登入視窗，開啟遊戲主畫面
                this.dispose();
                new GameJFrame();
            } else {
                showJDialog("用戶名或密碼錯誤");
            }

        } else if (obj == register) {
            // ── 註冊按鈕：關閉登入視窗，開啟註冊視窗 ─────────────────────────
            this.dispose();
            new RegisterJFrame();

        } else if (obj == codeButton || obj == rightCode) {
            // ── 刷新驗證碼：重新產生並更新顯示 ───────────────────────────────
            rightCode.setText(CodeUtil.getCode());
        }
    }

    /** 滑鼠按下事件（本視窗不需要處理，保留空實作） */
    @Override public void mousePressed(MouseEvent e)  {}

    /** 滑鼠放開事件（本視窗不需要處理，保留空實作） */
    @Override public void mouseReleased(MouseEvent e) {}

    /** 滑鼠移入元件事件（本視窗不需要處理，保留空實作） */
    @Override public void mouseEntered(MouseEvent e)  {}

    /** 滑鼠移出元件事件（本視窗不需要處理，保留空實作） */
    @Override public void mouseExited(MouseEvent e)   {}

    // ─────────────────────────────────────────────────────────────────────────
    // 工具方法
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 顯示提示對話框（模態，使用者必須確認後才能繼續操作）。
     *
     * @param content 要顯示的提示訊息文字
     */
    public void showJDialog(String content) {
        JOptionPane.showMessageDialog(this, content);
    }
}
