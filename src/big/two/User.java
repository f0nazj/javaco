package big.two;

/**
 * User — 使用者資料模型
 *
 * 儲存一位使用者的帳號與密碼資訊。
 * 提供 equals() 與 hashCode() 覆寫，支援 List.contains() 的登入驗證邏輯：
 *   只要帳號與密碼都相同，即視為同一個使用者（登入成功）。
 *
 * 使用情境：
 *   - LoginJFrame 呼叫 list.contains(new User(帳號, 密碼)) 驗證登入
 *   - RegisterJFrame 建立新的 User 物件並加入 LoginJFrame.list
 */
public class User {

    /** 使用者帳號（登入名稱，不可重複） */
    private String username;

    /** 使用者密碼（明文儲存，僅供練習用途） */
    private String password;

    // ─────────────────────────────────────────────────────────────────────────
    // 建構子
    // ─────────────────────────────────────────────────────────────────────────

    /** 無參數建構子（供序列化或框架使用） */
    public User() {}

    /**
     * 建立帶帳號與密碼的使用者物件。
     *
     * @param username 使用者帳號
     * @param password 使用者密碼
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Getter / Setter
    // ─────────────────────────────────────────────────────────────────────────

    /** 取得使用者帳號 */
    public String getUsername()              { return username; }
    /** 設定使用者帳號 */
    public void   setUsername(String username) { this.username = username; }

    /** 取得使用者密碼 */
    public String getPassword()              { return password; }
    /** 設定使用者密碼 */
    public void   setPassword(String password) { this.password = password; }

    // ─────────────────────────────────────────────────────────────────────────
    // equals / hashCode（用於 List.contains 登入驗證）
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 計算雜湊值，以 username 和 password 為依據。
     * 供 HashMap / HashSet 使用，與 equals() 保持一致。
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        return result;
    }

    /**
     * 判斷兩個 User 是否相等（帳號與密碼均相同才視為相等）。
     * LoginJFrame 使用 list.contains(user) 進行登入驗證，
     * 底層即呼叫此方法逐一比對集合中的使用者。
     *
     * @param obj 要比較的物件
     * @return true = 帳號密碼完全相同（登入成功條件）；false = 不相同
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;           // 同一個物件參考
        if (obj == null) return false;          // null 直接否定
        if (getClass() != obj.getClass()) return false; // 型別不同直接否定
        User other = (User) obj;
        // 比對帳號
        if (username == null ? other.username != null : !username.equals(other.username))
            return false;
        // 比對密碼
        if (password == null ? other.password != null : !password.equals(other.password))
            return false;
        return true;
    }
}
