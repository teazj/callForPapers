angular.module('CallForPaper').factory('Application', function($resource) {
  return $resource('application/:id', null,
  	{
        update: { method:'PUT' },
        getCurrentUser :{
          			url: 'application/currentUser',
          			method: 'GET'
          		}
    });
});
