import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Wordle {
    public static final String ANSI_RESET = "\u001B[0m";
    // public static final String ANSI_BLACK = "\u001B[30m";
    // public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    // public static final String ANSI_BLUE = "\u001B[34m";
    // public static final String ANSI_PURPLE = "\u001B[35m";
    // public static final String ANSI_CYAN = "\u001B[36m";
    // public static final String ANSI_WHITE = "\u001B[37m";
    public static final int WORD_LENGTH = 5;
    public static final int MAX_GUESSES = WORD_LENGTH + 1;

    public static void main(String[] args) {
        WordleHelperIO wordleIO = new WordleHelperIO("words.txt", WORD_LENGTH);
        ArrayList<String> words = wordleIO.getWords();
        Random r = new Random();

        String word = words.get(r.nextInt(words.size()));

        Scanner input = new Scanner(System.in);
        boolean guessed = false;
        int count = 1;

        while (!guessed && count <= MAX_GUESSES) {
            String guess = "";

            while (guess.length() != WORD_LENGTH) {
                System.out.print("Guess " + count + "/" + MAX_GUESSES + ": ");
                guess = input.next();

                if (!words.contains(guess)) {
                    guess = "";
                }
            }
            count++;

            // Add letters to arraylist
            ArrayList<Character> letters = new ArrayList<Character>();
            for (int i = 0; i < word.length(); i++) {
                letters.add(word.charAt(i));
            }

            // Remove green letters from array
            for (int i = 0; i < word.length(); i++) {
                if (guess.charAt(i) == word.charAt(i)) {
                    letters.remove((Character) guess.charAt(i));
                }

            }

            // Print out guess
            for (int i = 0; i < word.length(); i++) {
                if (guess.charAt(i) == word.charAt(i)) {
                    System.out.print(ANSI_GREEN + guess.charAt(i) + ANSI_RESET);
                } else if (letters.remove((Character) guess.charAt(i))) {
                    System.out.print(ANSI_YELLOW + guess.charAt(i) + ANSI_RESET);
                } else {
                    System.out.print(guess.charAt(i));
                }
            }
            System.out.println();

            guessed = guess.equals(word);
        }
        if (!guessed) {
            System.out.println("The word was " + word);
        } else {
            System.out.println("You got the word in " + count + " guesses");
        }

        input.close();
    }

}
