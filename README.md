<style>
body { counter-reset: h1counter h2counter h3counter h4counter h5counter h6counter; }

h1 { counter-reset: h2counter; }
h2 { counter-reset: h3counter; }
h3 { counter-reset: h4counter; }
h4 { counter-reset: h5counter; }
h5 { counter-reset: h6counter; }
h6 {}

h2:before {
    counter-increment: h2counter;
    content: counter(h2counter) ".\0000a0\0000a0";
}

h3:before {
    counter-increment: h3counter;
    content: counter(h2counter) "." counter(h3counter) ".\0000a0\0000a0";
}

h4:before {
    counter-increment: h4counter;
    content: counter(h2counter) "." counter(h3counter) "." counter(h4counter) ".\0000a0\0000a0";
}

h5:before {
    counter-increment: h5counter;
    content: counter(h2counter) "." counter(h3counter) "." counter(h4counter) "." counter(h5counter) ".\0000a0\0000a0";
}

h6:before {
    counter-increment: h6counter;
    content: counter(h2counter) "." counter(h3counter) "." counter(h4counter) "." counter(h5counter) "." counter(h6counter) ".\0000a0\0000a0";
}
</style>

# Churierp
Churi ERP Web Server

## How to install and deploy Churierp

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