import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import Game.Board;
import Game.RandomCodeGenerator;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

public class MockitoGameplayTests {

    @Test
    public void testBoardEvaluationAllGood(){
        RandomCodeGenerator codeGenerator = mock(RandomCodeGenerator.class);
        when(codeGenerator.generate(4)).thenReturn("AAAA");

        Board testBoard = new Board();
        testBoard.codeGenerator = codeGenerator;
        testBoard.generateNewCode(4);

        assertEquals(testBoard.evaluateGuess("AAAA"), "****");
    }
    @Test
    public void testBoardEvaluationAllWrong(){
        RandomCodeGenerator codeGenerator = mock(RandomCodeGenerator.class);
        when(codeGenerator.generate(4)).thenReturn("AAAA");

        Board testBoard = new Board();
        testBoard.codeGenerator = codeGenerator;
        testBoard.generateNewCode(4);

        assertEquals(testBoard.evaluateGuess("BCDE"), "____");
    }
    @Test
    public void testBoardEvaluationAllAtWrongPlace(){
        RandomCodeGenerator codeGenerator = mock(RandomCodeGenerator.class);
        when(codeGenerator.generate(4)).thenReturn("ABCD");

        Board testBoard = new Board();
        testBoard.codeGenerator = codeGenerator;
        testBoard.generateNewCode(4);

        assertEquals(testBoard.evaluateGuess("DCBA"), "????");
    }

    @Test
    public void testGameplayWinFirstTime(){
        RandomCodeGenerator mockCodeGenerator = mock(RandomCodeGenerator.class);
        when(mockCodeGenerator.generate(4)).thenReturn("ABCD");

        Board testBoard = new Board();
        testBoard.codeGenerator = mockCodeGenerator;

        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("ABCD", "");

        Application SUT = new Application(mockScanner, testBoard);

        PrintStream mockPrintStream = mock(PrintStream.class);
        System.setOut (mockPrintStream);

        SUT.run();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify (mockPrintStream, times(7)).println (captor.capture());

        assertTrue(captor.getAllValues().contains("You Won in 1 tries"));
    }

    @Test
    public void testGameplayLoose(){
        RandomCodeGenerator mockCodeGenerator = mock(RandomCodeGenerator.class);
        when(mockCodeGenerator.generate(4)).thenReturn("ABCD");

        Board testBoard = new Board();
        testBoard.codeGenerator = mockCodeGenerator;

        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("FFFF");

        Application SUT = new Application(mockScanner, testBoard);

        PrintStream mockPrintStream = mock(PrintStream.class);
        System.setOut (mockPrintStream);

        SUT.run();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify (mockPrintStream, times(80)).println (captor.capture());

        assertEquals(0, testBoard.playerScore);
        assertTrue(captor.getValue().contains("You Lost - Too Many Guesses"));
    }

    @Test
    public void testGameplayWinIn5Tries(){
        RandomCodeGenerator mockCodeGenerator = mock(RandomCodeGenerator.class);
        when(mockCodeGenerator.generate(4)).thenReturn("ABCD");

        Board testBoard = new Board();
        testBoard.codeGenerator = mockCodeGenerator;

        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("FFFF", "EEEE", "DDDD", "AAAA", "ABCD", "");

        Application SUT = new Application(mockScanner, testBoard);

        PrintStream mockPrintStream = mock(PrintStream.class);
        System.setOut (mockPrintStream);

        SUT.run();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify (mockPrintStream, times(25)).println (captor.capture());

        assertTrue(captor.getAllValues().contains("You Won in 5 tries"));
    }

    @Test
    public void testFlags(){
        RandomCodeGenerator mockCodeGenerator = mock(RandomCodeGenerator.class);
        when(mockCodeGenerator.generate(4)).thenReturn("ABCD");

        Board testBoard = new Board();
        testBoard.codeGenerator = mockCodeGenerator;

        Scanner mockScanner = mock(Scanner.class);
        when(mockScanner.nextLine()).thenReturn("FFFF", "AFFD", "DDDD", "DCBA", "ABCD", "");

        Application SUT = new Application(mockScanner, testBoard);

        PrintStream mockPrintStream = mock(PrintStream.class);
        System.setOut (mockPrintStream);

        SUT.run();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify (mockPrintStream, times(25)).println(captor.capture());

        assertTrue(captor.getAllValues().toString().contains("FFFF | ____"));
        assertTrue(captor.getAllValues().toString().contains("AFFD | *__*"));
        assertTrue(captor.getAllValues().toString().contains("DDDD | ???*"));
        assertTrue(captor.getAllValues().toString().contains("DCBA | ????"));
        assertTrue(captor.getAllValues().toString().contains("****"));
    }
}
