'use strict';

angular.module('CallForPaper').controller('AppTalksEditCtrl', function(tracks, talkformats, $scope, talk, Sessions, Drafts, $state, $q, dialogs, translateFilter, Notification) {

    $scope.talk = talk;
    $scope.tracks = tracks;
    $scope.talkFormats = talkformats;
    $scope.cospeakers = [];

    if (talk) {
        $scope.cospeakers = talk.cospeakers.map(function(speaker) {
            return speaker.email;
        });
        var length = tracks.length;
        for (var i = 0; i < length; i++) {
            if (tracks[i].id === talk.trackId) {
                $scope.selectedTrack = tracks[i];
                break;
            }
        }
    }


    $scope.sending = false;

    $scope.updateTrack = function() {
        $scope.talk.trackId = $scope.selectedTrack.id;
        $scope.talk.trackLabel = $scope.selectedTrack.libelle;
    };

    function validate(talk) {
        // Validation is only about some required fields
        return _.every(['format', 'name', 'description', 'difficulty', 'trackId'], function(field) {
            return Boolean(talk[field]);
        });
    }

    function processError(error) {
        $scope.sending = false;
        if (error.status === 400) {
            window.console.log(error);
            if (error.data.errorCode === 1) {
                Notification.error(translateFilter('step2.cospeakerNotFound', {value: error.data.errorCodeBody.email}));
            } else {
                Notification.error(translateFilter('verify.notVerified'));
            }

            return;
        }
        $scope.sendError = true;
    }

    function save(talk, isDraft) {
        var talkService = isDraft ? Drafts : Sessions;
        if (validate(talk)) {
            $scope.sending = true;
            talk.cospeakers = $scope.cospeakers.map(function(email) {
                return {email: email.text};
            });
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
