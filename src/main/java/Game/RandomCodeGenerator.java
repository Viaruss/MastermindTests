package Game;

import java.util.Random;

public class RandomCodeGenerator {
    int leftLimit = 65; // letter 'A'
    int rightLimit = 70; // letter 'F'

    public String generate(int targetStringLength){
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
