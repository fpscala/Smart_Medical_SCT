package doobie.domain.user

import protocols.AppProtocol.User

trait UserRepositoryAlgebra[F[_]] {
  def createUser(user: User): F[Int]

  def updateUser(user: User): F[Int]

  def findUserByLogin(login: String): fs2.Stream[F, User]

}
