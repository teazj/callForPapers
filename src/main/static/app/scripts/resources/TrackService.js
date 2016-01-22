'use strict';

angular.module('CallForPaper').service('TalkService', ['resourceRetries', function(resourceRetries) {

    var tracks = resourceRetries('/api/settings/talk/tracks', null,
        {
            findAll: {
                url: '/api/settings/talk/tracks',
                method: 'GET',
                isArray: true
            },
        });

    var formats = resourceRetries('/api/settings/talk/formats', null,
        {
            findAll: {
                url: '/api/settings/talk/formats',
                method: 'GET',
                isArray: true
            },
        });

    return {
        tracks: tracks,
        formats: formats
    };
}]);
