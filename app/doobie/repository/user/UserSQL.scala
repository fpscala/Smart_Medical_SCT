package doobie.repository.user

import java.util.Date

import doobie.Update0
import doobie.implicits._
import protocols.AppProtocol.User

object UserSQL extends CommonUserSql {
  private def dateToTimestamp(date: Date): Date = {
    new java.sql.Date((date).getTime)
  }

  override def createUser(user: User): Update0 = {
    sql"""
     |insert into users (first_name, last_name, login, phone, password, email, photo, created_at, disabled) values (
     |${user.firstname}, ${user.lastname}, ${user.login}, ${user.phone}, ${user.password},
     |${user.email}, ${user.photo}, ${user.createdAt}, ${user.disabled})"""
      .update
  }

  override def updateUser(user: User): Update0 = {
    sql""" UPDATE users SET first_name=${user.firstname},
                            last_name=${user.lastname},
                            email=${user.email},
                            login=${user.login},
                            phone=${user.phone},
                            password=${user.password},
                            created_at=${user.createdAt},
                            photo=${user.photo},
                            disabled=${user.disabled}
                            WHERE login=${user.login}
                            """.update
  }

  override def findUserByLogin(login: String): doobie.Query0[User] = {
    sql"""Select * From user WHERE login = $login""".query[User]
  }

}
