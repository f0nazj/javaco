package big.two;

import java.util.*;

public class Rule {

    // ── 輔助：取牌的數值與花色 ────────────────────────────────────────────────
    // 名稱格式："花色-點數"  花色∈[1-4]  點數∈[1-13]  (1=A, 2=2, 3=3 … 13=K)
    // 大小比較用值：3–13 原值；A(1)→14；2→15（大老二排序：3最小、2最大）

    static int getValue(Poker p) {
        int rank = getRawRank(p);
        if (rank == 1) return 14;
        if (rank == 2) return 15;
        return rank;
    }

    static int getSuit(Poker p) {
        String name = p.getName();
        return Integer.parseInt(name.substring(0, name.indexOf('-')));
    }

    // 原始點數（面值）：A=1, 2=2, 3=3 … K=13
    private static int getRawRank(Poker p) {
        String name = p.getName();
        return Integer.parseInt(name.substring(name.indexOf('-') + 1));
    }

    // 大小比較器：先比點數，相同時比花色
    static int cmp(Poker a, Poker b) {
        int d = getValue(a) - getValue(b);
        return d != 0 ? d : getSuit(a) - getSuit(b);
    }

    // 取出牌組中比較值最高的牌
    static Poker top(List<Poker> cards) {
        return Collections.max(cards, Rule::cmp);
    }

    // ── 取得所有牌的原始點數（面值）────────────────────────────────────────────
    private static List<Integer> rawRanks(List<Poker> cards) {
        List<Integer> rs = new ArrayList<>();
        for (Poker p : cards) rs.add(getRawRank(p));
        return rs;
    }

    // ── 牌型判斷 ──────────────────────────────────────────────────────────────
    // 0=無效  1=單張  2=對子  3=順子  4=同花
    // 5=葫蘆  6=鐵支(4+1)  7=同花順

    public static int typeOf(List<Poker> cards) {
        if (cards == null || cards.isEmpty()) return 0;
        int n = cards.size();
        if (n == 1) return 1;
        if (n == 2) return getValue(cards.get(0)) == getValue(cards.get(1)) ? 2 : 0;
        if (n == 5) return fiveType(cards);
        return 0;
    }

    private static int fiveType(List<Poker> c) {
        boolean flush    = isFlush(c);
        boolean straight = isStraight(c);
        if (flush && straight) return 7; // 同花順
        Map<Integer,Integer> freq = freq(c);
        int max = Collections.max(freq.values());
        if (max == 4) return 6;                            // 鐵支
        if (max == 3 && freq.containsValue(2)) return 5;  // 葫蘆
        if (flush)    return 4;                            // 同花
        if (straight) return 3;                            // 順子
        return 0;
    }

    // ── 同花判斷 ──────────────────────────────────────────────────────────────
    private static boolean isFlush(List<Poker> c) {
        int s = getSuit(c.get(0));
        for (Poker p : c) if (getSuit(p) != s) return false;
        return true;
    }

    // ── 順子判斷（使用面值，並處理特殊情況）─────────────────────────────────────
    // 有效順子：A-2-3-4-5（最小）、3-4-5-6-7 … 10-J-Q-K-A、2-3-4-5-6（最大）
    // 無效：J-Q-K-A-2 及任何含 2 但非 2-3-4-5-6 的組合
    private static boolean isStraight(List<Poker> c) {
        List<Integer> rs = rawRanks(c);
        Collections.sort(rs);

        // 特殊：A-2-3-4-5（A 作最小牌）
        if (rs.equals(Arrays.asList(1, 2, 3, 4, 5))) return true;
        // 特殊：2-3-4-5-6（2 作最大牌，這是最大的順子）
        if (rs.equals(Arrays.asList(2, 3, 4, 5, 6))) return true;
        // 特殊：10-J-Q-K-A（A 作最大牌，sorted 後 A=1 排在最前）
        if (rs.equals(Arrays.asList(1, 10, 11, 12, 13))) return true;

        // 一般順子：3–K（面值 3–13），不含 A(1) 或 2
        for (int r : rs) if (r == 1 || r == 2) return false;
        for (int i = 0; i < 4; i++) if (rs.get(i + 1) - rs.get(i) != 1) return false;
        return true;
    }

