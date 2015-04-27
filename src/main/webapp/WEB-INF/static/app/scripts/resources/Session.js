angular.module('CallForPaper').factory('Session', function($resource) {
  return $resource('devfest/session/:id');
});