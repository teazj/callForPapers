angular.module('CallForPaper').factory('RestrictedDraft', ['resourceRetries', function(resourceRetries) {
	return resourceRetries('api/restricted/drafts/:id', null, {
        update: { method:'PUT' , url: 'api/restricted/drafts/:id' },
		delete: {
			method: 'DELETE',
			url: 'api/restricted/drafts/:id'
		}
	});
}]);