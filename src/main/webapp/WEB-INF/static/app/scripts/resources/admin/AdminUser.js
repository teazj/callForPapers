angular.module('CallForPaper').factory('AdminUser', ['$resource', function($resource) {
	return $resource('/api/adminUser/:id', null, {
		getCurrentUser: {
			url: '/api/adminUser/currentUser',
			method: 'GET'
		},
		getLoginUrl: {
			url: '/api/adminUser/login',
			method: 'POST'
		},

		getLogoutUrl: {
			url: '/api/adminUser/logout',
			method: 'POST'
		}
	});
}]);