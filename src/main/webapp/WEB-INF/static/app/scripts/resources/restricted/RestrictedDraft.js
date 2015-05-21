angular.module('CallForPaper').factory('RestrictedDraft', ['$resource', function($resource) {
	return $resource('api/restricted/draft/:id', null, {
        update: { method:'PUT' , url: 'api/restricted/draft/:id' },
		delete: {
			method: 'DELETE',
			url: 'api/restricted/draft/:id'
		}
	});
}]);