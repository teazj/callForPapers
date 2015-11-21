'use strict';

angular.module('CallForPaper').factory('AdminContact', ['$resource', function($resource) {
    return $resource('api/admin/contacts/:id', {},
        {
            getByRowId: {
                url: 'api/admin/contacts/row/:rowId',
                method: 'GET',
                isArray: true
            },
            update: {method: 'PUT'}
        });
}]);
