# monitoraggio-manutenzione-quarkus project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Packaging the application

The application can be packaged using:

```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

### Running application

Find the .jar file in releases version, or build with mvn as described in previous point.

The application is now runnable using `java -jar monitoraggio-manutenzione-quarkus-1.0.0-runner.jar`.

### Creating a native executable

You can create a native executable using: 

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/monitoraggio-manutenzione-quarkus-1.0.0-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Installer guide

### Get the application

Find the .jar file in releases version or build with mvn as described in " build an _über-jar_ " .

### Configure application

Open the .jar file with an archive explorer program (for example: 7Zip). Inside the jar there are 2 files to configure:

#### Configure application.properties

application.properties is located on root of jar file. This file is responsible of Application Configs and Database connection.

Configure the Database connection changing this section:

```properties
%prod.quarkus.datasource.health.enabled=true
%prod.quarkus.datasource.db-kind=mysql 
%prod.quarkus.datasource.username=<your-username>
%prod.quarkus.datasource.password=<yout-password>
%prod.quarkus.datasource.jdbc.url=jdbc:mysql://<your-host>:<your-port>/<your-dbname>
%prod.quarkus.datasource.jdbc.max-size=10
```

An example is 

```properties
%prod.quarkus.datasource.health.enabled=true
%prod.quarkus.datasource.db-kind=mysql 
%prod.quarkus.datasource.username=masper
%prod.quarkus.datasource.password=31fu1iHu
%prod.quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/masper
%prod.quarkus.datasource.jdbc.max-size=10
```

p.s __Not__ edit the values starting with  `%dev` (these are only for developing)

Configure Mail

```properties
%prod.quarkus.mailer.auth-methods=DIGEST-MD5 CRAM-SHA256 CRAM-SHA1 CRAM-MD5 PLAIN LOGIN
%prod.quarkus.mailer.from=<Robot mail>
%prod.quarkus.mailer.host=<smtp/pop3 host name>
%prod.quarkus.mailer.port=111
%prod.quarkus.mailer.ssl=true
%prod.quarkus.mailer.username=<Robot mail username>
%prod.quarkus.mailer.password=<Robot mail password>
%prod.quarkus.mailer.mock=false
```

An example for a Gmail testing

```properties
%prod.quarkus.mailer.auth-methods=DIGEST-MD5 CRAM-SHA256 CRAM-SHA1 CRAM-MD5 PLAIN LOGIN
%prod.quarkus.mailer.from=simple.travel.site.quarkus@gmail.com
%prod.quarkus.mailer.host=smtp.gmail.com
%prod.quarkus.mailer.port=465
%prod.quarkus.mailer.ssl=true
%prod.quarkus.mailer.username=simple.travel.site.quarkus@gmail.com
%prod.quarkus.mailer.password=lzgmvusaovttbxkg
%prod.quarkus.mailer.mock=false
```

p.s __Not__ edit the values starting with  `%dev` (these are only for developing)

Configure cron task expression:

```properties
cron.expr.pompe=<your expression for pump process>
cron.expr.macchine=<your expression for machines process>
```

An example for repeat the process any 30 seconds

```properties
cron.expr.pompe=*/30 * * * * ?
cron.expr.macchine=*/30 * * * * ?
```

For syntax and more information visit: 
http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/crontrigger.html

#### Configure microprofile-config.properties

Location for microprofile-config.properties file is under /META-INF/microprofile-config.properties. This file is responsible for Logics configurations

`max.bar.pompe`

Configure the max pressure BAR, if in the DB the value for BAR field is upper then set the entry will be added in notice mail.

This example the max value is set on 80, all upper value (81 and major) will be considered as notifiable.

```properties
max.bar.pompe=80
```


`monitor.mail.subject`

This will be used as subject for the mail, in the following example the mail subject will be "Monitoraggio e Manutenzione"

```properties
monitor.mail.subject=Monitoraggio e Manutenzione
```

`monitor.mail.addresses`

This field needs to declare all email addresses that will be used as receiver, in the example these addresses will be noticed if the procedure gather some data.

Use ; character as separator between addresses

```properties
monitor.mail.addresses=user1@mail.com;user2@mail.com;user3@mail.com
```

### Finish the configuration

After changed the file overwrite the existing ones in the .jar file and close the archive. The configuration is completed

### Run the application

In order to run this application you need Java 11 or major, you can find the free open JDK here:

https://jdk.java.net/

For start application simple run in a bash/prompt/shell

```shell script
java -jar monitoraggio-manutenzione-quarkus-1.0.0-runner.jar
```

This is for version 1.0.0, the number can be change in future

The shell application must keep run in order to repeat the process as defined with cron task expression, and the shell host must be connected to internet for sending mails.
