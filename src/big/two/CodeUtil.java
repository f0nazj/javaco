package big.two;

import java.util.ArrayList;

/**
 * CodeUtil — 驗證碼產生工具類別
 *
 * 提供靜態方法 getCode()，用於登入畫面的驗證碼功能。
 *
 * 驗證碼組成規則：
 *   - 共 5 個字元
 *   - 前 4 個字元：從 a-z、A-Z 共 52 個字母中隨機抽取（允許重複）
 *   - 第 5 個字元：0-9 中的隨機數字（確保驗證碼一定含有數字）
 *
 * 設計目的：防止自動化程式暴力登入，要求使用者手動輸入顯示的驗證碼。
 */
public class CodeUtil {

    /**
     * 產生一組隨機驗證碼（4 個字母 + 1 個數字，共 5 碼）。
     *
     * 產生步驟：
     *   1. 建立包含 a-z 與 A-Z 的字母集合（共 52 個字元）
     *   2. 隨機從字母集合中取出 4 個字元（允許重複，每次獨立抽取）
     *   3. 隨機產生 1 個 0-9 的數字
     *   4. 將 4 個字母與 1 個數字串接後回傳
     *
     * 注意：數字固定放在最後一位，實際驗證比對時應忽略大小寫問題
     *      （本專案目前為大小寫敏感比對）。
     *
     * @return 5 碼驗證碼字串，例如 "aZbQ3"
     */
    public static String getCode() {
        // 步驟 1：建立字母候選集合（a-z 與 A-Z，共 52 個字元）
        ArrayList<Character> letters = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            letters.add((char) ('a' + i)); // 小寫字母 a～z
            letters.add((char) ('A' + i)); // 大寫字母 A～Z
        }

        // 步驟 2：隨機從字母集合中取出 4 個字元
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // 產生 [0, 52) 範圍的隨機索引，取出對應字母
            int index = (int) (Math.random() * letters.size());
            result.append(letters.get(index));
        }

        // 步驟 3：產生一個 0-9 的隨機數字並附加在末尾
        int digit = (int) (Math.random() * 10);
        result.append(digit);

        // 步驟 4：回傳最終的驗證碼字串
        return result.toString();
    }
}
