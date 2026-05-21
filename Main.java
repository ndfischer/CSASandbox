// --- FILE: Main.java ---
/**
 * Main entry point for the Gold Rush California game.
 *
 * <p>This class instantiates the Game controller and runs its core loop to begin
 * the text adventure simulation set in 1850s California.
 */
public class Main {
  /**
   * Main method to execute the application.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }
}
