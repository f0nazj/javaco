package gui.ui.itheima;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.BevelBorder;

/**
 * 拼圖遊戲主視窗。
 * 負責顯示遊戲畫面、處理鍵盤移動、功能選單與勝利判斷。
 */
public class GameJFrame extends JFrame implements KeyListener,ActionListener {
    // 用於存儲遊戲數據的二維陣列，4x4 的矩陣表示拼圖的位置。
    int[][] data = new int[4][4];

    // x 和 y 用來存儲空白塊（數字0）的行列位置
    int x = 0;
    int y = 0;

    // 目前使用的拼圖圖片路徑前綴，後面會接上 1.jpg ~ 15.jpg 或 all.jpg。
    String path = "/gui/ui/images/zero_two_1/zero two_";

    // 背景圖、勝利圖與關於視窗圖片都從 classpath 讀取，方便之後打包成 jar。
    ImageIcon pathbackground = new ImageIcon(getClass().getResource("/gui/ui/images/background/puzzle_background.png"));
    ImageIcon pathWin = new ImageIcon(getClass().getResource("/gui/ui/images/background/win2.jpg"));
    ImageIcon pathQRcode = new ImageIcon(getClass().getResource("/gui/ui/images/background/QRcode.png"));


    // 計數器，記錄玩家的步數
    int count = 0;

    // 創建 "功能" 菜單中的選項
    JMenuItem replayItem = new JMenuItem("重新遊戲");
    JMenuItem reLoginItem = new JMenuItem("重新登入");
    JMenuItem closeItem = new JMenuItem("關閉遊戲");

    // 創建 "關於" 菜單中的選項
    JMenuItem accountItem = new JMenuItem("公眾號");

    // 更換圖片分類的選單項目
    JMenuItem animal = new JMenuItem("動物");
    JMenuItem anime = new JMenuItem("動漫");
    JMenuItem car = new JMenuItem("車");
    /**
     * 構造函數，負責初始化窗口、菜單欄、數據和圖片。
     */
    public GameJFrame() {
        setFocusTraversalKeysEnabled(false);
        initJFrame();  // 初始化窗口屬性
        initJMenuBar();  // 初始化菜單欄
        initData();  // 初始化數據，隨機打亂的數組
        initImage();  // 初始化圖片顯示
        this.setVisible(true);  // 設置窗口可見
    }

    /**
     * 初始化窗口屬性，包括窗口大小、標題、置頂、位置等。
     */
    private void initJFrame() {
        this.setTitle("拼圖單機版 v1.0");  // 設置標題
        this.setSize(603, 680);  // 設置窗口大小
        this.setAlwaysOnTop(true);  // 設置窗口置頂
        this.setLocationRelativeTo(null);  // 設置窗口居中顯示
        this.setDefaultCloseOperation(3);  // 設置點擊關閉按鈕時退出程式
        this.setLayout(null);  // 設置布局為 null，自行定義控件的位置
        this.addKeyListener(this);  // 為窗口添加鍵盤監聽器
    }

