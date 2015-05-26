# Call For Paper

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
        <env-var name="GOOGLE_LOGIN" value="yourGoogleLogin"/>
        <env-var name="GOOGLE_PASSWORD" value="yourGooglePassword" />
        <env-var name="GOOGLE_CLIENT_ID" value="yourGoogleClientId"/>
        <env-var name="GOOGLE_CLIENT_SECRET" value="youtGoogleClientSecret"/>
        <env-var name="GITHUB_CLIENT_ID" value="yourGithubClientId"/>
        <env-var name="GITHUB_CLIENT_SECRET" value="youtGithubClientSecret"/>
        <env-var name="EMAIL_USERNAME" value="yourEmailUsername"/>
        <env-var name="EMAIL_PASSWORD" value="yourEmailPassword"/>
        <env-var name="AUTH_SECRET_TOKEN" value="yourRandomSecretToken"/>
        <env-var name="AUTH_CAPTCHA_PUBLIC" value="yourRecaptchaPublicToken"/>
        <env-var name="AUTH_CAPTCHA_SECRET" value="yourRecaptchaSecretToken"/>
    </env-variables>
</appengine-web-app>
```
Edit `src/main/webapp/WEB-INF/application-prod.properties` replace the informations to suit your need :

```properties
google.login=${GOOGLE_LOGIN}
google.password=${GOOGLE_PASSWORD}
google.spreadsheetName=CallForPaper // edit here
google.worksheetName=prod
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
Go to : YOUR_APP_ID.appspot.com

### Local :

```shell
grunt build
mvn appengine:devserver [-Dmaven.test.skip=true]
```
*For local testing purpose, set all `appengine-web.xml` env-var in you OS with the same value.*