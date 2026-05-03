package big.two;

import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Common — 遊戲通用工具類別
 *
 * 提供以下靜態功能：
 *   1. loadIcon()        ─ 統一圖片資源載入（支援 JAR 打包與 IDE 開發兩種環境）
 *   2. move()            ─ 帶動畫效果的牌張移動
 *   3. quickRePosition() ─ 無動畫的快速重排（Bot 出牌後使用）
 *   4. rePosition()      ─ 帶動畫效果的重新排列（發牌結束後使用）
 *
 * 注意：move() 與 rePosition() 內含 Thread.sleep()，
 *      必須在非 EDT（Event Dispatch Thread）的執行緒中呼叫，
 *      否則會凍結 UI 導致動畫無法顯示。
 */
public class Common {

    // ─────────────────────────────────────────────────────────────────────────
    // 圖片資源載入
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 載入圖片資源，自動相容 JAR 打包環境與 IDE 開發環境。
     *
     * 載入順序：
     *   1. 優先嘗試從 classpath（JAR 內部）載入 ─ 打包後使用此路徑
     *   2. 若 classpath 找不到，退回使用 "src" + resourcePath 的文件路徑 ─ 開發期使用
     *
     * @param resourcePath classpath 資源路徑，開頭須為 "/"，例如 "/big/two/images/LOGO.png"
     * @return 對應的 ImageIcon 物件；若兩種方式均失敗則回傳空圖示（不會拋出例外）
     */
    public static ImageIcon loadIcon(String resourcePath) {
        // 嘗試從 classpath 讀取（打包成 JAR 後的標準方式）
        URL url = Common.class.getResource(resourcePath);
        if (url != null) return new ImageIcon(url);
        // 開發環境備用：直接讀取 src 目錄下的文件
        // resourcePath 格式為 "/big/two/images/..."，
        // 加上 "src" 前綴後變成 "src/big/two/images/..."
        return new ImageIcon("src" + resourcePath);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 牌張移動（帶動畫）
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 將一張牌從 from 位置沿直線動畫移動至 to 位置。
     *
     * 動畫原理：
     *   計算兩點連線的斜率（k）與截距（b），以每次 20px 的步進移動牌張位置，
     *   每步暫停 5ms，使移動過程形成平滑的滑動效果。
     *   最後呼叫 setLocation(to) 確保牌張精確到達目標位置。
     *
     * 執行緒注意：此方法含 Thread.sleep()，必須在背景執行緒中呼叫，
     *             不可在 EDT 上直接呼叫，否則 UI 會凍結。
     *
     * @param poker 要移動的牌張物件
     * @param from  起點座標；若為 null 則預設從畫面中央 (360, 220) 出發
     * @param to    終點座標
     */
    public static void move(Poker poker, Point from, Point to) {
        // 起點為空時，預設從畫面中央開始（牌堆的初始位置）
        if (from == null) {
            from = new Point(360, 220);
        }

        // 只有當水平位移不為零時才執行動畫；純垂直移動直接跳至終點
        if (to.x != from.x) {
            // 計算直線方程式 y = k*x + b 的斜率 k 與截距 b
            double k    = (1.0) * (to.y - from.y) / (to.x - from.x);
            double b    = to.y - to.x * k;
            // 根據移動方向決定每步增量：向右為 +20，向左為 -20
            int    step = from.x < to.x ? 20 : -20;

            // 逐步移動直到距離終點 x 座標在 20px 以內
            for (int i = from.x; Math.abs(i - to.x) > 20; i += step) {
                double y = k * i + b;         // 根據當前 x 計算對應的 y 座標
                poker.setLocation(i, (int) y); // 更新牌張視覺位置（呼叫 Component 的版本）
                try {
                    Thread.sleep(5); // 暫停 5ms，形成平滑動畫
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢復中斷狀態
                }
            }
        }

        // 確保牌張精確抵達終點（更新 Poker 內部的 location 快取）
        poker.setLocation(to);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 牌張重排（無動畫，用於 Bot 出牌後立即更新畫面）
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 將玩家的手牌重新排列至正確位置，無動畫效果（立即跳到目標位置）。
     *
     * 適用情境：Bot 出牌後，剩餘手牌需要即時重新排列，
     *           若使用帶動畫的 rePosition() 會在 EDT 上造成延遲。
     *
     * 起始位置計算方式（與 rePosition 相同）：
     *   - flag=0（左側 Bot）：牌組靠左（x=50），垂直置中，牌間距 15px
     *   - flag=1（自己）    ：牌組水平置中，固定在下方（y=450），牌間距 20px
     *   - flag=2（右側 Bot）：牌組靠右（x=700），垂直置中，牌間距 15px
     *
     * @param m    遊戲主視窗（用來存取 container 並設定 Z 軸順序）
     * @param list 要重新排列的手牌集合（已排序）
     * @param flag 玩家標記：0=左側 Bot、1=自己、2=右側 Bot
     */
    public static void quickRePosition(GameJFrame m, ArrayList<Poker> list, int flag) {
        if (list.isEmpty()) {
            m.container.repaint();
            return;
        }

        // 根據玩家位置計算牌組的起始座標
        Point p = new Point();
        if (flag == 0) { p.x = 50;  p.y = (450 / 2) - (list.size() + 1) * 15 / 2; }
        if (flag == 1) { p.x = (800 / 2) - (list.size() + 1) * 21 / 2; p.y = 450; }
        if (flag == 2) { p.x = 700; p.y = (450 / 2) - (list.size() + 1) * 15 / 2; }

        for (int i = 0; i < list.size(); i++) {
            Poker poker = list.get(i);
            poker.setLocation(new Point(p));          // 直接跳至目標位置（無動畫）
            m.container.setComponentZOrder(poker, 0); // 每張牌放到最前層（後面的牌覆蓋前面）
            if (flag == 1) p.x += 20;  // 自己的牌水平展開
            else           p.y += 15;  // Bot 的牌垂直展開
        }
        m.container.repaint(); // 強制重繪容器，確保位置更新立即顯示
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 牌張重排（帶動畫，用於排序後的視覺呈現）
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 將玩家的手牌帶動畫效果地重新排列至正確位置。
     *
     * 通常在發牌完成後、排序後呼叫，讓玩家看到牌張滑入最終位置的過程。
     * 因內含 Thread.sleep()，必須在背景執行緒中呼叫。
     *
     * 起始位置計算方式同 quickRePosition。
     *
     * @param m    遊戲主視窗
     * @param list 要重新排列的手牌集合（已排序）
     * @param flag 玩家標記：0=左側 Bot、1=自己、2=右側 Bot
     */
    public static void rePosition(GameJFrame m, ArrayList<Poker> list, int flag) {
        // 計算牌組起始座標
        Point p = new Point();
        if (flag == 0) { p.x = 50;  p.y = (450 / 2) - (list.size() + 1) * 15 / 2; }
        if (flag == 1) { p.x = (800 / 2) - (list.size() + 1) * 21 / 2; p.y = 450; }
        if (flag == 2) { p.x = 700; p.y = (450 / 2) - (list.size() + 1) * 15 / 2; }

        int len = list.size();
        for (int i = 0; i < len; i++) {
            Poker poker = list.get(i);
            move(poker, poker.getLocation(), p);       // 帶動畫移動至目標位置
            m.container.setComponentZOrder(poker, 0);  // 設為最前層
            if (flag == 1) p.x += 20;  // 水平展開（自己的牌）
            else           p.y += 15;  // 垂直展開（Bot 的牌）
        }
    }
}
