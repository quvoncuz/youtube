package dasturlash.uz.util;

import java.util.Random;

public class RandomUtil {
    private static final Random random = new Random();
    public static int fiveDigit(){
        return random.nextInt(10000, 99999);
    }
}
