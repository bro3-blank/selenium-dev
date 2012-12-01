package m17.putei.example2;
import org.openqa.selenium.WebDriver;

/**
 * Seleniumを使ったログインテスト
 */
public class Bro3Example {
  public static void main(String[] args) throws Exception {

    //m17鯖以外にログインする場合はここを編集。
    int server = 17;

    //true:Firefoxで開く、false:何も開かないで処理
    //ぶらんくの環境ではなぜか前者30秒、後者60秒ぐらい。
    boolean useFirefox = false; 
    
    StopWatch.PRINT_LOG = true;// ログが出るように設定。

    WebDriver d = WebDriverFactory.createDriver( useFirefox );
    
    StopWatch sw = new StopWatch();
    MixiAccount mixiAccount = new MixiAccount(); 
    LogInAgent.logInMixi(d, mixiAccount);
    LogInAgent.selectBro3Server(d, server);
    int bp = BPFinder.getBP(d);
    System.out.println("----------- 全処理終了 -----------");
    sw.stop("累計処理時間");
  }
}
