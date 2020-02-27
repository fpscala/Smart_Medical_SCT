$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/addCheckupPeriod'
    getWorkType: '/get-workType'
    updateCheckupPeriod: '/update/checkupPeriod'

  defaultForm =
    numberPerYear: ''
    selectedDoctorType: []
    selectedLabType: []

  defaultButton =
    buttonMinus: ''

  vm = ko.mapping.fromJS
    labTypeList: []
    workType: ''
    workTypeList: []
    getCheckupPeriodList: []
    getWorkTypeList: []
    language: Glob.language
    formA: []
    formB: []

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.formA.push ko.mapping.fromJS(defaultForm)
  vm.labTypeList([{id: 1, labType: 'lab1'}, {id: 2, labType: 'lab2'}, {id: 3, labType: 'lab3'}])
  vm.getCheckupPeriodList([{id: 1, doctorType: 'doctor1'}, {id: 2, doctorType: 'doctor2'}, {id: 3, doctorType: 'doctor3'}])

  vm.addForm = -> ->
    if(ko.mapping.toJS(vm.formA()).length < 4)
      vm.formA.push ko.mapping.fromJS(defaultForm)
    if(ko.mapping.toJS(vm.formB()).length is 0)
      vm.formB.push ko.mapping.fromJS(defaultButton)
    vm.labTypeList([{id: 1, labType: 'lab1'}, {id: 2, labType: 'lab2'}, {id: 3, labType: 'lab3'}])
    vm.getCheckupPeriodList([{id: 1, doctorType: 'doctor1'}, {id: 2, doctorType: 'doctor2'}, {id: 3, doctorType: 'doctor3'}])
    console.log('formA', ko.mapping.toJS(vm.formA()))

  vm.addCheckupPeriod = ->
    toastr.clear()
    if (!vm.workType())
      toastr.error("please enter the work type!")
      return no
    else if ko.mapping.toJS(vm.formA()).length is 0
      toastr.error("please click button plus")
      return no
    else
      for form in ko.mapping.toJS(vm.formA())
        if (!form.numberPerYear)
          toastr.error("please enter the number per year!")
          return no
        else if (form.selectedDoctorType.length is 0)
          toastr.error("please enter the doctor type!")
          return no
        else if (form.selectedLabType.length is 0)
          toastr.error("please enter the laboratory type!")
          return no

      data =
        workType: vm.workType()
        form: ko.mapping.toJS(vm.formA())
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
      url: apiUrl.getWorkType
      type: 'GET'
    .fail handleError
    .done (response) ->
      for k,v of response
        vm.workTypeList.push(k)
      vm.getWorkTypeList(response)

  getCheckupPeriod()

  vm.translate = (fieldName) -> ko.computed () ->
    index = if vm.language() is 'en' then 0 else 1
    vm.labels[fieldName][index]

  vm.labels =
    label: [
      "Hello World!"
      "Salom Dunyo!"
    ]

  ko.applyBindings {vm}