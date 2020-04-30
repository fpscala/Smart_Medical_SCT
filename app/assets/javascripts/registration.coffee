$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/registration'
    getOrganization: '/getOrganizationName'
    getDepartment: '/get-department-for-organization'

  defaultRegistrationData =
    fio: ''
    passport: ''

  vm = ko.mapping.fromJS
    registration: defaultRegistrationData
    selectedOrganization: []
    organizationList: []
    selectedDepartment: []
    workTypeList: []
    language: Glob.language

  $('#demo1').selectMultiple();
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

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.registration.fio())
      toastr.error("F.I.O ni kiriting!")
      return no
    else if (!vm.registration.passport())
      toastr.error("Passport raqamini kiriting!")
      return no
    else
      data =
        fio: vm.registration.fio()
        passport: vm.registration.passport()
      $.ajax
        url: apiUrl.sendDoc
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
       .fail handleError
       .done (response) ->
        toastr.success(response)

  vm.translate = (fieldName) -> ko.computed () ->
    index = if vm.language() is 'en' then 0 else 1
    vm.labels[fieldName][index]

  vm.labels =
    label: [
      "Hello World!"
      "Salom Dunyo!"
    ]

  ko.applyBindings {vm}