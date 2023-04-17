/**
 * This class contains utility methods.
 *
 * They can use the settings of the application.
 */
public class Utils {

  public static void printDebug(String s) {
    if (SETTINGS.DEBUG) {
      System.out.println("ImageFilteringEngine_DEBUG |> " + s);
    }
  }

}
