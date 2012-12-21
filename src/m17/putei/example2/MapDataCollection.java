package m17.putei.example2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MapDataCollection implements Serializable {
  
  private static final long serialVersionUID = 1L;

  private final static int HANKEI = 600;
  private final static int CHOKKEI = HANKEI*2+1;
  
  private Map<Integer,MapData> coordToMapinfo = new HashMap<Integer,MapData>(CHOKKEI*CHOKKEI*4/3+1);

  private Dictionary dict = new Dictionary();
  
  public void register( int x, int y, String player, 
          String alliance, int level, boolean isNPC, 
          boolean isCapital, boolean isKyoten, boolean isHaika,
          byte wood, byte stone, byte iron, byte rice ) {
    int coordId = getCoordId(x, y);
    MapData info = new MapData(dict);
    info.setPlayer(player);
    info.setAlliance(alliance);
    info.setLevel(level);
    info.setIsNPC(isNPC);
    info.setIsCapital(isCapital);
    info.setIsKyoten(isKyoten);
    info.setIsHaika(isHaika);
    info.setWood(wood);
    info.setStone(stone);
    info.setIron(iron);
    info.setRice(rice);
    coordToMapinfo.put(coordId, info);
  }
  
  private int getCoordId(int x, int y) {
    int posX = x + HANKEI;
    int posY = y + HANKEI;
    int xy = posX + (CHOKKEI * posY);
    return xy; 
  }

  private int[] getCoord(int xy) {
    int x = xy % CHOKKEI - HANKEI;
    int y = xy / CHOKKEI - HANKEI;
    return new int[]{ x, y };
  }
  
  public MapData getMapInfo( int x, int y ) {
    int coordId = getCoordId(x, y);
    return coordToMapinfo.get(coordId);
  }
  
  public int getSize() {
    return coordToMapinfo.size();
  }

  public Dictionary getDict() {
    return dict;
  }

  //x座標,y座標,君主名,同盟名,本拠地かどうか,拠点かどうか,配下かどうか,NPC砦かどうか,領地レベル,木,石,鉄,糧
  public String toCSV() {
    StringBuilder sb = new StringBuilder();
    for ( int xy : coordToMapinfo.keySet() ) {
      MapData data = coordToMapinfo.get(xy);
      int[] xyArray = getCoord(xy);
      sb.append(xyArray[0]+",");//x
      sb.append(xyArray[1]+",");//y
      sb.append((data.getPlayer()!=null?data.getPlayer():"")+",");
      sb.append((data.getAlliance()!=null?data.getAlliance():"")+",");
      sb.append(data.getIsCapital()?1:0+",");
      sb.append(data.getIsKyoten()?1:0+",");
      sb.append(data.getIsHaika()?1:0+",");
      sb.append(data.getIsNPC()?1:0+",");
      sb.append(data.getLevel()+",");
      sb.append(data.getWood()+",");
      sb.append(data.getStone()+",");
      sb.append(data.getIron()+",");      
      sb.append(data.getRice());      
      sb.append("\n");
    }
    return sb.toString();
  }
}


