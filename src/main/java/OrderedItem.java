import java.sql.Date;

public class OrderedItem {
  private final int storeId;
  private final int customerId;
  private final String orderId;
  private final int itemID;
  private final int numOfItems;
  private final double price;
  private final Date date;
  private OrderStatus status;

  public OrderedItem(int storeId, int customerId, String orderId, int itemID, int numOfItems, double price, Date date) {
    this.storeId = storeId;
    this.customerId = customerId;
    this.orderId = orderId;
    this.itemID = itemID;
    this.numOfItems = numOfItems;
    this.price = price;
    this.date = date;
    this.status = OrderStatus.preparing;
  }

  public int getCustomerId() {
    return customerId;
  }

  public String getOrderId() {
    return orderId;
  }

  public int getItemID() {
    return itemID;
  }

  public int getNumOfItems() {
    return numOfItems;
  }

  public int getStoreId() {
    return storeId;
  }

  public double getPrice() {
    return price;
  }

  public Date getDate() {
    return date;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }
}
