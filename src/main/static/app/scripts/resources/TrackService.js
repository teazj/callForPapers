'use strict';

angular.module('CallForPaper').service('TalkService', ['resourceRetries', function(resourceRetries) {

    var tracks = resourceRetries('/api/restricted/talk/tracks', null,
        {
            findAll: {
                url: '/api/restricted/talk/tracks',
                method: 'GET',
                isArray: true
            },
        });

    var formats = resourceRetries('/api/restricted/talk/formats', null,
        {
            findAll: {
                url: '/api/restricted/talk/formats',
                method: 'GET',
                isArray: true
            },
        });

    return {
        tracks: tracks,
        formats: formats
    };
}]);
