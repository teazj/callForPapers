'use strict';

angular.module('CallForPaper').controller('AppTalksEditCtrl', function(TalkService, $scope, talk, Sessions, Drafts, $state, $q, dialogs, translateFilter, Notification) {


    $scope.talk = talk;

    TalkService.tracks.findAll().$promise.then(function(data) {$scope.tracks = data});

    TalkService.formats.findAll().$promise.then(function(data) {$scope.talkFormats = data});


    $scope.sending = false;

    function validate(talk) {
        // Validation is only about some required fields
        return _.every(['type', 'name', 'description', 'difficulty', 'track'], function(field) {
            return Boolean(talk[field]);
        });
    }

    function processError(error) {
        $scope.sending = false;
        if (error.status === 400) {
            $scope.sendErrorClose = true;
            return;
        }
        $scope.sendError = true;
    }

    function save(talk, isDraft) {
        var talkService = isDraft ? Drafts : Sessions;
        if (validate(talk)) {
            $scope.sending = true;
            return talkService.save(talk).then(function(savedTalk) {
                $scope.talk = savedTalk;
                $scope.sending = false;
                var notifMessage = isDraft ? translateFilter('talk.edit.saveDraft') : translateFilter('talk.edit.saveSession');
                Notification.success(notifMessage);
                $state.go('app.dashboard');
            }, processError);
        } else {
            $scope.talkInvalid = true;
            return $q.reject();
        }
    }

    $scope.submit = function submit(talk) {
        dialogs.confirm(translateFilter('confirmModal.title'), translateFilter('confirmModal.text'), {
            size: 'md'
        }).result.then(function() {
            save(talk);
        }, processError);
    };

    $scope.saveDraft = function saveDraft(talk) {
        save(talk, true);
    };
});
