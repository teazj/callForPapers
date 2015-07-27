'use strict';

angular.module('CallForPaper')
    .factory('authHttpResponseInterceptor', ['$q', '$injector', '$filter', 'lodash', function($q, $injector, $filter, lodash) {
        /**
         * Intercep every request and popup a notification if error
         */
        
        // Debounce error notifications
        var backendcommunication = lodash.throttle(function() {
            $injector.get('Notification').error({
                message: $filter('translate')('error.backendcommunication'),
                delay: 3000
            });
        },3000)

        var noInternet = lodash.throttle(function() {
            $injector.get('Notification').error({
                message: $filter('translate')('error.noInternet'),
                delay: 3000
            });
        },3000)

        return {
            response: function(response) {
                return response || $q.when(response);
            },
            responseError: function(rejection) {
                if (rejection.status === 0) {
                    noInternet();
                } else if (rejection.status === 401) {
                    $injector.get('$state').go('app.login');
                } else if (rejection.status === 403) {
                    $injector.get('$state').go('403');
                } else if (rejection.status === 404) {
                    $injector.get('$state').go('404');
                } else if (rejection.status === 409) {
                    // Nothing
                } else {
                    backendcommunication();
                }
                return $q.reject(rejection);
            }
        }
    }])