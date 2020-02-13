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

  $(document).ready ->
    max_fields = 4
    wrapper = $('.container1')
    add_button = $('.add_form_field')
    x = 1
    $(add_button).click (e) ->
      e.preventDefault()
      if x < max_fields
        x++
        $(wrapper).append '<div class="col-md-4">
                                <div class="form-group">
                                    <label for="numberPerYear" >Number per year: </label>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <span class="input-group-text">1 yilda</span>
                                        </div>
                                        <input type="text" class="form-control text-center">
                                        <div class="input-group-append">
                                            <span class="input-group-text">marta</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group one-line-dropdown">
                                    <label for="doctorType">Doctor Type</label>
                                    <select multiple name="doctorTypeId" class="custom-select selectpicker form-control" data-bind="options: vm.getCheckupPeriodList(), optionsText: \'nu\',
                optionsValue: \'id\', value: vm.doctorTypeId, optionsCaption: \'doctorTypeId\' " id="doctorType" data-show-subtext="true">
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group one-line-dropdown">
                                    <label for="labType">Laboratory Type</label>
                                    <select multiple name="labTypeId" class="custom-select selectpicker form-control" data-bind="options: vm.getCheckupPeriodList(), optionsText: \'name\',
                optionsValue: \'id\'  value: vm.labTypeId, optionsCaption: \'labTypeId\' " id="labType" data-show-subtext="true">
                                    </select>
                                </div>
                            </div>'
      else
        alert 'You Reached the limits'
      return
  $(wrapper).on 'click', '.delete', (e) ->
    e.preventDefault()
    $(this).parent('div').remove()
    x--
    return
  return

  ko.applyBindings {vm}