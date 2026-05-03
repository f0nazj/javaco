package big.two;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GameJFrame extends JFrame {
    public static GameJFrame instance = null;
    // 遊戲視窗的隱藏容器，統一存取用
    public static Container container = null;

    JButton outCardBut  = new JButton("出牌");
    JButton passCardBut = new JButton("Pass");
    JLabel  dizhu;
    // 狀態提示標籤（顯示回合資訊、錯誤訊息、勝負結果）
    JLabel  statusLabel = new JLabel("發牌中…");

    // currentList[i]：玩家 i 最近一次出的牌（桌面展示用）
    ArrayList<ArrayList<Poker>> currentList = new ArrayList<>();
    // playerList[i]：玩家 i 目前手上的牌
    ArrayList<ArrayList<Poker>> playerList  = new ArrayList<>();

    ArrayList<Poker> lordList  = new ArrayList<>(); // 底牌（保留欄位）
    ArrayList<Poker> pokerList = new ArrayList<>(); // 牌盒，裝全部52張牌

    JTextField time[] = new JTextField[3]; // 各玩家的倒計時文本（保留欄位）

    // ── 遊戲狀態 ──────────────────────────────────────────────────────────────
    int currentTurn  = 0;          // 當前輪到誰：0=左bot  1=自己  2=右bot
    int tableOwner   = -1;         // 桌面牌的出牌者（-1=新回合開始前）
    boolean[] passed = new boolean[3]; // 記錄各玩家在本回合是否已 Pass
    ArrayList<Poker> tablePlay = new ArrayList<>(); // 桌面上需要被超過的牌

    // ── 建構子 ────────────────────────────────────────────────────────────────
    public GameJFrame() {
        instance = this;
        // 設定工作列圖示（透過 Common.loadIcon 相容 JAR 打包環境）
        setIconImage(Common.loadIcon("/big/two/images/LOGO.png").getImage());
        initJFrame();
        initView();
        repaint();
        this.setVisible(true);
        // 將發牌動畫放到新執行緒執行，避免阻塞 EDT 導致動畫無法顯示
        new Thread(this::initCard).start();
    }

    // ── 介面元件設定 ──────────────────────────────────────────────────────────
    public void initView() {
        // 出牌按鈕
        outCardBut.setBounds(320, 400, 60, 20);
        outCardBut.addActionListener(this::outCard);
        outCardBut.setVisible(false);
        container.add(outCardBut);

        // Pass 按鈕
        passCardBut.setBounds(420, 400, 75, 20);
        passCardBut.addActionListener(this::passCard);
        passCardBut.setVisible(false);
        container.add(passCardBut);

        // 三個玩家前的文字提示（目前保留，尚未啟用）
        for (int i = 0; i < 3; i++) {
            time[i] = new JTextField("倒計時：");
            time[i].setEditable(false);
            time[i].setVisible(false);
            container.add(time[i]);
        }
        time[0].setBounds(140, 230, 60, 20);
        time[1].setBounds(374, 360, 60, 20);
        time[2].setBounds(620, 230, 60, 20);

        // 地主圖標（保留，尚未使用）
        dizhu = new JLabel();
        dizhu.setIcon(Common.loadIcon("/big/two/images/LOGO.png"));
        dizhu.setVisible(false);
        dizhu.setBounds(250, 370, 40, 40);
        container.add(dizhu);

        // 狀態提示標籤
        statusLabel.setBounds(240, 175, 370, 28);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        statusLabel.setForeground(Color.DARK_GRAY);
        container.add(statusLabel);
    }

    // ── 視窗基本設定 ──────────────────────────────────────────────────────────
    public void initJFrame() {
        this.setTitle("大老二遊戲");
        this.setSize(830, 620);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        container = this.getContentPane();
        container.setLayout(null);
        container.setBackground(Color.LIGHT_GRAY);
    }

    // ── 初始化牌（準備、洗牌、發牌、排序）────────────────────────────────────
    public void initCard() {
        // 每個玩家各準備一個出牌區集合
        currentList.clear();
        currentList.add(new ArrayList<>());
        currentList.add(new ArrayList<>());
        currentList.add(new ArrayList<>());

        // 建立 52 張牌（格式：花色-點數），初始位置放在畫面中央
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 13; j++) {
                Poker poker = new Poker(i + "-" + j, false);
                poker.setLocation(new Point(360, 220));
                pokerList.add(poker);
                container.add(poker);
            }
        }
        // 洗牌
        Collections.shuffle(pokerList);

        // 三個玩家的手牌集合
        ArrayList<Poker> p0 = new ArrayList<>();
        ArrayList<Poker> p1 = new ArrayList<>();
        ArrayList<Poker> p2 = new ArrayList<>();

        // 發牌（逐張動畫移動到各玩家位置）
        for (int i = 0; i < pokerList.size(); i++) {
            Poker poker = pokerList.get(i);
            if      (i % 3 == 0) { Common.move(poker, poker.getLocation(), new Point(30,           60 + i * 6)); p0.add(poker); }
            else if (i % 3 == 1) { Common.move(poker, poker.getLocation(), new Point(180 + i * 7,   450));       p1.add(poker); }
            else                 { Common.move(poker, poker.getLocation(), new Point(750,           60 + i * 6)); p2.add(poker); }
            container.setComponentZOrder(poker, 0);
        }
        playerList.add(p0);
        playerList.add(p1);
        playerList.add(p2);

        // 對三個玩家的手牌排序，並重新排列位置
        for (int i = 0; i < 3; i++) {
            order(playerList.get(i));
            Common.rePosition(this, playerList.get(i), i);
        }

        // 發牌動畫結束後，切回 EDT 執行剩餘 UI 操作及遊戲啟動
        SwingUtilities.invokeLater(() -> {
            // 翻開自己的牌（玩家1）並允許點擊
            for (Poker p : playerList.get(1)) {
                p.turnFront();
                p.setCanClick(true);
            }
            // 顯示出牌與 Pass 按鈕
            outCardBut.setVisible(true);
            passCardBut.setVisible(true);
            // 正式開始遊戲
            startGame();
        });
    }

    // ── 排序（由小到大：3→K→A→2，同點數以花色升序排列）──────────────────────
    public void order(ArrayList<Poker> list) {
        Collections.sort(list, (o1, o2) -> {
            int v = getValue(o1) - getValue(o2);
            return v != 0 ? v : getSuit(o1) - getSuit(o2);
        });
    }

    // 取得牌的大小值：Ace(1)→14，2→15，其餘等於點數（大老二排序：3最小、2最大）
    public int getValue(Poker poker) {
        String name = poker.getName();
        int rank = Integer.parseInt(name.substring(name.indexOf('-') + 1));
        if (rank == 1) return 14;
        if (rank == 2) return 15;
        return rank;
    }

    // 取得牌的花色編號：1=♦  2=♣  3=♥  4=♠
    public int getSuit(Poker poker) {
        return Integer.parseInt(poker.getName().substring(0, poker.getName().indexOf('-')));
    }

    // ── 遊戲流程控制 ──────────────────────────────────────────────────────────

    // 初始化遊戲狀態，找出持有 3♦（"1-3"）的玩家先手
    void startGame() {
        tablePlay.clear();
        tableOwner = -1;
        Arrays.fill(passed, false);
        currentTurn = findFirstPlayer();
        startTurn(currentTurn);
    }

    // 找出手上有 3♦（名稱 "1-3"）的玩家索引
    int findFirstPlayer() {
        for (int i = 0; i < 3; i++)
            for (Poker p : playerList.get(i))
                if (p.getName().equals("1-3")) return i;
        return 0; // 備用（不應執行到）
    }

    // 開始某位玩家的回合
    void startTurn(int player) {
        currentTurn = player;
        boolean isNewRound = tablePlay.isEmpty();
        String[] names = {"左邊電腦", "你", "右邊電腦"};
        statusLabel.setText(names[player] + " 的回合" + (isNewRound ? "（新回合，必須出牌）" : ""));

        if (player == 1) {
            // 人類回合：啟用按鈕；新回合時不可 Pass
            outCardBut.setEnabled(true);
            passCardBut.setEnabled(!isNewRound);
        } else {
            // Bot 回合：停用人類按鈕，延遲 1.2 秒後 Bot 行動
            outCardBut.setEnabled(false);
            passCardBut.setEnabled(false);
            Timer t = new Timer(1200, e -> doBotPlay(player));
            t.setRepeats(false);
            t.start();
        }
    }

    // Bot 執行出牌或 Pass
    void doBotPlay(int bot) {
        ArrayList<Poker> hand = playerList.get(bot);
        ArrayList<Poker> play = Rule.botFind(hand, tablePlay.isEmpty() ? null : tablePlay);

        if (play == null) {
            // Bot 找不到可出的牌，選擇 Pass（本回合不再行動）
            String[] names = {"左邊電腦", "你", "右邊電腦"};
            statusLabel.setText(names[bot] + " Pass（本回合不再行動）");
            doPass(bot);
            return;
        }

        // Bot 出牌：從手牌移除，顯示到桌面
        hand.removeAll(play);
        clearTable();

        int startX = 380 - play.size() * 10;
        for (int i = 0; i < play.size(); i++) {
            Poker p = play.get(i);
            p.turnFront();      // 翻到正面
            p.setCanClick(false);
            p.setLocation(new Point(startX + i * 21, 215));
            container.setComponentZOrder(p, 0);
            tablePlay.add(p);
        }
        tableOwner = bot;

        // 無動畫快速重新排列 Bot 的剩餘手牌
        Common.quickRePosition(this, hand, bot);

        // 檢查 Bot 是否已出完所有牌
        if (hand.isEmpty()) { showWin(bot); return; }
        nextTurn();
    }

    // 人類玩家按下「出牌」按鈕
    public void outCard(ActionEvent e) {
        // 收集所有已選取（升起）的牌
        ArrayList<Poker> selected = new ArrayList<>();
        for (Poker p : playerList.get(1))
            if (p.getClicked()) selected.add(p);

        if (selected.isEmpty()) {
            statusLabel.setText("請先點選要出的牌");
            return;
        }

        // 驗證出牌是否合法（必須符合組合規則，且比桌面的牌大）
        if (!Rule.canBeat(selected, tablePlay.isEmpty() ? null : tablePlay)) {
            statusLabel.setText("出牌無效！必須比桌上的牌大");
            return;
        }

        // 確認出牌：重置選取狀態並從手牌移除
        for (Poker p : selected) { p.setClicked(false); p.setCanClick(false); }
        playerList.get(1).removeAll(selected);

        // 更新桌面
        clearTable();
        int startX = 380 - selected.size() * 10;
        for (int i = 0; i < selected.size(); i++) {
            Poker p = selected.get(i);
            p.setLocation(new Point(startX + i * 21, 215));
            container.setComponentZOrder(p, 0);
            tablePlay.add(p);
        }
        tableOwner = 1;

        // 重新排列剩餘手牌；若已出完則判定勝利
        if (playerList.get(1).isEmpty()) { showWin(1); return; }
        Common.quickRePosition(this, playerList.get(1), 1);

        nextTurn();
    }

    // 人類玩家按下「Pass」按鈕
    public void passCard(ActionEvent e) {
        // 把所有升起的牌放回原位
        for (Poker p : playerList.get(1)) {
            if (p.getClicked()) {
                p.setClicked(false);
                Point loc = p.getLocation();
                p.setLocation(new Point(loc.x, loc.y + 20));
            }
        }
        doPass(1);
    }

    // 執行 Pass 邏輯：
    //   - 將該玩家標記為本回合已 Pass（之後跳過他的回合）
    //   - 若除了 tableOwner 外所有玩家都已 Pass → tableOwner 開新回合
    void doPass(int player) {
        passed[player] = true;

        // 檢查是否只剩 tableOwner 尚未 Pass
        if (tableOwner != -1) {
            boolean allOthersPassed = true;
            for (int i = 0; i < 3; i++) {
                if (i != tableOwner && !passed[i]) {
                    allOthersPassed = false;
                    break;
                }
            }
            if (allOthersPassed) {
                // 其他所有玩家都已 Pass → 清桌，重置 Pass 狀態，開始新回合
                clearTable();
                Arrays.fill(passed, false);
                startTurn(tableOwner);
                return;
            }
        }

        // 還有其他玩家尚未 Pass，推進到下一位
        nextTurn();
    }

    // 推進到下一位「本回合尚未 Pass」的玩家（跳過已 Pass 的玩家）
    void nextTurn() {
        int next = (currentTurn + 1) % 3;
        int safety = 0;
        // 跳過已 Pass 的玩家（safety 防止無限迴圈）
        while (passed[next] && safety++ < 3) {
            next = (next + 1) % 3;
        }
        currentTurn = next;
        startTurn(currentTurn);
    }

    // ── 桌面管理 ──────────────────────────────────────────────────────────────

    // 隱藏桌面上的舊牌並清空
    void clearTable() {
        for (Poker p : tablePlay) p.setVisible(false);
        tablePlay.clear();
    }

    // ── 勝利判定 ──────────────────────────────────────────────────────────────

    // 顯示勝利訊息並停用所有操作
    void showWin(int player) {
        String[] names = {"左邊電腦", "你", "右邊電腦"};
        statusLabel.setText("恭喜 " + names[player] + " 勝利！遊戲結束");
        statusLabel.setForeground(Color.RED);
        outCardBut.setEnabled(false);
        passCardBut.setEnabled(false);
        // 停用所有手牌的點擊功能
        for (ArrayList<Poker> hand : playerList)
            for (Poker p : hand) p.setCanClick(false);
    }
}
