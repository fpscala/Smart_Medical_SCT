$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/add-doctor-type'
    get: '/getDoctorType'
    delete: '/deleteDoctorType'
    update: '/updateDoctorType'
    sendLab: '/addLaboratory'
    getLab: '/getLaboratory'
    deleteLab: '/deleteLaboratory'
    updateLab: '/updateLaboratory'

  vm = ko.mapping.fromJS
    doctorTypeName: ''
    laboratoryName: ''
    getList: []
    getLaboratoryList: []
    id: 0

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.doctorTypeName())
      toastr.error("Please enter a Doctor type name")
      return no
    else
      data =
        doctorType: vm.doctorTypeName()
      console.log(data)
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

  vm.createLab = ->
    toastr.clear()
    if (!vm.laboratoryName())
      toastr.error("Please enter a name")
      return no
    else
      data =
        laboratoryName: vm.laboratoryName()
      $.ajax
        url: apiUrl.sendLab
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)
        $("#add_lab_type").modal("hide");
        vm.laboratoryName('')
        getLaboratory()

  getLaboratory = ->
    $.ajax
      url: apiUrl.getLab
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.getLaboratoryList(response)

  getLaboratory()

  vm.deleteLaboratory = (id) -> ->
    data =
      id: id
    $('#delete').open
    $(document).on 'click','#ask_delete', ->
      $.ajax
        url: apiUrl.deleteLab
        type: 'DELETE'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        console.log(data)
        $('#close_modal').click()
        toastr.success(response)
        getLaboratory()
        $(this).parents('tr').remove()

  vm.updateLaboratory = ->
    data =
      id: parseInt(vm.id())
      laboratoryName: vm.laboratoryName()
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