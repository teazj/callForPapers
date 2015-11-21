'use strict';

angular.module('CallForPaper')
    .controller('ConfigCtrl', ['$scope', '$rootScope', '$translate', 'AuthService', function($scope, $rootScope, $translate, AuthService) {
        $scope.isConnected = false;
        if (AuthService.user !== null) {
            $scope.isConnected = AuthService.user.connected;
        }

        $scope.language = $translate.use();

        $scope.changeLanguage = function(key) {
            $translate.use(key);
        };

        $rootScope.$on('$translateChangeEnd', function(event, args) {
            $scope.language = args.language;
        });
    }]);
