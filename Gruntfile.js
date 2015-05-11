module.exports = function(grunt) {
    'use strict';

    grunt.initConfig({
        subgrunt: {
            target0: {
                projects: {
                    'src/main/webapp/WEB-INF/static': 'build'
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-subgrunt');
    grunt.registerTask('build', ['subgrunt']);
}