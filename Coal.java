// --- FILE: Coal.java ---
/**
 * Represents raw fossil fuel coal energy resources.
 *
 * <p>Enforces concrete material boundaries separating standard geological items
 * within structural lists.
 */
public class Coal extends Ore {
  /**
   * Instantiates an active Coal resource tracking profile.
   */
  public Coal() {
    super("Coal", "Dark, high-grade chunks of mineralized carbon fuel.", 0.05);
  }
}
