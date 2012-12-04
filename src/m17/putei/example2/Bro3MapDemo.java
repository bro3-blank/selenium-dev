package m17.putei.example2;

import org.openqa.selenium.WebDriver;

/**
 * 地図を読み込んでデータにアクセスしてみるデモ
 */
public class Bro3MapDemo {

  public static void main(String[] args) throws Exception {

    StopWatch sw = new StopWatch();
    WebDriver d = CommonFlow.getBro3WebDriver(CommonSettings.USE_FIREFOX);

    //読み込むマップの中心座標を複数指定
    String[] xyPairs = {"3,-27", 
//                     "77,-175"
            };
    
    //map オブジェクトに51x51マップの領地データを読み込んでいく
    MapDataCollection map = new MapDataCollection();
    for ( String xy : xyPairs ) {
      String[] items = xy.split(",");
      String x = items[0];
      String y = items[1];
      String mapURL = "http://m"+CommonSettings.SERVER+".3gokushi.jp/map.php?x="+x+"&y="+y+"&type=4";
      String srcOf51x51Map = FileDownloader.getContentFromUrlSlow(d, mapURL);      
      ParserFor51x51Map.loadRawHtml(map, srcOf51x51Map);
    }

    //mapオブジェクトから領地データを読みだしてみる
    //指定された座標から半径1マスの領地データを表示
    for ( String item : xyPairs ) {
      String[] items = item.split(",");
      int x = Integer.parseInt(items[0]);
      int y = Integer.parseInt(items[1]);
      System.out.println("("+item+")から半径1マスの領地データを表示します");
      for ( int i=-1; i<=1; i++ ) {
        for ( int j=-1; j<=1; j++ ) {
          int thisX = x+i;
          int thisY = y+j;
          MapData data = map.getMapInfo(thisX, thisY);
          System.out.print("\t * ("+thisX+","+thisY+")\t");
          if (data==null) {
            System.out.println("空地");
          } else {
            System.out.println("君主="+data.getPlayer()+"\t"+"同盟="+data.getAlliance()+
                    "\t☆="+data.getLevel()+
                    "\t本拠地="+data.getIsCapital()+"\t拠点="+data.getIsKyoten()+
                    "\tNPC="+data.getIsNPC()+"\t配下="+data.getIsHaika());
          }
        }
      }
    }
    
    System.out.println("----------- 全処理終了 -----------");
    sw.stop("累計処理時間");
  }
}
