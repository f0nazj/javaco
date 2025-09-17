package animal.zoo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Text2 {
    public static void main(String[] args) throws IOException {
        File src = new File("/Users/f0nazj/Text");     // 資料來源
        File dest = new File("/Users/f0nazj/TextTwo"); // 複製到這裡

        copydir(src, dest);
    }

    /*
     * 作用：拷貝文件夾
     * 參數一：數據源
     * 參數二：目的地
     */
    private static void copydir(File src, File dest) throws IOException {
        // 1. 建立目的地資料夾（如果不存在）
        dest.mkdirs();

        // 2. 獲取來源目錄中的所有檔案/子資料夾
        File[] files = src.listFiles();
        if (files == null) return; // 避免空指標

        // 3. 遍歷每一個項目
        for (File file : files) {
            if (file.isFile()) {
                // 檔案：複製
                try (FileInputStream fis = new FileInputStream(file);
                    FileOutputStream fos = new FileOutputStream(new File(dest, file.getName()))) {
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = fis.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                    }
                }
            } else{
                // 資料夾：遞歸
                copydir(file, new File(dest, file.getName()));
            }
        }
    }
}