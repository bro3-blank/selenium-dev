package m17.putei.example2;
import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.ScriptException;

/**
 * Seleniumを使ったログインテスト
 */
public class Bro3CreateAccount {

  //m17鯖以外にログインする場合はここを編集。
  private final static int SERVER = 17;

  //true:Firefoxで開く、false:何も開かないで処理
  //ぶらんくの環境ではなぜか前者30秒、後者60秒ぐらい。
  private final static boolean USE_FIREFOX = true; 
  
  private WebDriver d;
  
  /**
   * WebDriver dを初期化するコンストラクタ
   */
  public Bro3CreateAccount() {
    this.d = WebDriverFactory.createDriver( USE_FIREFOX );
  }
  
  private void doAction() throws Exception {
    long t0 = System.currentTimeMillis();
    //さらに中のiframe内にフォーカスを移す
    d.switchTo().frame("mainframe");
    //本拠地画面へ
    d.navigate().to("http://m17.3gokushi.jp/village.php");

    long t1 = System.currentTimeMillis();
    System.out.println("本拠地画面を開きました ["+(double)(t1-t0)/(double)1000+"秒]");
    
    String bp = d.findElement(By.id("bptpcp_area")).findElements(By.tagName("span")).get(0).getText();
    
    //現在のBPはおいくら？
    long t2 = System.currentTimeMillis();
    System.out.println("現在のBP: "+bp+"["+(double)(t2-t1)/(double)1000+"秒]");

    //もしdがFirefoxDriverだったらスクリーンショットを撮って保存。
    if (FirefoxDriver.class.isAssignableFrom(d.getClass())) {
      File dir = new File("target");
      if (!dir.exists()) dir.mkdirs(); 
      File scrFile = ((TakesScreenshot)d).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(scrFile, new File(dir, "screenshot.png"));
    }

//  System.out.println("ページソース: "+d.getPageSource());
  
    //FFを閉じる
//    d.quit();
  }
  
  public static void main(String[] args) throws Exception {
    long t0 = System.currentTimeMillis();
    Bro3CreateAccount bro3 = new Bro3CreateAccount();
    MixiAccount mixiAccount = new MixiAccount(); 
    LogInAgent.logInMixi(bro3.d, mixiAccount);
    LogInAgent.selectBro3Server(bro3.d, SERVER);
    bro3.doAction();
    long t1 = System.currentTimeMillis();
    System.out.println("合計"+(double)(t1-t0)/(double)1000+" 秒で全処理を終えました。");
  }
}
