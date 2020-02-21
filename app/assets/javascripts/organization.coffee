$ ->
  my.initAjax()

  Glob = window.Glob || {}

  apiUrl =
    send: '/addOrganization'
    get: '/getOrganization'
    delete: '/deleteOrganization'
    update: '/updateOrganization'

  vm = ko.mapping.fromJS
    organizationName: ''
    user: defaultUserdata
    organizations: []
    roleList: []
    id: 0
    after: ''
    language: Glob.language
    selected:
      id: ''


  defaultUserdata =
    selectedRoles: []

  handleError = (error) ->
    if error.status is 500 or (error.status is 400 and error.responseText)
      toastr.error(error.responseText)
    else
      toastr.error('Something went wrong! Please try again.')

  vm.onSubmit = ->
    toastr.clear()
    if (!vm.organizationName())
      toastr.error("Please enter a Organization Name")
      return no
    else
      data =
        organizationName: vm.organizationName()
      $.ajax
        url: apiUrl.send
        type: 'POST'
        data: JSON.stringify(data)
        dataType: 'json'
        contentType: 'application/json'
      .fail handleError
      .done (response) ->
        toastr.success(response)
        $("#closeModalLanguage").click()
        getOrganization()

  getOrganization = ->
    $.ajax
      url: apiUrl.get
      type: 'GET'
    .fail handleError
    .done (response) ->
      vm.organizations(response)

  getOrganization()

  vm.askDelete = (id) -> ->
    vm.selected.id id
    $('#delete').open

  vm.deleteOrg = ->
    data =
      id: vm.selected.id()
    $.ajax
      url: apiUrl.delete
      type: 'DELETE'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      $('#close_delete_modal').click()
      toastr.success(response)
      getOrganization()
#    $(this).parents('tr').remove()

  vm.updateOrganization = ->
    data =
      id: parseInt(vm.id())
      organizationName: vm.organizationName()
    $.ajax
      url: apiUrl.update
      type: 'POST'
      data: JSON.stringify(data)
      dataType: 'json'
      contentType: 'application/json'
    .fail handleError
    .done (response) ->
      toastr.success(response)

  $(document).on 'click', '.clickOnRow', ->
    row = $(this).closest('tr')
    if row.next('tr').hasClass('hide')
      row.next('tr').show().removeClass('hide').addClass('show')
    else
      row.next('tr').hide().removeClass('show').addClass('hide')


  vm.translate = (fieldName) -> ko.computed () ->
    index = if vm.language() is 'en' then 0 else 1
    vm.labels[fieldName][index]

  vm.labels =
    label: [
      "Hello World!"
      "Salom Dunyo!"
    ]

  ko.applyBindings {vm}
