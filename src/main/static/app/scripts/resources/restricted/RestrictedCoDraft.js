'use strict';

angular.module('CallForPaper').factory('RestrictedCoDraft', ['resourceRetries', function(resourceRetries) {
    return resourceRetries('api/restricted/codrafts/:id', null, {
        update: {method: 'PUT', url: 'api/restricted/codrafts/:id'}
    });
}]);
