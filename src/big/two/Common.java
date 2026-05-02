package big.two;

import java.awt.Point;
import java.util.ArrayList;

public class Common {
    // 此類為尚未學習部分, 所以以下部分為拷貝過來的尚未自己寫
    // 移動牌(有移動的動畫效果)
    public static void move(Poker poker, Point from, Point to) {
        // 如果起點是空的，默認從中央位置開始移動
        if (from == null) {
            from = new Point(360, 220);
        }
        // 計算直線運動的斜率和截距 y = kx + b
        if (to.x != from.x) {
            double k = (1.0) * (to.y - from.y) / (to.x - from.x); // 斜率k
            double b = to.y - to.x * k; // 截距b
            // 設定移動方向標誌: 正數向右移動, 負數向左移動
            int flag = from.x < to.x ? 20 : -20;
            // 循环迭代: 每次将牌向目标方向移动一小步，形成动画效果
            for (int i = from.x; Math.abs(i - to.x) > 20; i += flag) {
                double y = k * i + b; // 根据当前 x 计算 y 坐标
                poker.setLocation(i, (int) y); // 更新牌的位置
                try {
                    Thread.sleep(5); // 暂停 5 毫秒，形成平滑动画效果
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // 移動完成後，確保牌完全到達目標位置
        poker.setLocation(to);
    }

    // 重新擺放牌
    // 參數一：遊戲介面
    // 參數二：要重新擺放順序的集合
    // 參數三：標記
    // // 0表示左邊玩家 1表示自己 2表示右邊玩家
    public static void rePosition(GameJFrame m, ArrayList<Poker> list, int flag) {
        // 根據玩家標記，確定牌組的起始位置
        Point p = new Point();
        if (flag == 0) { // 左边玩家
            p.x = 50; // x=50, 靠左
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2; // y居中
        }
        if (flag == 1) { // 中间玩家(自己)
            p.x = (800 / 2) - (list.size() + 1) * 21 / 2; // x居中
            p.y = 450; // y固定在下方
        }
        if (flag == 2) { // 右边玩家
            p.x = 700; // x=700, 靠右
            p.y = (450 / 2) - (list.size() + 1) * 15 / 2; // y居中
        }
        // 對當前玩家的每張牌，依次進行動畫移動
        int len = list.size();
        for (int i = 0; i < len; i++) {
            Poker poker = list.get(i);

            // 將每張牌移動到計算出的新位置
            move(poker, poker.getLocation(), p);

            // 將牌的顯示層級設置為最前(0為最高層)
            m.container.setComponentZOrder(poker, 0);

            // 根據玩家位置，選擇水平或垂直移動下一張牌的位置
            if (flag == 1) {
                p.x += 20;
            } else {
                p.y += 15;
            }
        }
    }
}