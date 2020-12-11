package doobie.repository.user

import doobie.Update0
import protocols.AppProtocol.User

trait CommonUserSql {
  def createUser(user: User): Update0

  def updateUser(user: User): Update0

  def findUserByLogin(login: String): doobie.Query0[User]

}
