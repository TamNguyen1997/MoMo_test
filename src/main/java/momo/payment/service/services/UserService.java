package momo.payment.service.services;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import momo.payment.service.exception.BillNotFoundException;
import momo.payment.service.exception.InsufficientBalanceException;
import momo.payment.service.model.Bill;
import momo.payment.service.model.User;

public class UserService {
  private User user;
  private static final String FILE_PATH = "src/main/resources/user.txt";
  
  public UserService() {

    try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
      this.user = new User(Integer.parseInt(lines.findFirst().orElseThrow()));
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
    Files.write(Path.of(FILE_PATH), String.valueOf(this.user.getBalance()).getBytes());
  }
  
  private void pay(int id) {
    Bill bill = BillService.bills.get(id);
    if (bill == null) {
      throw new BillNotFoundException();
    }
    if (this.user.getBalance() < bill.getAmount()) {
      throw new InsufficientBalanceException();
    }
    this.user.setBalance(this.user.getBalance() - bill.getAmount());
  } 
  private void printBalance() {
    System.out.printf("Your current balance is: %s", user.getBalance());
  }
}
 