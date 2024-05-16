package Game;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public String codeToGuess;
    public int maxGuesses = 10;
    public List<String> guesses = new ArrayList<>();
    List<String> evaluations = new ArrayList<>();
    public final List<String> validChars = List.of("A", "B", "C", "D", "E", "F");
    public RandomCodeGenerator codeGenerator = new RandomCodeGenerator();
    public int playerScore = 0;
    public String playerName;

    public Board() {
    }

    public Board(RandomCodeGenerator codeGenerator) {
        this.codeGenerator = codeGenerator;
    }

    public Board(String codeToGuess, List<String> guesses, List<String> evaluations, int playerScore, String playerName) {
        this.codeToGuess = codeToGuess;
        this.guesses = guesses;
        this.evaluations = evaluations;
        this.playerScore = playerScore;
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void generateNewCode(int length){
        codeToGuess = codeGenerator.generate(length);
    }

    public String evaluateGuess(String codeToCheck){
        guesses.add(codeToCheck);
        String[] code = codeToGuess.split("");
        String[] guess = codeToCheck.split("");
        StringBuilder evaluation = new StringBuilder();
        for(int i = 0; i < guess.length; i++){
            if(code[i].equals(guess[i])) evaluation.append("*");
            else if(
                    StringUtils.countMatches(codeToGuess, guess[i]) == StringUtils.countMatches(codeToCheck, guess[i])
            ) evaluation.append("?");
            else if(
                    codeToGuess.contains(guess[i])
            ) evaluation.append("?");
            else evaluation.append("_");
        }
        evaluations.add(evaluation.toString());
        playerScore = maxGuesses + 1 - evaluations.size();
        return evaluation.toString();
    }

    public void showGuesses(){
        if (guesses != null) {
            for (int i = 0; i < guesses.size(); i++) {
                System.out.println(guesses.get(i) + " | " +evaluations.get(i)) ;
            }
        }
    }

    public String checkState(){
        String gameState;
        if (evaluations.get(evaluations.size() - 1).equals("****")) gameState = "Won";
        else if (evaluations.size() <= maxGuesses){
            gameState = "";
        } else gameState = "Lost";
        return gameState;
    }
}


