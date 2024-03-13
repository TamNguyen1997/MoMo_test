package momo.payment.service

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
    "PAY"                         | new String[]{"PAY", "1"}
    "SCHEDULE"                    | new String[]{"SCHEDULE", "1", "23/11/2024"}
    "LIST_PAYMENT"                | new String[]{"LIST_PAYMENT"}
    "SEARCH_BILL_BY_PROVIDER"     | new String[]{"SEARCH_BILL_BY_PROVIDER", "VNPT"}
    "NO_ARG"                      | new String[]{}
  }

  void "Create then delete"() {
    when:
    MomoPaymentApplication.main(new String[]{"CREATE", "ELECTRIC", "200000", "13/03/2024", "NOT_PAID", "MoMo"})
    MomoPaymentApplication.main(new String[]{"DELETE", "4"})
    then:
    noExceptionThrown()
  }

}
