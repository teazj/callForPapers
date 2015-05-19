angular.module('CallForPaper').factory('AdminSession', ['$resource', function($resource) {
  return $resource('api/admin/session/:id', null, 
  	{
        update: { method:'PUT' },
    });
}]);