package momo.payment.service

import momo.payment.service.exception.InvalidArgumentsException
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class MomoPaymentApplicationSpec extends Specification {

  void "#command"() {
    when:
    MomoPaymentApplication.main(args)
    then:
    noExceptionThrown()

    where:
    command                       | args
    "Unknown"                     | new String[]{"Unknown"}
    "CASH_IN"                     | new String[]{"CASH_IN", "100000"}
    "LIST_BILL"                   | new String[]{"LIST_BILL"}
    "PAY"                         | new String[]{"PAY", "0"}
    "PAY"                         | new String[]{"PAY", "0", "0"}
    "SCHEDULE"                    | new String[]{"SCHEDULE", "1", "23/11/2024"}
    "LIST_PAYMENT"                | new String[]{"LIST_PAYMENT"}
    "SEARCH_BILL_BY_PROVIDER"     | new String[]{"SEARCH_BILL_BY_PROVIDER", "VNPT"}
    "NO_ARG"                      | new String[]{}
  }

  void "#command - invalid arguments"() {
    when:
    MomoPaymentApplication.main(args)
    then:
    thrown InvalidArgumentsException

    where:
    command                       | args
    "CASH_IN"                     | new String[]{"CASH_IN"}
    "CASH_IN"                     | new String[]{"CASH_IN", "100000", "1"}
    "PAY"                         | new String[]{"PAY"}
    "SCHEDULE"                    | new String[]{"SCHEDULE", "1"}
    "SCHEDULE"                    | new String[]{"SCHEDULE", "1", "1", "1"}
    "SEARCH_BILL_BY_PROVIDER"     | new String[]{"SEARCH_BILL_BY_PROVIDER"}
  }

  void "Create then delete"() {
    when:
    MomoPaymentApplication.main(new String[]{"CREATE", "ELECTRIC", "200000", "13/03/2024", "NOT_PAID", "MoMo"})
    MomoPaymentApplication.main(new String[]{"DELETE", "4"})
    then:
    noExceptionThrown()
  }

}
