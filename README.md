# BigTwo 大老二

Java Swing 製作的大老二小遊戲，包含登入/註冊畫面、發牌動畫、玩家出牌判定、兩位 bot 自動出牌、Pass 回合控制，以及先出完牌者勝利的流程。

## 遊戲功能

- 3 人遊戲：玩家自己 + 左右兩位 bot
- 登入與註冊畫面
- 發牌動畫與牌面顯示
- 單擊選牌，雙擊快速出牌
- Pass 後會等到下一輪才能再次行動
- bot 會自動尋找可以壓過桌面牌型的出牌組合
- 支援牌型：
  - 單張
  - 對子
  - 順子
  - 同花
  - 葫蘆
  - 鐵支
  - 同花順

## 預設帳號

```text
帳號：admin
密碼：123456
```

也可以在登入頁點「註冊」建立新帳號。註冊資料目前只存在程式記憶體中，關閉程式後不會保存。

## 下載遊玩

到 GitHub 的 **Actions** 或 **Releases** 下載對應平台的檔案：

- macOS：下載 `BigTwo-1.0.dmg`
- Windows：下載 `BigTwo-windows.zip`，解壓縮後執行 `BigTwo.exe`

如果專案有建立 `v` 開頭的 tag，例如 `v1.0.0`，GitHub Actions 會自動建立 Release 並附上 Mac/Windows 下載檔。

## 本機執行

需要 JDK 21，或至少 JDK 14 以上。

```bash
javac -encoding UTF-8 -d build/classes $(find src/big/two -name "*.java")
cp -r src/big/two/images build/classes/big/two/
java -cp build/classes big.two.Test
```

## 本機打包

### macOS

```bash
chmod +x build-mac.sh
./build-mac.sh
```

輸出位置：

```text
build/package/BigTwo-1.0.dmg
```

### Windows

在 Windows 的 cmd 執行：

```bat
build-windows.bat
```

輸出位置：

```text
build\package\BigTwo\BigTwo.exe
```

可以把整個 `build\package\BigTwo` 資料夾壓縮成 zip 分享。

## GitHub 自動打包

本專案已加入 GitHub Actions：

```text
.github/workflows/package-bigtwo.yml
```

觸發方式：

- push 到 `main`
- push 到 `claude/**` 分支
- 手動在 GitHub Actions 頁面點 `Run workflow`
- push `v*` tag，例如 `v1.0.0`

一般 push 會產生 Actions Artifacts；push tag 則會額外建立 GitHub Release。

建立 Release 的常用指令：

```bash
git tag v1.0.0
git push origin v1.0.0
```

## 專案結構

```text
src/big/two/
├── Test.java             # 程式進入點
├── LoginJFrame.java      # 登入畫面
├── RegisterJFrame.java   # 註冊畫面
├── GameJFrame.java       # 遊戲主畫面與回合流程
├── Poker.java            # 單張牌元件
├── Rule.java             # 出牌規則與 bot 判斷
├── Common.java           # 圖片載入、動畫、牌面重排工具
├── User.java             # 使用者資料物件
├── CodeUtil.java         # 驗證碼工具
└── images/               # 背景、LOGO、撲克牌圖片
```

## 備註

這是一個課程/練習型 Swing 專案。圖片資源已改成 classpath 載入，因此在 IDE、JAR、jpackage 打包後都能正常顯示。
