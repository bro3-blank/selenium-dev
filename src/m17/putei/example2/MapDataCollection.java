package m17.putei.example2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MapDataCollection implements Serializable {
  
  private static final long serialVersionUID = 1L;

  private Map<Integer,MapData> coordToMapinfo = new HashMap<Integer,MapData>(1500000);

  private int HANKEI = 600;
  private int CHOKKEI = HANKEI*2+1;
  
  private Dictionary dict = new Dictionary();
  
  public void register(int x, int y, String player, 
          String alliance, int level, boolean isNPC, 
          boolean isCapital, boolean isKyoten, boolean isHaika 
//          ,byte wood, byte stone, byte iron, byte rice
          ) {
    int coordId = getCoordId(x, y);
    MapData info = new MapData(dict);
    info.setPlayer(player);
    info.setAlliance(alliance);
    info.setLevel(level);
    info.setIsNPC(isNPC);
    info.setIsCapital(isCapital);
    info.setIsKyoten(isKyoten);
    info.setIsHaika(isHaika);
//    info.setWood(wood);
//    info.setStone(stone);
//    info.setIron(iron);
//    info.setRice(rice);
    coordToMapinfo.put(coordId, info);
  }
  
  private int getCoordId(int x, int y) {
    int posX = x + HANKEI;
    int posY = y + HANKEI;
    return posX + CHOKKEI * posY; 
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
}


