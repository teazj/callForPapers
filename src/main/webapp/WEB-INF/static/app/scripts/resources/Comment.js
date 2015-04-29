angular.module('CallForPaper').factory('Comment', function($resource) {
  return $resource('comment/:id',{},
  	{
  		getAll :{
  			method: 'GET',
  			isArray: true
  		},
  		getByRowId :{
  			url: 'comment/row/:rowId',
  			method: 'GET',
  			isArray: true
  		},
  		update: { method:'PUT' }
  	});
});