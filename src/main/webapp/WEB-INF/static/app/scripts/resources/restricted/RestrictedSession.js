angular.module('CallForPaper').factory('RestrictedSession', ['$resource', function($resource) {
  return $resource('api/restricted/session/:id', null, 
  	{
        update: { method:'PUT' },
    });
}]);