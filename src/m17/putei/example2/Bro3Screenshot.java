package m17.putei.example2;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openqa.selenium.WebDriver;

/**
 * FFでスクリーンショットを撮る
 */
public class Bro3Screenshot {
  public static void main(String[] args) throws Exception {
    StopWatch sw = new StopWatch();
    WebDriver d = CommonFlow.getBro3WebDriver(true);
    StopWatch sw1 = new StopWatch();
//    d.switchTo().frame("mainframe"); //さらに中のiframe内にフォーカスを移す
    d.navigate().to("http://m"+CommonSettings.SERVER+".3gokushi.jp/village.php"); //本拠地画面へ
    sw1.stop("本拠地画面を開きました");
    Screenshotter.takeScreenshot(d, 0, 0, 4);
    System.out.println("----------- 全処理終了 -----------");
    sw.stop("累計処理時間");
  }
}
