import javax.swing.*;
import javax.swing.table.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class WordleUI extends JPanel {
    public static final int BOX_SCALE = 40;
    public static final int PADDING = 10;
    private static final String WIN_MESSAGE = "You win!";
    private static final String LOSE_MESSAGE = "You lose!";

    public static String[] guesses = new String[Wordle.MAX_GUESSES];
    public static int numGuesses = 0;
    public static JFrame frame;
    private static String guess = "";
    private static String guessed = "";
    private static String word = "";
    private static WordleHelperIO wordleIO;
    private static boolean hasWon = false;
    private static boolean hasLost = false;
    private static ArrayList<String> words = new ArrayList<String>();

    private static final String[][] keyboard = { { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P" },
            { "A", "D", "S", "F", "G", "H", "J", "K", "L" },
            { "Z", "X", "C", "V", "B", "N", "M" }
    };

    public WordleUI() {
        wordleIO = new WordleHelperIO("words.txt", Wordle.WORD_LENGTH);
        words = wordleIO.getWords();

        addKeyListener(new WordleKeyListener());
        setFocusable(true);
        requestFocusInWindow();

        generateWord();
    }

    public static void generateWord() {
        Random r = new Random();
        word = words.get(r.nextInt(words.size()));
    }

    public void paint(Graphics g) {
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));

        guessed = "";
        if (guesses[guesses.length - 1] != null && guesses[guesses.length - 1].length() > 0 && !hasWon) {
            hasLost = true;
        }
        // Draws guessing grid
        for (int j = 0; j < Wordle.MAX_GUESSES; j++) { // Row
            ArrayList<Character> letters = new ArrayList<Character>();
            for (int i = 0; i < word.length(); i++) {
                letters.add(word.charAt(i));
            }

            if (guesses[j] != null) {
                guessed += guesses[j];
                // Remove green letters
                for (int i = 0; i < guesses[j].length(); i++) {
                    if (guesses[j].charAt(i) == word.charAt(i)) {
                        letters.remove((Character) word.charAt(i));
                    }
                }
                if (letters.size() == 0) {
                    hasWon = true;
                }
            }
            for (int i = 0; i < Wordle.WORD_LENGTH; i++) { // Col
                g.setColor(Color.gray);
                // Color green
                if (guesses[j] != null) {
                    if (word.charAt(i) == guesses[j].charAt(i)) {
                        g.setColor(Color.green);
                    }
                    // Color yellow
                    if (letters.contains(guesses[j].charAt(i))) {
                        letters.remove((Character) guesses[j].charAt(i));
                        g.setColor(Color.yellow);
                    }
                }

                g.drawRect(PADDING + (BOX_SCALE + PADDING) * i, PADDING + (BOX_SCALE + PADDING) * j, BOX_SCALE,
                        BOX_SCALE);

            }
            if (guesses[j] != null)
                for (int k = 0; k < guesses[j].length(); k++) { // Letters in row
                    char[] chars = { guesses[j].charAt(k) };

                    g.setColor(Color.white);
                    g.drawChars(chars, 0, 1, (BOX_SCALE / 2 - 8) + PADDING + (BOX_SCALE + PADDING) * k,
                            (BOX_SCALE / 2 + 7) +
                                    PADDING + (BOX_SCALE + PADDING) * j);
                }

            // Draw current guess
            if (j == numGuesses)
                for (int k = 0; k < guess.length(); k++) { // Letters in row
                    char[] chars = { guess.charAt(k) };

                    g.setColor(Color.white);
                    g.drawChars(chars, 0, 1, (BOX_SCALE / 2 - 8) + PADDING + (BOX_SCALE + PADDING) * k,
                            (BOX_SCALE / 2 + 7) +
                                    PADDING + (BOX_SCALE + PADDING) * j);
                }
        }

        // Draw letters available
        int maxKeys = 0;
        for (int i = 0; i < keyboard.length; i++) {
            if (maxKeys < keyboard[i].length)
                maxKeys = keyboard[i].length;
        }

        for (int i = 0; i < keyboard.length; i++) {
            for (int j = 0; j < keyboard[i].length; j++) {
                String letter = keyboard[i][j].toLowerCase();

                g.setColor(Color.GRAY);
                g.drawRect(
                        300 + ((maxKeys - keyboard[i].length) * (BOX_SCALE + PADDING)) / 2 + PADDING
                                + (BOX_SCALE + PADDING) * j,
                        PADDING + (BOX_SCALE + PADDING) * i,
                        BOX_SCALE,
                        BOX_SCALE);

                if (!guessed.contains(letter.toLowerCase()))
                    g.setColor(Color.WHITE);

                g.drawChars(letter.toCharArray(), 0, 1, (BOX_SCALE / 2 - 8) +
                        300 + ((maxKeys - keyboard[i].length) * (BOX_SCALE + PADDING)) / 2 + PADDING
                        + (BOX_SCALE + PADDING) * j,
                        (BOX_SCALE / 2 + 7) +
                                PADDING + (BOX_SCALE + PADDING) * i);
            }
        }

        g.setColor(Color.WHITE);
        if (hasWon) {
            g.drawChars(WIN_MESSAGE.toCharArray(), 0, WIN_MESSAGE.length(), (BOX_SCALE / 2 - 8) +
                    400 + PADDING
                    + (BOX_SCALE + PADDING), 200);
        } else if (hasLost) {
            String msg = LOSE_MESSAGE + " The word was " + word;
            g.drawChars(msg.toCharArray(), 0, msg.length(),
                    (BOX_SCALE / 2 - 8) +
                            300 + PADDING
                            + (BOX_SCALE + PADDING),
                    200);

        }
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

        // Create and set up the window.
        WordleUI ui = new WordleUI();

        frame = new JFrame("Wordle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setBackground(Color.black);
        frame.getContentPane().add(ui);
        frame.setSize(850, 350);
        frame.setVisible(true);
        frame.setResizable(false);

        // JButton b = new JButton("Play Again");
        // b.setBounds(50, 100, 95, 30);
        // frame.add(b);

        // Display the window.
        // frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }

        });
    }

    public static void resetGame() {
        hasWon = hasLost = false;
        guessed = "";
        guesses = new String[Wordle.MAX_GUESSES];
        numGuesses = 0;
        generateWord();
    }

    public class WordleKeyListener implements KeyListener {

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if ((hasWon || hasLost) && e.getKeyCode() == 10) {
                resetGame();
            }

            if (hasWon)
                return;

            char c = e.getKeyChar();
            frame.setBackground(Color.black);
            if (Character.isLetter(c)) {
                if (guess.length() < Wordle.WORD_LENGTH)
                    guess += Character.toLowerCase(c);

            } else if (e.getKeyCode() == 10) {
                // Guess complete
                if (guess.length() == Wordle.WORD_LENGTH) {
                    if (wordleIO.isWord(guess.toLowerCase()) && numGuesses < Wordle.MAX_GUESSES) {
                        guesses[numGuesses] = guess.toLowerCase();
                        numGuesses++;
                    } else {
                        frame.setBackground(Color.red);
                    }
                    guess = "";
                }
            } else if (e.getKeyCode() == 8) {
                // Backspace
                if (guess.length() > 0)
                    guess = guess.substring(0, guess.length() - 1);
            }

            repaint();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // Leave empty
        }

        public String getGuess() {
            return guess;
        }
    }

}