    // ── 順子排名（數值越大順子越大）────────────────────────────────────────────
    // A-2-3-4-5 = 0（最小）
    // 3-4-5-6-7 = 7 … 9-10-J-Q-K = 13 … 10-J-Q-K-A = 14
    // 2-3-4-5-6 = 16（最大）
    private static int straightOrd(List<Poker> c) {
        List<Integer> rs = rawRanks(c);
        Collections.sort(rs);
        if (rs.equals(Arrays.asList(1, 2, 3, 4, 5)))    return 0;  // 最小
        if (rs.equals(Arrays.asList(1, 10, 11, 12, 13))) return 14; // 10-J-Q-K-A
        if (rs.equals(Arrays.asList(2, 3, 4, 5, 6)))    return 16; // 最大
        return rs.get(4); // 一般順子：最高面值 (7–13)
    }

    // ── 頻率統計（以比較值分組）──────────────────────────────────────────────────
    private static Map<Integer,Integer> freq(List<Poker> c) {
        Map<Integer,Integer> m = new HashMap<>();
        for (Poker p : c) m.merge(getValue(p), 1, Integer::sum);
        return m;
    }

    // ── 同型比較鍵（數值越大牌組越大）───────────────────────────────────────────
    private static long key(List<Poker> c, int type) {
        switch (type) {
            case 1: case 2: { // 單張、對子：比最高牌（點數優先，再比花色）
                Poker h = top(c);
                return (long) getValue(h) * 10 + getSuit(h);
            }
            case 3: case 7: { // 順子、同花順：先比順子排名，再比代表牌花色
                // 同花順所有牌同花，top(c) 的花色即為同花色
                return (long) straightOrd(c) * 10 + getSuit(top(c));
            }
            case 4: { // 同花：先比花色，再比最高牌點數
                return (long) getSuit(c.get(0)) * 200 + getValue(top(c));
            }
            case 5: { // 葫蘆：比三張的點數
                for (Map.Entry<Integer,Integer> e : freq(c).entrySet())
                    if (e.getValue() == 3) return e.getKey();
                return 0;
            }
            case 6: { // 鐵支：比四張的點數
                for (Map.Entry<Integer,Integer> e : freq(c).entrySet())
                    if (e.getValue() == 4) return e.getKey();
                return 0;
            }
            default: return 0;
        }
    }

    // ── 出牌合法性判斷 ────────────────────────────────────────────────────────
    //
    // 規則層級（由低到高）：
    //   單張 < 對子 < 順子 < 同花 < 葫蘆 < 鐵支(4+1) < 同花順
    //
    // 同型才可互壓（順子只打順子、同花只打同花、葫蘆只打葫蘆）。
    // 鐵支例外：可壓 單張、對子、同花、葫蘆，以及比自己小的鐵支；但不能壓順子。
    // 同花順例外：可壓所有牌型（包括鐵支），同花順之間比大小。
    //
    // table == null 或為空 → 新回合，任何合法牌組皆可出。

    public static boolean canBeat(List<Poker> play, List<Poker> table) {
        if (table == null || table.isEmpty()) return typeOf(play) != 0;
        int pt = typeOf(play), tt = typeOf(table);
        if (pt == 0) return false;

        // 同花順（7）：壓所有牌型；同花順 vs 同花順比大小
        if (pt == 7) {
            return tt != 7 || key(play, 7) > key(table, 7);
        }

        // 鐵支（6）：壓單張(1)、對子(2)、同花(4)、葫蘆(5) 及更小的鐵支(6)
        //           不能壓順子(3) 和 同花順(7)
        if (pt == 6) {
            switch (tt) {
                case 1: case 2: case 4: case 5: return true;
                case 6: return key(play, 6) > key(table, 6);
                default: return false; // 不能壓順子(3) 或同花順(7)
            }
        }

        // 其餘牌型：必須同型同張數才可互壓
        if (pt != tt || play.size() != table.size()) return false;
        return key(play, pt) > key(table, tt);
    }

