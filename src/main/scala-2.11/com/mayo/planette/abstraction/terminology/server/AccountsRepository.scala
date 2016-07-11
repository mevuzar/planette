package com.mayo.planette.abstraction.terminology.server

import scala.util.Try
import scalaz.Free

/**
 * @author yoav @since 7/7/16.
 */

trait AccountsRepository[Account, AccountId] {
  type AccountRepo[A] = Free[AccountRepoF,A]
  import scalaz.{Free, Functor}
  import Free._

  type RepositoryAccountId = AccountId
  type RepositoryAccount = Account

  sealed trait AccountRepoF[+A]

  case class Query[+A](id: RepositoryAccountId, onResult: Try[RepositoryAccount] => A) extends AccountRepoF[A]

  case class Store[+A](account: RepositoryAccount, next: A) extends AccountRepoF[A]

  case class Delete[+A](id: RepositoryAccountId, next: A) extends AccountRepoF[A]


  object AccountRepoF {
    implicit val functor: Functor[AccountRepoF] = new Functor[AccountRepoF] {
      def map[A, B](action: AccountRepoF[A])(f: A => B): AccountRepoF[B] = action match {
        case Store(account, next) => Store(account, f(next))
        case Query(no, onResult) => Query(no, onResult andThen f)
        case Delete(no, next) => Delete(no, f(next))
      }
    }
  }

    def store(account: RepositoryAccount): AccountRepo[Try[Unit]] =
      liftF(Store(account, Try{()}))

    def query(id: RepositoryAccountId, f: Try[RepositoryAccount] => Try[RepositoryAccount] = identity): AccountRepo[Try[RepositoryAccount]] =
      liftF(Query(id, f))

    def delete(id: RepositoryAccountId): AccountRepo[Unit] =
      liftF(Delete(id, ()))

    def update(id: RepositoryAccountId, f: RepositoryAccount => RepositoryAccount): AccountRepo[(Try[RepositoryAccount], Try[Unit])] = for {
      tryQuery <- query(id)
      tryStore <- store(f(tryQuery.get))
    } yield (tryQuery, tryStore)
  
  }


