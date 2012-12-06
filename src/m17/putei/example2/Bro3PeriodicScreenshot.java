package m17.putei.example2;
import java.util.Timer;

/**
 * 定期的にFFでスクリーンショットを撮る
 */
public class Bro3PeriodicScreenshot {
  public static void main(String[] args) {
    Bro3Screenshot.initialize(args);
    try {
      int min = 1;
      long millisec = min*60*1000;//repeat period
      new Timer().schedule(new Bro3Screenshot(), 0, millisec);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
