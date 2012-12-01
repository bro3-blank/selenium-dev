package m17.putei.example2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BPFinder {
  
  public static int getBP( WebDriver d ) throws Exception {
    StopWatch sw1 = new StopWatch();
    d.switchTo().frame("mainframe"); //さらに中のiframe内にフォーカスを移す
    d.navigate().to("http://m"+CommonSettings.SERVER+".3gokushi.jp/village.php"); //本拠地画面へ
    sw1.stop("本拠地画面を開きました");
    
    StopWatch sw2 = new StopWatch();
    String bpString = d.findElement(By.id("bptpcp_area")).findElements(By.tagName("span")).get(0).getText();
    int bp = Integer.parseInt(bpString);
    sw2.stop("現在のBP: "+bp);

    return bp;
  }
  
}
