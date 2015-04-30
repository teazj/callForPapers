# Call For Paper

## Setup :

Edit `src/main/webapp/WEB-INF/appengine-web.xml` replace the informations to suit your need :

```xml
<?xml version="1.0" encoding="utf-8"?>
<appengine-web-app xmlns="http://appengine.google.com/ns/1.0">
    <application>YOUR_APP_ID</application>
    <version>1</version>
    <threadsafe>true</threadsafe>
    <system-properties>
        <property name="java.util.logging.config.file" value="WEB-INF/logging.properties"/>
    </system-properties>
    <env-variables>
        <env-var name="ENV" value="prod"/>
        <env-var name="GOOGLE_LOGIN" value="YOUR_GOOGLE_LOGIN"/>
        <env-var name="GOOGLE_PASSWORD" value="YOUR_GOOGLE_PASSWORD" />
        <env-var name="GOOGLE_CONSUMER_KEY" value="YOUR_GOOGLE_CONSUMER_KEY"/>
        <env-var name="GOOGLE_CONSUMER_SECRET" value="YOUR_GOOGLE_CONSUMER_SECRET"/>
        <env-var name="EMAIL_USERNAME" value="YOUR_EMAIL_USERNAME"/>
        <env-var name="EMAIL_PASSWORD" value="YOUR_EMAIL_PASSWORD"/>
    </env-variables>
</appengine-web-app>
```
Edit `src/main/webapp/WEB-INF/application-prod.properties` replace the informations to suit your need :

```properties
google.login=${GOOGLE_LOGIN}
google.password=${GOOGLE_PASSWORD}
google.spreadsheetName=CallForPaper // edit
google.worksheetName=prod // edit
google.consumerKey=${GOOGLE_CONSUMER_KEY}
google.consumerSecret=${GOOGLE_CONSUMER_SECRET}
database.load=true
email.smtphost=smtp.gmail.com // edit
email.smtpport=587 // edit
email.username=${EMAIL_USERNAME}
email.password=${EMAIL_PASSWORD}
webapp.dir=dist
```

## Deployment :

```shell
 bower install
 mvn appengine:update [-Dmaven.test.skip=true]
```
Go to : YOUR_APP_ID.appspot.com

## Local :

```shell
 bower install
 mvn appengine:devserver [-Dmaven.test.skip=true]
```
*For local testing purpose, set all `appengine-web.xml` env-var in you OS with the same value.*