package big.two;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

/**
 * Poker — 單張撲克牌元件
 *
 * 繼承 JLabel，同時實作 MouseListener，作為遊戲畫面中可互動的牌張物件。
 *
 * 職責：
 *   - 持有牌張的名稱（格式："花色-點數"，例如 "3-7" 代表花色3、點數7）
 *   - 管理牌面朝向（正面／反面）
 *   - 處理玩家的點擊互動（選取／取消選取、雙擊出牌）
 *   - 維護牌張在畫面上的精確座標（覆寫 getLocation/setLocation）
 *
 * 牌名格式說明：
 *   第一部分（花色）：1=♦  2=♣  3=♥  4=♠
 *   第二部分（點數）：1=A  2=2  3=3 … 13=K
 *   例："1-3" = ♦3（梅花3）、"4-1" = ♠A（黑桃A）
 *
 * 圖片命名格式（與牌名相反，為「點數-花色」）：
 *   例：牌名 "3-7" → 圖片 "7-3.jpg"
 */
public class Poker extends JLabel implements MouseListener {

    // ─────────────────────────────────────────────────────────────────────────
    // 成員變數
    // ─────────────────────────────────────────────────────────────────────────

    /** 牌張名稱，格式為 "花色-點數"，例如 "2-13" 代表 ♣K */
    private String  name;

    /** 牌面朝向：true = 正面（顯示花色點數）；false = 反面（顯示牌背） */
    private Boolean up;

    /**
     * 是否允許被點擊。
     * 僅自己的手牌（玩家1）設為 true；Bot 的牌及已出的牌設為 false。
     */
    private Boolean canClick = false;

    /**
     * 當前的選取狀態。
     * true  = 已被點選（牌張往上移 20px，表示「選中」狀態）
     * false = 未被點選（牌張在原位）
     */
    private Boolean clicked = false;

    /**
     * 牌張在畫面上的邏輯座標（快取值）。
     * 因 JLabel 繼承自 Component，其 getLocation() 直接查詢 Swing 視覺位置，
     * 但動畫中途會呼叫 Component.setLocation(int,int)（不更新此欄位），
     * 所以需要此欄位來追蹤「最終確定的邏輯位置」，避免錯誤計算偏移量。
     */
    private Point location;

    // ─────────────────────────────────────────────────────────────────────────
    // 建構子
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 建立一張撲克牌元件。
     *
     * @param name 牌張名稱，格式 "花色-點數"，例如 "1-3"
     * @param up   初始牌面朝向：true = 正面；false = 反面
     */
    public Poker(String name, Boolean up) {
        this.name = name;
        this.up   = up;

        // 根據初始朝向設定對應圖片
        if (up) turnFront();
        else    turnBack();

        // 設定牌張的固定尺寸（寬 71px、高 96px，對應圖片大小）
        this.setSize(71, 96);
        // 設為可見
        this.setVisible(true);
        // 為此牌張加入滑鼠事件監聽器
        this.addMouseListener(this);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 牌面翻轉
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 將牌張翻到正面，顯示對應的花色點數圖片。
     *
     * 圖片命名規則：檔名格式為 "點數-花色.jpg"（與牌名 "花色-點數" 相反）。
     * 例：牌名 "3-7"（花色3、點數7）→ 圖片 "7-3.jpg"
     *
     * 使用 Common.loadIcon() 載入，自動支援 JAR 打包與開發環境兩種路徑。
     */
    public void turnFront() {
        // 從牌名 "花色-點數" 中拆分出花色與點數
        int    dash = name.indexOf('-');
        String suit = name.substring(0, dash);            // 花色（1–4）
        String rank = name.substring(dash + 1);           // 點數（1–13）
        // 圖片檔名為 "點數-花色.jpg"，與牌名順序相反
        this.setIcon(Common.loadIcon("/big/two/images/PokerCard/" + rank + "-" + suit + ".jpg"));
        this.up = true;
    }

    /**
     * 將牌張翻到反面，顯示統一的牌背圖片（green_back.jpg）。
     */
    public void turnBack() {
        this.setIcon(Common.loadIcon("/big/two/images/PokerCard/green_back.jpg"));
        this.up = false;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 滑鼠事件處理
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 滑鼠點擊事件處理器。
     *
     * 單擊行為（clickCount == 1）：
     *   - 若牌張未選取 → 往上移 20px，標記為已選取（clicked = true）
     *   - 若牌張已選取 → 往下移 20px，標記為未選取（clicked = false）
     *   - 選取時同步將此牌張的 Z 軸層級設為最前，避免被相鄰牌遮蓋
     *
     * 雙擊行為（clickCount == 2）：
     *   - 若牌張尚未選取，先選取（往上移）
     *   - 接著直接觸發出牌（呼叫 GameJFrame.instance.outCard()）
     *   - 相當於「選取並立即出牌」的快捷操作
     *
     * 注意：此方法只有在 canClick == true 時才執行（防止點擊 Bot 的牌或已出的牌）。
     * 注意：Swing 雙擊會先觸發一次 clickCount=1 事件，再觸發 clickCount=2 事件，
     *      因此雙擊時牌張會先被選取（由第一次事件），再觸發出牌（由第二次事件）。
     *
     * @param e 滑鼠事件，包含點擊次數（getClickCount）與事件來源
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!canClick) return; // 此牌不可被點擊，直接忽略

        if (e.getClickCount() == 2) {
            // ── 雙擊：確保牌已選取，然後立即出牌 ───────────────────────────
            if (!clicked) {
                // 牌尚未選取，先將其往上移並標記為已選取
                clicked = true;
                Point from = this.getLocation();
                this.setLocation(new Point(from.x, from.y - 20));
                // 選取時移到最前層，避免被鄰近的牌遮蓋
                Container parent = getParent();
                if (parent != null) {
                    parent.setComponentZOrder(this, 0);
                    parent.repaint();
                }
            }
            // 觸發出牌邏輯（outCard 會收集所有已選取的牌並驗證後出牌）
            if (GameJFrame.instance != null) {
                GameJFrame.instance.outCard(null);
            }

        } else if (e.getClickCount() == 1) {
            // ── 單擊：切換選取狀態 ───────────────────────────────────────────
            // 若已選取 → 往下 20px（放回）；若未選取 → 往上 20px（選取）
            int step = clicked ? 20 : -20;
            clicked = !clicked;

            Point from = this.getLocation();
            this.setLocation(new Point(from.x, from.y + step));

            // 選取時移到最前層，避免被右側的牌遮蓋
            if (clicked) {
                Container parent = getParent();
                if (parent != null) {
                    parent.setComponentZOrder(this, 0);
                    parent.repaint();
                }
            }
        }
    }

