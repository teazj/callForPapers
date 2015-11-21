'use strict';

angular.module('CallForPaper')
    .controller('AdminSessionsExportCtrl', ['$scope', 'AdminSession', function($scope, AdminSession) {
        /**
         * Get all sessions (talks)
         * @param  {void}
         * @return {[AdminSession]}
         */
        AdminSession.query().$promise.then(function(sessionsTmp) {
            $scope.sessions = sessionsTmp;
        });
    }]);

angular.module('CallForPaper')
    .controller('AdminSessionsExportJsonCtrl', ['$scope', 'AdminSession', function($scope, AdminSession) {
        /**
         * Get all sessions (talks)
         * @param  {void}
         * @return {[AdminSession]}
         */
        AdminSession.query().$promise.then(function(sessionsTmp) {
            $scope.sessions = _.map(sessionsTmp, function(session) {
                /*jshint camelcase: false */
                return {
                    id: session.email,
                    topspeaker: false,
                    firstname: session.firstname,
                    lastname: _.capitalize(session.name.toLowerCase()),
                    image: 'todo à la main ?',
                    category: {
                        class: session.track,
                        title: _.capitalize(session.track)
                    },
                    ribon: {
                        class: 'gde',
                        title: 'GDE',
                        link: 'https://developers.google.com/experts/',
                        tile_long: 'Google Developer Expert'
                    },
                    company: session.company,
                    about: session.bio,
                    socials: [
                        {class: 'google-plus', link: session.googleplus},
                        {class: 'twitter', link: session.twitter},
                        {class: 'github', link: session.github},
                        {class: 'site', link: session.social}
                    ],
                    session: [{
                        id: 's4',
                        title: session.sessionName,
                        confRoom: 'Salle Titan',
                        desc: session.description,
                        type: session.track,
                        difficulty: parseInt(session.difficulty + '0' + session.difficulty),
                        all: false,
                        lang: 'fr',
                        hour: '',
                        video: '',
                        slides: '',
                        speakers: [session.email]
                    }]
                };
            });
        });
    }]);
