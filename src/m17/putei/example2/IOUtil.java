package m17.putei.example2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Serializer / deserializer (直列化関係のクラス)
 * 
 * 使用例（MapDataCollectionを読み書きする場合）
 * <code>
 * //MapDataCollectionをディスクに保存、圧縮
 * IOUtil<MapDataCollection> ioutil = new IOUtil<MapDataCollection>();
 * ioutil.saveZippedData(map, new File("target/map.zip"));
 *     
 * //圧縮したMapDataCollectionデータをディスクから読み込み
 * MapDataCollection map2 = ioutil.loadZippedData(new File("target/map.zip"));
 * </code>
 */
public class IOUtil<T> {

  /**
   * オブジェクトをserialize.
   * 
   * @param obj
   * @param path
   * @throws Exception
   */
  @Deprecated
  public void saveData( T obj, File path ) throws Exception {
    FileOutputStream outFile = new FileOutputStream(path);
    ObjectOutputStream outObject = new ObjectOutputStream(outFile);
    outObject.writeObject(obj);
    outObject.close();
    outFile.close();
  }

  /**
   * オブジェクトをzip圧縮してserialize.
   * 
   * @param obj
   * @param path
   * @throws Exception
   */
  public void saveZippedData( T obj, File path ) throws Exception {
    if (!path.getParentFile().exists()) {
      path.getParentFile().mkdirs();
    }
    OutputStream os = new FileOutputStream(path);
    ZipOutputStream zipos = new ZipOutputStream(os);
    
    ZipEntry e = new ZipEntry("obj.ser");
    e.setMethod(ZipOutputStream.DEFLATED);
    zipos.putNextEntry(e);
    ObjectOutputStream outObject = new ObjectOutputStream(zipos);
    outObject.writeObject(obj);
//    outObject.close(); // NG
    zipos.closeEntry();
    zipos.close();
  }
  
  /**
   * serializeしたデータをオブジェクトとして読み込みます
   * 
   * @param path
   * @throws Exception
   */
  @Deprecated
  public T loadData( File path ) throws Exception {
    FileInputStream inFile = new FileInputStream(path);
    ObjectInputStream inObject = new ObjectInputStream(inFile);
    @SuppressWarnings("unchecked")
    T obj = (T) inObject.readObject();
    inObject.close();
    inFile.close();
    return obj;
  }
  
  /**
   * serializeデータ（zip圧縮済み）を読み込みます
   * 
   * @param path
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public T loadZippedData( File path ) throws Exception {
    InputStream is = new FileInputStream(path);
    BufferedInputStream bis = new BufferedInputStream(is);
    ZipInputStream in = new ZipInputStream(bis);

    T obj = null;
    
    @SuppressWarnings("unused")
    ZipEntry zipEntry = null;
    while ((zipEntry = in.getNextEntry()) != null) {
      @SuppressWarnings("resource")
      ObjectInputStream inObject = new ObjectInputStream(in);
      obj = (T) inObject.readObject();
//      inObject.close(); //NG
      in.closeEntry();
    }
    in.close();
    
    return obj;
  }
  
}
