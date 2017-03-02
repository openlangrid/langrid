# Reverse proxy module for the authentication of Service Grid and Galaxy.

This module works as reverse proxy with following functions
* Authentication usign the user table of Service Grid DB.
* Insertion of HTTP headers (REMOTE_USER and GX_SECRET).

Using this module, users can log-in to Galaxy with the userId and password of Service Grid.

## Set-up

### This module and Tomcat
1. Download war archive from https://github.com/openlangrid/langrid/releases/tag/galaxyreverseproxy-20170302
1. Unpack war file to webapp folder of Tomcat.
1. Change GX_SECRET value in WEB-INF/web.xml
```xml
    <init-param>
      <param-name>additionalHeaders</param-name>
      <param-value>
        GX_SECRET:${GX_SECRET}
      </param-value>
    </init-param>
```
1. Change mapping in WEB-INF/web.xml
```xml
    <init-param>
      <param-name>mappings</param-name>
      <param-value>
        /,${URL_TO_GALAXY}
      </param-value>
    </init-param>
```
1. Move META-INF/context.xml to your tomcat configuration folder with certain name such like ${TOMCAT}/conf/Catalina/localhost/galaxy.xml
1. Change DB setting of context.xml (or galaxy.xml)
```xml
    <Resource
        name="jdbc/langrid" auth="Container" type="javax.sql.DataSource"
        maxActive="100" maxIdle="50" maxWait="10000"
        username="${DB_USER}" password="${DB_PASSWORD}"
        driverClassName="org.postgresql.Driver"
        url="jdbc:postgresql:${DB_NAME}"
        />

	<Parameter name="langrid.node.gridId" value="${NODE_GRIDID}" />
	<Parameter name="langrid.node.nodeId" value="${NODE_NODEID}" />
```

### Galaxy
1. Enable user_remote_user and change additional settings in config/galaxy.ini
```
use_remote_user = True
remote_user_header = HTTP_REMOTE_USER
remote_user_secret = ${GX_SECRET}
```
1. Restart Galaxy

### Example construction
Be sure you operate this proxy and galaxy in same context path such like:
```
Browser --> http://host/galaxy --> http://anotherhost:8080/galaxy
   |           | (This module)            (Galaxy)
   |           ↓
   |        jdbc:postgresql:servicegriddb
   |           ↑
   |           |
   +------> http://host/service_manager
                (Service Grid)
```
