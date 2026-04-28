package animal.zoo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Text2 {
    public static void main(String[] args) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/animal/zoo/hello.txt"));
            bw.write("Java IO流");
            bw.newLine();
            bw.write("學習中！");
            bw.close();
            System.out.println("寫入完成");
        
        BufferedReader br = new BufferedReader(new FileReader("src/animal/zoo/hello.txt"));
        String line;
        while((line = br.readLine()) != null){
            System.out.println(line);
        }
        br.close();
    }
}