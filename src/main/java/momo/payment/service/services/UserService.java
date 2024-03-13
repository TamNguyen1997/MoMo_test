package momo.payment.service.services;

import static momo.payment.service.repository.BillRepository.bills;
import static momo.payment.service.repository.UserRepository.FILE_PATH;
import static momo.payment.service.repository.UserRepository.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import momo.payment.service.enums.BillState;
import momo.payment.service.exception.BillNotFoundException;
import momo.payment.service.exception.InsufficientBalanceException;

public class UserService {
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
    if (bills.get(id) == null) {
      throw new BillNotFoundException();
    }
    if (user.getBalance() < bills.get(id).getAmount()) {
      throw new InsufficientBalanceException();
    }
    bills.get(id).setState(BillState.PAID);
    user.setBalance(user.getBalance() - bills.get(id).getAmount());
  }

  private void printBalance() {
    System.out.printf("Your current balance is: %s", user.getBalance());
  }
}
