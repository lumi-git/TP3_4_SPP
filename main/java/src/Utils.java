/**
 * This class contains utility methods.
 *
 * They can use the settings of the application.
 */
public class Utils {

  /**
   * Print a message if the debug mode is on in the SETTINGS.
   *
   * @param s The message to print
   */
  public static void printDebug(String s) {
    if (SETTINGS.DEBUG) {
      System.out.println("ImageFilteringEngine_DEBUG |> " + s);
    }
  }

}
