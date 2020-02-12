$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/addLaboratory'
    get: '/getLaboratory'
    delete: '/deleteLaboratory'
    update: '/updateLaboratory'


  vm = ko.mapping.fromJS
    laboratoryName: ''
    getList: []
    id: 0


  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  console.log 'laboratoryName: ', vm.laboratoryName()

  vm.onSubmit = ->
    console.log 'laboratoryName: ', vm.laboratoryName()
    toastr.clear()
    if (!vm.laboratoryName())
      toastr.error("Please enter a name")
      return no
    else
      data =
        laboratoryName: vm.laboratoryName()
      $.ajax
        url: apiUrl.send
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)

  vm.getLaboratory = ->
    $.ajax
      url: apiUrl.get
      type: 'GET'
    .fail handleError
    .done (response) ->
      console.log('1: ', vm.getList().length)
      vm.getList(response)
      console.log('2: ', vm.getList().length)

  vm.deleteLaboratory = ->
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