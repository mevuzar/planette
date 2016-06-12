package fddd.book.chapter_six.ex_6_2

import java.util.Date

import scalaz.Kleisli.kleisli
import scalaz.Scalaz._
import scalaz.concurrent.Task
import scalaz.{Order => OrderZ, _}

/**
 * Created by Owner on 6/9/2016.
 */

trait AccountService[Account, Amount, Balance] {


  def operation[A](op: AccountRepository => Valid[A]) = kleisli[Valid, AccountRepository, A](op)

  def open(no: String, name: String, rate: Option[BigDecimal],
           openingDate: Option[Date], accountType: AccountType):
  AccountOperation[Account]

  def close(no: String, closeDate: Option[Date]): AccountOperation[Account]

  def debit(no: String, amount: Amount): AccountOperation[Account]

  def credit(no: String, amount: Amount): AccountOperation[Account]

  def balance(no: String): AccountOperation[Balance]

  def transfer(from: String, to: String, amount: Amount): AccountOperation[(Account, Account)] = operation[(Account, Account)] { (repo: AccountRepository) =>
    //val r = Task.gatherUnordered(Seq(debit(from, amount).run(repo), credit(to, amount).run(repo)))
    //r.run.foldLeft(List.empty[\/[NonEmptyList[String],Account]])((a, b) => b.m)
    //        val result = for{
    //            debitted <- debit(from, amount).run(repo)
    //            acreditted <- credit(to, amount).run(repo)
    //        }yield debitted ++ acreditted

    val a = debit(from, amount).run(repo)
    val b = credit(to, amount).run(repo)
    val c = a map {
      case \/-(acc1: Account) =>
        val res1 = b map {
          case \/-(acc2: Account) => val res11 = (acc1, acc2).right[NonEmptyList[String]]
            res11

          case anyOther =>
            val res22 = anyOther.toEither.left.getOrElse(NonEmptyList[String]("Failure2")).left[(Account, Account)]
            res22
        }
        res1
      case failurReason =>
        val res2 = failurReason.toEither.left.getOrElse(NonEmptyList[String]("Failure2")).left[(Account, Account)]
        val res3 = Task.now(res2)
        res3

    }

    c.flatMap(identity)
  }
}

trait AccountServiceInterpreter extends AccountService[Account, Amount, Balance] {
  override def open(no: String,
                    name: String,
                    rate: Option[BigDecimal],
                    openingDate: Option[Date],
                    accountType: AccountType): AccountOperation[Account] = operation[Account]((repo: AccountRepository) => {

    Task {
      repo.query(no) match {
        case \/-(Some(a)) => NonEmptyList(s"Already existing account with no $no").left[Account]
        case \/-(None) => accountType match {
          case Checking => Account.checkingAccount(no, name, openingDate, None, Balance()).flatMap(repo.store)
          case Savings => rate map { r =>
            Account.savingsAccount(no, name, r, openingDate, None, Balance()).flatMap(repo.store)
          } getOrElse {
            NonEmptyList(s"Rate needs to be given for savings account").left[Account]
          }
        }
        case a@ -\/(_) => a
      }

    }
  })


  override def balance(no: String): AccountOperation[Balance] = operation[Balance]((repo: AccountRepository) => Task(repo.balance(no)))

  override def close(no: String, closeDate: Option[Date]): AccountOperation[Account] = operation[Account]((repo: AccountRepository) => {
    Task {
      repo.query(no) match {
        case \/-(None) => NonEmptyList(s"Account $no does not exist").left[Account]
        case \/-(Some(a)) =>
          val cd = closeDate.getOrElse(today)
          Account.close(a, cd).flatMap(repo.store)
        case a@ -\/(_) => a
      }
    }
  })

  override def debit(no: String, amount: Amount): AccountOperation[Account] = up(no, amount, D)

  override def credit(no: String, amount: Amount): AccountOperation[Account] = up(no, amount, C)

  private trait DC

  private case object D extends DC

  private case object C extends DC

  private def up(no: String, amount: Amount, dc: DC): AccountOperation[Account] = operation[Account] { (repo: AccountRepository) =>
    Task {
      repo.query(no) match {
        case \/-(None) => NonEmptyList(s"Account $no does not exist").left[Account]
        case \/-(Some(a)) => dc match {
          case D => Account.updateBalance(a, -amount).flatMap(repo.store)
          case C => Account.updateBalance(a, amount).flatMap(repo.store)
        }
        case a@ -\/(_) => a
      }
    }
  }


}