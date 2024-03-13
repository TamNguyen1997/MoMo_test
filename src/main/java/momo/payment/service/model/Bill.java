package momo.payment.service.model;

import java.time.LocalDate;
import momo.payment.service.enums.BillState;
import momo.payment.service.enums.BillType;

public class Bill {
  private final int id;
  private final BillType type;
  private final int amount;
  private String provider;
  private LocalDate dueDate;
  private BillState state;

  public Bill(
      int id, BillType type, int amount, LocalDate dueDate, BillState state, String provider) {
    this.id = id;
    this.type = type;
    this.amount = amount;
    this.dueDate = dueDate;
    this.state = state;
    this.provider = provider;
  }

  public int getId() {
    return this.id;
  }

  public BillType getType() {
    return this.type;
  }

  public int getAmount() {
    return this.amount;
  }

  public LocalDate getDueDate() {
    return this.dueDate;
  }

  public void setDueDate(LocalDate date) {
    this.dueDate = date;
  }

  public BillState getState() {
    return this.state;
  }

  public void setState(BillState state) {
    this.state = state;
  }

  public String getProvider() {
    return this.provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }
}
