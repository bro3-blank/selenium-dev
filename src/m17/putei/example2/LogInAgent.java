package m17.putei.example2;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.ScriptException;

public class LogInAgent {

  /**
   * Mixiにログインする
   */
  public static void logInMixi( WebDriver d, MixiAccount account ) {
    StopWatch sw1 = new StopWatch();
    try {
      d.navigate().to("http://mixi.jp/run_appli.pl?id=6598");
    } catch (WebDriverException e) {
      //Javascript実行エラーが出るときがあるが、無視
    }
    sw1.stop("Mixiトップページを開きました");
    
    StopWatch sw2 = new StopWatch();
    // ログインしてない状態なので、mixiトップが表示される。
    //メール欄にメールアドレス入力。
    WebElement input1 = d.findElement(By.name("email"));
    input1.sendKeys( account.getMixiEmail() );
    //パスワード欄にメールアドレス入力。
    WebElement input2 = d.findElement(By.name("password"));
    input2.sendKeys( account.getMixiPassword() );
    try {
      //seleniumが送信すべきフォームを探してくれるので、input1.submit()でも結果は同じ。
      input2.submit();
    } catch (ScriptException e) {
      //Javascript実行エラーが出るときがあるが、無視
    }
    sw2.stop("Mixiにログインしました");
  }
  
  /**
   * ブラ三にログイン(サーバ選択)する。
   * @param serverNumber サーバID。17鯖の場合は17。
   */
  public static void selectBro3Server( WebDriver d, int serverNumber ) {
    StopWatch sw = new StopWatch();
    //iframe内にフォーカスを移す
    d.switchTo().frame("app_content_6598");
    
    if (HtmlUnitDriver.class.isAssignableFrom(d.getClass())) {
      //鯖選択ボタンたちが現れるまで待つ。タイムアウトは30秒。
      //この部分がFFでうまく動かなかったのは、FFのバージョンに違いによるよう。
      //https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/ZwMtc3j4jO0

      (new WebDriverWait(d, 30)).until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver d) {
          //鯖選択ボタンの下に出てくる【推奨ブラウザ】 のエレメントが表示されるまで待つ
//          return d.findElement(By.className("serverBrowser")).isDisplayed();
          return d.getPageSource().indexOf("ロード中")==-1;
        }
      });
    } else {
      //FFなど本物のブラウザを操作する場合
      try {
  //      FileUtils.writeStringToFile(new File("a1.html"), d.getPageSource());
  //      System.out.println("start waiting");
        Thread.sleep(15*1000);
  //      FileUtils.writeStringToFile(new File("a2.html"), d.getPageSource());
  //      System.out.println("end waiting");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    sw.stop("ブラ三サーバ選択画面を開きました");

    /**
     * 上のが動かなかったら以下を試してください。
     */
    //d.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    //Thread.sleep(30*1000);//30秒待つ
    
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
  
}
