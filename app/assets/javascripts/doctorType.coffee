$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    sendDoc: '/add-doctor-type'
    getDoctor: '/getDoctorType'
    deleteDoc: '/deleteDoctorType'
    updateDoc: '/updateDoctorType'
    sendLab: '/addLaboratory'
    getLab: '/getLaboratory'
    deleteLab: '/deleteLaboratory'
    updateLab: '/updateLaboratory'

  vm = ko.mapping.fromJS
    doctorTypeName: ''
    laboratoryName: ''
    getDoctorTypeList: []
    getLaboratoryList: []
    id: 0
    selectedLanguage: Glob.language
    selected:
      id: ''
      name: ''
    selectedDoctor:
      id: ''
      doctorName: ''

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText) or error.status is 200
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
      $.ajax
        url: apiUrl.sendDoc
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)
        $("#add_doctor_type").modal("hide")
        vm.doctorTypeName('')
        getDoctorType()

  getDoctorType = ->
    $.ajax
      url: apiUrl.getDoctor
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.getDoctorTypeList(response)

  getDoctorType()

  vm.deleteDoctorType = ->
    data =
      id: vm.selectedDoctor.id()
    $.ajax
      url: apiUrl.deleteDoc
      type: 'DELETE'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      $('#close_doc_modal').click()
      toastr.success(response)
      getDoctorType()
      $(this).parents('tr').remove()

  vm.updateDoctorType =  ->
    data =
      id: vm.selectedDoctor.id()
      doctorTypeName: vm.selectedDoctor.doctorName()
    $.ajax
      url: apiUrl.updateDoc
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)
      $('#edit_doctor_type').modal("hide")
      getDoctorType()

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
        $("#add_lab_type").modal("hide")
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

  vm.askDelete = (id) -> ->
    vm.selected.id id
    $('#delete').open

  vm.askDeleteDoctor = (id) -> ->
    vm.selectedDoctor.id (id)
    $('#delete').open

  vm.openEditForm = (data) -> ->
    vm.selected.id (data.id)
    vm.selected.name(data.laboratoryName)
    $('#edit_lab_type').open

  vm.openEditFormDoctor = (data) -> ->
    vm.selectedDoctor.id (data.id)
    vm.selectedDoctor.doctorName(data.doctorTypeName)
    $('#edit_doctor_type').open

  vm.deleteLaboratory = ->
    data =
      id: vm.selected.id()
    $.ajax
      url: apiUrl.deleteLab
      type: 'DELETE'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      $('#close_modal').click()
      toastr.success(response)
      getLaboratory()
      $(this).parents('tr').remove()

  vm.updateLaboratory =  ->
    data =
      id: vm.selected.id()
      laboratoryName: vm.selected.name()
    $.ajax
      url: apiUrl.updateLab
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      $('#closeModalLanguage').click()
      toastr.success(response)
      getLaboratory()


  vm.translate = (fieldName) -> ko.computed () ->
    index = if vm.selectedLanguage() is 'en' then 0 else 1
    vm.labels[fieldName][index]

  vm.labels =
    labelTitle: [
      "Doctor type"
      "Mutahasislik"
    ]

  ko.applyBindings {vm}