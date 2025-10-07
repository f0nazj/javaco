package animal.zoo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
public class Text {
    /*
     * 字節緩衝流 成員方法 說明 public BufferedInputStream(InputStream is). 把基本流包裝成高級流,
     * 提高讀取數據的性能 public BufferedOutputStream(OutputStream os). 把基本流包裝成高級流, 提高寫出數據的性能
     * 原理：底層自帶了長度為8192的緩衝區提高效能
     *
     * 練習題：拷貝文件 利用字節緩衝流拷貝文件
     */
    public static void main(String[] args) throws IOException {
        // 1.創建緩衝流的對象
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("src/animal/zoo/1.txt"));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("src/animal/zoo/3.txt"));
        //2.讀寫數據
        int b;
        while ((b = bis.read()) != -1){
                bos.write(b);
        }
        //3.關閉資源
        bis.close();
        bos.close();
    }
}