package protocols

import java.time.Period

import play.api.libs.json.{Json, OFormat}

object RegistrationProtocol {

  case object GetRegistrationList

  case class AddRegistration(data: Registration)

  case class Registration(id: Option[Int] = None,
                          firsName: String,
                          surName: String,
                          lastName: String,
                          phone: String,
                          createdDate: String,
                          period: Period)

  implicit val registrationFormat: OFormat[Registration] = Json.format[Registration]


}
