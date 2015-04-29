angular.module('CallForPaper').factory('Rate', function($resource) {
  return $resource('/rate/:id', null, 
  	{
        update: { method:'PUT' }
    });
});