package doobie.common

import cats.effect._
import doobie.Transactor
import doobie.config._
import doobie.repository.user.UserRepositoryInterpreter
import play.api.Configuration

class DoobieModule[F[_] : Effect : ContextShift](configuration: Configuration) {
  val transactor: Transactor[F] = DatabaseConfig.dbTransactor(configuration)
  val parsonRepo = new UserRepositoryInterpreter[F](transactor)
}
