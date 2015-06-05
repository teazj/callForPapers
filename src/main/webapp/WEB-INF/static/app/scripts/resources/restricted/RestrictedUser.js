angular.module('CallForPaper').factory('RestrictedUser', ['$resource', function($resource) {
	return $resource('api/restricted/user/:id', null, {
        query: { method:'GET' , isArray: false}
	});
}]);