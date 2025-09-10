package animal.zoo;

import java.io.FileOutputStream;
import java.io.IOException;

public class Text {
    public static void main(String[] args) throws IOException {
        // 換行
        // 1.創建對象
        FileOutputStream fos = new FileOutputStream("src/animal/zoo/1.txt");
        // 2.寫入數據
        fos.write("wtf".getBytes());
        //換行
        fos.write("\r".getBytes());
        // 再寫一次數據
        fos.write("666".getBytes());
        // 3.釋放資源
        fos.close();

        // 續寫
        // 1.創建對象
        FileOutputStream fos2 = new FileOutputStream("src/animal/zoo/1.txt", true);
        // 2.寫入數據
        fos2.write("789".getBytes());
        // 3.釋放資源
        fos.close();
    }
}