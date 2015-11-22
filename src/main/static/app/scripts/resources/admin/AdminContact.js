'use strict';

angular.module('CallForPaper').factory('AdminContact', ['$resource', function($resource) {
    return $resource('api/admin/sessions/:rowId/contacts', {},
        {
            getByRowId: {
                url: 'api/admin/sessions/:rowId/contacts',
                method: 'GET',
                isArray: true
            },
            update: {method: 'PUT'}
        });
}]);
