package m17.putei.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class EnseiSheetAnalyzer {

  //このディレクトリはこんなかんじです
  //http://gyazo.com/a2606e2da4dbb3c77148f0bfbff00963
  private File dir = new File("data/ensei");
  
  public void run() throws Exception {
    //各ファイルは各遠征シートに対応
    //テキスト形式（タブ区切り）でエクスポートすること
    //http://gyazo.com/71551f7fa04c2d8c719a5210621a4dee
    //コピペだと大きいシートは仕様上一部しか情報取得できません
    //TODO: リアルタイムにシート情報を取れるとイイネ！きっとぷりさんか鯛さんあたりが移植したコードでやってくれるはず。。。？！
    for ( File f : dir.listFiles()) {
      List<String> lines = FileUtils.readLines(f);
      int numRegistered = 0;
      int numObtained = 0;
      //key:領地取得君主名, value:この君主がいくつの領地を取ったか
      Map<String,Integer> counter = new HashMap<String,Integer>();
      //Example cells: [437, -102, (437,-102)  ★1, (437,-102), 伊波, 476.05, 12/11/2012 4:07:53]
      for ( String line : lines ) {
        String[] cells = line.split("\t");
        //もしもこの行が領地情報だったら...
        if ( cells.length>2 && isNumber(cells[0]) && isNumber(cells[1]) ) {
          numRegistered++;
          String player = cells[4].replaceAll(" ", "");//誤入力の空白除去
          //もしも取得／予約済みだったら...
          if (player.length()>0) {
            numObtained++;
            Integer count = counter.get(player);
            if (count==null) count = 0;
            counter.put(player, ++count);
          }
        }
      }
      //counterをカウント数で降順ソート
      List<Entry<String,Integer>> counter2 = new ArrayList<Entry<String,Integer>>( counter.entrySet() );
      Collections.sort(counter2, new Comparator<Entry<String,Integer>>() {
        @Override
        public int compare(Entry<String,Integer> e1, Entry<String,Integer> e2) {
          return e2.getValue().compareTo(e1.getValue());
        }
      });
      System.out.println("----"+f.getName().replaceAll("\\..+", "")+"----");
      int progress = numRegistered>0 ? (100*numObtained/numRegistered) : 0;
      System.out.println("進捗率: "+numObtained+" / "+numRegistered+" = "+progress+"%");
      System.out.println("君主別領地取得数: "+counter2);
      System.out.println();
    }
  }
  
  private boolean isNumber( String s ) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
  
  public static void main(String[] args) throws Exception {
    new EnseiSheetAnalyzer().run();
  }
  
}
