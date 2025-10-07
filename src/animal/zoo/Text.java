package animal.zoo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Text {
    /*
     * 練習題：利用字節緩衝流拷貝文件
     */
    public static void main(String[] args) throws IOException {
        // 1.創建緩衝流的對象
        try (
            BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream("src/animal/zoo/1.txt"));
            BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream("src/animal/zoo/3.txt"));
        ) {
            // 2.讀寫數據
            int b;
            while ((b = bis.read()) != -1) {
                bos.write(b);
            }
            System.out.println("拷貝完成 ✅");
        }
    }
}