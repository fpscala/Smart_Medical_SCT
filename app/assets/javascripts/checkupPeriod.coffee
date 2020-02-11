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
    id: 0
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
    if (!vm.doctorTypeId())
      toastr.error("please enter the doctor type!")
      return no
    if (!vm.labTypeId())
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

  $(document).on 'click', '.editCheckupPeriod', ->
    row = $(this).closest('tr').children('td')
    numberPerYear = row[1].innerText
    doctorTypeId = row[2].innerText
    labTypeId = row[3].innerText
    workTypeId = row[4].innerText
    row[1].innerHTML = '<input type="text" class="form-control" value="' + numberPerYear + '">'
    row[2].innerHTML = '<input type="text" class="form-control" value="' + doctorTypeId + '">'
    row[3].innerHTML = '<input type="text" class="form-control" value="' + labTypeId + '">'
    row[4].innerHTML = '<input type="text" class="form-control" value="' + workTypeId + '">'
    $(this).parents('tr').find('.addCheckupPeriod, .editCheckupPeriod').toggle()


  $(document).on 'click', '.addCheckupPeriod', ->
    row = $(this).closest('tr').children('td')
    data =
      id: row[0].innerText
      numberPerYear: row[1].innerText
      doctorTypeId: row[2].innerText
      labTypeId: row[3].innerText
      workTypeId: row[4].innerText
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