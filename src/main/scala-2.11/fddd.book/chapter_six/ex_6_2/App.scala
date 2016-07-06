package fddd.book.chapter_six.ex_6_2
/**
 * Created by Owner on 6/9/2016.
 */
object App extends App with AccountServiceInterpreter{
  val openAccount1: Valid[Account] = open("12345", "yoav", Some(23), None, Checking).run(AccountRepositoryInMemory)
  val credit1 = credit("12345", 44).run(AccountRepositoryInMemory)
  val openAccount2: Valid[Account] = open("123456", "yoav", Some(23), None, Checking).run(AccountRepositoryInMemory)
  val transfer1To2 = transfer("12345", "123456", 20).run(AccountRepositoryInMemory)

  val op = for{
    acc1 <- openAccount1
    acc2 <- openAccount2
    acc3 <- credit1
    trans <- transfer1To2
  }yield (trans)

  val result = op.run
  println(result)
  //val accountEither: NonEmptyList[String] \/ Account = openAccount.run
  //val account = (openAccount.run | None).right
  //scalaz.concurrent.Task.reduceUnordered()
  //println(account)
}
