angular.module('CallForPaper').factory('Application', ['$resource', function($resource) {
	return $resource('application/:id', null, {
	});
}]);