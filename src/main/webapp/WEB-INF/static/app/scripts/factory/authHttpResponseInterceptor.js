angular.module('CallForPaper')
    .factory('authHttpResponseInterceptor', function($q, $injector, $filter) {
        /**
         * Intercep every request and popup a notification if error
         */
        return {
            response: function(response) {
                return response || $q.when(response);
            },
            responseError: function(rejection) {
                var Notification = $injector.get('Notification');
                if (rejection.status === 0) {
                    Notification.error({
                        message: $filter('translate')('error.backendcommunication'),
                        delay: 3000
                    });

                } else {
                    Notification.error({
                        message: $filter('translate')('error.backendcommunication'),
                        delay: 3000
                    });
                }
                return $q.reject(rejection);
            }
        }
    })
    .config(['$httpProvider', function($httpProvider) {
        //Http Intercpetor to check auth failures for xhr requests
        $httpProvider.interceptors.push('authHttpResponseInterceptor');
    }]);