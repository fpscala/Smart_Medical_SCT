$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/create'
    getRegistration: '/get'

  vm = ko.mapping.fromJS
    shaxslar: ''
    muddat: ''
    mutaxassis: ''
    tekshirishlar: ''
    monelik: ''
    id: 0
    getRegistrationList: []

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.shaxslar())
      toastr.error("Ko`rikdan o`tuvchi shaxslar nomini kiriting!")
      return no
    if (!vm.muddat())
      toastr.error("Muddatni kiriting!")
      return no
    if (!vm.mutaxassis())
      toastr.error("Mutaxassisni kiriting!")
      return no
    if (!vm.tekshirishlar())
      toastr.error("Funksianal tekshiruvlari kiriting!")
      return no
    if (!vm.monelik())
      toastr.error("Qo`shimcha tibbiy moneliklarni kiriting!")
      return no
    else
      data =
        shaxslar: vm.shaxslar()
        muddat: vm.muddat()
        mutaxassis: vm.mutaxassis()
        tekshirishlar: vm.tekshirishlar()
        monelik: vm.monelik()
      $.ajax
        url: apiUrl.send
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)
        getRegistration()

  getRegistration = ->
    $.ajax
      url: apiUrl.getRegistration
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.getRegistrationList(response)
  getRegistration()

  $(document).on 'click', '.addDirection', ->
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
      $(this).parents('tr').find('.addDirection, .editDirection').toggle()

  $(document).on 'click', '.addDirection', ->
    row = $(this).closest('tr').children('td')
    data =
      id: row[0].innerText
      name: row[1].innerText
    console.log(data)
    $.ajax
      url: apiUrl.updateDirection
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)



  ko.applyBindings {vm}