    /** 滑鼠按下事件（本遊戲不需要處理，保留空實作）*/
    @Override public void mousePressed(MouseEvent e)  {}

    /** 滑鼠放開事件（本遊戲不需要處理，保留空實作）*/
    @Override public void mouseReleased(MouseEvent e) {}

    /** 滑鼠移入元件事件（本遊戲不需要處理，保留空實作）*/
    @Override public void mouseEntered(MouseEvent e)  {}

    /** 滑鼠移出元件事件（本遊戲不需要處理，保留空實作）*/
    @Override public void mouseExited(MouseEvent e)   {}

    // ─────────────────────────────────────────────────────────────────────────
    // Getter / Setter
    // ─────────────────────────────────────────────────────────────────────────

    /** 取得牌張名稱（格式："花色-點數"） */
    public String getName()             { return name; }
    /** 設定牌張名稱 */
    public void   setName(String name)  { this.name = name; }

    /** 取得牌面朝向（true=正面，false=反面） */
    public Boolean getUp()              { return up; }
    /** 設定牌面朝向 */
    public void    setUp(Boolean up)    { this.up = up; }

    /** 取得是否可被點擊 */
    public Boolean getCanClick()                  { return canClick; }
    /** 設定是否可被點擊（true=自己的手牌；false=Bot/已出的牌） */
    public void    setCanClick(Boolean canClick)  { this.canClick = canClick; }

    /** 取得選取狀態（true=已選取/往上移，false=未選取/在原位） */
    public Boolean getClicked()                 { return clicked; }
    /** 設定選取狀態（會直接修改欄位，不移動牌張位置，由呼叫方負責視覺同步） */
    public void    setClicked(Boolean clicked)  { this.clicked = clicked; }

    // ─────────────────────────────────────────────────────────────────────────
    // 位置管理（覆寫 Component 的 getLocation/setLocation）
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 取得牌張的邏輯座標（快取值）。
     *
     * 覆寫原因：Common.move() 的動畫過程中呼叫的是 Component.setLocation(int,int)
     * （不更新本類別的 location 欄位），只有動畫結束時才呼叫 Poker.setLocation(Point)
     * 更新快取。因此 getLocation() 回傳的是「最後一次確定的位置」，
     * 不會受動畫中途座標影響，確保偏移量計算正確。
     *
     * @return 牌張的邏輯座標（Point 物件）
     */
    @Override
    public Point getLocation() { return location; }

    /**
     * 設定牌張的邏輯座標，同步更新快取並移動 Swing 元件的視覺位置。
     *
     * 注意：此方法儲存的是 location 的副本（new Point），而非原始引用。
     *       若直接儲存引用，rePosition() 迴圈中的共享 Point p 物件
     *       在迴圈結束後會持續被修改，導致所有牌的 location 都指向最終值，
     *       造成點擊時偏移量計算錯誤（所有牌都跳到同一個位置）。
     *
     * @param location 目標座標
     */
    @Override
    public void setLocation(Point location) {
        // 儲存副本，防止外部的 Point 物件被修改後影響此牌的位置記錄
        this.location = new Point(location.x, location.y);
        // 同步更新 Swing 元件的實際視覺位置
        super.setLocation(location.x, location.y);
    }
}
