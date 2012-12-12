package m17.putei.example2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;

/**
 * マルチスレッドでマップを取得してCSV形式で保存。
 * CSVカラム:　x座標,y座標,君主名,同盟名,本拠地フラグ,拠点フラグ,配下フラグ,NPC砦フラグ,領地レベル,木,石,鉄,糧
 * ※君主名が空のときは空地
 * ※フラグは1がTRUE、0がFALSE
 * ※領地レベル、木、石、鉄、糧は領地以外の場合は-1
 */
public class Bro3MapMultiDemo {

  private static final int SIZE = 51;
  private static final int OFFSET = 600-25;
  
  private static int numThread = 8;
  private static int threadLife = 2*60*60;//スレッドの寿命(秒)
  private static File outputFile = new File("map-"+Util.getTimestamp()+".csv");
  
  public static void main(String[] args) throws Exception {
    StopWatch sw = new StopWatch();    
    System.out.println("Output file:"+outputFile.getAbsolutePath());
    // 読み込むマップの中心座標を複数指定
    // 上位配列の長さ分スレッドが増える
    //(-575,-575), (-575,-524), (-575,-473), ...
    List<List<String>> xyPairsPerThread = new ArrayList<List<String>>();
    for ( int i=0; i<numThread; i++ ) {
      xyPairsPerThread.add( new ArrayList<String>() ); 
    }
    int counter = 0;
    for (int i=0; i*SIZE-OFFSET<=600; i++) {
      for (int j=0; j*SIZE-OFFSET<=600; j++) {
        String zahyo = (i*SIZE-OFFSET)+","+(j*SIZE-OFFSET);
        xyPairsPerThread.get(counter++ % numThread).add(zahyo);
      }
    }
    ExecutorService exec = Executors.newFixedThreadPool(numThread);
    for (int i=0; i<numThread; i++) {
      List<String> xyPairs = xyPairsPerThread.get(i);
      exec.execute(new Bro3MapDemoThread(xyPairs, outputFile, i+1));
    }
    try {
      exec.shutdown();
      // (全てのタスクが終了した場合、trueを返してくれる)
      if (!exec.awaitTermination(threadLife, TimeUnit.SECONDS)) {
        // タイムアウトした場合、全てのスレッドを中断(interrupted)してスレッドプールを破棄する。
        exec.shutdownNow();
      }
    } catch (InterruptedException e) {
      // awaitTerminationスレッドがinterruptedした場合も、全てのスレッドを中断する
      System.out.println("awaitTermination interrupted: " + e);
      exec.shutdownNow();
    }
    System.out.println("----------- 全処理終了 -----------");
    sw.stop("合計計処理時間");
    System.out.println("出力ファイル:"+outputFile.getAbsolutePath());
  }
}

class Bro3MapDemoThread implements Runnable {
  private List<String> xyPairs;
  private File f;
  private int threadNumber;
  
  public Bro3MapDemoThread(List<String> xyPairs, File f, int threadNumber) {
    this.xyPairs = xyPairs;
    this.f = f;
    this.threadNumber = threadNumber;
  }

  public void run() {
    WebDriver d = CommonFlow.getBro3WebDriver(CommonSettings.USE_FIREFOX);
//    MapDataCollection map = new MapDataCollection();
    int counter = 0;
    for (String xy : xyPairs) {
      try {
        StopWatch sw = new StopWatch();
        String[] items = xy.split(",");
        String x = items[0];
        String y = items[1];
        String mapURL = "http://m" + CommonSettings.SERVER + ".3gokushi.jp/map.php?x=" + x + "&y="
                + y + "&type=4";
        String srcOf51x51Map = FileDownloader.getContentFromUrlSlow(d, mapURL);
        MapDataCollection map = new MapDataCollection();//declare here to save memory space
        ParserFor51x51Map.loadRawHtml(map, srcOf51x51Map);
        FileUtils.write(f, map.toCSV(), true);//append csv content to file
        sw.stop("スレッドID="+threadNumber+"が ("+x+","+y+") 中心の51×51マップから領地情報を取得しました。　("+(++counter)+"/"+xyPairs.size()+")");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}