package big.two;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Poker extends JLabel implements MouseListener {
    // 屬性
    // 1.牌的名字 格式：數字 - 數字
    private String name;
    // 2. 牌顯示的是正面是反面
    private Boolean up;
    // 3.是否可以被點擊(不能點別人的牌)
    private Boolean canClick = false;
    // 4.當前的狀態, 表是當前的牌是否已經是否已經被點擊
    // 如果是沒有被點擊的狀態, 此時點擊了, 會執行彈起的狀態
    // 如果是已經被點擊的狀態, 此時點擊了, 會執行放下的狀態
    private Boolean clicked = false;
    private Point location;

    public Poker(String name, Boolean up) {
        this.name = name;
        this.up = up;

        // 判斷當前的牌面是正面還是反面
        if (up) {
            // 顯示正面
            turnFront();
        } else {
            // 顯示反面
            turnBack();
        }

        // 設置牌的寬高
        this.setSize(71, 96);
        // 把牌顯示出來
        this.setVisible(true);
        // 給每一張牌添加監聽
        this.addMouseListener(this);
    }

    // 顯示正面
    public void turnFront() {
        // 圖片命名為 rank-suit，但 name 格式為 suit-rank，需要對調
        int dash = name.indexOf('-');
        String suit = name.substring(0, dash);
        String rank = name.substring(dash + 1);
        this.setIcon(new ImageIcon("src/big/two/images/PokerCard/" + rank + "-" + suit + ".jpg"));
        this.up = true;
    }

    // 顯示反面
    public void turnBack() {
        // 給牌設置反面
        this.setIcon(new ImageIcon("src/big/two/images/PokerCard/green_back.jpg"));
        // 修改成員變量(up變成是指牌的正反面)
        this.up = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!canClick) return;

        if (e.getClickCount() == 2) {
            // 雙擊：確保牌已選取後直接出牌
            if (!clicked) {
                clicked = true;
                Point from = this.getLocation();
                this.setLocation(new Point(from.x, from.y - 20));
                Container parent = getParent();
                if (parent != null) {
                    parent.setComponentZOrder(this, 0);
                    parent.repaint();
                }
            }
            if (GameJFrame.instance != null) {
                GameJFrame.instance.outCard(null);
            }
        } else if (e.getClickCount() == 1) {
            // 單擊：切換選取狀態
            int step = clicked ? 20 : -20;
            clicked = !clicked;
            Point from = this.getLocation();
            this.setLocation(new Point(from.x, from.y + step));
            if (clicked) {
                Container parent = getParent();
                if (parent != null) {
                    parent.setComponentZOrder(this, 0);
                    parent.repaint();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUp() {
        return up;
    }

    public void setUp(Boolean up) {
        this.up = up;
    }

    public Boolean getCanClick() {
        return canClick;
    }

    public void setCanClick(Boolean canClick) {
        this.canClick = canClick;
    }

    public Boolean getClicked() {
        return clicked;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = new Point(location.x, location.y);
        super.setLocation(location.x, location.y);
    }
}
