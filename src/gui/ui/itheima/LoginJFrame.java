package gui.ui.itheima;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginJFrame extends JFrame implements ActionListener {
    static ArrayList<User> list = new ArrayList<>();
    static {
        list.add(new User("f0nazj","123"));
        list.add(new User("admin","123"));
    }
    //	getClass()：
    // 這個方法返回當前類的 Class 對象。每個類在運行時都與一個 Class 類型的對象相關聯，這個對象包含了該類的元數據。

    // getResource(String path)：
    // 這個方法會從當前類所在的路徑中查找資源文件，然後返回該資源的 URL 對象。該資源可以是打包在 JAR 文件中的文件，或是在運行環境中的文件。
    //path 參數：可以是相對路徑或絕對路徑。當路徑以 / 開頭時，它是從根目錄開始查找資源；否則，它是相對於當前類所在的包目錄來查找資源。
    ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/gui/ui/images/background/login_background.jpg"));

    JTextField username = new JTextField();
    JPasswordField userpassword = new JPasswordField();
    
    JButton login = new JButton("登錄");
    JButton register = new JButton("註冊");

    String code = verification();
    JTextField ver = new JTextField();
    JLabel verJLabel2 = new JLabel(code);

    public LoginJFrame(){
        initJFrame();
        initView();
        this.setVisible(true);
    }
    public void initJFrame(){
        this.setSize(500, 430);
        this.setTitle("登入");
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setLayout(null);
    }
    public void initView(){
        //1.添加用戶名文字
        JLabel usernameLabel = new JLabel("用戶名:");
        usernameLabel.setBounds(100, 100, 80, 30);
        this.getContentPane().add(usernameLabel);
        //2.添加用戶名輸入框
        username.setBounds(180, 100, 200, 30);
        this.getContentPane().add(username);

        //3.添加密碼文字
        JLabel userpasswordLabel = new JLabel("密碼:");
        userpasswordLabel.setBounds(100, 150, 80, 30);
        this.getContentPane().add(userpasswordLabel);
        //4.添加密碼框
        userpassword.setBounds(180, 150, 200, 30);
        this.getContentPane().add(userpassword);

        //驗證碼提示
        JLabel verJLabel = new JLabel("驗證碼:");
        verJLabel.setBounds(100, 200, 80, 30);
        this.getContentPane().add(verJLabel);

        //驗證碼生成
        verJLabel2.setBounds(300, 200, 70, 30);
        this.getContentPane().add(verJLabel2);

        // 添加鼠标点击事件监听器，点击后重新生成验证码
        verJLabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("更換驗證碼");
                verJLabel2.setText(verification());  // 更新 JLabel 显示的验证码
            }
        });

        //驗證碼輸入框
        ver.setBounds(180, 200, 100, 30);
        this.getContentPane().add(ver);

        //5.添加登錄按鈕
        login.setBounds(100, 250, 80, 30);
        this.getContentPane().add(login);
        login.addActionListener(this);
        //6.添加註冊按鈕
        register.setBounds(300, 250, 80, 30);
        this.getContentPane().add(register);
        register.addActionListener(this);
        //7.添加背景圖片
        JLabel background = new JLabel(backgroundIcon);
        background.setBounds(0, 0, 500, 430);
        this.getContentPane().add(background);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean validUser = false;
        Object obj = e.getSource();
        if (obj == login) {
            System.out.println("登錄");
            String user = username.getText();
            String password = new String(userpassword.getPassword());
            String vercode = ver.getText();

            // 首先檢查驗證碼是否正確
            if (!vercode.equals(code)) {
                showJDialog("登錄失敗, 驗證碼輸入錯誤");
                return; // 直接返回，避免繼續檢查用戶名和密碼
            }

            //.isEmpty()：
            //功能：檢查字串是否為空，也就是字串長度是否為 0。當字串的長度為 0 時，.isEmpty() 會返回 true，否則返回 false。

            //.trim()：
            //功能：去除字串開頭和結尾的所有空
            // 檢查用戶名和密碼是否為空
            if (user.trim().isEmpty() || password.trim().isEmpty()) {
                showJDialog("登錄失敗, 用戶名或密碼不能為空");
                return; // 直接返回，避免繼續檢查
            }

            // 檢查用戶名和密碼是否正確
            for (User u : list) {
                if (u.getName().equals(user) && u.getPassword().equals(password)) {
                    validUser = true;
                    break;
                }
            }

            if (validUser) {
                System.out.println("登錄成功");
                this.setVisible(false);
                new GameJFrame();
            } else {
                showJDialog("登錄失敗, 用戶名或密碼輸入錯誤");
            }
        } else if (obj == register) {
            System.out.println("註冊");
            this.setVisible(false);
            new RegisterJFrame();
        }
    }
    public void showJDialog(String content) {
        //創建彈窗對象
        JDialog jDialog = new JDialog();
        //給彈框設定大小
        jDialog.setSize(200, 150);
        //讓彈框置頂
        jDialog.setAlwaysOnTop(true);
        //讓彈框置中
        jDialog.setLocationRelativeTo(null);
        //彈框不關閉無法操作其他介面
        jDialog.setModal(true);
        //創建Jlabel對象管理文字並添加到彈框當中
        JLabel jLabel = new JLabel(content);
        jDialog.add(jLabel);
        //讓彈框顯示出來
        jDialog.setVisible(true);
    }
    public String verification() {
        int[] EnglishArr = new int[52];  // 用來存放 A-Z 和 a-z 的 ASCII 值
    
        // 填充 EnglishArr，存入大寫和小寫字母的 ASCII 值
        for (int i = 0; i < 52; i++) {
            if (i < 26) {
                EnglishArr[i] = 'A' + i;  // 'A' 到 'Z'
            } else {
                EnglishArr[i] = 'a' + (i - 26);  // 'a' 到 'z'
            }
        }
    
        char[] verification = new char[5];  // 用來存放 4 個字母 + 1 個數字
    
        // 隨機從 EnglishArr 陣列中選取 4 個字母
        for (int i = 0; i < 4; i++) {
            int r = (int) (Math.random() * 52);  // 隨機生成 0 到 51 的索引
            verification[i] = (char) EnglishArr[r];  // 將數字轉換為對應的字符
        }
    
        // 定義數字字符陣列
        char[] NumberArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    
        // 隨機選取一個數字，存入 verification 的最後一個位置
        int d = (int) (Math.random() * 10);
        verification[4] = NumberArr[d];
    
        // 將數字與隨機字母對調
        char index = verification[4];  // 存儲數字
        int a = (int) (Math.random() * 5);  // 隨機選擇一個字母的位置
        verification[4] = verification[a];  // 交換字母與數字
        verification[a] = index;
    
        // 將 char 陣列轉換為 String 並返回
        return new String(verification);
    }
}