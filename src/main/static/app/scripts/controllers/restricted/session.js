'use strict';

angular.module('CallForPaper')
    .controller('RestrictedSessionCtrl', function($scope, $stateParams, $filter, RestrictedSession, CommonProfilImage, RestrictedContact, $modal, Config, talkformats) {
        $scope.tab = $stateParams.tab;

        $scope.session = null;

        $scope.talkformats=talkformats;

        /**
         * Get talk
         * @return {RestrictedSession}
         */
        RestrictedSession.get({
            id: $stateParams.id
        }).$promise.then(function(sessionTmp) {
            $scope.session = sessionTmp;

            // Add link to social
            $scope.session.socialLinks = [];
            if (sessionTmp.speaker.social !== null) {
                var links = sessionTmp.speaker.social.split(',').map(function(value) {
                    return $filter('createLinks')(value);
                });
                $scope.session.socialLinks = links;
            }
            if (sessionTmp.speaker.twitter !== null) {
                $scope.session.speaker.twitter = $filter('createLinks')(sessionTmp.speaker.twitter);
            }
            if (sessionTmp.speaker.googleplus !== null) {
                $scope.session.speaker.googleplus = $filter('createLinks')(sessionTmp.speaker.googleplus);
            }
            if (sessionTmp.speaker.github !== null) {
                $scope.session.speaker.github = $filter('createLinks')(sessionTmp.speaker.github);
            }
            $scope.session.keyDifficulty = (['beginner', 'confirmed', 'expert'])[sessionTmp.difficulty - 1];


            $scope.session.speaker.profilImageUrl = $scope.session.speaker.socialProfilImageUrl;

        });

        /**
         * CONTACT
         */

        /**
         * get contacts of the session
         * @return {[RestrictedContact]}
         */
        var updateContacts = function() {
            RestrictedContact.getByRowId({
                rowId: $stateParams.id
            }, function(contactsTmp) {
                $scope.contacts = contactsTmp;
            });
        };
        updateContacts();

        $scope.captchaShow = true;
        $scope.recaptchaId = Config.recaptchaPublicKey;
        $scope.captcha = null;
        $scope.setResponse = function(response) {
            // send the `response` to your server for verification.
            $scope.captcha = response;
        };

        $scope.contactButtonDisabled = false;
        /**
         * Post current contact in textarea
         * @return {RestrictedContact} posted contact
         */
        $scope.postContact = function() {
            $scope.contactButtonDisabled = true;
            RestrictedContact.save({rowId: $stateParams.id}, {
                'comment': $scope.contactMsg,
                'id': $stateParams.id,
                'captcha': $scope.captcha
            }, function() {
                $scope.captchaShow = !$scope.captchaShow;
                $scope.contactMsg = '';
                $scope.contactButtonDisabled = false;
                updateContacts();
            }, function() {
                $scope.captchaShow = !$scope.captchaShow;
                $scope.contactButtonDisabled = false;
            });
        };

        /**
         * PUT contact on server
         * @return {RestrictedContact} edited contact
         */
        var putContact = function(contact) {
            RestrictedContact.update({
                rowId: $stateParams.id,
                id: contact.id
            }, contact, function() {
                updateContacts();
            }, function() {
            });
        };

        /**
         * Open modal for editing
         * @param  {RestrictedContact} contact to edit
         * @return {RestrictedContact} edited contact text
         */
        $scope.editContact = function(localContact) {
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: 'views/admin/editModal.html',
                controller: 'EditModalInstanceCtrl',
                resolve: {
                    comment: function() {
                        return localContact.comment;
                    }
                }
            });
            modalInstance.result.then(function(comment) {
                localContact.comment = comment;
                putContact(localContact);
            }, function() {
                // cancel
            });
        };

    });
