package doobie.repository.user

import cats.effect.Bracket
import doobie._
import doobie.domain.user.UserRepositoryAlgebra

class UserRepositoryInterpreter[F[_] : Bracket[*[_], Throwable]](override val xa: Transactor[F])
  extends CommonUserRepositoryInterpreter[F](xa) with UserRepositoryAlgebra[F] {
  override val commonSql: CommonUserSql = UserSQL
}

object UserRepositoryInterpreter {
  def apply[F[_] : Bracket[*[_], Throwable]](xa: Transactor[F]): UserRepositoryInterpreter[F]
  = new UserRepositoryInterpreter(xa)
}
