angular.module('CallForPaper').factory('AdminComment', ['$resource', function($resource) {
  return $resource('api/admin/comments/:id',{},
  	{
  		getAll :{
  			method: 'GET',
  			isArray: true
  		},
  		getByRowId :{
  			url: 'api/admin/comments/row/:rowId',
  			method: 'GET',
  			isArray: true
  		},
  		update: { method:'PUT' }
  	});
}]);