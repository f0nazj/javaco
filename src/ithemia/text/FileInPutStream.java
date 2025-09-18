package ithemia.text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileInPutStream {
    /*
    * FileInPutStream一次讀多個字節
    * 方法名稱。                        說明
    * public int read()               一次讀一個字節
    * public int read(byte[] buffer)  一次讀一個字節數組數據
    */

    // 演示： 字節流輸入流FileInputStream
    // 實現需求：讀取文件中的數據
    // 實現步驟：
            // 1.創建對象
                // 細節1：如果文件不存在, 就直接報錯
            // 2.讀取數據
                // 細節1：一次讀取一個字節
                // 細節2：讀到末尾了, read方法返回-1
            // 3.釋放資源
                // 細節1：每次使用完流之後都要釋放資源

        public FileInPutStream(String string) {
        //TODO Auto-generated constructor stub
    }

        public static void main(String[] args) throws FileNotFoundException, IOException {
        // public int read()               一次讀一個字節
        // 1.創建對象
        FileInputStream fis = new FileInputStream("src/animal/zoo/1.txt");
        // 2.讀取數據
        int data;
        while((data = fis.read()) != -1){
            System.out.print((char)data);
        }
        // 3.釋放資源
        fis.close();

        System.out.println("\n------------------------------");
        // public int read(byte[] buffer)  一次讀一個字節數組數據
        // 1.創建對象
        // fis2 的數據檔案為mp4格式
        FileInputStream fis2 = new FileInputStream("/Users/f0nazj/Downloads/Sequence 01_1.mp4");
        // fos 為要拷貝的檔案位置
        FileOutputStream fos = new FileOutputStream("src/animal/zoo/1.mp4");
        // 2.讀取數據
        byte[] buffer = new byte[1024 * 1024 * 5];
        // 一次讀取多個字節數據, 具體讀多少, 跟數組的長度有關
        // 返回值：本次讀取到了多少個字節數據
        int len;
        // 讀取數據的時候, 會返回-1, 代表讀取完畢
        while((len = fis2.read(buffer)) != -1){
            // 寫入數據
            // fos.write(buffer, 0, len);  // 寫入的數據長度是len
            // 0代表從數組的第0個位置開始寫入
            fos.write(buffer, 0, len);
        }
        System.out.println("拷貝完畢");

        // 3.釋放資源
        fos.close();
        fis2.close();
    }

        public int read() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'read'");
        }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
