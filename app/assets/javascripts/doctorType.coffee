$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/addDoctorType'
    get: '/getDoctorType'
    delete: '/deleteDoctorType'
    update: '/updateDoctorType'

  vm = ko.mapping.fromJS
    doctorTypeName: ''
    getList: []
    id: 0

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.doctorTypeName())
      toastr.error("Please enter a login")
      return no
    else
      data =
        doctorTypeName: vm.doctorTypeName()
      $.ajax
        url: apiUrl.send
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)

  vm.getDoctorType = ->
    $.ajax
      url: apiUrl.get
      type: 'GET'
    .fail handleError
    .done (response) ->
      console.log('1: ', vm.getList().length)
      vm.getList(response)
      console.log('2: ', vm.getList().length)

  vm.deleteDoctorType = ->
    data =
      id: parseInt(vm.id())
    $.ajax
      url: apiUrl.delete
      type: 'DELETE'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)

  vm.updateDoctorType = ->
    data =
      id: parseInt(vm.id())
      doctorTypeNmae: vm.doctorTypeName()
    $.ajax
      url: apiUrl.update
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)

  ko.applyBindings {vm}