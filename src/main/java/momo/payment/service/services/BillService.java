package momo.payment.service.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import momo.payment.service.enums.BillState;
import momo.payment.service.enums.BillType;
import momo.payment.service.model.Bill;

public class BillService {
  public static Map<Integer, Bill> bills = new HashMap<>();
  public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  public static String FILE_PATH = "src/main/resources/bill.txt";

  public BillService() {
    try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
      lines.forEach(
          line -> {
            String[] values = line.split(":");
            bills.put(
                Integer.parseInt(values[0]),
                new Bill(
                    Integer.parseInt(values[0]),
                    BillType.valueOf(values[1]),
                    Integer.parseInt(values[2]),
                    LocalDate.parse(values[3], DATE_TIME_FORMATTER),
                    BillState.valueOf(values[4]),
                    values[5]));
          });
    } catch (IOException e) {
      System.out.println("ERROR");
    }
  }

  public void printAll(Map<Integer, Bill> bills) {
    System.out.println("Bill No.     Type      Amount     Due Date     State     PROVIDER");
    bills.forEach(
        (id, bill) ->
            System.out.printf(
                "%s          %s        %s          %s         %s         %s",
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
            .filter(entry -> !entry.getValue().getProvider().equals(provider))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (billsToPrint.isEmpty()) {
      System.out.printf("Found no bill with provider %s", provider);
    } else {
      printAll(billsToPrint);
    }
    printAll(billsToPrint);
  }

  public void schedule(int id, String date) {
    bills.get(id).setDueDate(LocalDate.parse(date, DATE_TIME_FORMATTER));
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
                    bill.getDueDate().format(DATE_TIME_FORMATTER),
                    bill.getState(),
                    bill.getProvider())));

    Files.write(path, stringBuilder.toString().getBytes());
  }
}