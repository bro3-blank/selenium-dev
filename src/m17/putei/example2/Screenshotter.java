package m17.putei.example2;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Screenshotter {
  
  public static void takeScreenshot( WebDriver d, int x, int y, int type ) throws Exception {
    StopWatch sw1 = new StopWatch();
//    d.switchTo().frame("mainframe"); //さらに中のiframe内にフォーカスを移す
    String url = "http://m"+CommonSettings.SERVER+".3gokushi.jp/map.php?x="+x+"&y="+y+"&type="+type;
    d.navigate().to(url); //地図画面
    sw1.stop("地図画面を開きました");
    
    if (FirefoxDriver.class.isAssignableFrom(d.getClass())) {
      StopWatch sw2 = new StopWatch();
      File dir = new File("target/("+x+","+y+")");
      if (!dir.exists()) dir.mkdirs();
      File scrFile = ((TakesScreenshot)d).getScreenshotAs(OutputType.FILE);
      File f = new File(dir, Util.getTimestamp()+".png");
      FileUtils.copyFile(scrFile, f);
      sw2.stop("スクリーンショットを保存しました: "+f);
    }
  }
  
}
