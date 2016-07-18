package com.mayo.planette.business_scripts.tech_samples

import com.mayo.planette.abstraction.production.common.model.AccountModel
import AccountModel.{AccountCredentials, UserAccount}
import com.mayo.planette.abstraction.production.server.accounts.dal.interpreters.AccountsRepositoryLoggerInterpreter
import com.mayo.planette.business_scripts.interpreters.AccountsRepositoryInMemInterpreter
import com.mayo.planette.abstraction.production.server.accounts.dal.AccountRepositoryF.AccountDataOperations._
import scalaz.Free

/**
 * @author yoav @since 7/13/16.
 */

object AccountRepositoryFSample extends App {

  val account = UserAccount.generateAccount(AccountCredentials("Moshe", "Buhbut"), "mobut", "mobut@gmail.com").get
  val script /*: Free[Requestable, List[Try[Any]]]*/ = for {
    stored <- store(account)
    query <- query(account.id)
    delete <- delete(account.id)
  } yield List(stored, query, delete)


  val scriptResultRight = Free.runFC(script)(AccountsRepositoryInMemInterpreter)
  scriptResultRight.foreach(r => println(r.right.get))

  val scriptResultLeft = Free.runFC(script)(AccountsRepositoryLoggerInterpreter)
  scriptResultLeft.foreach(l => println(l.left.get))
}