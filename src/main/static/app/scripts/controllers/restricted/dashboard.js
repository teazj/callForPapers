'use strict';

angular.module('CallForPaper')
    .controller('DashboardCtrl', ['$scope', '$filter', 'RestrictedSession', 'RestrictedDraft', 'AuthService', 'Application', 'RestrictedStats', function($scope, $filter, RestrictedSession, RestrictedDraft, AuthService, Application, RestrictedStats) {
        $scope.realDifficulty = [$filter('translate')('step2.beginner'), $filter('translate')('step2.confirmed'), $filter('translate')('step2.expert')];

        /**
         * Get current user sessions
         * @return {[RestrictedSession]}
         */
        $scope.sessions = [];
        $scope.sessionsLoaded = false;
        function querySession() {
            RestrictedSession.query(function(sessionsTmp) {
                $scope.sessions = sessionsTmp.map(function(session) {
                    session.fullname =  session.firstname;
                    session.keyDifficulty = (['beginner', 'confirmed', 'expert'])[session.difficulty - 1];
                    return session;
                });
                $scope.sessionsLoaded = true;
            });
        }

        /**
         * Get current user drafts
         * @return {[RestrictedDraft]}
         */
        $scope.drafts = [];
        $scope.draftsLoaded = false;
        function queryDraft() {
            RestrictedDraft.query().$promise.then(function(draftsTmp) {
                $scope.drafts = draftsTmp;
                $scope.draftsLoaded = true;
            });
        }

        function queryMeter() {
            RestrictedStats.meter().$promise.then(function(statsTmp) {
                $scope.stats = statsTmp;
            });
        }

        /**
         * Delete draft
         * @param  {number} draft id
         * @return {void}
         */
        $scope.delete = function(added) {
            RestrictedDraft.delete({
                id: added
            }).$promise.then(function() {
                queryDraft();
            });
        };

        $scope.isVerified = AuthService.isVerified();
        if ($scope.isVerified) {
            queryDraft();
            querySession();
            queryMeter();
        }

        $scope.konamiCode = false;
        $scope.launchKonami = function() {
            $scope.konamiCode = !$scope.konamiCode;
            $scope.$apply();
        };

        Application.get(function(config) {
            $scope.submission = config.open;
        });
    }]);
