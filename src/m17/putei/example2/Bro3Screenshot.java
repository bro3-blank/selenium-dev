package m17.putei.example2;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;

/**
 * FFでスクリーンショットを撮る
 */
public class Bro3Screenshot extends TimerTask {
  
  private static String[] args;
  
  public static void initialize( String[] args ) {
    Bro3Screenshot.args = args;
  }
  
  @Override
  public void run() {
    if (args.length<=2) {
      System.err.println("Usage: bro3-ss.jar [mixi.email] [mixi.password] ([server]) [zahyo1] [zahyo2] ...");
      System.err.println("Example1: bro3-ss.jar example@gmail.com 4429 (0,0)");
      System.err.println("Example1: bro3-ss.jar example@gmail.com 4429 17 (0,0)");
      System.err.println("Example2: bro3-ss.jar example@gmail.com 4429 (0,0) (77,-175) (88,-88)");
      System.exit(-1);
    }
    System.out.println("\n\n---- "+Calendar.getInstance().getTime()+" ----");
    
    StopWatch swAll = new StopWatch();
    boolean serverIdSpecified = false;
    try {
      CommonSettings.SERVER = Integer.parseInt(args[2]);
      serverIdSpecified = true;
    } catch (NumberFormatException e) {
      // もしサーバIDが与えられなかったら、デフォルト(１７鯖)を使う。
    }
    WebDriver d = CommonFlow.getBro3WebDriver(true, args[0], args[1]);
//    System.out.println(d.getPageSource());
//    StopWatch sw1 = new StopWatch();
//    d.switchTo().frame("mainframe"); //さらに中のiframe内にフォーカスを移す
//    d.navigate().to("http://m"+CommonSettings.SERVER+".3gokushi.jp/village.php"); //本拠地画面へ
//    d.get("http://m1.3gokushi.jp/map.php?x=0&y=0&type=4");
//    sw1.stop("本拠地画面を開きました");

//    try {
//      System.out.println("started");
//      Thread.sleep(10*1000);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
    
    Pattern pZahyo = Pattern.compile("\\(([0-9-]+?),([0-9-]+?)\\)");
    for ( int i=(serverIdSpecified ? 3 : 2); i<args.length; i++ ) {
      Matcher m = pZahyo.matcher(args[i]);
      if (m.find()) {
        try {
          Screenshotter.takeScreenshot(d, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), 4);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    try {
      d.close();
    } catch (Exception e) {
      //org.openqa.selenium.WebDriverException: Component returned failure code: 0x80004005 (NS_ERROR_FAILURE) [xpcIJSWeakReference.get]
      e.printStackTrace();
    }
    System.out.println("----------- 全処理終了 -----------");
    swAll.stop("累計処理時間");
  }
  
  public static void main(String[] args) throws Exception {
    Bro3Screenshot.initialize(args);
    new Bro3Screenshot().run();
  }

}
