'use strict';

angular.module('CallForPaper').factory('RestrictedStats', ['resourceRetries', function(resourceRetries) {
    return resourceRetries('api/restricted/stats', null,
        {
            meter: {
                url: 'api/restricted/stats/meter',
                method: 'GET',
                isArray: false
            },
        });
}]);
