## How To Configure

* add serviceAccount.json in glassfish6/glassfish/domains/domains1/config
* add cacerts.jks in glassfish6/glassfish/domains/domains1/config
* make sure that domain1 jvm options are the following
  (http://localhost:4848 -> configurations/server-config/jvm/options)

   -Djavax.net.ssl.trustStore=${com.sun.aas.instanceRoot}/config/cacerts.jks
   --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED
   --add-opens=java.base/sun.net.www.protocol.jrt=ALL-UNNAMED
   --add-opens=java.base/java.lang=ALL-UNNAMED
   --add-opens=java.base/java.util=ALL-UNNAMED
   --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED
   -Djdk.corba.allowOutputStreamSubclass=true
   -Djdk.tls.rejectClientInitiatedRenegotiation=true
   -Djavax.management.builder.initial=com.sun.enterprise.v3.admin.AppServerMBeanServerBuilder
   -Djavax.net.ssl.trustStorePassword=changeit
   -Dosgi.shell.telnet.maxconn=1
   -Dcom.sun.enterprise.config.config_environment_factory_class=com.sun.enterprise.config.serverbeans.AppserverConfigEnvironmentFactory
   -Dgosh.args=--nointeractive
   -Dorg.glassfish.additionalOSGiBundlesToStart=org.apache.felix.shell,org.apache.felix.gogo.runtime,org.apache.felix.gogo.shell,org.apache.felix.gogo.command,org.apache.felix.shell.remote,org.apache.felix.fileinstall
   -Djava.security.auth.login.config=${com.sun.aas.instanceRoot}/config/login.conf
   -Dmaven.wagon.http.ssl.allowall=true
   -Dfelix.fileinstall.disableConfigSave=false
   -Djava.security.policy=${com.sun.aas.instanceRoot}/config/server.policy
   -Dfelix.fileinstall.bundles.new.start=true
   -Djavax.xml.accessExternalSchema=all
   -Dosgi.shell.telnet.port=6666
   -Dfelix.fileinstall.log.level=2
   -Djava.awt.headless=true
   -Dfelix.fileinstall.poll=5000
   -Djdbc.drivers=org.apache.derby.jdbc.ClientDriver
   -XX:+UnlockDiagnosticVMOptions
   -Dfelix.fileinstall.dir=${com.sun.aas.installRoot}/modules/autostart/
   -Xbootclasspath/a:${com.sun.aas.installRoot}/lib/grizzly-npn-api.jar
   --add-exports=java.naming/com.sun.jndi.ldap=ALL-UNNAMED
   -Djavax.net.ssl.keyStore=${com.sun.aas.instanceRoot}/config/keystore.jks
   -DANTLR_USE_DIRECT_CLASS_LOADING=true
   -Djavax.net.ssl.keyStorePassword=changeit
   -Xmx512m
   -Dorg.glassfish.gmbal.no.multipleUpperBoundsException=true
   -Dfelix.fileinstall.bundles.startTransient=true
   --add-opens=java.naming/javax.naming.spi=ALL-UNNAMED
   -Dcom.ctc.wstx.returnNullForDefaultNamespace=true
   -Dosgi.shell.telnet.ip=127.0.0.1
   -Dcom.sun.enterprise.security.httpsOutboundKeyAlias=s1as
   -Dmaven.wagon.http.ssl.insecure=true
   -Djava.net.preferIPv4Stack=true
   -XX:NewRatio=2
