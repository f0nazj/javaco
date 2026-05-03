@echo off
setlocal

set APP_NAME=PuzzleGame
set MAIN_CLASS=gui.ui.App
set JAR_NAME=puzzle-game.jar
set DIST_DIR=dist
set INPUT_DIR=%DIST_DIR%\input

if not exist "%INPUT_DIR%" mkdir "%INPUT_DIR%"

echo Compiling Java files...
javac -encoding UTF-8 -cp bin -d bin src\gui\ui\App.java src\gui\ui\itheima\*.java
if errorlevel 1 exit /b 1

echo Packaging executable jar...
jar --create --file "%INPUT_DIR%\%JAR_NAME%" --main-class %MAIN_CLASS% -C bin gui\ui
if errorlevel 1 exit /b 1

echo Building Windows app image with jpackage...
jpackage ^
  --type app-image ^
  --name "%APP_NAME%" ^
  --input "%INPUT_DIR%" ^
  --main-jar "%JAR_NAME%" ^
  --main-class "%MAIN_CLASS%" ^
  --dest "%DIST_DIR%\windows"

if errorlevel 1 exit /b 1

echo Done. Run %DIST_DIR%\windows\%APP_NAME%\%APP_NAME%.exe
endlocal
