$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/addCheckupPeriod'
    getCheckupPeriod: '/get'
    updateCheckupPeriod: '/update/checkupPeriod'

  vm = ko.mapping.fromJS
    numberPerYear: 0
    doctorTypeId: []
    labTypeId: []
    workTypeId: 0
    getCheckupPeriodList: []

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.addCheckupPeriod = ->
    toastr.clear()
    if (!vm.numberPerYear())
      toastr.error("please enter the number per year!")
      return no
    if (vm.doctorTypeId().length is 0)
      toastr.error("please enter the doctor type!")
      return no
    if (vm.labTypeId().length is 0)
      toastr.error("please enter the laboratory type!")
      return no
    if (!vm.workTypeId())
      toastr.error("please enter the work type!")
      return no
    else
      data =
        numberPerYear: vm.numberPerYear()
        doctorTypeId: vm.doctorTypeId()
        labTypeId: vm.labTypeId()
        workTypeId: vm.workTypeId()
      $.ajax
        url: apiUrl.send
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)
        getCheckupPeriod()

  getCheckupPeriod = ->
    $.ajax
      url: apiUrl.getCheckupPeriod
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.getCheckupPeriodList(response)
  getCheckupPeriod()


  ko.applyBindings {vm}