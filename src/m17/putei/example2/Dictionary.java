package m17.putei.example2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Dictionary implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private short idCounter = Short.MIN_VALUE;
  private Map<String,Short> stringToId = new HashMap<String,Short>(32768);
  private Map<Short,String> idToString = null;
  
  public int getIdFromText(String text) {
    Short id = stringToId.get(text);
    if (id!=null) {
      return id;
    } else {
      idCounter++;
      if ( idCounter >= Short.MAX_VALUE ) throw new RuntimeException("Name space exploded!");
      stringToId.put(text, idCounter);
      return idCounter;
    }
  }

  /**
   * @return the idCounter
   */
  public int getIdCounter() {
    return idCounter;
  }

  /**
   * @param idCounter the idCounter to set
   */
  public void setIdCounter(short idCounter) {
    this.idCounter = idCounter;
  }

  /**
   * @return the stringToId
   */
  public Map<String,Short> getStringToId() {
    return stringToId;
  }

  /**
   * @param stringToId the stringToId to set
   */
  public void setStringToId(Map<String,Short> stringToId) {
    this.stringToId = stringToId;
  }

  /**
   * @return the idToString
   */
  public Map<Short,String> getIdToString() {
    return idToString;
  }

  /**
   * @param idToString the idToString to set
   */
  public void setIdToString(Map<Short,String> idToString) {
    this.idToString = idToString;
  }
  
  
}
