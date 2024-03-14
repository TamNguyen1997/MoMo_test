package momo.payment.service.services;

import static momo.payment.service.repository.BillRepository.FILE_PATH;
import static momo.payment.service.repository.BillRepository.bills;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import momo.payment.service.enums.BillState;
import momo.payment.service.enums.BillType;
import momo.payment.service.exception.BillNotFoundException;
import momo.payment.service.model.Bill;
import momo.payment.service.repository.BillRepository;
import momo.payment.service.utils.ParseUtil;

public class BillService {

  public void printAll(Map<Integer, Bill> bills) {
    if (bills.isEmpty()) {
      System.out.println("No bill");
      return;
    }
    System.out.println("Bill No.     Type      Amount     Due Date     State     PROVIDER");
    bills.forEach(
        (id, bill) ->
            System.out.printf(
                "%s          %s        %s          %s         %s         %s" + System.lineSeparator(),
                bill.getId(),
                bill.getType(),
                bill.getAmount(),
                bill.getDueDate(),
                bill.getState(),
                bill.getProvider()));
  }

  public void printPayment() {
    Map<Integer, Bill> billsToPrint =
        bills.entrySet().stream()
            .filter(entry -> !entry.getValue().getState().equals(BillState.NOT_PAID))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (billsToPrint.isEmpty()) {
      System.out.println("No bill has been paid");
    } else {
      printAll(billsToPrint);
    }
  }

  public void searchByProvider(String provider) {
    Map<Integer, Bill> billsToPrint =
            bills.entrySet().stream()
            .filter(entry -> entry.getValue().getProvider().equals(provider))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    printAll(billsToPrint);
  }

  public void schedule(int id, String date) {
    if (bills.get(id) == null) {
      throw new BillNotFoundException();
    }
    BillRepository.bills.get(id).setDueDate(ParseUtil.parseLocalDate(date));
    System.out.printf("Payment for bill id %s is scheduled on %s", id, date);
  }

  public void delete(int id) {
    if (BillRepository.bills.get(id) == null) {
      throw new BillNotFoundException();
    }
    bills.remove(id);
    System.out.printf("Payment for bill id %s has been deleted", id);
  }

  public void create(
      BillType type, int amount, LocalDate dueDate, BillState state, String provider) {

    int id = Collections.max(bills.keySet()) + 1;
    bills.put(id, new Bill(id, type, amount, dueDate, state, provider));
    System.out.printf("Payment for bill id %s has been deleted", id);
  }

  public void dueDate() {
    Map<Integer, Bill> billsToPrint =
            bills.entrySet().stream()
                    .filter(entry -> !entry.getValue().getDueDate().isBefore(LocalDate.now()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    printAll(billsToPrint);
  }

  public void save() throws IOException {
    Path path = Paths.get(FILE_PATH);
    StringBuilder stringBuilder = new StringBuilder();
    bills.forEach(
        (id, bill) ->
            stringBuilder.append(
                String.format(
                    "%s:%s:%s:%s:%s:%s" + System.lineSeparator(),
                    bill.getId(),
                    bill.getType(),
                    bill.getAmount(),
                    bill.getDueDate().format(ParseUtil.DATE_TIME_FORMATTER),
                    bill.getState(),
                    bill.getProvider())));

    Files.write(path, stringBuilder.toString().getBytes());
  }
}
