'use strict';

angular.module('CallForPaper')
    .controller('AdminConfigCtrl', ['$scope', '$http', 'Application',
        function($scope, $http, Application) {

            $scope.submission = false;
            Application.get(function(config) {
                $scope.submission = config.open;
            });

            /**
             * Enable or disable submission of new talks
             * @param  {boolean} enable or disable submissions
             * @return {void}
             */
            $scope.toggleSubmit = function(value) {
                $http.post('/api/admin/config/enableSubmissions', {
                    key: value
                }).then(function() {
                }, function() {
                    $scope.submission = !value;
                });
            };
        }
    ]);
