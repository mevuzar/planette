package fddd.book
package chapter_one

import scala.util.{Failure, Success, Try}

/**
 * Created by yoav on 1/13/16.
 */

trait AccountService {
  def debit(a: Account, amount: Amount): Try[Account] = {
    if (a.balance.amount < amount)
      Failure(new Exception("Insufficiant funds in account"))
    else
      Success(a.copy(balance = Balance(a.balance.amount - amount)))
  }

  def credit(a: Account, amount: Amount): Try[Account] = Success(a.copy(balance = Balance(a.balance.amount + amount)))
}

object AccountService extends AccountService
