package momo.payment.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import momo.payment.service.services.BillService;
import momo.payment.service.services.UserService;

public class MomoPaymentService {

  private static final BillService billService = new BillService();
  private static final UserService userService = new UserService();

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      return;
    }

    String command = args[0];

    switch (command) {
      case "CASH_IN":
        userService.cashIn(Integer.parseInt(args[1]));
        break;
      case "LIST_BILL":
        billService.printAll(BillService.bills);
        break;
      case "PAY":
        userService.pay(
                Arrays.stream(Arrays.copyOfRange(args, 1, args.length))
                        .map(Integer::parseInt).collect(Collectors.toList()));
        break;
      case "SCHEDULE":
        billService.schedule(Integer.parseInt(args[1]), args[2]);
        break;
      case "LIST_PAYMENT":
        billService.printPayment();
        break;
      case "SEARCH_BILL_BY_PROVIDER":
        billService.searchByProvider(args[1]);
        break;
      default:
        System.out.println("Unknown command");
    }

    billService.save();
    userService.save();
  }

}
