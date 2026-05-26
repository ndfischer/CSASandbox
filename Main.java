/**
 * Entry point for the Gold Rush California game.
 *
 * This class instantiates the main Game object and begins the game loop.
 * It connects the application launch to the interactive game session.
 */
public class Main {
  /**
   * Main method to execute the program.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }
}
