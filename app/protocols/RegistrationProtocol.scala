package protocols

import java.util.Date

import play.api.libs.json.{JsValue, Json, OFormat}

object RegistrationProtocol {

  case object GetRegistrationList

  case class AddRegistration(data: Patient)

  case class Patient(id: Option[Int] = None,
                          firsName: String,
                          middleName: String,
                          lastName: String,
                          passport_sn: String,
                          gender: String,
                          birthday: String,
                          address: String,
                          phone: Option[String],
                          cardNumber: String,
                          profession: Option[String],
                          workTypeId: Option[Int],
                          lastCheckup: Option[Date],
                          photo: Option[String],
                          organizationId: Option[Int])

  implicit val registrationFormat: OFormat[Patient] = Json.format[Patient]

  case class Organization(id: Option[Int] = None,
                          organizationName: String)

  implicit val organizationFormat: OFormat[Organization] = Json.format[Organization]

  case class WorkType(id: Option[Int] = None,
                      workType: String)

  implicit val workTypeFormat: OFormat[WorkType] = Json.format[WorkType]

  case class Laboratory(id: Option[Int] = None,
                        laboratoryType: String)

  implicit val laboratoryFormat: OFormat[Laboratory] = Json.format[Laboratory]

  case class CheckupType(id: Option[Int] = None,
                         checkupType: String)

  implicit val checkupTypeFormat: OFormat[CheckupType] = Json.format[CheckupType]

  case class DoctorType(id: Option[Int] = None,
                        doctorType: String)

  implicit val doctorTypeFormat: OFormat[DoctorType] = Json.format[DoctorType]

  case class CheckupPeriod(id: Option[Int] = None,
                            numberPerYear: Int,
                            doctorTypeId: JsValue,
                            labTypeId: JsValue,
                            workTypeId: Int
                           )

  implicit val CheckupPeriodFormat: OFormat[CheckupPeriod] = Json.format[CheckupPeriod]

}
