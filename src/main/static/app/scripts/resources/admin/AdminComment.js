'use strict';

angular.module('CallForPaper').factory('AdminComment', ['$resource', function($resource) {
    return $resource('api/admin/sessions/:rowId/comments', {},
        {
            getAll: {
                method: 'GET',
                isArray: true
            },
            getByRowId: {
                url: 'api/admin/sessions/:rowId/comments',
                method: 'GET',
                isArray: true
            },
            update: {method: 'PUT',url: 'api/admin/sessions/:rowId/comments/:id'},
            delete: {method: 'DELETE',url: 'api/admin/sessions/:rowId/comments/:id'}

        });
}]);
