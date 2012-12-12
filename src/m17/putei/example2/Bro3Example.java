package m17.putei.example2;
import org.openqa.selenium.WebDriver;

/**
 * Seleniumを使ったログインテスト。
 */
public class Bro3Example {
  public static void main(String[] args) throws Exception {
    StopWatch sw = new StopWatch();
    WebDriver d = CommonFlow.getBro3WebDriver(CommonSettings.USE_FIREFOX);
    BPFinder.getBP(d);
    System.out.println("----------- 全処理終了 -----------");
    sw.stop("合計計処理時間");
  }
}
