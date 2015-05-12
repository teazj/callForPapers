angular.module('CallForPaper').factory('User', ['$resource', function($resource) {
	return $resource('user/:id', null, {
		getCurrentUser: {
			url: 'user/currentUser',
			method: 'GET'
		},
		getLoginUrl: {
			url: 'user/login',
			method: 'POST'
		},

		getLogoutUrl: {
			url: 'user/logout',
			method: 'POST'
		}
	});
}]);