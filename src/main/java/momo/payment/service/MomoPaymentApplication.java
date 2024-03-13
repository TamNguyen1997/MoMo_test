package momo.payment.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;
import momo.payment.service.enums.BillState;
import momo.payment.service.enums.BillType;
import momo.payment.service.exception.InvalidArgumentsException;
import momo.payment.service.exception.ParseIntException;
import momo.payment.service.exception.ParseLocalDateException;
import momo.payment.service.services.BillService;
import momo.payment.service.services.UserService;

public class MomoPaymentApplication {

  private static final BillService billService = new BillService();
  private static final UserService userService = new UserService();

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      return;
    }

    String command = args[0];

    switch (command) {
      case "CASH_IN":
        userService.cashIn(parseInt(args[1]));
        break;
      case "LIST_BILL":
        billService.printAll(BillService.bills);
        break;
      case "PAY":
        if (args.length != 2) {
          throw new InvalidArgumentsException();
        }
        userService.pay(
            Arrays.stream(Arrays.copyOfRange(args, 1, args.length))
                .map(MomoPaymentApplication::parseInt)
                .collect(Collectors.toList()));
        break;
      case "SCHEDULE":
        billService.schedule(parseInt(args[1]), args[2]);
        break;
      case "LIST_PAYMENT":
        billService.printPayment();
        break;
      case "SEARCH_BILL_BY_PROVIDER":
        billService.searchByProvider(args[1]);
        break;
      case "DELETE":
        billService.delete(parseInt(args[1]));
        break;
      case "CREATE":
        billService.create(
            BillType.valueOf(args[1]),
            parseInt(args[2]),
            parseLocalDate(args[3]),
            BillState.valueOf(args[4]),
            args[5]);
        break;
      default:
        System.out.println("Unknown command");
    }

    billService.save();
    userService.save();
  }

  private static int parseInt(String string) {
    try {
      return Integer.parseInt(string);
    } catch (Exception e) {
      throw new ParseIntException();
    }
  }

  private static LocalDate parseLocalDate(String string) {
    try {
      return LocalDate.parse(string, BillService.DATE_TIME_FORMATTER);
    } catch (Exception e) {
      throw new ParseLocalDateException();
    }
  }
}
