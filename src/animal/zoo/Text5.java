package animal.zoo;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Text5 {
    public static void main(String[] args) throws Exception {

        // 1.創建File對象
        File f1 = new File("src/animal/zoo/1.txt");

        // 2.刪除文檔(判斷文檔是否存在)
        if(f1.delete()){
            System.out.println("刪除成功");
        }else{
            System.out.println("刪除失敗");
        }

        // 3.創建文檔(如果有同樣的文檔則會覆蓋)
        if(f1.createNewFile()){
            System.out.println("創建文件成功");
        }else{
            System.out.println("創建文件失敗");
        }

        //  4.創建寫入對象(寫入文檔)
        FileWriter fw = new FileWriter(f1);
        fw.write("Hello World");
        fw.close();

        // 5.讀取文檔
        Scanner sc = new Scanner(f1);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            System.out.println(line);
        }
        sc.close();
    }
}