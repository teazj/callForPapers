'use strict';

angular.module('CallForPaper').factory('Tracks', ['resourceRetries', function(resourceRetries) {
    return resourceRetries('/api/restricted/talk/tracks', null,
        {
            findAll: {
                url: '/api/restricted/talk/tracks',
                method: 'GET',
                isArray: true
            }
        });
}]);
