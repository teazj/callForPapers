angular.module('CallForPaper').factory('RestrictedSession', ['resourceRetries', function(resourceRetries) {
    return resourceRetries('api/restricted/sessions/:id', null,
        {
            update: {method: 'PUT', url: 'api/restricted/sessions/:id'},
        });
}]);
