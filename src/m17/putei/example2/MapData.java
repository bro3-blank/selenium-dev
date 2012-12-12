package m17.putei.example2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapData implements Serializable {

  // Assumption: unique player & alliance name is less than 65536.
  private static final long serialVersionUID = 1L;

  private short playerId;

  private short allianceId;

  private boolean isNPC;
  private boolean isCapital; //capital or NPC
  private boolean isKyoten; //kyoten or honkyochi
  private boolean isHaika; //is capital and haika
  private int level;
  private byte wood;
  private byte stone;
  private byte iron;
  private byte rice;
  
  private Dictionary dict;

  public MapData(Dictionary dict) {
    this.dict = dict;
  }
  
  private void initialize() {
    if (dict.getIdToString()!=null) return;
    Map<Short,String> idToString = new HashMap<Short,String>(dict.getStringToId().size());
    dict.setIdToString(idToString);
    for ( Entry<String,Short> e : dict.getStringToId().entrySet() ) {
      idToString.put(e.getValue(), e.getKey());
    }
  }
  
  public void setPlayer(String text) {
    playerId = (short) dict.getIdFromText(text);
  }

  public void setAlliance(String text) {
    allianceId = (short) dict.getIdFromText(text);
  }

  public void setIsNPC(boolean isNPC) {
    this.isNPC = isNPC;
  }
  
  public void setIsCapital(boolean isCapital) {
    this.isCapital = isCapital;
  }
  
  public void setIsKyoten(boolean isKyoten) {
    this.isKyoten = isKyoten;
  }

  public void setIsHaika(boolean isHaika) {
    this.isHaika = isHaika;
  }
  
  public String getPlayer() {
    initialize();
    return dict.getIdToString().get(playerId);
  }
  
  public String getAlliance() {
    initialize();
    return dict.getIdToString().get(allianceId);
  }
  
  public boolean getIsNPC() {
    return isNPC;
  }
  
  public boolean getIsCapital() {
    return isCapital;
  }

  public boolean getIsKyoten() {
    return isKyoten;
  }
  
  public boolean getIsHaika() {
    return isHaika;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setWood(byte wood) {
    this.wood = wood;
  }

  public void setStone(byte stone) {
    this.stone = stone;
  }

  public void setIron(byte iron) {
    this.iron = iron;
  }

  public void setRice(byte rice) {
    this.rice = rice;
  }

  public byte getWood() {
    return wood;
  }

  public byte getStone() {
    return stone;
  }

  public byte getIron() {
    return iron;
  }

  public byte getRice() {
    return rice;
  }

}
