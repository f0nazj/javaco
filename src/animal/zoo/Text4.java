package animal.zoo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Text4{
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // 1.讀取數據
        StringBuffer sb = new StringBuffer();
        try(FileReader fr = new FileReader("src/animal/zoo/1.txt")){
            int ch;
            while((ch = fr.read()) != -1){
                sb.append((char)ch);
            }
            System.out.println(sb);
        }catch(Exception e){
            e.printStackTrace();
        }
        // 2.排序
        Integer[] arr = Arrays.stream(sb.toString()
            .split("-"))
            .map(Integer::parseInt)
            .sorted()
            .toArray(Integer[]::new);
        System.out.println(Arrays.toString(arr));

        // 3.寫入數據
        try(FileWriter fw = new FileWriter("src/animal/zoo/2.txt")){
            String s = Arrays.toString(arr).replace(", ", "-");
            String result = s.substring(1, s.length() - 1);
            fw.write(result);
        }
    }
}