package animal.zoo;

import java.io.FileReader;
import java.io.IOException;

public class Text{
        public static void main(String[] args) throws IOException {
            /* 創建字符輸出對象
            方法名稱。                                            說明
            public FileReader(File file)                        創建字符輸出流關聯本地文件
            public FileReader(String pathname).                 創建字符輸出流關聯本地文件
        讀取數據
            方法名稱。                                               說明
            public int read()                                      讀取數據, 讀到末尾返回-1
            public int read(char[] buffer)                         讀取多個數據, 讀到末尾返回-1
            細節：1.按字節進行讀取, 遇到中文, 一次讀多個字節, 讀取後解碼, 返回一個整數
            讀到文件末尾了, read方法返回-1
        釋放資源
            方法名稱。                                               說明
            public int close()                                      釋放資源/關流 */
            // 1.創建對象
            FileReader fr = new FileReader("src/animal/zoo/1.txt");
            // 2.讀取數據
            char[] buffer = new char[1024];
            int len;
            // read(char): 讀取數據, 解碼, 強轉三部合併了, 強轉之後的字符放在數組當中
            // 空參的read + 強轉類型轉換
            while((len = fr.read(buffer)) != -1){
                // 把數組中的數據變成字符串在進行打印
                System.out.print(new String(buffer, 0, len));
            }
            // 3.釋放資源
            fr.close();
        }
    }