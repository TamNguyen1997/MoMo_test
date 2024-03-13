package momo.payment.service.model;

import momo.payment.service.enums.BillState;
import momo.payment.service.enums.BillType;

import java.time.LocalDate;

public class Bill {
    private final int id;
    private final BillType type;
    private final int amount;
    private LocalDate dueDate;
    private BillState state;
    private final String provider;
    public Bill(int id, BillType type,
                int amount, LocalDate  dueDate,
                BillState state, String provider) {
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
}
