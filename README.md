
# Call For Paper

<img src="https://raw.githubusercontent.com/SII-Nantes/callForPaper/master/readme/screenshot.png" alt="alt text" width="100%">

## Features :

 - Powered by App Engine
 - Spring Back-end
 - AngularJS Front-end
 - Google Spreadsheet
 - Datastore database
 - JWT Token
 - Localization (fr, en)
 - Material design

### User Panel :

 - Google, Github OAuth authentification
 - Account register (with email confirmation)
 - Talks submission (with email confirmation)
 - Save/delete draft

### Admin Panel :

 - Sort talks (by rate, date, track...)
 - Filter talks (by track, talker name, description...)
 - Rate talk
 - Comment talk

## Setup :

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
        <env-var name="GOOGLE_LOGIN" value="yourGoogleLogin"/> <!--owner's email of the spreadsheet-->
        <env-var name="GOOGLE_PASSWORD" value="yourGooglePassword" /> <!--optional password for testing purpose-->
        <env-var name="GOOGLE_CLIENT_ID" value="yourGoogleClientId"/> <!--app client id-->
        <env-var name="GOOGLE_CLIENT_SECRET" value="youtGoogleClientSecret"/> <!--app secret-->
        <env-var name="GITHUB_CLIENT_ID" value="yourGithubClientId"/> <!--github app id-->
        <env-var name="GITHUB_CLIENT_SECRET" value="youtGithubClientSecret"/> <!--github app secret-->
        <env-var name="EMAIL_USERNAME" value="yourEmailUsername"/> <!--sender's email address-->
        <env-var name="EMAIL_PASSWORD" value="yourEmailPassword"/> <!--sender's email password-->
        <env-var name="AUTH_SECRET_TOKEN" value="yourRandomSecretToken"/> <!--random secret token-->
        <env-var name="AUTH_CAPTCHA_PUBLIC" value="yourRecaptchaPublicToken"/> <!--recaptcha public key-->
        <env-var name="AUTH_CAPTCHA_SECRET" value="yourRecaptchaSecretToken"/> <!--recaptcha private key-->
    </env-variables>
</appengine-web-app>
```
Edit `src/main/webapp/WEB-INF/application-prod.properties` replace the informations to suit your need :

```properties
google.login=${GOOGLE_LOGIN}
google.password=${GOOGLE_PASSWORD}
google.spreadsheetName=CallForPaper // google spreadsheet name
google.worksheetName=prod // google spreadsheet name
google.clientid=${GOOGLE_CLIENT_ID}
google.clientsecret=${GOOGLE_CLIENT_SECRET}

github.clientid=${GITHUB_CLIENT_ID}
github.clientsecret=${GITHUB_CLIENT_SECRET}

auth.secrettoken=${AUTH_SECRET_TOKEN}
auth.captchapublic=${AUTH_CAPTCHA_PUBLIC}
auth.captchasecret=${AUTH_CAPTCHA_SECRET}  

database.loaded=true

email.smtphost=smtp.gmail.com // edit here
email.smtpport=587 // edit here
email.username=${EMAIL_USERNAME}
email.password=${EMAIL_PASSWORD}
email.send=true

webapp.dir=dist

app.eventName=DevFest 2015 // edit here
app.community=GDG Nantes // edit here
app.date=06/11/2015 // edit here
app.releasedate=01/09/2015 // edit here
app.hostname=http://aesthetic-fx-89513.appspot.com/ // edit here
```
Edit `src/main/webapp/WEB-INF/static/app/scripts/app.js` add your providers tokens :

```javascript
  .constant('Config', {
    'recaptcha': 'yourRecaptchaPublicToken',
    'googleClientId': 'yourGoogleClientId',
    'githubClientId': 'yourGithubClientId'
  })
```

## Deployment :

### App Engine :

```shell
grunt build
mvn appengine:update [-Dmaven.test.skip=true]
```
Go to : http://YOUR_APP_ID.appspot.com

### Local :

```shell
grunt build
mvn appengine:devserver [-Dmaven.test.skip=true]
```
*For local deployment, set all `appengine-web.xml` env-var in you OS with the same value.*

Go to : http://127.0.0.1:8080

The application will guie you througth the process to link your Google Drive account to the application spreadsheet.

## Usage :

### App entry points :

 - http://127.0.0.1/ : User login page (create new talks)

 - http://127.0.0.1/#/config : Google Drive linking panel (autorize CFP to access your account to create the spreadsheet)

 - http://127.0.0.1/#/admin : Admin panel (rating, comment...) 
