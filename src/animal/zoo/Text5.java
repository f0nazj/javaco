package animal.zoo;

import java.io.FileInputStream;
import java.io.IOException;

public class Text5 {
    public static void main(String[] args) throws IOException{
        try (FileInputStream fis = new FileInputStream("src/animal/zoo/1.txt")) {
            int b1;
            while((b1 = fis.read()) != -1){
                System.out.print((char)b1);
            }
        }
    }
}