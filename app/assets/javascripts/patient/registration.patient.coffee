$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    get: '/get-patient'
    delete: '/delete-patient'

  defaultPatientData =
    firstName: ''
    middleName: ''
    lastName: ''
    passport_sn: ''
    gender: ''
    birthday: ''
    address: ''
    checkupType: ''
    phone: ''
    isWorker: ''
    cardNumber: ''
    profession: ''
    workTypeId: 0
    lastCheckup: ''
    photo: ''
    organizationId: 0

  vm = ko.mapping.fromJS
    patient: defaultPatientData
    enableSubmitButton: yes
    patientList: []


  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.patient.isWorker.subscribe (boolean) ->
    vm.patient.isWorker(boolean)

  $contentFile = $('input[name=attachedFile]')
  $contentFile.change ->
    filePath = $(this).val()
    fileName = filePath.replace(/^.*[\\\/]/, '')

    reAllowedTypes = /^.+((\.jpg)|(\.jpeg)|(\.png))$/i
    if !reAllowedTypes.test(fileName)
      alert('Only PNG or JPG files are allowed.')
      return false

    $('#file-name').html fileName
    vm.enableSubmitButton yes

  formData = null
  $fileUploadForm = $('#patient-form')
  $fileUploadForm.fileupload
    dataType: 'text'
    autoUpload: no
    singleFileUploads: false
    replaceFileInput: true
    multipart: true
    add: (e, data) ->
      formData = data
    fail: (e, data) ->
      handleError(data.jqXHR)
      vm.enableSubmitButton(yes)
    done: (e, data) ->
      result = data.result
      if result is 'OK'
        toastr.success('Form has been successfully submitted!')
        ko.mapping.fromJS(defaultPatientData, {}, vm.patient)
      else
        vm.enableSubmitButton(yes)
        toastr.error(result or 'Something went wrong! Please try again.')


  vm.onSubmit = ->
    toastr.clear()
    if (!vm.patient.firstName())
      toastr.error("Ko'rikdan o'tuvchi shaxs nomini kiriting!")
      return no
    else if (!vm.patient.middleName())
      toastr.error("Ko'rikdan o'tuvchi shaxs sharfini kiriting!")
      return no
    else if (!vm.patient.lastName())
      toastr.error("Ko'rikdan o'tuvchi shaxs familiyasini kiriting!")
      return no
    else if (!vm.patient.birthday())
      toastr.error("Ko'rikdan o'tuvchi shaxs tug'ilgan sanasini tanlang!")
      return no
    else if (!vm.patient.address())
      toastr.error("Ko'rikdan o'tuvchi shaxs mamzilini kiriting!")
      return no
    else if (!vm.patient.cardNumber())
      toastr.error("Ko'rikdan o'tuvchi shaxs karta raqamini kiriting!")
      return no
    else if (!vm.patient.gender())
      toastr.error("Ko'rikdan o'tuvchi shaxs jinsini tanlang!")
      return no
    else if (!vm.patient.checkupType())
      toastr.error("Ko'rik maqsadini tanlang!")
      return no
    else if (!vm.patient.isWorker())
      toastr.error("Tashkilot ishchisimi yoki yo'q!")
      return no
    else if (!vm.patient.organizationId() && vm.patient.isWorker() is 'yes')
      toastr.error("Ko'rikdan o'tuvchi shaxs tashkilotini tanlang")
      return no
    else if (!vm.patient.workTypeId() && vm.patient.isWorker() is 'yes')
      toastr.error("Ko'rikdan o'tuvchi shaxs ish turini tanlang!")
      return no
    else if (!vm.patient.profession() && vm.patient.isWorker() is 'yes')
      toastr.error("Ko'rikdan o'tuvchi shaxs kasbini tanlang!")
      return no
    if formData
      vm.enableSubmitButton(no)
      formData.submit()
    else
      $fileUploadForm.fileupload('send', {files: ''})
      return no

  getPatient = ->
    $.get(apiUrl.get)
      .fail(handleError)
      .done (response) ->
        for res in response
          res.firstName = res.lastName + ' ' + res.firstname + ' '+ res.middleName
        vm.patientList(response)
  getPatient()

  $(document).on 'click', '#delete_patient', ->
    row = $(this).closest('tr').children('td')
    data = id: row[0].innerText
    $.ajax
      url: apiUrl.delete
      type: 'DELETE'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      $('#dropdown').click()
      getPatient()
      toastr.success(response)
    $(this).parents('tr').remove()

  vm.convertIntToDate = (intDate)->
    moment(+intDate).format('MMM DD, YYYY')

  vm.convertStrToDate = (strDate) ->
    if strDate
      moment(+strDate).format('MMM DD, YYYY')


  ko.applyBindings {vm}