public class Utils {

  public static void printDebug(String s) {
    if (SETTINGS.DEBUG) {
      System.out.println("ImageFilteringEngine_DEBUG |> " + s);
    }
  }

}
