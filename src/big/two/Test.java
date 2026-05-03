package big.two;

/**
 * Test — 程式進入點（main 方法所在類別）
 *
 * 負責啟動整個大老二遊戲應用程式。
 * 程式流程如下：
 *
 *   LoginJFrame（登入頁）
 *     ├─ 登入成功 → GameJFrame（遊戲主畫面）
 *     └─ 點選註冊 → RegisterJFrame（註冊頁）
 *                      └─ 註冊完成 → LoginJFrame（返回登入頁）
 *
 * 注意：LoginJFrame 的建構子會將視窗設為可見並進入 Swing 事件循環，
 *       main() 方法在建立 LoginJFrame 後即結束，
 *       程式的後續生命週期由 Swing 的 EDT 管理。
 */
public class Test {

    /**
     * 應用程式進入點。
     *
     * 直接在主執行緒建立 LoginJFrame；
     * 若需嚴格遵循 Swing 執行緒安全規範，
     * 可改為 SwingUtilities.invokeLater(() -> new LoginJFrame())。
     *
     * @param args 命令列引數（本程式不使用）
     */
    public static void main(String[] args) {
        // 開啟登入視窗，作為遊戲的起點
        new LoginJFrame();
    }
}
