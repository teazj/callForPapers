angular.module('CallForPaper').factory('Session', function($resource) {
  return $resource('http://127.0.0.1\\:8080/devfest/session/:id');
});