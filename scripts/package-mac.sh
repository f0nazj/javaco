#!/usr/bin/env bash
set -euo pipefail

APP_NAME="PuzzleGame"
MAIN_CLASS="gui.ui.App"
JAR_NAME="puzzle-game.jar"
DIST_DIR="dist"
INPUT_DIR="$DIST_DIR/input"

mkdir -p "$INPUT_DIR"

echo "Compiling Java files..."
javac -encoding UTF-8 -cp bin -d bin src/gui/ui/App.java src/gui/ui/itheima/*.java

echo "Packaging executable jar..."
jar --create --file "$INPUT_DIR/$JAR_NAME" --main-class "$MAIN_CLASS" -C bin gui/ui

echo "Building macOS app with jpackage..."
rm -rf "$DIST_DIR/mac"
jpackage \
  --type app-image \
  --name "$APP_NAME" \
  --input "$INPUT_DIR" \
  --main-jar "$JAR_NAME" \
  --main-class "$MAIN_CLASS" \
  --dest "$DIST_DIR/mac"

echo "Done. Open $DIST_DIR/mac/$APP_NAME.app"
