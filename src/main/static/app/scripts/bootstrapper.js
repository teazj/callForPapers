'use strict';

/*
 No ng-app attribute in index.html. Config is retrieved from backend then Angular app is bootstrapped.
 */
(function(window, document) {
    window.deferredBootstrapper.bootstrap({
        element: document.body,
        module: 'CallForPaper',
        resolve: {
            Config: ['$http', function($http) {
                return $http.get('api/settings/serviceproviders');
            }]
        }
    });
})(window, window.document);
