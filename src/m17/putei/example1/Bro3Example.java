package m17.putei.example1;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;

/**
 * Seleniumを使ったログインテスト
 */
public class Bro3Example {

  //m17鯖以外にログインする場合はここを編集。
  private final static int SERVER = 17;

  //true:Firefoxで開く、false:何も開かないで処理
  //ぶらんくの環境ではなぜか前者30秒、後者60秒ぐらい。
  private final static boolean USE_FIREFOX = false; 
  
  private WebDriver d;
  
  /**
   * WebDriver dを初期化するコンストラクタ
   */
  public Bro3Example() {
    //重要でないエラーメッセージがたくさん表示されてしまうので、阻止。
    Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
    Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    
    if ( USE_FIREFOX ) {
      this.d = new FirefoxDriver();
    } else { 
      // FFの窓を開かず実行
      //mixiがボットをはじくようになっているのでUserAgentを偽装
      BrowserVersion bv = new BrowserVersion(
              "Netscape", 
              "5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5", 
              1.2f );
      HtmlUnitDriver d = new HtmlUnitDriver(bv);
      d.setJavascriptEnabled(true);
      // クラス変数のdにローカル変数のdを代入。
      this.d = d;
    }
  }
  
  /**
   * Mixiにログインする
   */
  private void logInMixi( String mixiEmail, String mixiPassword ) {
    try {
      d.navigate().to("http://mixi.jp/run_appli.pl?id=6598");
    } catch (WebDriverException e) {
      //Javascript実行エラーが出るときがあるが、無視
    }
    System.out.println("[1] 現在のページ名: " + d.getTitle());
    // ログインしてない状態なので、mixiトップが表示される。
    //メール欄にメールアドレス入力。
    WebElement input1 = d.findElement(By.name("email"));
    input1.sendKeys( mixiEmail );
    //パスワード欄にメールアドレス入力。
    WebElement input2 = d.findElement(By.name("password"));
    input2.sendKeys( mixiPassword );
    try {
      //seleniumが送信すべきフォームを探してくれるので、input1.submit()でも結果は同じ。
      input2.submit();
    } catch (ScriptException e) {
      //Javascript実行エラーが出るときがあるが、無視
    }
    System.out.println("[2] 現在のページ名: " + d.getTitle());
  }
  
  /**
   * ブラ三にログインする。
   * @param serverNumber サーバID。17鯖の場合は17。
   */
  private void logInBro3( int serverNumber ) {
    //iframe内にフォーカスを移す
    d.switchTo().frame("app_content_6598");

    //鯖選択ボタンたちが現れるまで待つ。タイムアウトは30秒。
    (new WebDriverWait(d, 30)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        //鯖選択ボタンの下に出てくる【推奨ブラウザ】 のエレメントが表示されるまで待つ
        return d.findElement(By.className("serverBrowser")).isDisplayed();
      }
    });

    /**
     * 上のが動かなかったら以下を試してください。
     */
    //d.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    //Thread.sleep(30*1000);//30秒待つ
    
    //System.out.println(d.getPageSource());
    
    //鯖選択画面に行くので、指定された鯖ボタンをクリック！
    List<WebElement> worldButtons = d.findElements(By.tagName("a"));
    for ( WebElement wb : worldButtons ) {
      String title = wb.getAttribute("title");
      if ( title !=null && title.equals("m"+serverNumber+"ワールド") ) {
        wb.click();
        break;
      }
    }
  }
  
  private void doAction() throws Exception {
    //さらに中のiframe内にフォーカスを移す
    d.switchTo().frame("mainframe");
    //本拠地画面へ
    d.navigate().to("http://m17.3gokushi.jp/village.php");

    System.out.println("[3] 現在のページ名: " + d.getTitle());
    
    String bp = d.findElement(By.id("bptpcp_area")).findElements(By.tagName("span")).get(0).getText();
    
    //現在のBPはおいくら？
    System.out.println("現在のBP: "+bp);

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
    //この場合、this.mixiEmail と mixiEmailは同じクラス変数をさします
    String mixiEmail = System.getProperty("mixi.email");
    String mixiPassword = System.getProperty("mixi.password");
    
    //パスワードちゃんと入ってるかチェック
    if (mixiEmail==null || mixiEmail.length()==0 || 
            mixiPassword==null || mixiPassword.length()==0 ) {
      System.err.println("メールアドレスとパスワードをDオプションで指定してください。\n例： http://gyazo.com/e3ee52fb8bdf2206478da397fccbe174");
      System.exit(-1);
    }
    
    long t0 = System.currentTimeMillis();
    Bro3Example bro3 = new Bro3Example(); 
    bro3.logInMixi(mixiEmail, mixiPassword);
    bro3.logInBro3(SERVER);
    bro3.doAction();
    long t1 = System.currentTimeMillis();
    System.out.println((double)(t1-t0)/(double)1000+" 秒で全処理を終えました。");
  }
}
