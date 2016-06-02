package fddd.book.chapter_three.injection

import java.util.Date

import scala.collection.mutable.{Map => MMap}
import scala.util.{Try, Success}

/**
 * @author yoav @since 5/30/16.
 */
object AccountRepositoryInMemory extends AccountRepositoryInMemory

trait AccountRepositoryInMemory extends AccountRepository {
  lazy val repo = MMap.empty[String, Account]

  def query(no: String): Try[Option[Account]] = Success(repo.get(no))

  def store(a: Account): Try[Account] = {
    val r = repo += ((a.no, a))
    Success(a)
  }

  def query(openedOn: Date): Try[Seq[Account]] = Success(repo.values.filter(_.dateOfOpening == openedOn).toSeq)
}