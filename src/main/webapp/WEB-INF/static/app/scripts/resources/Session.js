angular.module('CallForPaper').factory('Session', function($resource) {
  return $resource('session/:id', null, 
  	{
        update: { method:'PUT' }
    });
});