    /**
     * 按住 Tab 時暫時顯示完整圖片，方便玩家對照。
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // 監聽 "Tab" 鍵，顯示完整拼圖圖片
        int code = e.getKeyCode();
        if (code == 9) {  // "Tab" 鍵
            this.getContentPane().removeAll();
            JLabel all = new JLabel(new ImageIcon(getClass().getResource(path + "all" + ".jpg")));
            all.setBounds(83, 134, 420, 420);
            this.getContentPane().add(all);

            // 添加背景圖片
            JLabel background = new JLabel(pathbackground);
            background.setBounds(40, 80, 500, 500);
            this.getContentPane().add(background);
            this.getContentPane().repaint();  // 重繪窗口
        }
    }

    /**
     * 當按鍵釋放時觸發的事件，根據不同的按鍵進行拼圖移動。
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(victory()){
            return;
        }
        int code = e.getKeyCode();  // 獲取按下的按鍵代碼
        // 向左移動
        if (code == 'A') {
            if (y == 3) {
                return;  // 若空白塊位於最右邊，則不能再向左移動
            }
            System.out.println("向左移動");
            data[x][y] = data[x][y + 1];  // 交換空白塊與右側塊
            data[x][y + 1] = 0;
            y++;  // 更新空白塊的 y 坐標
            count++;   // 增加步數
            initImage();  // 更新圖片
        }
        // 向右移動
        else if (code == 'D') {
            if (y == 0) {
                return;  // 若空白塊位於最左邊，則不能再向右移動
            }
            System.out.println("向右移動");
            data[x][y] = data[x][y - 1];  // 交換空白塊與左側塊
            data[x][y - 1] = 0;
            y--;  // 更新空白塊的 y 坐標
            count++;  // 增加步數
            initImage();  // 更新圖片
        }
        // 向上移動
        else if (code == 'W') {
            if (x == 3) {
                return;  // 若空白塊位於最下邊，則不能再向上移動
            }
            System.out.println("向上移動");
            data[x][y] = data[x + 1][y];  // 交換空白塊與下側塊
            data[x + 1][y] = 0;
            x++;  // 更新空白塊的 x 坐標
            count++;  // 增加步數
            initImage();  // 更新圖片
        }
        // 向下移動
        else if (code == 'S') {
            if (x == 0) {
                return;  // 若空白塊位於最上邊，則不能再向下移動
            }
            System.out.println("向下移動");
            data[x][y] = data[x - 1][y];  // 交換空白塊與上側塊
            data[x - 1][y] = 0;
            x--;  // 更新空白塊的 x 坐標
            count++;  // 增加步數
            initImage();  // 更新圖片
        }else if(code == 9){
            initImage();
        }
        // 處理 "V" 鍵，直接完成拼圖
        else if (code == 'V') {
            data = new int[][] {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 0}
            };
            x = 3;  // 更新空白塊的行位置
            y = 3;  // 更新空白塊的列位置
            initImage();  // 重新繪製拼圖
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * 初始化菜單欄，添加功能和關於的菜單選項。
     */
    private void initJMenuBar() {
        JMenuBar JBar = new JMenuBar();  // 創建菜單欄

        JMenu functionJMenu = new JMenu("功能");  // 創建 "功能" 菜單
        JMenu aboutJMenu = new JMenu("關於");  // 創建 "關於" 菜單
        JMenu replaceJMenu = new JMenu("更換圖片");  // 圖片分類子選單

        // 將選項添加到對應的菜單中
        functionJMenu.add(replaceJMenu);
        replaceJMenu.add(animal);
        replaceJMenu.add(anime);
        replaceJMenu.add(car);
        
        functionJMenu.add(replayItem);
        functionJMenu.add(reLoginItem);
        functionJMenu.add(closeItem);
        aboutJMenu.add(accountItem);
        
        // 為菜單項添加事件監聽器
        replayItem.addActionListener(this);
        reLoginItem.addActionListener(this);
        closeItem.addActionListener(this);
        accountItem.addActionListener(this);

        animal.addActionListener(this);
        anime.addActionListener(this);
        car.addActionListener(this);

        // 將菜單添加到菜單欄中
        JBar.add(functionJMenu);
        JBar.add(aboutJMenu);

        // 將菜單欄設置為窗口的菜單欄
        this.setJMenuBar(JBar);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // 獲取觸發事件的物件
        Object obj = e.getSource();

        // 當使用者選擇 "重新遊戲" 菜單項時
        if (obj == replayItem) {
            System.out.println("重新遊戲");
            initData();  // 重新初始化遊戲數據
            count = 0;  // 步數重置為0
            initImage();  // 重新繪製拼圖圖片
        } 
        // 當使用者選擇 "重新登入" 菜單項時
        else if (obj == reLoginItem) {
            System.out.println("重新登錄");
            this.dispose();  // 關閉當前遊戲窗口
            new LoginJFrame();  // 打開登錄窗口
        } 
        // 當使用者選擇 "關閉遊戲" 菜單項時
        else if (obj == closeItem) {
            System.out.println("關閉");
            System.exit(0);  // 退出程序
        } 
        // 當使用者選擇 "公眾號" 菜單項時
        else if (obj == accountItem) {
            System.out.println("關於");
            JDialog jDialog = new JDialog();
            JLabel jLabel = new JLabel(pathQRcode);
            jLabel.setBounds(0, 0, 614, 614);
            jDialog.getContentPane().add(jLabel);
            jDialog.setSize(700, 700);
            jDialog.setAlwaysOnTop(true);
            jDialog.setLocationRelativeTo(null);
            jDialog.setModal(true);
            jDialog.setVisible(true);
        }
        // 更換圖片分類後保留目前拼圖排列，只重新載入對應圖片。
        else if(obj == animal){
            System.out.println("動物");
            pathanimal();
            initImage();
        }else if(obj == anime){
            System.out.println("動漫");
            pathanime();
            initImage();
        }else if(obj == car){
            System.out.println("車");
            pathcar();
            initImage();
        }
    }

    /**
     * 初始化數據，隨機打亂包含 0 到 15 的一維數組，並轉換為 4x4 矩陣。
     */
    private void initData() {
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};  // 初始化一維數組

        do {
            // 使用 Fisher-Yates 洗牌演算法隨機打亂數組
            for (int i = tempArr.length - 1; i > 0; i--) {
                int r = (int) (Math.random() * (i + 1));  // 隨機選擇一個索引
                int index = tempArr[i];
                tempArr[i] = tempArr[r];  // 交換元素
                tempArr[r] = index;
            }
        } while (!isSolvable(tempArr) || isFinished(tempArr));  // 避免產生無解或一開始就完成的局面

