import Game.RandomCodeGenerator;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitTests {
        @Test
        public void testCodeGeneratorOutputLength(){
                RandomCodeGenerator SUT = new RandomCodeGenerator();
                assertEquals(SUT.generate(4).length(), 4);
                assertEquals(SUT.generate(10).length(), 10);
                assertEquals(SUT.generate(0).length(), 0);
        }
}