package m17.putei.example2;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * ウェブドライバ作成に特化したクラス
 */
public class WebDriverFactory {

  /**
   * ウェブドライバを生成するメソッド
   * @param useFF　Firefoxを開くか、ウィンドウを開かずに処理するか。
   * @return　web driver
   */
  public static WebDriver createDriver( boolean useFF ) {
    long t0 = System.currentTimeMillis();
    //重要でないエラーメッセージがたくさん表示されてしまうので、阻止。
    Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
    Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    
    if ( useFF ) {
      return new FirefoxDriver();
    } else { 
      // FFの窓を開かず実行
      //mixiがボットをはじくようになっているのでUserAgentを偽装
      BrowserVersion bv = new BrowserVersion(
              "Netscape", 
              "5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_4) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.56 Safari/536.5", 
              1.2f );
      HtmlUnitDriver d = new HtmlUnitDriver(bv);
      d.setJavascriptEnabled(true);
      long t1 = System.currentTimeMillis();
      System.out.println("本拠地画面を開きました ["+(double)(t1-t0)/(double)1000+"秒]");
      return d;
    }
  }
}
