# Churierp
Churi ERP Web Server

## 1. How to install and deploy Churierp

You can download and clone git repository with:

<code>git clone https://github.com/vellebue/churierp </code>

Churierp requires a postgresql database to work properly. Yo must install
Postgresql and create an empty database. Once created you must execute the 
following script to create Churierp database tables:

<code>/churierpback/sql/create-tables.sql</code>

And later to insert initial data:

<code>/churierpback/sql/insert-tables.sql</code>

You must adjust database access for Churierp. This is done editing
the following file:

<code>/churierpweb/src/main/resources/application.properties</code>

And edit the following properties:

<code>
jdbc.churierpweb.url=jdbc:postgresql://yourpostgresqlhost:yourpostgresqlport/yourdatabasename<br>
jdbc.churierpweb.username=yourdatabaseuser
jdbc.churierpweb.password=databaseuserpassword
</code>


Churierpweb requires jdk 17 and Maven 3.5.8 to be built properly. Once installed these tools
you can build Churierp as usual with Maven.

<code>mvn clean install</code>

And then you should deploy churierpweb war package located at 
<code>/churierpweb/target/churierpweb-CUURENTVERSION.war</code>. Deploy it on 
Tomcat or your favorite java application server. 