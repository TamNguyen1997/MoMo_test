package momo.payment.service.services;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import momo.payment.service.enums.BillState;
import momo.payment.service.exception.BillNotFoundException;
import momo.payment.service.exception.InsufficientBalanceException;
import momo.payment.service.model.User;

public class UserService {
  public static final String FILE_PATH = "src/main/resources/user.txt";
  static final User user = new User();

  static  {
    try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
      user.setBalance(Integer.parseInt(lines.findFirst().orElseThrow()));
    } catch (IOException e) {
      System.out.println("ERROR");
    }
  }

  public void cashIn(int balance) {
    user.setBalance(user.getBalance() + balance);
    printBalance();
  }

  public void pay(List<Integer> billIds) {
    billIds.forEach(this::pay);
    printBalance();
  }

  public void save() throws IOException {
    Files.write(Path.of(FILE_PATH), String.valueOf(user.getBalance()).getBytes());
  }
  
  private void pay(int id) {
    if (BillService.bills.get(id) == null) {
      throw new BillNotFoundException();
    }
    if (user.getBalance() < BillService.bills.get(id).getAmount()) {
      throw new InsufficientBalanceException();
    }
    BillService.bills.get(id).setState(BillState.PAID);
    user.setBalance(user.getBalance() - BillService.bills.get(id).getAmount());
  }

  private void printBalance() {
    System.out.printf("Your current balance is: %s", user.getBalance());
  }
}
 