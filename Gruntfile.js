module.exports = function (grunt) {
    'use strict';
 
    grunt.initConfig({
        hub: {
            all: {
                src: ['src/main/webapp/WEB-INF/static/Gruntfile.js'],
                tasks: ['build']
            }
        },
        copy: {
            node_modules: {
                expand: true,
                src: 'node_modules/**/*',
                dest: 'src/main/webapp/WEB-INF/static/'
            }
        },
        clean: {
            app: {
                src: ["src/main/webapp/WEB-INF/static/app/","src/main/webapp/WEB-INF/static/node_modules/"]
            }
        }
    });
 
    grunt.loadNpmTasks('grunt-hub');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.registerTask('build', ['copy:node_modules','hub','clean:app']);
}