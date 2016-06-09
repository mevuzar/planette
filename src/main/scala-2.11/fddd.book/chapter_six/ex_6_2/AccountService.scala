package fddd.book.chapter_six.ex_6_2

import java.util.Date

import scala.util.Success

import scalaz.concurrent.Task
import scalaz._
import Scalaz._
import scalaz.\/
import \/._
import Kleisli.kleisli
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
    def transfer(from: String, to: String, amount: Amount): AccountOperation[(Account, Account)] = operation[(Account, Account)]{ (repo: AccountRepository) =>
        val debitted = debit(from, amount).run(repo)
        val acreditted = credit(to, amount).run(repo)
        val fullOperation = debitted

        null
    }
  }

object AccountService extends AccountService[Account, Amount, Balance] {
    override def open(no: String,
                      name: String,
                      rate: Option[BigDecimal],
                      openingDate: Option[Date],
                      accountType: AccountType): AccountOperation[Account] = operation[Account]((repo: AccountRepository) =>{

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

    override def debit(no: String, amount: Amount): AccountOperation[Account] = up(no, amount, C)

    override def credit(no: String, amount: Amount): AccountOperation[Account] = up(no, amount, D)

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