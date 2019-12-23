import org.junit.Test;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.*;

public class DemoTest {
    @Test
    public void clustering() throws Exception {
        String otvet = new String();
        otvet = "0.5496659221256996";
        String reshenie = new String("");
        Scanner in = new Scanner(new File("1.txt"));
        while(in.hasNext())
            reshenie += in.nextLine();
        in.close();
        System.out.println(reshenie);
        System.out.println(otvet);
        assertEquals(reshenie, otvet);
    }

}