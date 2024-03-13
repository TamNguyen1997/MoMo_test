package momo.payment.service.services

import momo.payment.service.enums.BillState
import momo.payment.service.enums.BillType
import momo.payment.service.exception.BillNotFoundException
import momo.payment.service.model.Bill
import momo.payment.service.utils.ParseUtil
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

import static momo.payment.service.repository.BillRepository.FILE_PATH
import static momo.payment.service.repository.BillRepository.bills

class BillServiceSpec extends Specification {
  private final PrintStream standardOut = System.out
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream()
  private final BillService billService = new BillService()

  void setup() {
    System.setOut(new PrintStream(outputStreamCaptor))
  }

  def "Print all"() {
    when:
    billService.printAll(bills)
    then:
    assert outputStreamCaptor.toString().length() > "Bill No.     Type      Amount     Due Date     State     PROVIDER".length()
  }

  def "Print payment - No payment is found"() {
    given:
    bills.each { key, value -> value.state = BillState.NOT_PAID }
    when:
    billService.printPayment()

    then:
    assert outputStreamCaptor.toString().contains("No bill has been paid")
  }

  def "Print payment"() {
    given:
    bills.get(2).state = BillState.PAID

    when:
    billService.printPayment()
    then:
    assert !outputStreamCaptor.toString().contains(BillState.NOT_PAID.toString())
    assert outputStreamCaptor.toString().contains(bills.get(2).provider)
    assert outputStreamCaptor.toString().contains(bills.get(2).amount.toString())
    assert outputStreamCaptor.toString().contains(bills.get(2).id.toString())
    assert outputStreamCaptor.toString().length() > "Bill No.     Type      Amount     Due Date     State     PROVIDER".length()
  }

  def "Search for provider - No bill is found"() {
    given:
    String searchString = "Random String"

    when:
    billService.searchByProvider(searchString)

    then:
    assert outputStreamCaptor.toString().contains(("No bill"))
  }

  def "Search for provider"() {
    given:
    String searchString = bills.get(1).provider

    when:
    billService.searchByProvider(searchString)

    then:
    assert !outputStreamCaptor.toString().contains("No bill")
    assert outputStreamCaptor.toString().length() > "Bill No.     Type      Amount     Due Date     State     PROVIDER".length()
  }

  def "Schedule bill"() {
    given:
    String date = "13/03/2024"
    int id = 1

    when:
    billService.schedule(id, date)

    then:
    assert bills.get(id).dueDate.format(ParseUtil.DATE_TIME_FORMATTER) == date
  }

  def "Schedule bill - No bill found with given ID"() {
    given:
    String date = "13/03/2024"
    int id = 10000

    when:
    billService.schedule(id, date)

    then:
    thrown BillNotFoundException
  }

  def "Save to file"() {
    given:
    String newProvider = "MoMo"
    int id = 1
    bills.get(id).provider = newProvider

    when:
    billService.save()

    then:
    Map<Integer, Bill> bills = new HashMap<>()
    try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
      lines.forEach(
          line -> {
            String[] values = line.split(":")
            bills.put(
                ParseUtil.parseInt(values[0]),
                new Bill(
                    ParseUtil.parseInt(values[0]),
                    BillType.valueOf(values[1]),
                    ParseUtil.parseInt(values[2]),
                    ParseUtil.parseLocalDate(values[3]),
                    BillState.valueOf(values[4]),
                    values[5]))
          })
    } catch (IOException ignored) {
      assert false
    }

    assert bills.get(id).provider == newProvider
  }

  void cleanup() {
    System.setOut(standardOut)
  }

}
