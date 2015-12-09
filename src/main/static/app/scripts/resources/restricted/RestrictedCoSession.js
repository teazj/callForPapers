'use strict';

angular.module('CallForPaper').factory('RestrictedCoSession', ['resourceRetries', function(resourceRetries) {
    return resourceRetries('api/restricted/cosessions/:id', null,
        {
            update: {method: 'PUT', url: 'api/restricted/cosessions/:id'},
        });
}]);