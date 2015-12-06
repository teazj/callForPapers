'use strict';

angular.module('CallForPaper')
    .controller('SignupCtrl', ['$scope', '$auth', 'Config', function($scope, $auth, Config) {
        $scope.recaptchaId = Config.recaptchaPublicKey;
        $scope.alreadyExists = false;
        $scope.loading = false;
        $scope.captcha = null;
        $scope.captchaShow = true;
        $scope.setResponse = function(response) {
            // send the `response` to your server for verification.
            $scope.captcha = response;
        };

        $scope.signup = function() {
            $scope.loading = true;
            $auth.signup({
                email: $scope.email,
                password: $scope.password,
                captcha: $scope.captcha
            }).then(function() {
                $scope.captchaShow = !$scope.captchaShow;
                $scope.loading = false;
            }).catch(function(response) {
                $scope.captchaShow = !$scope.captchaShow;
                $scope.loading = false;
                if (response.status === 409) {
                    $scope.alreadyExists = true;
                } else if (response.status === 400) {
                    $scope.password = '';
                    $scope.confirmPassword = '';
                }
            });
        };
    }]);
