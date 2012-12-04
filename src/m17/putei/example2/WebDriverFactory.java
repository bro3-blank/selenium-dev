package m17.putei.example2;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * ウェブドライバ作成に特化したクラス
 * 
 * UserAgent一覧も参考になるかも。
 * http://www.openspc2.org/userAgent/
 */
public class WebDriverFactory {

  /**
   * ウェブドライバを生成するメソッド
   * @param useFF　Firefoxを開くか、ウィンドウを開かずに処理するか。
   * @return　web driver
   */
  public static WebDriver createDriver( boolean useFF ) {
    StopWatch sw = new StopWatch();
    //重要でないエラーメッセージがたくさん表示されてしまうので、阻止。
    Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
    Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    
    WebDriver d;
    if ( useFF ) {
      d = new FirefoxDriver();
    } else { 
      // FFの窓を開かず実行
      //mixiがボットをはじくようになっているのでUserAgentを偽装
      BrowserVersion bv = new BrowserVersion(
              "Netscape", 
              "5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5", 
              1.2f );
      HtmlUnitDriver hud = new HtmlUnitDriver(bv);
      hud.setJavascriptEnabled(true);
      d = hud;
    }
    sw.stop("ウェブドライバを初期化しました");
    return d;
  }
}
