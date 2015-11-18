'use strict';

angular.module('CallForPaper')
    .factory('NextPreviousSessionService', ['localStorageService', function(localStorageService) {
        var nextPreviousSessionService = {};

        var scope = this;

        scope.sessionsByType = {
            sessions: [],
            sessionscodelab: [],
            sessionsconference: []
        };

        var type = '';
        /**
         * Load sessions
         **/
        if (localStorageService.isSupported) {
            var typeString = localStorageService.get('nextPreviousSessionType');
            if (typeString !== null) {
                type = typeString;
            }
            var sessionsString = localStorageService.get('nextPreviousSession' + type);
            try {
                if (sessionsString !== null) {
                    scope.sessionsByType['sessions' + type] = angular.fromJson(sessionsString);
                }
            } catch (e) {
            }
        }

        nextPreviousSessionService.setType = function(typeTmp) {
            type = typeTmp;
            localStorageService.set('nextPreviousSessionType', type);
        };

        nextPreviousSessionService.getType = function() {
            return localStorageService.get('nextPreviousSessionType');
        };

        nextPreviousSessionService.setSessions = function(array, typeTmp) {
            var sessionsTmp = array.map(function(session) {
                return session.added;
            });
            scope.sessionsByType['sessions' + typeTmp] = sessionsTmp;
            localStorageService.set('nextPreviousSession' + typeTmp, sessionsTmp);
        };

        nextPreviousSessionService.getNextSessions = function(currentSessionId) {
            var index = scope.sessionsByType['sessions' + type].indexOf(parseInt(currentSessionId, 10));
            if (index !== -1) {
                if (index < scope.sessionsByType['sessions' + type].length - 1) {
                    return scope.sessionsByType['sessions' + type][index + 1];
                }
            }
            return null;
        };
        nextPreviousSessionService.getPreviousSessions = function(currentSessionId) {
            var index = scope.sessionsByType['sessions' + type].indexOf(parseInt(currentSessionId, 10));
            if (index !== -1) {
                if (index > 0) {
                    return scope.sessionsByType['sessions' + type][index - 1];
                }
            }
            return null;
        };

        return nextPreviousSessionService;
    }]);
