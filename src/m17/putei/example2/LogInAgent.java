package m17.putei.example2;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.ScriptException;

public class LogInAgent {

  /**
   * Mixiにログインする
   */
  public static void logInMixi( WebDriver d, MixiAccount account ) {
    long t0 = System.currentTimeMillis();
    try {
      d.navigate().to("http://mixi.jp/run_appli.pl?id=6598");
    } catch (WebDriverException e) {
      //Javascript実行エラーが出るときがあるが、無視
    }
    long t1 = System.currentTimeMillis();
    System.out.println("Mixiトップページを開きました ["+(double)(t1-t0)/(double)1000+"秒]");
    
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
    long t2 = System.currentTimeMillis();
    System.out.println("Mixiにログインしました ["+(double)(t2-t1)/(double)1000+"秒]");
  }
  
  /**
   * ブラ三にログイン(サーバ選択)する。
   * @param serverNumber サーバID。17鯖の場合は17。
   */
  public static void selectBro3Server( WebDriver d, int serverNumber ) {
    long t0 = System.currentTimeMillis();
    //iframe内にフォーカスを移す
    d.switchTo().frame("app_content_6598");

    //鯖選択ボタンたちが現れるまで待つ。タイムアウトは30秒。
    (new WebDriverWait(d, 30)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        //鯖選択ボタンの下に出てくる【推奨ブラウザ】 のエレメントが表示されるまで待つ
        return d.findElement(By.className("serverBrowser")).isDisplayed();
      }
    });
    long t1 = System.currentTimeMillis();
    System.out.println("ブラ三サーバ選択画面を開きました ["+(double)(t1-t0)/(double)1000+"秒]");

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
  
}
