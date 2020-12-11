package doobie.repository.user

import cats.effect.Bracket
import doobie.domain.user.UserRepositoryAlgebra
import protocols.AppProtocol.User
import doobie.implicits._
import doobie.util.transactor._

abstract class CommonUserRepositoryInterpreter[F[_] : Bracket[*[_], Throwable]](val xa: Transactor[F])
  extends UserRepositoryAlgebra[F] {

  val commonSql: CommonUserSql

  override def createUser(user: User): F[Int] = {
    commonSql.createUser(user).run.transact(xa)
  }

  override def updateUser(user: User): F[Int] = {
    commonSql.updateUser(user).run.transact(xa)
  }

  override def findUserByLogin(login: String): fs2.Stream[F, User] = {
    commonSql.findUserByLogin(login).stream.transact(xa)
  }

}
