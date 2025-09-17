package animal.zoo;

import java.io.FileWriter;
import java.io.IOException;

public class Text{
    /* flush方法和close方法
            成員方法                  說明
            public void flush()     將緩衝的數據, 刷新到本地文件中
                                    flush：刷新之後, 還可以繼續往文件中寫出數據
            public void close()     釋放資源/關流
                                    close：斷開通道, 無法再往文件中寫出數據 */
    public static void main(String[] args) throws IOException{
        try (FileWriter fw = new FileWriter("src/animal/zoo/1.txt")) {
            fw.write("我");
            fw.write("好");

            fw.flush(); //刷新之後, 還可以繼續往文件中寫出數據

            fw.write("帥");
            //fw.close(); // 如果先關閉通道, 下面的輸入則會報錯
            fw.write("啊");

            fw.close(); //斷開通道, 無法再往文件中寫出數據
        }
    }
}