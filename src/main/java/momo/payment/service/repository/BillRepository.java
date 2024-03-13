package momo.payment.service.repository;

import momo.payment.service.enums.BillState;
import momo.payment.service.enums.BillType;
import momo.payment.service.model.Bill;
import momo.payment.service.utils.ParseUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BillRepository {
  public static Map<Integer, Bill> bills = new HashMap<>();

  public static String FILE_PATH = "src/main/resources/bill.txt";

  static {
    try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
      lines.forEach(
          line -> {
            String[] values = line.split(":");
            bills.put(
                ParseUtil.parseInt(values[0]),
                new Bill(
                    ParseUtil.parseInt(values[0]),
                    BillType.valueOf(values[1]),
                    ParseUtil.parseInt(values[2]),
                    ParseUtil.parseLocalDate(values[3]),
                    BillState.valueOf(values[4]),
                    values[5]));
          });
    } catch (IOException e) {
      System.out.println("ERROR");
    }
  }
}
