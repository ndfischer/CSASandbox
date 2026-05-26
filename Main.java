// --- FILE: Main.java ---
/**
 * Entry point for the Gold Rush California text adventure game.
 *
 * This class contains the main method which instantiates the Game class
 * and begins the core game loop. It serves as the primary launcher for the program.
 */
public class Main {
  /**
   * The main method that starts the application.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }
}
