'use strict';

angular.module('CallForPaper')
    .controller('DashboardCtrl', ['$scope', '$filter', 'RestrictedSession', 'RestrictedCoSession', 'RestrictedDraft','RestrictedCoDraft', 'AuthService', 'Application', 'RestrictedStats', function($scope, $filter, RestrictedSession,RestrictedCoSession, RestrictedDraft,RestrictedCoDraft, AuthService, Application, RestrictedStats) {
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

        /**
         * Get current user cosession
         * @return {[RestrictedCoDraft]}
         */
        $scope.coDrafts = [];
        $scope.coDraftsLoaded = false;
        function queryCoDrafts() {
            RestrictedCoDraft.query(function(sessionsTmp) {
                $scope.coDrafts = sessionsTmp.map(function(session) {
                    session.fullname =  session.firstname;
                    session.keyDifficulty = (['beginner', 'confirmed', 'expert'])[session.difficulty - 1];
                    return session;
                });

                $scope.coDraftsLoaded = true;
            });
        }
        
        /**
         * Get current user cosession
         * @return {[RestrictedDraft]}
         */
        $scope.coTalks = [];
        $scope.coTalksLoaded = false;
        function queryCoTalks() {
            RestrictedCoSession.query(function(sessionsTmp) {
                $scope.coTalks = sessionsTmp.map(function(session) {
                    session.fullname =  session.firstname;
                    session.keyDifficulty = (['beginner', 'confirmed', 'expert'])[session.difficulty - 1];
                    return session;
                });

                $scope.coTalksLoaded = true;
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
            queryCoDrafts();
            querySession();
            queryMeter();
            queryCoTalks();
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
