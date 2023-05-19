package seok.springBank.utility;

import java.util.Random;
public class AccountNumberGenerator {
    public static String generateRandomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(13);
        for (int i = 0; i < 13; i++) {
            int digit = random.nextInt(10);  // 0부터 9까지의 랜덤 수 생성
            sb.append(digit);
        }
        return sb.toString();
    }

}
