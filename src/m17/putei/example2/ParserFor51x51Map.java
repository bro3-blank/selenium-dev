package m17.putei.example2;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserFor51x51Map {

  /**51x51**/
  //本拠地 
  //<li class="bg_enemy_territory  ">
  //<div>
  //<a href="/land.php?x=13&amp;y=21#ptop" onmouseover="return gloss('&lt;dl class=&quot;bigmap&quot;&gt;&lt;dt class=&quot;bigmap-caption&quot;&gt;大東亜城&lt;/dt&gt;&lt;dd class=&quot;bigmap-subcap&quot;&gt;&amp;nbsp;&lt;/dd&gt;&lt;dt&gt;君主名&lt;/dt&gt;&lt;dd&gt;大東亜&lt;/dd&gt;&lt;dt&gt;人口&lt;/dt&gt;&lt;dd&gt;992&lt;/dd&gt;&lt;dt&gt;座標&amp;nbsp;/&amp;nbsp;距離&lt;/dt&gt;&lt;dd&gt;(13,21)&amp;nbsp;/&amp;nbsp;[208.24]&lt;/dd&gt;&lt;dt class=&quot;bottom-popup-l&quot;&gt;同盟名&lt;/dt&gt;&lt;dd class=&quot;bottom-popup-r&quot;&gt;乱悠☆旅団&lt;/dd&gt;&lt;/dl&gt;');" onmouseout="nd();">
  //城</a></div></li>
  
  //拠点
  //<li class="bg_enemy_territory  ">
  //<div>
  //<a href="/land.php?x=24&amp;y=21#ptop" onmouseover="return gloss('&lt;dl class=&quot;bigmap&quot;&gt;&lt;dt class=&quot;bigmap-caption&quot;&gt;リーフィア&lt;/dt&gt;&lt;dd class=&quot;bigmap-subcap&quot;&gt;&amp;nbsp;&lt;/dd&gt;&lt;dt&gt;君主名&lt;/dt&gt;&lt;dd&gt;イーブイ兄弟&lt;/dd&gt;&lt;dt&gt;人口&lt;/dt&gt;&lt;dd&gt;627&lt;/dd&gt;&lt;dt&gt;座標&amp;nbsp;/&amp;nbsp;距離&lt;/dt&gt;&lt;dd&gt;(24,21)&amp;nbsp;/&amp;nbsp;[208]&lt;/dd&gt;&lt;dt class=&quot;bottom-popup-l&quot;&gt;同盟名&lt;/dt&gt;&lt;dd class=&quot;bottom-popup-r&quot;&gt;乱悠☆旅団&lt;/dd&gt;&lt;/dl&gt;');" onmouseout="nd();">
  //村</a></div></li>
  
  //領地
  
  //NPC
  //<li class="bg_npc_fort bg_vacant_land strong-text ">
  //<div>
  //<a href="/land.php?x=0&amp;y=0#ptop" onmouseover="return gloss('&lt;dl class=&quot;bigmap&quot;&gt;&lt;dt class=&quot;bigmap-caption&quot;&gt;洛陽&lt;/dt&gt;&lt;dd class=&quot;bigmap-subcap&quot;&gt;&amp;nbsp;&lt;/dd&gt;&lt;dt&gt;君主名&lt;/dt&gt;&lt;dd&gt;献帝&lt;/dd&gt;&lt;dt&gt;座標&amp;nbsp;/&amp;nbsp;距離&lt;/dt&gt;&lt;dd&gt;(0,0)&amp;nbsp;/&amp;nbsp;[230.15]&lt;/dd&gt;&lt;dt&gt;戦力&lt;/dt&gt;&lt;dd&gt;&lt;span class=&quot;npc-red-star&quot;&gt;★★★★★★★★★&lt;/span&gt;&lt;/dd&gt;&lt;/dl&gt;');" onmouseout="nd();">
  //城</a></div></li>
  
  private static Pattern pX = Pattern.compile("x=([0-9\\-]+)&amp;");
  private static Pattern pY = Pattern.compile("y=([0-9\\-]+)");
  private static Pattern pPlayer = Pattern.compile("君主名(?:<|&lt;)/dt(?:>|&gt;)(?:<|&lt;)dd(?:>|&gt;)(.+?)(?:<|&lt;)");
  private static Pattern pAlliance = Pattern.compile("同盟名(?:<|&lt;)/dt(?:>|&gt;)(?:<|&lt;)dd(?: class=[^ ]+)?(?:>|&gt;)(.+?)(?:<|&lt;)/dd(?:>|&gt;)");
  private static Pattern pLevel = Pattern.compile("戦力(?:<|&lt;)/dt(?:>|&gt;)(?:<|&lt;)dd(?:>|&gt;)([☆★]+)(?:<|&lt;)");
  private static Pattern pType = Pattern.compile(">\\s*([^\\s]+?)\\s*</a>", Pattern.DOTALL);
  private static Pattern pClass = Pattern.compile("<li class=\"(.+?)\">");
  private static Pattern pShigen = Pattern.compile("(?:>|&gt;)資源(?:<|&lt;)/dt(?:>|&gt;)(?:<|&lt;)dd class=&quot;bottom-popup-r&quot;(?:>|&gt;)" +
          "木([0-9]+)&amp;nbsp;岩([0-9]+)&amp;nbsp;鉄([0-9]+)&amp;nbsp;糧([0-9]+)(?:<|&lt;)");
  
//  <tr>
//  <th class="rank">ランク</th>
//  <th class="loadName">同盟略称</th>
//      <th class="all">ポイント</th>
//    <th class="human">メンバー</th>
//  <th class="loadName">盟主</th>
//  <th class="defense">座標</th>
//  <th class="defense">合流日時</th>
//</tr>
//<tr>
//  <td class="rankNum">1</td>
//  <td><a href="info.php?id=1637">三顧EX</a></td>
//  <td class="center">47932875</td>
//  <td class="center">215</td>
//  <td class="center"><a href="/user/?user_id=5177">凉翠</a></td>
//  <td class="center"><a href="../land.php?x=-81&amp;y=34">(-81,34)</a></td>
//  <td class="center">2012-04-05 15:15</td>
//</tr>
  private static String extractTextByPatternMatch( Pattern p, String text ) {
    Matcher m = p.matcher(text);
    return m.find() ? m.group(1) : null;
  }
  
  /**
   * 51x51マップのHTMLソースファイルをパースして、データをmapオブジェクトに保存する。
   * @param map
   * @param srcOf51x51Map
   */
  public static void loadRawHtml(MapDataCollection map, String srcOf51x51Map) {
//    StopWatch sw = new StopWatch();
    Set<String> history = new HashSet<String>();
      
    String[] lines = srcOf51x51Map.split("\n");
    boolean in = false;
    StringBuilder sbRawMapInfo = new StringBuilder();
    for ( String line : lines ) {
      if (line.indexOf("<div id=\"map51-content\">")!=-1) in = true;
      if (line.indexOf("<!-- End : #map51-content -->")!=-1) in = false;
      if (!in) continue;
      sbRawMapInfo.append(line+"\n");
      if (!line.trim().endsWith("</li>")) {
        continue;
      }
      String rawMapInfo = sbRawMapInfo.toString();
      sbRawMapInfo = new StringBuilder();
      String cssClass = extractTextByPatternMatch(pClass, rawMapInfo);
      if (cssClass.indexOf("bg_out_of_map")!=-1) continue;
      int x = Integer.parseInt( extractTextByPatternMatch(pX, rawMapInfo) );
      int y = Integer.parseInt( extractTextByPatternMatch(pY, rawMapInfo) );
      history.add(x+","+y);
      String player = extractTextByPatternMatch(pPlayer, rawMapInfo);
      String alliance = extractTextByPatternMatch(pAlliance, rawMapInfo);
      String textLevel = extractTextByPatternMatch(pLevel, rawMapInfo);
      int level = textLevel!=null ? textLevel.length() : -1;
      String type = extractTextByPatternMatch(pType, rawMapInfo);
      if (type==null) {
        System.err.println("Regex failed.\t"+pType+"\tTEXT="+rawMapInfo);
        type = "BUG";
      }
      boolean isNPC = cssClass.indexOf("bg_npc_fort")!=-1;
      boolean isCapital = !isNPC && type.equals("城");
      boolean isKyoten = (!isNPC && type.equals("砦")) || type.equals("村");
      boolean isHaika = cssClass.indexOf("bg_junior_alliance")!=-1 || cssClass.indexOf("bg_enemy_junior_alliance")!=-1;

      Matcher mShigen = pShigen.matcher(rawMapInfo);
      byte wood = -1, stone = -1, iron = -1, rice = -1;
      if (mShigen.find()) {
        wood  = Byte.parseByte(mShigen.group(1));
        stone = Byte.parseByte(mShigen.group(2));
        iron  = Byte.parseByte(mShigen.group(3));
        rice  = Byte.parseByte(mShigen.group(4));
      }
      //もしデータサイズ削減のため空地は無視する場合：
      //if (player==null) continue;
      map.register( x, y, player, alliance, level, 
              isNPC, isCapital, isKyoten, isHaika,
              wood, stone, iron, rice);
    }
//    sw.stop(history.size()+"マスの領地データを抽出しました");
  }
  
}
