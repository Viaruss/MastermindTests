import Game.Board;
import Game.RandomCodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.min;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/*
TODO: CHECK utworzenie szkieletu connectora do bazy danych
TODO: CHECK testowanie zapisu na koncu
TODO: CHECK sprawdzenie czy przechodza odpowiednie dane
TODO: CHECK sprawdzenie czy zwracane sa 3 najlepsze wyniki
 */
@RunWith(MockitoJUnitRunner.class)
public class MockitoDBTests {
    @Mock
    DBConnector mockDBConnector;
    @Mock
    Scanner mockScanner;
    @Mock
    RandomCodeGenerator mockCodeGenerator;
    @InjectMocks
    Application SUT;

    @Before
    public void setUp() {
        openMocks(this);
        SUT.board = new Board(mockCodeGenerator);
    }

    @Test
    public void testGameSave() {
        when(mockCodeGenerator.generate(4)).thenReturn("ABCD");
        when(mockScanner.nextLine()).thenReturn("ABCD", "Darek");

        SUT.run();

        verify(mockDBConnector, times(1)).saveGame(any());
    }

    @Test
    public void testSavedGameData() {
        ArrayList<Board> savedGames = new ArrayList<>();
        when(mockCodeGenerator.generate(4)).thenReturn("ABCD");
        doAnswer(invocation -> {
            Board board = invocation.getArgument(0);
            savedGames.add(board);
            return null;
        }).when(mockDBConnector).saveGame(any(Board.class));

        when(mockScanner.nextLine()).thenReturn("BFFD", "EEEE", "DDDD", "ABCD", "Tomek");
        SUT.run();

        verify(mockDBConnector, times(1)).saveGame(any(Board.class));
        assertEquals(1, savedGames.size());
        assertEquals(7, savedGames.get(0).playerScore);
        assertEquals(4, savedGames.get(0).guesses.size());
        assertEquals("Tomek", savedGames.get(0).playerName);
    }

    @Test
    public void testBest3Games() {
        ArrayList<Board> savedGames = new ArrayList<>();
        when(mockCodeGenerator.generate(4)).thenReturn("ABCD");
        doAnswer(invocation -> {
            Board board = invocation.getArgument(0);
            savedGames.add(board);
            return null;
        }).when(mockDBConnector).saveGame(any(Board.class));
        when(mockDBConnector.getBestGames()).thenAnswer(invocation -> {
            savedGames.sort(Comparator.comparingInt(Board::getPlayerScore).reversed());
            return savedGames.subList(0, min(savedGames.size(), 3));
        });

        when(mockScanner.nextLine()).thenReturn("ABCD", "Darek");
        SUT.run();

        SUT.board = new Board(mockCodeGenerator);
        when(mockScanner.nextLine()).thenReturn("FFFF", "EEEE", "DDDD", "ABCD", "Tomek");
        SUT.run();

        SUT.board = new Board(mockCodeGenerator);
        when(mockScanner.nextLine()).thenReturn("FFFF", "ABCD", "Borys");
        SUT.run();

        SUT.board = new Board(mockCodeGenerator);
        when(mockScanner.nextLine()).thenReturn("BCDE", "FFFF", "ABCD", "Karol");
        SUT.run();

        SUT.board = new Board(mockCodeGenerator);
        when(mockScanner.nextLine()).thenReturn("FFFF", "EEEE", "DDDD", "FFFF", "CCCC", "ABCD", "Wojtek");
        SUT.run();



        verify(mockDBConnector, times(5)).saveGame(any(Board.class));
        List<Board> bestGames = mockDBConnector.getBestGames();
        verify(mockDBConnector, times(1)).getBestGames();

        assertEquals(3, bestGames.size());
        assertFalse(bestGames.contains(SUT.board));
        assertTrue(bestGames.get(0).playerScore == 10 && bestGames.get(0).playerName.equals("Darek"));
        assertTrue(bestGames.get(1).playerScore == 9 && bestGames.get(1).playerName.equals("Borys"));
        assertTrue(bestGames.get(2).playerScore == 8 && bestGames.get(2).playerName.equals("Karol"));
    }
}
