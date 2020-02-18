$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/addCheckupPeriod'
    getCheckupPeriod: '/get'
    updateCheckupPeriod: '/update/checkupPeriod'

  vm = ko.mapping.fromJS
    numberPerYear: ''
    selectedDoctorType: []
    selectedLabType: []
    labTypeList: [{id: 1, labType: 'lab1'}, {id: 2, labType: 'lab2'}, {id: 3, labType: 'lab3'}]
    workType: ''
    getCheckupPeriodList: [{id: 1, doctorType: 'doctor1'}, {id: 2, doctorType: 'doctor2'}, {id: 3, doctorType: 'doctor3'}]
    language: Glob.language


  form = (x) ->
    fields = document.getElementById('fields')
    return fields.outerHTML

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')


  vm.addCheckupPeriod = ->
    toastr.clear()
    if (!vm.workType())
      toastr.error("please enter the work type!")
      return no
    else if (!vm.numberPerYear())
      toastr.error("please enter the number per year!")
      return no
    else if (vm.selectedDoctorType().length is 0)
      toastr.error("please enter the doctor type!")
      return no
    else if (vm.selectedLabType().length is 0)
      toastr.error("please enter the laboratory type!")
      return no
    else
      data =
        workType: vm.workType()
        numberPerYear: vm.numberPerYear()
        doctorType: vm.selectedDoctorType()
        labType: vm.selectedLabType()
      $.ajax
        url: apiUrl.send
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)
#        getCheckupPeriod()

  getCheckupPeriod = ->
    $.ajax
      url: apiUrl.getCheckupPeriod
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.getCheckupPeriodList(response)

  $(document).ready ->
    max_fields = 4
    wrapper = $('.container1')
    add_button = $('.add_form_field')
    x = 1
    $(add_button).click (e) ->
      e.preventDefault()
      if x < max_fields
        x++
        $(wrapper).after form(x)
        document.getElementById('fields').nextSibling.setAttribute('id', x)
      else
        alert 'You Reached the limits'
    $(wrapper).on 'click', '.delete', (e) ->
      e.preventDefault()
      $(this).parent('div').remove()
      x--

  vm.translate = (fieldName) -> ko.computed () ->
    index = if vm.language() is 'en' then 0 else 1
    vm.labels[fieldName][index]

  vm.labels =
    label: [
      "Hello World!"
      "Salom Dunyo!"
    ]

  ko.applyBindings {vm}