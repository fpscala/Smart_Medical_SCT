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

  case object GetCheckupId

  case object GetWorkTypeWithCheckupPeriod

  case class DeleteDoctorType(id: Int)

  case class UpdateDoctorType(update: DoctorType)

  case class AddDoctorType(data: DoctorType)

  case class AddWorkType(data: WorkType)

  case object GetLaboratoryList

  case class DeleteLaboratory(id: Int)

  case class UpdateLaboratory(update: Laboratory)

  case class DeletePatient(id: Option[Int])

  implicit val deleteFormat: OFormat[DeletePatient] = Json.format[DeletePatient]

  case class FindWorkTypeIdByWorkType(id: String)

  implicit val FindWorkTypeIdByWorkTypeFormat: OFormat[FindWorkTypeIdByWorkType] = Json.format[FindWorkTypeIdByWorkType]

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
                          organizationName: String,
                          phoneNumber: String,
                          address: String,
                          email: String,
                          workType: JsValue)

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
                           numberPerYear: Int
                          )

  implicit val CheckupPeriodFormat: OFormat[CheckupPeriod] = Json.format[CheckupPeriod]

  case class CheckupPeriodForm(id: Option[Int] = None,
                               numberPerYear: String,
                               selectedDoctorType: Array[Int],
                               selectedLabType: Array[Int],
                              )

  implicit val checkupPeriodFormFormat: OFormat[CheckupPeriodForm] = Json.format[CheckupPeriodForm]

  case class AddIds(table: TmpTable)

  case class TmpTable(workTypeId: Int,
                       checkupId: Int,
                       doctorType: Int,
                       labTypeId: Int
                      )

  implicit val tmpTableFormFormat: OFormat[TmpTable] = Json.format[TmpTable]


  case class Region(id: Int, name: String)

  implicit val regionFormat: OFormat[Region] = Json.format[Region]

  val regionList = Seq(
    Region(1, "Andijon viloyati"),
    Region(2, "Buxoro viloyati"),
    Region(3, "Fargʻona viloyati"),
    Region(4, "Jizzax viloyati"),
    Region(5, "Xorazm viloyati"),
    Region(6, "Namangan viloyati"),
    Region(7, "Navoiy viloyati"),
    Region(8, "Qashqadaryo viloyati"),
    Region(9, "Samarqand viloyati"),
    Region(10, "Sirdaryo viloyati"),
    Region(11, "Surxondaryo viloyati"),
    Region(12, "Toshkent viloyati"),
    Region(13, "Qoraqalpogʻiston Respublikasi")
  )

}
