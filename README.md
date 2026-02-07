# operaton-rewrite
OpenRewrite recipes for Operaton upgrades (1.0.x → 2.0.x), resolution of deprecations and best practices.

# Using the snapshot version

To use the latest snapshot version of the Operaton Rewrite recipes, add the following dependency to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.operaton</groupId>
        <artifactId>operaton-rewrite</artifactId>
        <version>x.y.z-SNAPSHOT</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <name>Central Portal Snapshots</name>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

```
