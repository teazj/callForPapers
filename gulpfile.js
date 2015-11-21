'use strict';

var gulp = require('gulp'),
    autoprefixer = require('gulp-autoprefixer'),
    minifyCss = require('gulp-minify-css'),
    usemin = require('gulp-usemin'),
    uglify = require('gulp-uglify'),
    minifyHtml = require('gulp-minify-html'),
    livereload = require('gulp-livereload'),
//imagemin = require('gulp-imagemin'),
    ngAnnotate = require('gulp-ng-annotate'),
    jshint = require('gulp-jshint'),
    stylish = require('jshint-stylish'),
    rev = require('gulp-rev'),
    connect = require('gulp-connect'),
    proxy = require('proxy-middleware'),
    url = require('url'),
    es = require('event-stream'),
    flatten = require('gulp-flatten'),
    replace = require('gulp-replace'),
    less = require('gulp-less'),
    path = require('path'),
    del = require('del');

/*var KarmaServer = require('karma').Server,
 karmaConfigFile = __dirname + '/karma.conf.js';*/

var devPort = 3000;

var yeoman = {
    app: 'src/main/static/app/',
    dist: 'src/main/webapp/WEB-INF/static/app/',
    tmp: 'src/main/static/.tmp/'
};

var middlewareDef = function() {

    function configureProxy(path) {
        var appBaseUrl = 'http://127.0.0.1:8080',
            options = url.parse(appBaseUrl + path);
        options.route = path;
        return proxy(options);
    }

    return [
        configureProxy('/api'),
        configureProxy('/auth'),
        configureProxy('/_ah')
    ];
};

gulp.task('clean', function() {
    del.sync([yeoman.dist, yeoman.tmp]);
});

gulp.task('test', function() {
    /* new KarmaServer({
     configFile: karmaConfigFile,
     singleRun: true
     }).start();*/
});

gulp.task('tdd', function() {
    /* new KarmaServer({
     configFile: karmaConfigFile
     }).start();*/
});

gulp.task('copy', function() {
    return es.merge(
        gulp.src([
                yeoman.app + '*.{ico,png,txt}',
                yeoman.app + '{403,404}.html',
                yeoman.app + '.htaccess',
                yeoman.app + 'images/{,*/}*.{gif,webp,png,jpg}',
                yeoman.app + 'styles/fonts/*',
                yeoman.app + '/fonts/*',
                yeoman.app + '/lib/**/*'
            ], {base: yeoman.app})
            .pipe(gulp.dest(yeoman.dist))
    );
});

gulp.task('images', function() {
    /* return gulp.src(yeoman.app + 'images/!**').
     pipe(imagemin({optimizationLevel: 5})).
     pipe(gulp.dest(yeoman.dist + 'images'));*/
});

gulp.task('less', function() {
    /* return gulp.src(yeoman.app + 'app.less').
     pipe(less()).
     pipe(gulp.dest(yeoman.tmp + 'styles'));*/
});

gulp.task('lint', function() {
    return gulp.src([
            yeoman.app + 'scripts/**/*.js',
            '!' + yeoman.app + 'scripts/directives/angular-language-picker.templates.js',
            '!' + yeoman.app + 'scripts/directives/tags-input.js',
            '!' + yeoman.app + 'scripts/modules/satellizer*.js'])
        .pipe(jshint())
        .pipe(jshint.reporter(stylish));
});

gulp.task('styles', ['less'], function() {
    // TODO deprecated?
    /*return gulp.src([yeoman.app + '*.css',
     yeoman.app + '!(bower_components)/!*.css',
     yeoman.app + '!(bower_components)/!**!/!*.css']).
     pipe(gulp.dest(yeoman.tmp));*/
});

gulp.task('serve', ['watch', 'tdd', 'lint'], function() {
    connect.server(
        {
            root: [yeoman.app, yeoman.tmp],
            port: devPort,
            livereload: true,
            middleware: middlewareDef
        }
    );
});

gulp.task('watch', ['styles'], function() {
    livereload.listen();

    var excludedGlob = '!' + yeoman.app + '{bower_components,bower_components/**}';

    gulp.watch([yeoman.app + '**/*.js', excludedGlob], ['lint']).on('change', livereload.changed);
    gulp.watch([yeoman.app + '**/*.html', excludedGlob]).on('change', livereload.changed);
    gulp.watch([yeoman.app + '**/*.less', excludedGlob], ['less']);
    gulp.watch([yeoman.tmp + 'styles/*.css']).on('change', livereload.changed);
});

gulp.task('serve:dist', ['build'], function() {
    connect.server(
        {
            root: [yeoman.dist],
            port: devPort,
            //livereload: true,
            middleware: middlewareDef
        }
    );
});

gulp.task('build', ['usemin']);

gulp.task('usemin', ['images', 'styles', 'copy'], function() {

        var minifyHtmlOptions = {empty: true, conditionals: true, loose: true};

        return es.merge(
            gulp.src(yeoman.app + 'index.html')
                .pipe(usemin({
                    vendor_css: ['concat', rev()],
                    main_css: [autoprefixer(), 'concat', rev()],
                    html: [minifyHtml(minifyHtmlOptions)],
                    ie_js: [uglify(), 'concat', rev()],
                    vendor_js: [uglify(), 'concat', rev()],
                    modules_js: [ngAnnotate(), uglify(), 'concat', rev()], // some 3rd party libs need ngAnnotate
                    app_js: [ngAnnotate(), uglify(), 'concat', rev()]
                })),
            // minify all HTML files in yeoman.app sub-dirs except bower_components
            gulp.src(yeoman.app + '!(bower_components)/{*,**/*}.html')
                .pipe(minifyHtml(minifyHtmlOptions))
        ).pipe(gulp.dest(yeoman.dist));
    }
);

gulp.task('default', ['build']);