        // 將打亂後的一維數組轉換為 4x4 矩陣，存儲到 data 中
        for (int i = 0; i < tempArr.length; i++) {
            if (tempArr[i] == 0) {
                x = i / 4;  // 記錄空白塊的位置
                y = i % 4;
            }
                data[i / 4][i % 4] = tempArr[i];  // 計算矩陣中的行列位置
        }
    }

    /**
     * 初始化圖片，將拼圖塊顯示在窗口中。
     */
    private void initImage() {
        // 移除窗口中所有的控件，為了準備重新繪製拼圖圖片
        this.getContentPane().removeAll();
        
        // 如果遊戲勝利，顯示勝利畫面
        if (victory()) {
            // 創建一個 JLabel 並顯示勝利圖片
            JLabel winJLabel = new JLabel(pathWin);
            winJLabel.setBounds(150, 233, 300, 200);  // 設定圖片的位置和大小
            getContentPane().add(winJLabel);  // 添加勝利圖片到窗口中
        }
        JLabel stepCount = new JLabel("步數:" + count);
        stepCount.setBounds(0, 0, 300, 200);
        getContentPane().add(stepCount);
        // 迭代 data 陣列中的每一個數據，根據數字顯示對應的圖片
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                int number = data[j][i];  // 獲取拼圖塊的數字
                // 0 代表空白格，不需要讀取圖片。
                JLabel jLabel = number == 0 ? new JLabel() : new JLabel(new ImageIcon(getClass().getResource(path + number + ".jpg")));
                jLabel.setBounds(105 * i + 83, 105 * j + 134, 105, 105);  // 設置圖片顯示的位置和大小
                jLabel.setBorder(new BevelBorder(0));  // 給圖片加上邊框
                this.getContentPane().add(jLabel);  // 添加圖片到窗口
            }
        }

        // 添加背景圖片
        JLabel background = new JLabel(pathbackground);
        background.setBounds(40, 80, 500, 500);  // 設置背景圖片的位置和大小

        this.getContentPane().add(background);  // 添加背景到窗口
        this.getContentPane().repaint();  // 重繪窗口
    }
    public Boolean victory() {
        // 定義一個表示拼圖完成狀態的目標陣列
        int[][] WinArr = {
            {1, 2, 3, 4},    // 第一行的目標數字
            {5, 6, 7, 8},    // 第二行的目標數字
            {9, 10, 11, 12}, // 第三行的目標數字
            {13, 14, 15, 0}  // 第四行的目標數字，最後一個數字為 0 代表空白塊
        };
    
        // 遍歷遊戲目前的 data 陣列，與目標陣列 WinArr 進行比較
        for (int i = 0; i < WinArr.length; i++) {  // 遍歷每一行
            for (int j = 0; j < 4; j++) {  // 遍歷每一列
                // 如果任何一個數字不符合目標陣列的數字，則返回 false，表示還未達到勝利狀態
                if (data[i][j] != WinArr[i][j]) {
                    return false;
                }
            }
        }
        
        // 如果所有數字都符合目標陣列，返回 true，表示遊戲勝利
        return true;
    }

    /**
     * 判斷 4x4 拼圖排列是否可解。
     * 4x4 拼圖需要同時考慮逆序數與空白格由下往上數的列數。
     */
    private boolean isSolvable(int[] arr) {
        int inversions = 0;
        int blankIndex = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0) {
                blankIndex = i;
                continue;
            }
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] != 0 && arr[i] > arr[j]) {
                    inversions++;
                }
            }
        }

        int blankRowFromBottom = 4 - (blankIndex / 4);
        return blankRowFromBottom % 2 == 0 ? inversions % 2 == 1 : inversions % 2 == 0;
    }

    /**
     * 判斷洗牌結果是否已經是完成狀態，避免新遊戲一開始就勝利。
     */
    private boolean isFinished(int[] arr) {
        for (int i = 0; i < 15; i++) {
            if (arr[i] != i + 1) {
                return false;
            }
        }
        return arr[15] == 0;
    }

    // 以下三個方法會在同一分類中隨機挑選一組圖片。
    private void pathanimal(){
        String pathArr[] = {
            "/gui/ui/images/Animal/animal_1/animal_",
            "/gui/ui/images/Animal/animal_2/animal_",
            "/gui/ui/images/Animal/animal_3/animal_",
            "/gui/ui/images/Animal/animal_4/animal_",
        };
        int r = (int)(Math.random() * pathArr.length);
        path = pathArr[r];
    }
    private void pathanime(){
        String pathArr[] = {
            "/gui/ui/images/Anime/anime_1/anime_",
            "/gui/ui/images/Anime/anime_2/anime_",
            "/gui/ui/images/Anime/anime_3/anime_",
            "/gui/ui/images/Anime/anime_4/anime_",
        };
        int r = (int)(Math.random() * pathArr.length);
        path = pathArr[r];
    }
    private void pathcar(){
        String pathArr[] = {
            "/gui/ui/images/Car/car_1/car_",
            "/gui/ui/images/Car/car_2/car_",
            "/gui/ui/images/Car/car_3/car_",
            "/gui/ui/images/Car/car_4/car_",
        };
        int r = (int)(Math.random() * pathArr.length);
        path = pathArr[r];
    }
}
