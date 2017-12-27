package zh.gamedata.tool;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CodeTest {
    public static void main(String[] args) {
        String date = "2017-12-27";

        System.out.println(LocalDate.parse(date));
    }
}
