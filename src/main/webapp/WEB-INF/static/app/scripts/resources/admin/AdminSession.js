angular.module('CallForPaper').factory('AdminSession', ['resourceRetries', function(resourceRetries) {
  return resourceRetries('api/admin/sessions/:id', null, 
  	{
        update: { method:'PUT' },
        delete: {
			method: 'DELETE',
			url: 'api/admin/sessions/:id'
		},
        getIds: {
			method: 'GET',
			url: 'api/admin/sessions/ordered',
			isArray:true
		}
    });
}]);