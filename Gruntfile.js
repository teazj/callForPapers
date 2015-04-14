module.exports = function (grunt) {
    'use strict';
 
    grunt.initConfig({
        hub: {
            all: {
                src: ['src/main/resources/static/Gruntfile.js'],
                tasks: ['build']
            }
        },
        copy: {
            node_modules: {
                expand: true,
                src: 'node_modules/**/*',
                dest: 'src/main/resources/static/'
            }
        },
        clean: {
            app: {
                src: ["src/main/resources/static/app/**/*","src/main/resources/static/node_modules/**/*"]
            }
        }
    });
 
    grunt.loadNpmTasks('grunt-hub');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.registerTask('heroku', ['copy:node_modules','hub','clean:app']);
}