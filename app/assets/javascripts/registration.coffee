$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/registration'
    getOrganization: '/getOrganizationName'
    getDepartment: '/get-department-for-organization'
    searchByName: '/search-patient-by-name'
    searchByPassportSn: '/search-patient-by-passport-sn'
    getPatient: '/search-patient-by-department'
    registerPatient: '/register-patients'

  defaultSearchData =
    fullName: ''
    passportSn: ''

  vm = ko.mapping.fromJS
    search: defaultSearchData
    selectedOrganization: ''
    organizationList: []
    selectedDepartment: ''
    workTypeList: []
    patientList: []
    selectedPatients: []
    language: Glob.language

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  getOrganization = ->
    $.get(apiUrl.getOrganization)
    .fail handleError
    .done (response) ->
      vm.organizationList(response)
  getOrganization()

  vm.selectedOrganization.subscribe (name) ->
    if (name isnt undefined )
      $.post(apiUrl.getDepartment, JSON.stringify({name: name}))
       .fail handleError
       .done (response) ->
         vm.workTypeList(response)

  vm.selectedDepartment.subscribe (id) ->
    if(id isnt undefined )
      data =
        organizationName: vm.selectedOrganization()
        departmentId: id
      $.post(apiUrl.getPatient, JSON.stringify(data))
      .fail handleError
      .done (response) ->
        ko.mapping.fromJS(defaultSearchData, {}, vm.patientList())
        for obj in response
          obj.fullName = obj.lastName + " " + obj.firstName + " " + obj.middleName
        vm.patientList(response)
        $('#demo1').selectMultiple('refresh' );

  $('#search-by-name').keyup (event) ->
    if (event.keyCode == 13)
      if !vm.search.fullName()
        toastr.error("Iltimos ko'rsatilgan tartibda kiriting: \n Bemor Familiyasi Ismi Sharifi")
      else
        $.post(apiUrl.searchByName, JSON.stringify(fullName: vm.search.fullName()))
        .fail handleError
        .done (response) ->
          for obj in response
            obj.fullName = obj.lastName + " " + obj.firstName + " " + obj.middleName
          vm.patientList(response)
          $('#demo1').selectMultiple('refresh');

  $('#search-passport-sn').keyup (event) ->
    if (event.keyCode == 13)
      passport = vm.search.passportSn().length
      data =
        if passport == 10
          passportSn: vm.search.passportSn()
        else
          toastr.error("Iltimos ko'rsatilgan tartibda kiriting: \n Bemorning passport seria raqami")
      $.post(apiUrl.searchByPassportSn, JSON.stringify(data))
      .fail handleError
      .done (response) ->
        for obj in response
          obj.fullName = obj.lastName + " " + obj.firstName + " " + obj.middleName
        vm.patientList(response)
        $('#demo1').selectMultiple('refresh')

  vm.onSubmitRegister = ->
    $.post(apiUrl.registerPatient, JSON.stringify(ids: vm.selectedPatients()))
    .fail handleError
    .done ->
      vm.search.fullName('')
      vm.patientList('')
      $('#demo1').selectMultiple('refresh')

  vm.translate = (fieldName) -> ko.computed () ->
    index = if vm.language() is 'en' then 0 else 1
    vm.labels[fieldName][index]

  vm.labels =
    label: [
      "Hello World!"
      "Salom Dunyo!"
    ]

  ko.applyBindings {vm}