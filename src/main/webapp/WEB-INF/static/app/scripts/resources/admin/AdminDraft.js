angular.module('CallForPaper').factory('AdminDraft', ['resourceRetries', function(resourceRetries) {
	return resourceRetries('api/admin/draft/:id', null, {
        update: { method:'PUT' , url: 'api/admin/draft/:id' }
	});
}]);