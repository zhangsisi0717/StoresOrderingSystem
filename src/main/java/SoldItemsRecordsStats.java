import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SoldItemsRecordsStats {
  private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> recordsStoreToItem;
  private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> recordsItemToStore;

  static {
    recordsStoreToItem=new ConcurrentHashMap<>();
    recordsItemToStore=new ConcurrentHashMap<>();
  }

  public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> getRecordsStoreToItem() {
    return recordsStoreToItem;
  }

  public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> getRecordsItemToStore() {
    return recordsItemToStore;
  }


}
