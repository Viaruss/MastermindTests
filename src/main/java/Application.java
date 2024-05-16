/*
-----------------------------------Zadanie--------------------------------

Napisz kod gry Mastermind w trybie tekstowym i przygotuj
zestaw testów jednostkowych. Aplikacja powinna zapisywać zakończoną roz-
grywkę do bazy danych i pozwalać na wyświetlenie 3 najlepszych rozgrywek
w historii. Załóż, że funkcja sprawdzania propozycji kodu z sekretnym kodem
oraz baza danych nie są dostępne (np. realizuje je inny zespół) i dlatego
należy je zastąpić dublerami. Wszystkie dublery należy zrealizować na dwa
sposoby: za pomocą pakietu Mockito oraz jako własną implementację. W te-
stach jednostkowych należy sprawdzić poprawność działania aplikacji m.in.
w przypadku braku połączenia z bazą danych oraz błędnie wprowadzonych
danych przez użytkownika.
*/

import Game.Board;
import java.util.Scanner;

public class Application {
    Scanner sc;
    Board board;
    public DBConnector dbConnector;

    public Application(Scanner sc, Board board) {
        this.sc = sc;
        this.board = board;
    }

    public void run(){
        System.out.println(
                "Welcome to the mastermind game\n" +
                "Try to guess the 4 letter code in the least amount of tries possible. If You don't guess in " + board.maxGuesses + " tries, You lose.\n\n" +
                "after each guess, you will get a evaluation of your code:\n" +
                "_ means that this letter is wrong, \n? means the letter is right but in the wrong place, \n* means the letter and its place is right\n"
        );
        board.generateNewCode(4);
        System.out.println("The code has been generated! Good luck");
        do {
            String guess;
            while (true) {
                boolean isValid = true;
                System.out.println("Please input your code: (Letters A - F)\n");
                board.showGuesses();
                guess = sc.nextLine().toUpperCase();
                if (guess.length() == 4) {
                    for (int i = 0; i < 4; i++) {
                        if (!board.validChars.contains(String.format("" + guess.charAt(i)))) {
                            isValid = false;
                            break;
                        }
                    }
                    if (isValid) break;
                }
            }
            //pinezki
            String result = board.evaluateGuess(guess);
            System.out.println(result);
        } while (board.checkState().isEmpty());
        if (board.checkState().equals("Won")){
            System.out.println("You Won in " + board.guesses.size() + " tries");
            try {
                System.out.println("Insert Your name to save this game in ranking, or press ENTER to continue");
                String name = sc.nextLine();
                if(!name.isEmpty()){
                    board.playerName = name;
                    dbConnector.saveGame(board);
                    System.out.println("Game saved, SCORE: " + board.playerScore + " - " + name);
                } else System.out.println("Game was not saved");
            } catch (Exception ignore){}
        } else {
            System.out.println("You Lost - Too Many Guesses\nThe code was: " + board.codeToGuess);
        }
    }

    public static void main(String[] args) {
        Application app = new Application(new Scanner(System.in), new Board());
        app.run();
    }

}

/*
-----------------------code validator for 2 player gameplay-------------------------
while (true){
                boolean isValid = true;
                System.out.println("Please input your code: (Letters A - F)\n");
                String code = sc.nextLine();
                if(code.length() == 4) {
                    for (int i = 0; i < 4; i++){
                        if(!board.validChars.contains(String.format("" + code.charAt(i)))) isValid = false;
                    }
                    if (isValid) break;
                }
            }
 */
