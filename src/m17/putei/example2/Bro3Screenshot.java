package m17.putei.example2;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;

/**
 * FFでスクリーンショットを撮る
 */
public class Bro3Screenshot {
  public static void main(String[] args) throws Exception {
    if (args.length<=2) {
      System.err.println("Usage: bro3-ss.jar [mixi.email] [mixi.password] [zahyo1] [zahyo2] ...");
      System.err.println("Example1: bro3-ss.jar example@gmail.com 4429 (0,0)");
      System.err.println("Example2: bro3-ss.jar example@gmail.com 4429 (0,0) (77,-175) (88,-88)");
      System.exit(-1);
    }
    StopWatch sw = new StopWatch();
    WebDriver d = CommonFlow.getBro3WebDriver(true, args[0], args[1]);
    StopWatch sw1 = new StopWatch();
//    d.switchTo().frame("mainframe"); //さらに中のiframe内にフォーカスを移す
    d.navigate().to("http://m"+CommonSettings.SERVER+".3gokushi.jp/village.php"); //本拠地画面へ
    sw1.stop("本拠地画面を開きました");
    Pattern pZahyo = Pattern.compile("\\(([0-9-]+?),([0-9-]+?)\\)");
    for ( int i=2; i<args.length; i++ ) {
      Matcher m = pZahyo.matcher(args[i]);
      if (m.find()) {
        try {
          Screenshotter.takeScreenshot(d, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), 4);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    d.close();
    System.out.println("----------- 全処理終了 -----------");
    sw.stop("累計処理時間");
  }
}
