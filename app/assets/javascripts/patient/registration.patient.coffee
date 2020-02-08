$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/send-patient'

  defaultPatientData =
    firsName: ''
    middleName: ''
    lastName: ''
    passport_sn: ''
    gender: ''
    birthday: ''
    address: ''
    phone: ''
    cardNumber: ''
    profession: ''
    workTypeId: 0
    lastCheckup: ''
    photo: ''
    organizationId: 0

  vm = ko.mapping.fromJS
    patient: defaultPatientData

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.patient.firsName())
      toastr.error("Ko`rikdan o`tuvchi shaxslar nomini kiriting!")
    else if (!vm.patient.middleName())
      toastr.error("Ko`rikdan o`tuvchi shaxslar nomini kiriting!")
    else if (!vm.patient.lastName())
      toastr.error("Ko`rikdan o`tuvchi shaxslar nomini kiriting!")
    else if (!vm.patient.birthday())
      toastr.error("Ko`rikdan o`tuvchi shaxslar nomini kiriting!")
    else if (!vm.patient.address())
      toastr.error("Ko`rikdan o`tuvchi shaxslar nomini kiriting!")
    else if (!vm.patient.gender())
      toastr.error("Ko`rikdan o`tuvchi shaxslar nomini kiriting!")
    else
      data = ko.mapping.toJS(vm.patient)
      $.post(apiUrl.send, JSON.stringify(data))
      .fail handleError
      .done (response) ->
        toastr.success(response)

  ko.applyBindings {vm}