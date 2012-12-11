package m17.putei.example2;
import java.util.Timer;

/**
 * 定期的にFFでスクリーンショットを撮る
 */
public class Bro3PeriodicScreenshot {
  public static void main(String[] args) {
    Bro3Screenshot.initialize(args);
    try {
      //何分に一度スクリーンショットを撮影するか
      int min = 3;
      long millisec = min*60*1000;//repeat period
      new Timer().schedule(new Bro3Screenshot(), 0, millisec);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
