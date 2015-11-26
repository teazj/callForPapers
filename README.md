
# Call For Papers

<img src="https://raw.githubusercontent.com/SII-Nantes/callForPaper/master/readme/screenshot.png" alt="alt text" width="100%">

## Features

 - Powered by App Engine
 - Spring Back-end
 - AngularJS Front-end
 - Google Spreadsheet
 - Datastore database
 - JWT Token
 - Localization (fr, en)
 - Material design

### User Panel

 - Google, Github OAuth authentification
 - Account register (with email confirmation)
 - Talks submission (with email confirmation)
 - Save/delete draft

### Admin Panel

 - Sort talks (by rate, date, track...)
 - Filter talks (by track, talker name, description...)
 - Rate talk
 - Comment talk

## Setup

*Change **127.0.0.1:8080** to your application domain for production*

### Obtaining OAuth Keys

<img src="http://images.google.com/intl/en_ALL/images/srpr/logo6w.png" width="150">

- Visit [Google Cloud Console](https://cloud.google.com/console/project)
- Click **CREATE PROJECT** button
- Enter *Project Name*, then click **CREATE**
- Then select *APIs & auth* from the sidebar and click on *Credentials* tab
- Click **CREATE NEW CLIENT ID** button
 - **Application Type**: Web Application
 - **Authorized Javascript origins**: *http://127.0.0.1:8080*
 - **Authorized redirect URI**: *http://127.0.0.1:8080*

**Note:** Make sure you have turned on **Contacts API**, **Google+ API** and **Drive API** in the *APIs* tab.

<hr>

<img src="https://www.cloudamqp.com/images/blog/github.png" height="70">

- Visit [Github developers settings](https://github.com/settings/developers)
- Click **REGISTER NEW APPLICATION** button
 - **Application name**: *Application name*
 - **Homepage URL**: *http://127.0.0.1:8080*
 - **Authorization callback URL**: *http://127.0.0.1:8080*

### Obtaining reCAPTCHA Keys

<img src="https://www.gstatic.com/recaptcha/admin/logo_recaptcha_color_24dp.png" height="70">

- Visit [reCaptcha panel](https://www.google.com/recaptcha/admin)
 - **Domaines**: *127.0.0.1:8080*
- Click **Save** button

### Edit CFP Settings

Add following environment variables in your system.


Edit `src/main/webapp/WEB-INF/appengine-web.xml` replace the informations to suit your need :

```xml
<?xml version="1.0" encoding="utf-8"?>
<appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
    <application>yourAppID</application>
    <version>1</version>
    <threadsafe>true</threadsafe>
    <system-properties>
        <property name="java.util.logging.config.file" value="WEB-INF/logging.properties"/>
    </system-properties>
    <sessions-enabled>true</sessions-enabled>
    <env-variables>
        <env-var name="ENV" value="dev"/>
        <env-var name="GOOGLE_LOGIN" value="yourGoogleLogin"/> <!--owner's email of the spreadsheet (google drive account)-->
        <env-var name="GOOGLE_PASSWORD" value="yourGooglePassword" /> <!-- optional password for testing purpose, empty string otherwise-->
        <env-var name="GOOGLE_CLIENT_ID" value="yourGoogleClientId"/> <!--google oauth client id-->
        <env-var name="GOOGLE_CLIENT_SECRET" value="youtGoogleClientSecret"/> <!--google oauth secret-->
        <env-var name="GITHUB_CLIENT_ID" value="yourGithubClientId"/> <!--github oauth client id-->
        <env-var name="GITHUB_CLIENT_SECRET" value="youtGithubClientSecret"/> <!--github oauth client secret-->
        <env-var name="EMAIL_SENDER" value="yourEmailUsername"/> <!--sender's email address-->
        <env-var name="AUTH_SECRET_TOKEN" value="yourRandomSecretToken"/> <!--random secret token for jwt-->
        <env-var name="AUTH_CAPTCHA_PUBLIC" value="yourRecaptchaPublicToken"/> <!--recaptcha public key-->
        <env-var name="AUTH_CAPTCHA_SECRET" value="yourRecaptchaSecretToken"/> <!--recaptcha private key-->
        <env-var name="DB_HOST" value="yourDbHost"/> <!--Database host and port (e.g. localhost:3306)-->
        <env-var name="DB_NAME" value="yourDbName"/> <!--Name of the CFP database-->
        <env-var name="DB_USER" value="yourDbUser"/> <!--User to connect to the database-->
        <env-var name="DB_PASS" value="yourDbPassword"/> <!--Password to connect to database--> 
    </env-variables>
</appengine-web-app>
```
Edit `src/main/webapp/WEB-INF/application-prod.properties` replace the informations to suit your need :

```properties
google.login=${GOOGLE_LOGIN}
google.password=${GOOGLE_PASSWORD}
google.spreadsheetName=CallForPaper // google spreadsheet name
google.worksheetName=prod // google worksheet name
google.clientid=${GOOGLE_CLIENT_ID}
google.clientsecret=${GOOGLE_CLIENT_SECRET}

github.clientid=${GITHUB_CLIENT_ID}
github.clientsecret=${GITHUB_CLIENT_SECRET}

auth.secrettoken=${AUTH_SECRET_TOKEN}
auth.captchapublic=${AUTH_CAPTCHA_PUBLIC}
auth.captchasecret=${AUTH_CAPTCHA_SECRET}  

database.loaded=true

email.emailsender=${EMAIL_SENDER}
email.send=true // disable emailing

webapp.dir=dist

app.eventName=DevFest 2015 // navbar title "Call for paper - {{eventName}}"
app.community=GDG Nantes // community name (for email)
app.date=06/11/2015 // event date
app.releasedate=01/09/2015 // speakers publication date
app.hostname=http://aesthetic-fx-89513.appspot.com // root domain (email images/links)

db.host=${DB_HOST}
db.name=${DB_NAME}
db.user=${DB_USER}
db.pass=${DB_PASS}
```
Edit `src/main/webapp/WEB-INF/static/app/scripts/app.js` add your providers tokens :

```javascript
  .constant('Config', {
    'recaptcha': 'yourRecaptchaPublicToken',
    'googleClientId': 'yourGoogleClientId',
    'githubClientId': 'yourGithubClientId'
  })
```

## Deployment

* NodeJS is required to run frontend build tools. The build has been tested with version 4.2.2. Its installation is
included in the Maven build. However, if you want to contribute to frontend, you may install it by yourself to be able
to use frontend build tools from the command line. 
* Java 7 is required by the AppEngine SDK.

### App Engine

*TO UPDATE*
```bash
$ npm install
$ bower install
$ gulp build
$ mvn appengine:update [-Dmaven.test.skip=true]
```
 Go to : http://YOUR_APP_ID.appspot.com

### Local

#### Backend development

If you intend to contribute to backend exclusively and don't want to deal with the NodeJS tools necessary for frontend
development and build, you can start the application with the following command:

```bash
$ mvn appengine:devserver -Pautojs [-Dmaven.test.skip=true]
```

#### Frontend development

Frontend contributors can use usual frontend build tools from the command line:

* Gulp and Bower are required to build. You can install them with:
    ```bash
    $ npm install -g gulp bower
    ```

* To install build tools dependencies specified in `package.json`, you have to run the following in project root folder:
    ```bash
    $ npm install
    ```

* To install project third party libraries specified in `bower.json`, you have to run the following in project root folder:
    ```bash
    $ bower install
    ```

A Gulp task `gulp serve` is available to facilitate contributions to the front code. It starts a
web server on port 3000 for files in `src/main/webapp/WEB-INF/static/app` directory. Livereload is configured and
requests to the backend are forwarded to the AppEngine development server (`http://127.0.0.1:8080`) thanks to an proxy.

    ```bash
    $ gulp serve # or gulp serve:dist for minified version of front files, but no livereload in this case
    ```

*For local deployment, set all `appengine-web.xml` env-var in you OS with the same value.*

 - Go to : http://127.0.0.1:8080

 - The application will guide you through the process to link your Google Drive account to the application.

## Usage :

### Manage admin users :

- Visit [Google Cloud Console](https://cloud.google.com/console/project)
- Select your project
- Click **Permissions** button
- Click **Add member** button :
 - **Email**: New member e-mail
 - **Can view**

### App entry points :

 - http://127.0.0.1:8080/ : User login page (create new talks)

 - http://127.0.0.1:8080/#/config : Google Drive linking panel (autorize CFP to access your account to create the spreadsheet)

 - http://127.0.0.1:8080/#/admin : Admin panel (rating, comment...)
