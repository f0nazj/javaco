package animal.zoo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Text3 {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        try (
            // 1. 創建與原始文件關聯的輸入流
            FileInputStream fis = new FileInputStream("src/animal/zoo/1.JPG");
            // 2. 創建與加密文件關聯的輸出流
            FileOutputStream fos = new FileOutputStream("src/animal/zoo/ency.JPG")
        ) {
            int b;
            while ((b = fis.read()) != -1) {
            // 3. 對讀取到的數據進行加密處理（簡單異或）
            b = b ^ 3;
            // 4. 將加密後的數據寫入到加密文件中
            fos.write(b);
            }
            System.out.println("加密完成");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}