#!/bin/bash
# =============================================================================
#  build-mac.sh  ─  大老二遊戲 Mac 打包腳本
# =============================================================================
#
#  執行結果：在 build/package/ 目錄下產生 BigTwo.dmg
#            （自帶 JRE，使用者不需要另外安裝 Java）
#
#  前置需求：
#    - macOS 10.15 (Catalina) 或更新版本
#    - JDK 14 或更新版本（需包含 jpackage 指令）
#    - 在終端機執行前請確認有執行權限：chmod +x build-mac.sh
#
#  使用方式：
#    ./build-mac.sh
#
# =============================================================================

set -e  # 任何指令失敗即立刻終止腳本（避免後續步驟在錯誤狀態下繼續）

# ── 路徑設定 ─────────────────────────────────────────────────────────────────
# 取得腳本所在目錄的絕對路徑，確保無論從哪個目錄執行腳本都能正確找到資源
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"                   # Java 原始碼目錄
BUILD_DIR="$PROJECT_DIR/build"               # 所有中間產物目錄
CLASSES_DIR="$BUILD_DIR/classes"             # 編譯後的 .class 檔案
DIST_DIR="$BUILD_DIR/dist"                   # JAR 暫存目錄（供 jpackage 讀取）
PACKAGE_DIR="$BUILD_DIR/package"             # 最終輸出目錄（DMG）
ICONS_DIR="$BUILD_DIR/icons"                 # 圖示轉換暫存目錄
LOGO_PNG="$SRC_DIR/big/two/images/LOGO.png"  # 遊戲 LOGO 圖片（用來產生 App 圖示）

echo "========================================"
echo "  大老二遊戲 Mac 打包腳本"
echo "========================================"
echo ""

# ── 步驟 1：清理並建立輸出目錄 ──────────────────────────────────────────────
echo "[1/6] 清理舊的輸出目錄..."
rm -rf "$BUILD_DIR"
mkdir -p "$CLASSES_DIR" "$DIST_DIR" "$PACKAGE_DIR" "$ICONS_DIR"
echo "      完成"

# ── 步驟 2：找出所有 .java 原始碼並編譯 ─────────────────────────────────────
echo "[2/6] 編譯 Java 原始碼..."
# 將所有 .java 路徑寫入暫存檔（避免超出 shell 命令列長度限制）
find "$SRC_DIR/big/two" -name "*.java" > "$BUILD_DIR/sources.txt"
# -encoding UTF-8：確保中文字元備注能正確編譯
# -d：指定 .class 輸出目錄
javac -encoding UTF-8 -d "$CLASSES_DIR" @"$BUILD_DIR/sources.txt"
rm "$BUILD_DIR/sources.txt"
echo "      完成"

# ── 步驟 3：複製圖片資源（維持 classpath 結構）──────────────────────────────
echo "[3/6] 複製圖片資源..."
# 圖片必須放在 CLASSES_DIR 的對應套件目錄下，才能被 Common.loadIcon() 正確讀取
# 目標路徑：classes/big/two/images/（對應 classpath 資源路徑 /big/two/images/）
cp -r "$SRC_DIR/big/two/images" "$CLASSES_DIR/big/two/"
echo "      完成（複製至 $CLASSES_DIR/big/two/images/）"

# ── 步驟 4：打包成可執行 JAR ─────────────────────────────────────────────────
echo "[4/6] 打包成可執行 JAR..."
# 建立 MANIFEST.MF：告知 JVM 程式進入點為 big.two.Test
cat > "$BUILD_DIR/MANIFEST.MF" << 'EOF'
Manifest-Version: 1.0
Main-Class: big.two.Test
EOF
# 建立包含所有 class 與圖片資源的 JAR
jar cfm "$DIST_DIR/BigTwo.jar" "$BUILD_DIR/MANIFEST.MF" -C "$CLASSES_DIR" .
echo "      完成（$DIST_DIR/BigTwo.jar）"

# ── 步驟 5：將 LOGO.png 轉換為 macOS 所需的 ICNS 圖示格式 ──────────────────
echo "[5/6] 轉換 App 圖示（PNG → ICNS）..."
ICONSET_DIR="$ICONS_DIR/BigTwo.iconset"
ICNS_FILE="$ICONS_DIR/BigTwo.icns"
mkdir -p "$ICONSET_DIR"

# sips：macOS 內建圖片轉換工具，用來產生各種尺寸的圖示
# macOS App 圖示需要多種解析度（Retina 顯示器需要 @2x 版本）
for size in 16 32 64 128 256 512; do
    sips -z $size $size "$LOGO_PNG" --out "$ICONSET_DIR/icon_${size}x${size}.png" \
         > /dev/null 2>&1 || true
done
# 額外產生 Retina (@2x) 版本
for size in 16 32 128 256 512; do
    double=$((size * 2))
    sips -z $double $double "$LOGO_PNG" \
         --out "$ICONSET_DIR/icon_${size}x${size}@2x.png" \
         > /dev/null 2>&1 || true
done

# iconutil：將 iconset 資料夾轉換為 .icns 檔
if iconutil -c icns "$ICONSET_DIR" -o "$ICNS_FILE" 2>/dev/null; then
    ICON_ARG="--icon $ICNS_FILE"
    echo "      完成（$ICNS_FILE）"
else
    # 圖片太小或 iconutil 失敗時跳過圖示，使用預設圖示繼續
    ICON_ARG=""
    echo "      警告：圖示轉換失敗，將使用預設圖示"
fi

# ── 步驟 6：使用 jpackage 建立 Mac DMG 安裝包 ───────────────────────────────
echo "[6/6] 使用 jpackage 建立 DMG..."
# jpackage 說明：
#   --input      ：JAR 所在目錄（jpackage 會把此目錄下的所有檔案打包進 App）
#   --dest       ：DMG 輸出目錄
#   --name       ：App 名稱（會顯示在 Launchpad 和 Finder 中）
#   --main-jar   ：程式進入點 JAR 的檔名
#   --main-class ：JAR 中的 main 方法所在類別
#   --type dmg   ：輸出格式（dmg = Mac 磁碟映像，安裝後可出現在 Applications）
#   --app-version：App 版本號（顯示於 Finder 的「取得資訊」）
#   --vendor     ：開發者名稱
#   --mac-package-name：選用，控制 App bundle 中的顯示名稱
jpackage \
    --input        "$DIST_DIR"       \
    --dest         "$PACKAGE_DIR"    \
    --name         "BigTwo"          \
    --main-jar     "BigTwo.jar"      \
    --main-class   "big.two.Test"    \
    --type         dmg               \
    --app-version  "1.0"             \
    --vendor       "Student Project" \
    $ICON_ARG

echo ""
echo "========================================"
echo "  打包完成！"
echo "  DMG 路徑：$PACKAGE_DIR/BigTwo-1.0.dmg"
echo ""
echo "  使用方式："
echo "    1. 雙擊 DMG 檔案掛載磁碟映像"
echo "    2. 將 BigTwo.app 拖曳至 Applications 資料夾"
echo "    3. 在 Launchpad 或 Applications 中開啟 BigTwo"
echo "========================================"
