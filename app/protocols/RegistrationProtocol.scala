package protocols

import java.util.Date

import play.api.libs.json.{JsValue, Json, OFormat}

object RegistrationProtocol {

  case object GetRegistrationList

  case object GetOrganizationList

  case object GetOrganizationDist

  case class DeleteOrganization(id: Int)

  case class UpdateOrganization(update: Organization)

  case class CreatePatient(data: Patient)

  case class AddOrganization(data: OrganizationReader)

  case class AddLaboratory(data: Laboratory)

  case class AddCheckupPeriod(data: CheckupPeriod)

  case object GetDoctorTypeList

  case object GetDepartmentList

  case object GetPatient

  case object GetCheckupId

  case object GetWorkTypeWithCheckupPeriod

  case object GetRegion

  case class GetTown(id: Int)

  case class GetWorkTypeByOrganizationName(id: String)

  case class GetPatientsByDepartment(id: Int)

  case class DeleteDoctorType(id: Int)

  case class DeleteWorkType(id: Int)

  case class UpdateDoctorType(update: DoctorType)

  case class AddDoctorType(data: DoctorType)

  case class GetPatientsByFullName(params: SearchParams)

  case class GetPatientsByPassportSn(passport: String)

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
                     region: Int,
                     city: Int,
                     address: String,
                     phone: Option[String],
                     cardNumber: String,
                     workTypeId: Option[Int],
                     lastCheckup: Date,
                     photo: Option[String] = None,
                     organizationName: Option[String],
                     specPartJson: Option[JsValue] = None
                    )

  implicit val registrationFormat: OFormat[Patient] = Json.format[Patient]

  case class AddImage(originalFileName: String, content: Array[Byte])

  implicit val ImageFormat: OFormat[AddImage] = Json.format[AddImage]

  case class Organization(id: Option[Int] = None,
                          organizationName: String,
                          phoneNumber: String,
                          address: String,
                          email: String,
                          countWorkers: Option[Int] = None,
                          totalWorkers: Option[Int] = None,
                          workType: Int)

  implicit val organizationFormat: OFormat[Organization] = Json.format[Organization]

  case class OrganizationName(organizationName: String)

  implicit val organizationNameFormat: OFormat[OrganizationName] = Json.format[OrganizationName]

  case class OrganizationData(name: String, address: String,  phone: String, email: String, totalWorkers: Option[Int])
  implicit val organizationDataFormat = Json.format[OrganizationData]

  case class OrganizationReader(id: Option[Int] = None,
                          organizationName: String,
                          phoneNumber: String,
                          address: String,
                          email: String,
                          workType: Array[Int])

  implicit val organizationReaderFormat: OFormat[OrganizationReader] = Json.format[OrganizationReader]

  case class WorkType(id: Option[Int] = None,
                      workType: String)

  implicit val workTypeFormat: OFormat[WorkType] = Json.format[WorkType]

  case class AddDepartmentAndCheckupPeriod(department: String,
                                           checkupForm: Array[CheckupPeriodForm])

  case class Laboratory(id: Option[Int] = None,
                        laboratoryName: String)

  implicit val laboratoryFormat: OFormat[Laboratory] = Json.format[Laboratory]

  case class DoctorType(id: Option[Int] = None,
                        doctorType: String)

  implicit val doctorTypeFormat: OFormat[DoctorType] = Json.format[DoctorType]

  case class CheckupPeriod(id: Option[Int] = None,
                           workTypeId: Int,
                           numberPerYear: Int,
                           doctorType: Option[Int],
                           labTypeId: Option[Int],
                           specPartJson: Option[JsValue] = None
                          )

  implicit val CheckupPeriodFormat: OFormat[CheckupPeriod] = Json.format[CheckupPeriod]

  case class CheckupPeriodForm(id: Option[Int] = None,
                               numberPerYear: String,
                               selectedDoctorType: Array[Int],
                               selectedLabType: Array[Int],
                              )

  implicit val checkupPeriodFormFormat: OFormat[CheckupPeriodForm] = Json.format[CheckupPeriodForm]

  case class Region(id: Option[Int] = None,
                    name: String)

  implicit val regionFormat: OFormat[Region] = Json.format[Region]

  case class Town(id: Option[Int] = None,
                  name: String,
                  regionId: Int)

  implicit val townFormat: OFormat[Town] = Json.format[Town]

  case class SearchParams(lastName: Option[String] = None,
                          firstName: Option[String] = None,
                          secondName: Option[String] = None)

  implicit val searchParamsFormat: OFormat[SearchParams] = Json.format[SearchParams]

}
