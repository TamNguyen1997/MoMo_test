package momo.payment.service;

import static momo.payment.service.repository.BillRepository.bills;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import momo.payment.service.enums.BillState;
import momo.payment.service.enums.BillType;
import momo.payment.service.exception.InvalidArgumentsException;
import momo.payment.service.services.BillService;
import momo.payment.service.services.UserService;
import momo.payment.service.utils.ParseUtil;

public class MomoPaymentApplication {

  private static final BillService billService = new BillService();
  private static final UserService userService = new UserService();

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      return;
    }
    int length = args.length;
    String command = args[0];

    switch (command) {
      case "CASH_IN":
        validateArgs(length, 2);
        userService.cashIn(ParseUtil.parseInt(args[1]));
        break;
      case "LIST_BILL":
        billService.printAll(bills);
        break;
      case "PAY":
        if (length < 2) {
          throw new InvalidArgumentsException();
        }
        userService.pay(
            Arrays.stream(Arrays.copyOfRange(args, 1, args.length))
                .map(ParseUtil::parseInt)
                .collect(Collectors.toList()));
        break;
      case "SCHEDULE":
        validateArgs(length, 3);
        billService.schedule(ParseUtil.parseInt(args[1]), args[2]);
        break;
      case "LIST_PAYMENT":
        billService.printPayment();
        break;
      case "SEARCH_BILL_BY_PROVIDER":
        validateArgs(length, 2);
        billService.searchByProvider(args[1]);
        break;
      case "DELETE":
        validateArgs(length, 2);
        billService.delete(ParseUtil.parseInt(args[1]));
        break;
      case "DUE_DATE":
        billService.dueDate();
        break;
      case "CREATE":
        validateArgs(length, 6);
        billService.create(
            BillType.valueOf(args[1]),
            ParseUtil.parseInt(args[2]),
            ParseUtil.parseLocalDate(args[3]),
            BillState.valueOf(args[4]),
            args[5]);
        break;
      case "EXIT":
        System.out.println("Good bye!");
        break;
      default:
        System.out.println("Unknown command");
    }

    billService.save();
    userService.save();
  }

  private static void validateArgs(int argsLength, int requiredLength) {
    if (argsLength != requiredLength) {
      throw new InvalidArgumentsException();
    }
  }
}
