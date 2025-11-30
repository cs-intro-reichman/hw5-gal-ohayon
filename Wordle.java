import java.util.Random;

public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        In in = new In(filename);
        return in.readAllStrings();
    }

    // Choose a random secret word from the dictionary. 
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(dict.length);
        return dict[randomIndex];
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
        for (int i = 0; i < secret.length(); i++) {
            char check = secret.charAt(i);
            if (check == c) {
                return true;
            }
        }
        return false;
    }

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        for (int i = 0; i < resultRow.length; i++) {
            char g = guess.charAt(i);

            if (g == secret.charAt(i)) {
                resultRow[i] = 'G';
            } else if (containsChar(secret, g)) {
                resultRow[i] = 'Y';
            } else {
                resultRow[i] = '_';
            }
        }
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    public static void storeGuess(String guess, char[][] guesses, int row) {
        for (int i = 0; i < guess.length(); i++) {
            guesses[row][i] = guess.charAt(i);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print(" Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        for (int i = 0; i < resultRow.length; i++) {
            if (resultRow[i] != 'G') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Print secret word (כמו בדוגמאות: רק המילה)
        System.out.println(secret);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[MAX_ATTEMPTS][WORD_LENGTH];
        char[][] results = new char[MAX_ATTEMPTS][WORD_LENGTH];

        // Prepare to read from the standard input 
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readString();
                guess = guess.toUpperCase(); // להתאים למילון ולפידבק

                // שים לב: פה בודקים *רק* שהאורך 5, לא אם במילון
                if (guess.length() != WORD_LENGTH) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            // Store guess and compute feedback
            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, results[attempt]);

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            System.out.println("Sorry, you did not guess the word.");
            System.out.println("The secret word was:");
            System.out.println(secret);
        }

        inp.close();
    }
}
