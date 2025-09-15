package animal.zoo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Text4 {
    /*
    * FileInPutStream一次讀多個字節
    * 方法名稱。                        說明
    * public int read()               一次讀一個字節
    * public int read(byte[] buffer)  一次讀一個字節數組數據
    */
    public static void main(String[] args) throws Exception {
            // 1.創建對象
            // fis2 的數據檔案為mp4格式
            FileInputStream fis2 = new FileInputStream("/Users/f0nazj/Downloads/Sequence 01_1.mp4");
            FileOutputStream fos = new FileOutputStream("src/animal/zoo/1.mp4");
            try (fis2; fos) {
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
            } catch (FileNotFoundException e) {
                // 如果有異常就會進入這個區域
                e.printStackTrace();
            } catch (IOException e) {
                // 只要執行完try區域, 就會執行這個區域
                // 簡單來說就是一定會執行這個區域
                e.printStackTrace();
            }
        }
    }