package fddd.book.chapter_six.ex_6_1

trait ExecutionModel {this: RefModel =>
  case class Execution(account: Account, instrument: Instrument, refNo: String, market: Market,
    unitPrice: BigDecimal, quantity: BigDecimal)
}

