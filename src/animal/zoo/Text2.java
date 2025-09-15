package animal.zoo;

import java.io.FileWriter;
import java.io.IOException;

public class Text2 {
    /* FileWriter 構造方法
            構造方法                                                 說明
            public FileWriter(File file)                           創建字符輸出流關聯本地文件
            public FileWriter(String pathname).                    創建字符輸出流關聯本地文件
            public FileWriter(File file, boolean append)           創建字符輸出流關聯本地文件, 續寫
            public FileWriter(String pathname, boolean append)     創建字符輸出流關聯本地文件, 續寫
            成員方法                                                 說明
            publie write(int c)                                    寫出一個字符
            publie write(String str)                               寫出一個字符串
            publie write(String str, int off, int len)             寫出一個字符串的一部分
            publie write(char[] cbuf)                              寫出一個字符數組
            publie write(char[] cbuf,  int off, int len)           寫出一個字符數組的一部分
            FileWriter 書寫細節
                1.創建字符輸出流對象
                    細節一：參數是字符串表示得路徑或者File對象都是可以的
                    細節二：如果文件不存在會創建一個新的文件, 但是要保證父級路徑是存在的
                    細節三：如果文件已經存在, 則會清空文件, 如果不想清空可以打開續寫開關
            2.寫數據
                    細節：如果writer方法的參數是整數, 但是實際上寫到本地文件中的是整數在字符集上對應的數字
            3.釋放資源
                      細節：每次使用完之後都要釋放資源 */
    public static void main(String[] args) throws IOException{
        FileWriter fw = new FileWriter("src/animal/zoo/1.txt");
        //publie write(int c)   寫出一個字符
        fw.write(25105); //我
        fw.write("\r\n");
        //publie write(String str)  寫出一個字符串
        fw.write("我是你爸"); //根據字符集的編碼方法進行編碼, 把編碼之後的數據寫到文件中 // UTF-8
        fw.write("\r\n");
        //publie write(String str, int off, int len)   寫出一個字符串的一部分
        fw.write("我是你爸", 1, 3); //是你爸
        fw.write("\r\n");
        //publie write(char[] cbuf)  寫出一個字符數組
        char[] chars = {'我', '是', '你', '爸'};
        fw.write(chars); //我是你爸
        fw.write("\r\n");
        //publie write(char[] cbuf,  int off, int len)  寫出一個字符數組的一部分
        fw.write(chars, 1, 3); //是你爸

        // 釋放資源
        fw.close();
    }
}