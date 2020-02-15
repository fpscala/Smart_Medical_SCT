package protocols

import java.util.Date

import play.api.libs.json.{JsValue, Json, OFormat}

object RegistrationProtocol {

  case object GetRegistrationList

  case object GetOrganizationList

  case class DeleteOrganization(id: Int)

  case class UpdateOrganization(update: Organization)

  case class CreatePatient(data: Patient)

  case class AddOrganization(data: Organization)

  case class AddLaboratory(data: Laboratory)

  case class AddCheckupPeriod(data: CheckupPeriod)

  case object GetDoctorTypeList

  case object GetPatient

  case class DeleteDoctorType(id: Int)

  case class UpdateDoctorType(update: DoctorType)

  case class AddDoctorType(data: DoctorType)

  case object GetLaboratoryList

  case class DeleteLaboratory(id: Int)

  case class UpdateLaboratory(update: Laboratory)

  case class DeletePatient(id: Option[Int])

  implicit val deleteFormat: OFormat[DeletePatient] = Json.format[DeletePatient]

  case class Patient(id: Option[Int] = None,
                     firstName: String,
                     middleName: String,
                     lastName: String,
                     passport_sn: Option[String],
                     gender: Int,
                     birthday: Date,
                     address: String,
                     phone: Option[String],
                     cardNumber: String,
                     profession: Option[String],
                     workTypeId: Option[Int],
                     lastCheckup: Date,
                     photo: Option[String] = None,
                     organizationId: Option[Int])

  implicit val registrationFormat: OFormat[Patient] = Json.format[Patient]

  case class AddImage(originalFileName: String, content: Array[Byte])

  implicit val ImageFormat: OFormat[AddImage] = Json.format[AddImage]

  case class Organization(id: Option[Int] = None,
                          organizationName: String)

  implicit val organizationFormat: OFormat[Organization] = Json.format[Organization]

  case class WorkType(id: Option[Int] = None,
                      workType: String)

  implicit val workTypeFormat: OFormat[WorkType] = Json.format[WorkType]

  case class Laboratory(id: Option[Int] = None,
                        laboratoryName: String)

  implicit val laboratoryFormat: OFormat[Laboratory] = Json.format[Laboratory]

  case class CheckupType(id: Option[Int] = None,
                         checkupType: String)

  implicit val checkupTypeFormat: OFormat[CheckupType] = Json.format[CheckupType]

  case class DoctorType(id: Option[Int] = None,
                        doctorType: String)

  implicit val doctorTypeFormat: OFormat[DoctorType] = Json.format[DoctorType]

  case class CheckupPeriod(id: Option[Int] = None,
                           numberPerYear: Int,
                           doctorType: JsValue,
                           labType: JsValue,
                           workType: String
                          )

  implicit val CheckupPeriodFormat: OFormat[CheckupPeriod] = Json.format[CheckupPeriod]

}
