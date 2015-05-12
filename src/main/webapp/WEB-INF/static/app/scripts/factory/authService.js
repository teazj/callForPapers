angular.module('CallForPaper')
  .factory('AuthService', ['$q', 'User', '$window', '$state', function($q, User, $window, $state) {
    var authService = {};
    authService.user = null;

    /**
     * Initialise user
     * @return {void}
     */
    authService.init = function() {
      User.getCurrentUser(function(userInfo) {
        authService.user = userInfo;
      })
    }

    /**
     * Get login url with custom redirection url
     * @param  {"redirection" : "wantedUrl"}
     * @return {string : url}
     */
    authService.getCustomLoginUrl = function(data) {
      var promise = $q.defer();
      User.getLoginUrl(data, function(uri) {
        promise.resolve(uri);
      }, function() {
        promise.reject();
      });
      return promise.promise;
    };

    /**
     * Get logout url with custom redirection url
     * @param  {"redirection" : "wantedUrl"}
     * @return {string : url}
     */
    authService.getCustomLogoutUrl = function(data) {
      var promise = $q.defer();
      User.getLogoutUrl(data, function(uri) {
        promise.resolve(uri);
      }, function() {
        promise.reject();
      });
      return promise.promise;
    };

    /**
     * Get logout url with root url as redirection
     * @return {string : url}
     */
    authService.getLogoutUri = function() {
      if (authService.user !== null && authService.user.connected === true) {
        return authService.user.uri;
      }
      return null;
    };

    /**
     * Get login url with root url as redirection
     * @return {string : url}
     */
    authService.getLogintUri = function() {
      if (authService.user !== null && authService.user.connected === false) {
        return authService.user.uri;
      }
      return null;
    };


    /**
     * Redirect user if not admin
     */
    authService.isAutorizedAdmin = function($q, $window, $state) {
      var deferred = $q.defer();
      authService.getCurrentUser().then(function(userInfo) {
        // connected
        if (userInfo.connected == true) {
          // admin
          if (userInfo.admin == true) {
            deferred.resolve();
            // not admin
          } else {
            // not autorized
            $state.go("403");
            deferred.reject();
          }
        } else {
          // login with admin redirection
          authService.getCustomLoginUrl({
            "redirect": "/#/admin"
          }).then(function(login) {
            if (login.uri !== null) {
              // go to login page
              $window.location.href = login.uri;
              deferred.reject();
            }
          }, function() {
            // error 
            $state.go("form.step1");
            deferred.reject();
          })
        }
      })
      return deferred.promise;
    }

    /**
     * Login the user and redirect to the given state
     * @param  {string : stateName}
     * @param  {string : stateNameIfAdmin}
     * @return {void}
     */
    authService.login = function(state, stateAdmin) {
      authService.getCurrentUser().then(function(user) {
        if (user.connected === false) {
          $window.location.href = user.uri;
        } else {
          if (user.admin === true) {
            $state.go(stateAdmin);
          } else {
            $state.go(state);
          }
        }
      })
    }


    /**
     * Logout the user and redirect to the given state
     * @param  {string : stateName}
     * @return {void}
     */
    authService.logout = function(state) {
      authService.getCurrentUser().then(function(user) {
        if (user.connected === true) {
          $window.location.href = user.uri;
        } else {
          $state.go(state);
        }
      })
    }

    /**
     * Get current logged in user info
     * @return { connected : bool, admin : bool, uri : string(login/out url depending on the connected state)}
     */
    authService.getCurrentUser = function() {
      var promise = $q.defer();
      User.getCurrentUser(function(userInfo) {
        authService.user = userInfo;
        promise.resolve(userInfo);
      }, function() {
        promise.reject();
      });
      return promise.promise;
    }
    return authService;
  }])