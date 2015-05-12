angular.module('CallForPaper').factory('Session', ['$resource', function($resource) {
  return $resource('session/:id', null, 
  	{
        update: { method:'PUT' },
        save: { method:'POST', url: 'postSession/:id' }
    });
}]);