angular.module('CallForPaper').factory('AdminDraft', ['resourceRetries', function(resourceRetries) {
	return resourceRetries('api/admin/drafts/:id', null, {
        update: { method:'PUT' , url: 'api/admin/drafts/:id' }
	});
}]);