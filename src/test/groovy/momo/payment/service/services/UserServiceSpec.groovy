package momo.payment.service.services

import momo.payment.service.exception.InsufficientBalanceException
import momo.payment.service.model.Bill
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class UserServiceSpec extends Specification {
  private final UserService userService = new UserService()
  private int initBalance

  void setup() {
    try (Stream<String> lines = Files.lines(Paths.get(UserService.FILE_PATH))) {
      initBalance = Integer.parseInt(lines.findFirst().orElseThrow())
    } catch (IOException ignored) {
      assert false
    }
  }

  def "Cash in - success"() {
    given:
    assert userService.user.getBalance() == initBalance
    int cashInAmount = 100000
    when:
    userService.cashIn(cashInAmount)
    then:
    assert userService.user.balance == initBalance + cashInAmount
  }

  def "Write balance to file - success"() {
    when:
    userService.save()
    then:
    assert userService.user.balance == getSavedBalance()
  }

  def "Pay 1 bill - success"() {
    given:
    int billId = 1
    Bill billToPay = BillService.bills.get(billId)
    assert billToPay != null

    and:
    int balance = userService.user.getBalance()

    when:
    userService.pay(List.of(billId))

    then:
    assert userService.user.balance == balance - billToPay.amount
  }

  def "Pay 1 bill - insufficient balance"() {
    given:
    int billId = 1
    Bill billToPay = BillService.bills.get(billId)
    assert billToPay != null

    and:
    userService.user.setBalance(0)

    when:
    userService.pay(List.of(billId))

    then:
    thrown InsufficientBalanceException
  }

  def "Pay 2 bills - success"() {
    given:
    int billId_1 = 1
    int billId_2 = 2
    Bill billToPay_1 = BillService.bills.get(billId_1)
    Bill billToPay_2 = BillService.bills.get(billId_2)
    assert billToPay_1 != null
    assert billToPay_2 != null

    and:
    int balance = billToPay_1.amount + billToPay_2.amount
    userService.user.setBalance(balance)

    when:
    userService.pay(List.of(billId_1, billId_2))

    then:
    assert userService.user.balance == balance - billToPay_1.amount - billToPay_2.amount
  }

  def "Pay 2 bills - insufficient balance at second second bill"() {
    given:
    int billId_1 = 1
    int billId_2 = 2
    Bill billToPay_1 = BillService.bills.get(billId_1)
    Bill billToPay_2 = BillService.bills.get(billId_2)
    assert billToPay_1 != null
    assert billToPay_2 != null

    and:
    int balance = billToPay_1.amount
    userService.user.setBalance(balance)

    when:
    userService.pay(List.of(billId_1, billId_2))

    then:
    thrown InsufficientBalanceException
  }

  private static Integer getSavedBalance() {
    try (Stream<String> lines = Files.lines(Paths.get(UserService.FILE_PATH))) {
      return Integer.parseInt(lines.findFirst().orElseThrow())
    } catch (IOException ignored) {
      assert false
    }
    return null
  }
}
