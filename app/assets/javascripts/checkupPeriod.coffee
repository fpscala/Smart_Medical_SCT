$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/create'
    getCheckupPeriod: '/get'
    updateCheckupPeriod: '/update/checkupPeriod'

  vm = ko.mapping.fromJS
    numberPerYear: ''
    doctorType: ''
    labType: ''
    workType: ''
    id: 0
    getCheckupPeriodList: []

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.numberPerYear())
      toastr.error("please enter the number per year!")
      return no
    if (!vm.doctorType())
      toastr.error("please enter the doctor type!")
      return no
    if (!vm.labType())
      toastr.error("please enter the laboratory type!")
      return no
    if (!vm.workType())
      toastr.error("please enter the work type!")
      return no
    else
      data =
        numberPerYear: vm.numberPerYear()
        doctorType: vm.doctorType()
        labType: vm.labType()
        workType: vm.workType()
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

  $(document).on 'click', '.addCheckupPeriod', ->
    empty = false
    input = $(this).parents('tr').find('input[type="text"]')
    input.each ->
      if !$(this).val()
        $(this).addClass 'error'
        empty = true
      else
        $(this).removeClass 'error'
    $(this).parents('tr').find('.error').first().focus()
    if !empty
      input.each ->
        $(this).parent('td').html $(this).val()
      $(this).parents('tr').find('.addCheckupPeriod, .editCheckupPeriod').toggle()

  $(document).on 'click', '.addCheckupPeriod', ->
    row = $(this).closest('tr').children('td')
    data =
      id: row[0].innerText
      name: row[1].innerText
    console.log(data)
    $.ajax
      url: apiUrl.updateCheckupPeriod
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)



  ko.applyBindings {vm}