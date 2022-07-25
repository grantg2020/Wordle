import java.awt.event.*;

public class WordleKeyListener implements KeyListener {
    private String guess = "";

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isLetter(c)) {
            if (guess.length() < Wordle.WORD_LENGTH)
                guess += e.getKeyChar();

        } else if (e.getKeyCode() == 10) {
            // Guess complete
            guess = "";
        } else if (e.getKeyCode() == 8) {
            // Backspace
            if (guess.length() > 0)
                guess = guess.substring(0, guess.length() - 1);
        }
        System.out.println(guess);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    public String getGuess() {
        return guess;
    }
}
