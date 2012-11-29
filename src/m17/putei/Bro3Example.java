package m17.putei;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Seleniumを使ったログインテスト
 */
public class Bro3Example {
  //メールアドレスとパスワードはDオプションで指定。設定例：
  //http://gyazo.com/e3ee52fb8bdf2206478da397fccbe174
  
  public static void main(String[] args) {
//    WebDriver d = new FirefoxDriver();
    WebDriver d = new HtmlUnitDriver(BrowserVersion.FIREFOX_3_6); 
    d.navigate().to("http://mixi.jp/run_appli.pl?id=6598");
    System.out.println("[1] 現在のページ名: " + d.getTitle());
    // ログインしてない状態なので、mixiトップが表示される。
    //メール欄にメールアドレス入力。
    WebElement input1 = d.findElement(By.name("email"));
    input1.sendKeys( System.getProperty("mixi.email") );
    //パスワード欄にメールアドレス入力。
    WebElement input2 = d.findElement(By.name("password"));
    input2.sendKeys( System.getProperty("mixi.password") );
    //seleniumが送信すべきフォームを探してくれるので、email.submit()でも結果は同じ。
    input2.submit();
    
    System.out.println("[2] 現在のページ名: " + d.getTitle());
    
    //iframe内にフォーカスを移す
    d.switchTo().frame("app_content_6598");

    System.out.println(d.getPageSource());
    
    System.out.println("----");
    
    d.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    //鯖選択ボタンたちが現れるまで待つ。タイムアウトは30秒。
//    (new WebDriverWait(d, 30)).until(new ExpectedCondition<Boolean>() {
//      public Boolean apply(WebDriver d) {
//          return d.findElement(By.xpath("//*[@id=\"serverList\"]/a[12]")).isDisplayed();
//      }
//    });

    System.out.println(d.getPageSource());
    
    //鯖選択画面に行くので、17鯖ボタンをクリック！
    d.findElement(By.xpath("//*[@id=\"serverList\"]/a[12]")).click();
    
    //さらに中のiframe内にフォーカスを移す
    d.switchTo().frame("mainframe");

    //本拠地画面へ
    d.navigate().to("http://m17.3gokushi.jp/village.php");

    System.out.println("[3] 現在のページ名: " + d.getTitle());
    
    String bp = d.findElement(By.id("bptpcp_area")).findElements(By.tagName("span")).get(0).getText();
    
    //現在のBPはおいくら？
    System.out.println("現在のBP: "+bp);
    
//    System.out.println("ページソース: "+d.getPageSource());
    
    //FFを閉じる
//    d.quit();
  }
}