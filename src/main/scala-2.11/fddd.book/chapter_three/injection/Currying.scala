package fddd.book.chapter_three.injection

/**
 * @author yoav @since 5/30/16.
 */

import java.util.Date

import fddd.book.chapter_three.injection.common._

import scala.collection.mutable.{Map => MMap}
import scala.util.{Failure, Success, Try}


trait AccountService[Account, Amount, Balance] {
  def open(no: String, name: String, openingDate: Option[Date]): AccountRepository => Try[Account]

  def close(no: String, closeDate: Option[Date]): AccountRepository => Try[Account]

  def debit(no: String, amount: Amount): AccountRepository => Try[Account]

  def credit(no: String, amount: Amount): AccountRepository => Try[Account]

  def balance(no: String): AccountRepository => Try[Balance]
}


object AccountService extends AccountService[Account, Amount, Balance] {

  def open(no: String, name: String, openingDate: Option[Date]) = (repo: AccountRepository) =>
    repo.query(no) match {
      case Success(Some(a)) => Failure(new Exception(s"Already existing account with no $no"))
      case Success(None) =>
        if (no.isEmpty || name.isEmpty) Failure(new Exception(s"Account no or name cannot be blank"))
        else if (openingDate.getOrElse(today) before today) Failure(new Exception(s"Cannot open account in the past"))
        else repo.store(Account(no, name, openingDate.getOrElse(today)))
      case Failure(ex) => Failure(new Exception(s"Failed to open account $no: $name", ex))
    }

  def close(no: String, closeDate: Option[Date]) = (repo: AccountRepository) =>
    repo.query(no) match {
      case Success(Some(a)) =>
        if (closeDate.getOrElse(today) before a.dateOfOpening)
          Failure(new Exception(s"Close date $closeDate cannot be before opening date ${a.dateOfOpening}"))
        else repo.store(a.copy(dateOfClosing = closeDate))
      case Success(None) => Failure(new Exception(s"Account not found with $no"))
      case Failure(ex) => Failure(new Exception(s"Fail in closing account $no", ex))
    }

  def debit(no: String, amount: Amount) =
    (repo: AccountRepository) =>
      repo.query(no) match {
        case Success(Some(a)) =>
          if (a.balance.amount < amount) Failure(new Exception("Insufficient balance"))
          else repo.store(a.copy(balance = Balance(a.balance.amount - amount)))
        case Success(None) => Failure(new Exception(s"Account not found with $no"))
        case Failure(ex) => Failure(new Exception(s"Fail in debit from $no amount $amount", ex))
      }

  def credit(no: String, amount: Amount) =
    (repo: AccountRepository) =>
      repo.query(no) match {
        case Success(Some(a)) => repo.store(a.copy(balance = Balance(a.balance.amount + amount)))
        case Success(None) => Failure(new Exception(s"Account not found with $no"))
        case Failure(ex) => Failure(new Exception(s"Fail in credit to $no amount $amount", ex))
      }

  def balance(no: String) = {
    (repo: AccountRepository) => repo.balance(no)
  }

}

import scala.language.higherKinds

trait Monad[F[_]] extends Functor[F] {
  def unit[A](a: => A): F[A]

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] =
    join(map(fa)(f))

  def map[A, B](ma: F[A])(f: A => B): F[B] =
    flatMap(ma)(a => unit(f(a)))

  def map2[A, B, C](ma: F[A], mb: F[B])(f: (A, B) => C): F[C] = {
    flatMap(ma)(a => map(mb)(b => f(a, b)))
  }

  def sequence[A](lma: List[F[A]]): F[List[A]] =
    lma.foldRight(unit(List[A]())) { (ma, mla) => println(s"from monad $ma"); map2(ma, mla)(_ :: _) }

  def traverse[A, B](la: List[A])(f: A => F[B]): F[List[B]] =
    la.foldRight(unit(List[B]()))((a, mlb) => map2(f(a), mlb)(_ :: _))

  def join[A](mma: F[F[A]]): F[A] = flatMap(mma)(ma => ma)
}

object Monad {
  def apply[F[_] : Monad]: Monad[F] =
    implicitly[Monad[F]]

  implicit val optionMonad = new Monad[Option] {
    def unit[A](a: => A) = Some(a)

    override def flatMap[A, B](ma: Option[A])(f: A => Option[B]) = ma flatMap f
  }

  implicit val listMonad = new Monad[List] {
    def unit[A](a: => A) = List(a)

    override def flatMap[A, B](ma: List[A])(f: A => List[B]) = ma flatMap f
  }

  implicit def function1Monad[A1]: Monad[({type f[x] = Function1[A1, x]})#f] = new Monad[({type f[x] = Function1[A1, x]})#f] {
    def unit[A](a: => A) = (_: A1) => a

    override def flatMap[A, B](r: A1 => A)(f: A => A1 => B) = (t: A1) => {
      val rt = r(t)
      val frt = f(rt)
      val frtt = frt(t)
      f(r(t))(t)
    }
  }
}

trait Functor[F[_]] {
  def map[A, B](a: F[A])(f: A => B): F[B]
}

object Functor {
  def apply[F[_] : Functor]: Functor[F] =
    implicitly[Functor[F]]

  implicit def ListFunctor: Functor[List] = new Functor[List] {
    def map[A, B](a: List[A])(f: A => B): List[B] = a map f
  }

  implicit def OptionFunctor: Functor[Option] = new Functor[Option] {
    def map[A, B](a: Option[A])(f: A => B): Option[B] = a map f
  }

  implicit def Tuple2Functor[A1]: Functor[({type f[x] = (A1, x)})#f] = new Functor[({type f[x] = (A1, x)})#f] {
    def map[A, B](a: (A1, A))(f: A => B): (A1, B) = (a._1, f(a._2))
  }

  implicit def Function1Functor[A1]: Functor[({type f[x] = Function1[A1, x]})#f] = new Functor[({type f[x] = Function1[A1, x]})#f] {
    def map[A, B](fa: A1 => A)(f: A => B) = fa andThen f
  }

}

object Currying extends App {

  import AccountService._

  implicit class Function1MonadSyntax[A1, A](a: Function1[A1, A]) {
    def unit[A](a: => A) = Monad[({type f[x] = Function1[A1, x]})#f].unit(a)

    def flatMap[B](f: A => A1 => B) = {
      val monada = Monad[({type f[x] = Function1[A1, x]})#f]
      monada.flatMap(a)(f)
    }
  }

  implicit class Function1FunctorSyntax[A1, A](a: Function1[A1, A]) {
    def map[B](f: A => B) = Functor[({type f[x] = Function1[A1, x]})#f].map(a)(f)
  }

  def op(no: String) = for {
    _ <- credit(no, BigDecimal(100))
    _ <- credit(no, BigDecimal(300))
    _ <- debit(no, BigDecimal(160))
    b <- balance(no)
  } yield b

  AccountRepositoryInMemory.store(Account("dada", "dudu"))
  val opped = op("dada")(AccountRepositoryInMemory)
  println(opped)
}



