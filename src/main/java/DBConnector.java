import Game.Board;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DBConnector {
    void saveGame(Board board);
    List<Board> getBestGames();
}
