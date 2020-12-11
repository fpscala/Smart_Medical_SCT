package protocols

import java.util.Date

import play.api.libs.json.{Json, OFormat}

object AppProtocol {
  case class User(firstname: String,
                  lastname: String,
                  login: String,
                  phone: String,
                  email: Option[String],
                  password: String,
                  createdAt: Date,
                  photo: Option[String] = None,
                  disabled: Boolean = false
                 ) {
    def id: Option[Int] = None
    def apply(createdAt: Date) =  new java.sql.Date(createdAt.getTime)

  }

  case class Role(name: String, description: String, code: String) {
    def id: Option[Int] = None
  }

  implicit val roleFormat: OFormat[Role] = Json.format[Role]
  implicit val userFormat: OFormat[User] = Json.format[User]
}
