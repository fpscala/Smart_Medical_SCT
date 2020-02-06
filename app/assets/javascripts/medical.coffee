$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/create'
    get: '/get'

  vm = ko.mapping.fromJS
    zmnomi: ''
    muddat: ''
    mutaxassis: ''
    lftek: ''
    qmonelik: ''
    getList: []

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.zmnomi())
      toastr.error("Zaharli moddalar nomini kiriting!")
      return no
    if (!vm.muddat())
      toastr.error("Muddatni kiriting!")
      return no
    if (!vm.mutaxassis())
      toastr.error("Mutaxassisni kiriting!")
      return no
    if (!vm.lftek())
      toastr.error("Funksianal tekshiruvlari kiriting!")
      return no
    if (!vm.qmonelik())
      toastr.error("Qo`shimcha tibbiy moneliklarni kiriting!")
      return no
    else
      data =
        zmnomi: vm.zmnomi()
        muddat: vm.muddat()
        mutaxassis: vm.mutaxassis()
        lftek: vm.lftek()
        qmonelik: vm.qmonelik()
      $.ajax
        url: apiUrl.send
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)

  vm.getAllNames = ->
    $.ajax
      url: apiUrl.get
      type: 'GET'
    .fail handleError
    .done (response) ->
      console.log('1: ', vm.getList().length)
      vm.getList(response)
      console.log('2: ', vm.getList().length)

  ko.applyBindings {vm}