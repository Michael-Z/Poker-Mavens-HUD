-libraryjars /Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/rt.jar
-libraryjars /Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/ext/jfxrt.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/antlr-2.7.7.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/aspectjweaver-1.8.9.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/commons-io-2.5.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/commons-lang3-3.6.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/dom4j-1.6.1.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/h2-1.4.193.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/hibernate-commons-annotations-5.0.1.Final.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/hibernate-core-5.0.11.Final.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/hibernate-entitymanager-5.0.11.Final.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/hibernate-jpa-2.1-api-1.0.0.Final.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jandex-2.0.0.Final.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/javassist-3.21.0-GA.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/javax.transaction-api-1.2.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jboss-logging-3.3.0.Final.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jcl-over-slf4j-1.7.22.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jul-to-slf4j-1.7.22.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jxbrowser-6.14.2.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jxbrowser-linux32-6.14.2.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jxbrowser-linux64-6.14.2.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jxbrowser-mac-6.14.2.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/jxbrowser-win32-6.14.2.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/license.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/log4j-over-slf4j-1.7.22.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/logback-classic-1.1.9.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/logback-core-1.1.9.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/proguard.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/slf4j-api-1.7.22.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/snakeyaml-1.17.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-aop-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-aspects-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-beans-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-boot-1.5.1.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-boot-autoconfigure-1.5.1.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-boot-starter-1.5.1.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-boot-starter-aop-1.5.1.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-boot-starter-data-jpa-1.5.1.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-boot-starter-jdbc-1.5.1.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-boot-starter-logging-1.5.1.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-context-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-core-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-data-commons-1.13.0.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-data-jpa-1.11.0.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-expression-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-jdbc-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-orm-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/spring-tx-4.3.6.RELEASE.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/tomcat-dbcp-8.0.30.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/tomcat-jdbc-8.5.11.jar
-libraryjars /Users/arminnaderi/Desktop/obfuscation/libz/tomcat-juli-8.5.11.jar

# Don't modify any classes
-dontshrink
-dontoptimize

# Keep generic types
-keepattributes *Annotation*,Signature

# Allows for easier debugging
-verbose



# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,includedescriptorclasses,allowshrinking class * {
    native <methods>;
}

-keep class !com.chronpwn.pokermavenshud.browser.**,!com.chronpwn.pokermavenshud.hud.** { *; }

-keepclassmembers class * {
  @org.springframework.beans.factory.annotation.Autowired *;
  @org.springframework.beans.factory.annotation.Qualifier *;
  @org.springframework.beans.factory.annotation.Value *;
  @org.springframework.beans.factory.annotation.Required *;
  @org.springframework.context.annotation.Bean *;
  @org.springframework.context.annotation.Primary *;
  @org.springframework.boot.context.properties.ConfigurationProperties *;
  @org.springframework.boot.context.properties.EnableConfigurationProperties *;
  @javax.inject.Inject *;
  @javax.annotation.PostConstruct *;
  @javax.annotation.PreDestroy *;
}

-keep @org.springframework.stereotype.Service public class *
-keep @org.springframework.stereotype.Controller public class *
-keep @org.springframework.stereotype.Component public class *
-keep @org.springframework.stereotype.Repository public class *
-keep @org.springframework.cache.annotation.EnableCaching public class *
-keep @org.springframework.context.annotation.Configuration public class *
-keep @org.springframework.boot.context.properties.ConfigurationProperties public class *
-keep @org.springframework.boot.autoconfigure.SpringBootApplication public class *

# Name required by JS injection code
-keepclassmembernames class com.chronpwn.pokermavenshud.browser.HandHistoryHandler {
  public void handle(java.lang.String);
}