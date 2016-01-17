'use strict';

angular.module('CallForPaper').controller('AdminSessionsCtrl', function($scope, translateFilter, NgTableParams, $q, format, talkformats, tracks, sessions, stats) {

    $scope.talkFormats = talkformats;
    $scope.format = format;

    $scope.stats = stats;

    $scope.sessions = sessions;

    $scope.difficulties = [{id: '', title: ''}].concat(_.map(_.range(1, 4), function(difficulty) {
        return {
            id: difficulty,
            title: translateFilter('talk.difficulty.' + difficulty)
        };
    }));

    $scope.tracks = [{id: '', title: ''}].concat(_.map(tracks, function(track) {
        return {
            id: track.libelle,
            title: track.libelle
        };
    }));

    $scope.tableParams = new NgTableParams({
        count: 10,
        sorting: {added: 'desc'}
    }, {
        data: sessions
    });

    /**
     * Filter tables according to checkbox state
     * @return {void}
     */
    $scope.handleNotReviewed = function() {
        if ($scope.notReviewed === true) {
            $scope.tableParams.filter().reviewed = false;
        } else {
            $scope.tableParams.filter().reviewed = '';
        }
    };
});
