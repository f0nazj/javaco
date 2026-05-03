@echo off
REM =============================================================================
REM  build-windows.bat  ─  大老二遊戲 Windows 打包腳本
REM =============================================================================
REM
REM  執行結果：在 build\package\ 目錄下產生 BigTwo 資料夾（含可執行 .exe + JRE）
REM            若有安裝 WiX Toolset 3.0+，可改 --type app-image 為 --type exe
REM            產生單一的 .exe 安裝程式
REM
REM  前置需求：
REM    - Windows 10 或更新版本
REM    - JDK 14 或更新版本（需包含 jpackage.exe）
REM      下載：https://www.oracle.com/java/technologies/downloads/
REM    - 確認 JDK/bin 已加入系統 PATH 環境變數
REM    - （可選）WiX Toolset 3.0+，若要產生 .exe 安裝程式
REM      下載：https://wixtoolset.org/
REM
REM  使用方式：
REM    在命令提示字元（cmd）中執行：build-windows.bat
REM    （請勿在 PowerShell 中直接執行，可能有路徑問題）
REM
REM =============================================================================

REM ── 路徑設定 ─────────────────────────────────────────────────────────────────
REM %~dp0 = 此批次檔所在目錄的完整路徑（含尾端反斜線）
SET PROJECT_DIR=%~dp0
SET SRC_DIR=%PROJECT_DIR%src
SET BUILD_DIR=%PROJECT_DIR%build
SET CLASSES_DIR=%BUILD_DIR%\classes
SET DIST_DIR=%BUILD_DIR%\dist
SET PACKAGE_DIR=%BUILD_DIR%\package

echo ========================================
echo   大老二遊戲 Windows 打包腳本
echo ========================================
echo.

REM ── 步驟 1：檢查 JDK 與 jpackage 是否可用 ───────────────────────────────────
echo [1/6] 檢查環境...
where javac >nul 2>&1
IF ERRORLEVEL 1 (
    echo   錯誤：找不到 javac！請確認 JDK 已安裝且 JDK\bin 已加入 PATH。
    echo   下載 JDK：https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)
where jpackage >nul 2>&1
IF ERRORLEVEL 1 (
    echo   錯誤：找不到 jpackage！需要 JDK 14 或更新版本。
    pause
    exit /b 1
)
echo   完成

REM ── 步驟 2：清理並建立輸出目錄 ──────────────────────────────────────────────
echo [2/6] 清理舊的輸出目錄...
IF EXIST "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"
mkdir "%CLASSES_DIR%"
mkdir "%DIST_DIR%"
mkdir "%PACKAGE_DIR%"
echo   完成

REM ── 步驟 3：收集所有 .java 原始碼並編譯 ─────────────────────────────────────
echo [3/6] 編譯 Java 原始碼...
REM 使用 dir /s /b 遞迴找出所有 .java 檔案並寫入 sources.txt
dir /s /b "%SRC_DIR%\big\two\*.java" > "%BUILD_DIR%\sources.txt"
REM -encoding UTF-8：確保中文備注能正確編譯
REM -d：指定 .class 輸出目錄
javac -encoding UTF-8 -d "%CLASSES_DIR%" @"%BUILD_DIR%\sources.txt"
IF ERRORLEVEL 1 (
    echo   錯誤：編譯失敗！請檢查 Java 原始碼是否有語法錯誤。
    pause
    exit /b 1
)
del "%BUILD_DIR%\sources.txt"
echo   完成

REM ── 步驟 4：複製圖片資源（維持 classpath 結構）──────────────────────────────
echo [4/6] 複製圖片資源...
REM 圖片須放在 classes\big\two\images\ 才能被 Common.loadIcon() 正確讀取
xcopy /s /i /q "%SRC_DIR%\big\two\images" "%CLASSES_DIR%\big\two\images"
echo   完成

REM ── 步驟 5：建立 MANIFEST.MF 並打包成可執行 JAR ─────────────────────────────
echo [5/6] 打包成 JAR...
REM 建立 MANIFEST.MF 指定程式進入點
(
    echo Manifest-Version: 1.0
    echo Main-Class: big.two.Test
) > "%BUILD_DIR%\MANIFEST.MF"
REM 打包所有 class 與圖片資源為 BigTwo.jar
jar cfm "%DIST_DIR%\BigTwo.jar" "%BUILD_DIR%\MANIFEST.MF" -C "%CLASSES_DIR%" .
echo   完成 ^(%DIST_DIR%\BigTwo.jar^)

REM ── 步驟 6：使用 jpackage 建立 Windows 應用程式 ─────────────────────────────
echo [6/6] 使用 jpackage 建立 Windows App...
REM
REM 打包類型說明：
REM   app-image  ─ 產生一個資料夾，內含 BigTwo.exe + 完整 JRE
REM                不需要 WiX，使用者直接執行資料夾內的 .exe 即可
REM                （可壓縮成 .zip 分享）
REM
REM   exe        ─ 產生單一 .exe 安裝程式（需要 WiX Toolset 3.0+）
REM                安裝後會在「開始選單」建立捷徑，並支援「新增或移除程式」
REM                若已安裝 WiX，將下方的 app-image 改為 exe 即可
REM
jpackage ^
    --input       "%DIST_DIR%"         ^
    --dest        "%PACKAGE_DIR%"      ^
    --name        "BigTwo"             ^
    --main-jar    "BigTwo.jar"         ^
    --main-class  "big.two.Test"       ^
    --type        app-image            ^
    --app-version "1.0"                ^
    --vendor      "Student Project"    ^
    --win-console

IF ERRORLEVEL 1 (
    echo.
    echo   錯誤：jpackage 失敗！
    echo   請確認 JDK 版本 ^>= 14，並檢查上方錯誤訊息。
    pause
    exit /b 1
)

echo.
echo ========================================
echo   打包完成！
echo   App 路徑：%PACKAGE_DIR%\BigTwo\
echo.
echo   使用方式：
echo     1. 開啟 %PACKAGE_DIR%\BigTwo\ 資料夾
echo     2. 雙擊 BigTwo.exe 啟動遊戲
echo     3. 可將整個 BigTwo 資料夾壓縮成 .zip 分享給他人
echo.
echo   若要產生單一 .exe 安裝程式：
echo     1. 安裝 WiX Toolset 3.0+（https://wixtoolset.org/）
echo     2. 將此腳本中的 --type app-image 改為 --type exe
echo     3. 重新執行此腳本
echo ========================================
pause