    // ── Bot 出牌 AI ───────────────────────────────────────────────────────────
    // 回傳「能打敗桌面且最小的合法牌組」，回傳 null 代表 Pass。
    // table == null 或為空 → 新回合，Bot 出手上最小的單張。

    public static ArrayList<Poker> botFind(ArrayList<Poker> hand, List<Poker> table) {
        if (hand.isEmpty()) return null;
        if (table == null || table.isEmpty()) {
            // 新回合：出最小單張
            ArrayList<Poker> play = new ArrayList<>();
            play.add(Collections.min(hand, Rule::cmp));
            return play;
        }

        int size = table.size();
        if (size == 1) return findSingle(hand, table);
        if (size == 2) return findPair(hand, table);
        if (size == 5) return findFive(hand, table);
        return null;
    }

    // 找最小能打敗桌面的單張
    private static ArrayList<Poker> findSingle(ArrayList<Poker> hand, List<Poker> table) {
        ArrayList<Poker> sorted = new ArrayList<>(hand);
        sorted.sort(Rule::cmp);
        for (Poker p : sorted) {
            ArrayList<Poker> play = new ArrayList<>();
            play.add(p);
            if (canBeat(play, table)) return play;
        }
        return null;
    }

    // 找最小能打敗桌面的對子
    private static ArrayList<Poker> findPair(ArrayList<Poker> hand, List<Poker> table) {
        Map<Integer, ArrayList<Poker>> byRank = new HashMap<>();
        for (Poker p : hand)
            byRank.computeIfAbsent(getValue(p), k -> new ArrayList<>()).add(p);
        List<Integer> ranks = new ArrayList<>(byRank.keySet());
        Collections.sort(ranks);
        for (int rank : ranks) {
            ArrayList<Poker> g = byRank.get(rank);
            if (g.size() < 2) continue;
            g.sort(Rule::cmp);
            // 取同點數中最大的兩張（花色最大的兩張）組成對子
            ArrayList<Poker> pair = new ArrayList<>(g.subList(g.size() - 2, g.size()));
            if (canBeat(pair, table)) return pair;
        }
        return null;
    }

    // 找最小能打敗桌面的五張組合
    // 因 canBeat 已內建型別限制（順子只打順子、鐵支可打同花/葫蘆…），
    // 這裡直接枚舉所有 C(n,5) 組合並過濾，再取最小有效的。
    private static ArrayList<Poker> findFive(ArrayList<Poker> hand, List<Poker> table) {
        int n = hand.size();
        ArrayList<ArrayList<Poker>> cands = new ArrayList<>();
        for (int a = 0; a < n - 4; a++)
            for (int b = a + 1; b < n - 3; b++)
                for (int c = b + 1; c < n - 2; c++)
                    for (int d = c + 1; d < n - 1; d++)
                        for (int e = d + 1; e < n; e++) {
                            ArrayList<Poker> combo = new ArrayList<>(Arrays.asList(
                                hand.get(a), hand.get(b), hand.get(c), hand.get(d), hand.get(e)));
                            if (canBeat(combo, table)) cands.add(combo);
                        }
        if (cands.isEmpty()) return null;
        // 先選型別最小的（保留較大的炸彈），再選同型別中鍵值最小的
        cands.sort((x, y) -> {
            int tx = typeOf(x), ty = typeOf(y);
            return tx != ty ? tx - ty : Long.compare(key(x, tx), key(y, ty));
        });
        return cands.get(0);
    }
}
