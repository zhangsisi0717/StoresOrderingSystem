import java.time.LocalDate;

public class OrderedItem {
  private final int storeId;
  private final int clientId;
  private final String orderId;
  private final int itemID;
  private final int numOfItems;
  private final double price;
  private final LocalDate date;
  private OrderStatus status;

  public OrderedItem(int storeId, int clientId, String orderId, int itemID, int numOfItems, double price, LocalDate date) {
    this.storeId = storeId;
    this.clientId = clientId;
    this.orderId = orderId;
    this.itemID = itemID;
    this.numOfItems = numOfItems;
    this.price = price;
    this.date = date;
    this.status = OrderStatus.preparing;
  }

  public int getClientId() {
    return clientId;
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

  public double getPrice() {
    return price;
  }

  public LocalDate getDate() {
    return date;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }
}
