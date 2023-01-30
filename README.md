<h2 align="center">🛠️ A complete, modular and flexible toolset for all your Java ☕ projects</h2>

<div align="center">Nullptr-tools is a collection of tools for Java projects. Its includes a lot of modules, to make it easier for you to include and interact with many commonly used libraries and frameworks.</div>
<div align="center">All modules are separated from the whole project and are standalone, to keep your app lightweight. They have only a common base package included.</div>
<br>
<div align="center">⚠️ The project is still in development, so many modules are planed but not released yet. See "Planned modules and features" below.</div>
<br>
<div align="center">
  <a href="https://github.com/nullptr-rs/nullptr-tools/issues/new?assignees=&labels=bug&template=BUG_REPORT.md&title=bug%3A+">Report a Bug</a>
  ·
  <a href="https://github.com/nullptr-rs/nullptr-tools/discussions/new?assignees=&labels=enhancement&title=feat%3A+">Request a Feature</a>
  ·
  <a href="https://github.com/nullptr-rs/nullptr-tools/discussions/new?assignees=&labels=help%20wanted&title=ask%3A+">Ask a Question</a>
</div>

---

<p align="center">
  <img src="https://img.shields.io/github/workflow/status/nullptr-rs/nullptr-tools/Github%20CI%20-%20Production/master?label=Master%20CI&logo=Github" alt="Master CI"/>
  <img src="https://img.shields.io/github/v/release/nullptr-rs/nullptr-tools?label=Release&logo=Github" alt="Release"/>
  <img src="https://img.shields.io/github/license/nullptr-rs/nullptr-tools?label=License&logo=Github" alt="License">
  <img src="https://img.shields.io/github/deployments/nullptr-rs/nullptr-tools/github-pages?label=Javadoc%20on%20Pages&logo=Github" alt="Pages">
  <img src="https://img.shields.io/github/workflow/status/nullptr-rs/nullptr-tools/Github%20CI%20-%20Development/develop?label=Develop%20CI&logo=Github" alt="Develop CI"/>
<p align="center">
  <img src="https://img.shields.io/github/discussions/nullptr-rs/nullptr-tools?label=Discussions&logo=Github" alt="Discussions">
  <img src="https://img.shields.io/github/issues-raw/nullptr-rs/nullptr-tools?label=Issues&logo=Github" alt="Issues">
  <img src="https://img.shields.io/github/issues-pr-raw/nullptr-rs/nullptr-tools?label=Pull requests&logo=Github" alt="Pull requests">
  <img src="https://img.shields.io/github/stars/nullptr-rs/nullptr-tools?label=Stars&logo=Github" alt="Stars">
  <img src="https://img.shields.io/github/forks/nullptr-rs/nullptr-tools?label=Forks&logo=Github" alt="Forks">
<p align="center">
  <img src="https://deepsource.io/gh/nullptr-rs/nullptr-tools.svg/?label=Active+issues&token=8gtp_2o9u3QVkQK-xezNEtix" alt="Deepsource Active">
  <img src="https://img.shields.io/codefactor/grade/github/nullptr-rs/nullptr-tools/master?label=Code%20quality&logo=Codefactor" alt="Codefactor">
  <img src="https://deepsource.io/gh/nullptr-rs/nullptr-tools.svg/?label=resolved+issues&token=8gtp_2o9u3QVkQK-xezNEtix" alt="Deepsource Resolved">

# 📂 Available modules

- [💼 Tools](#-tools): The main module, a basic common tools base used by all other modules.
- [📦 Redis](#-redis): A module to interact with Redis, including a connection manager and a Pub/Sub listener system.
- [🐳 Docker](#-docker): A module to interact with the Docker client, with wrappers and callbacks for all commands, and a Dockerfile generator. You can easily interact with containers, images, networks, Swarm and volumes.

# 📥 How to get it

⚠️ The project is still in development, so publishing to MavenCentral will be done later. Creating a modular project require to open a new ticket for every module, so it's very long. <br>
📦 All artifacts are available on [Maven Central](https://github.com/nullptr-rs/nullptr-tools), and in [GitHub Packages](https://github.com/nullptr-rs/nullptr-tools/packages/) as fallback.
- If you are using [Gradle](https://gradle.org/), you can get it by adding the following code in your build.gradle:
```groovy
repositories {
    // Maven Central
    mavenCentral()
    
    // My Github Packages, as fallback if there is any problem with Maven Central
    maven {
        url = "https://maven.pkg.github.com/nullptr-rs/nullptr-tools"
        credentials {
            username System.getenv("Your GitHub username")
            password System.getenv("Your GitHub password or personal access token")
        }
    }
}

dependencies {
    implementation "io.github.nullptr:Module ID:Latest version"
}
```
- If you are using [Maven](https://maven.apache.org/), you can get it by adding the following code in your pom.xml:
```xml
<project>
    <repositories>
        <!-- My GitHub Packages, as fallback if there is any problem with my Maven Central -->
        <repository>
            <id>github</id>
            <name>GitHub nullptr-rs Apache Maven Packages</name>
            <url>https://maven.pkg.github.com/nullptr-rs/nullptr-tools</url>
            <credentials>
                <username>Your GitHub username</username>
                <password>Your GitHub password or personal access token</password>
            </credentials>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>io.github.nullptr</groupId>
            <artifactId>Module ID</artifactId>
            <version>Latest version</version>
        </dependency>
    </dependencies>
</project>
```
- If you don't use any build tool, you can get it by downloading the latest release of your wanted module from [GitHub Releases](https://github.com/nullptr-rs/nullptr-tools/releases/latest)

# 🚀 How to use it

## 💼 Tools
- 📦 Module ID: 'tools'
- 📁 Size: ~350Ko
- 📅 Release date: 2022-03-25
- 🧠 Developer: [nullptr-rs](https://github.com/nullptr-rs)
- 📄 Dependencies: [Slf4j API](https://mvnrepository.com/artifact/org.slf4j/slf4j-api), [JetBrains Annotations](https://mvnrepository.com/artifact/org.jetbrains/annotations), [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson)
- 📝 Description: The main module, a basic common tools base used by all other modules.
### 🚦 Getting started
- Check the [Wiki](https://github.com/nullptr-rs/nullptr-tools/wiki) to learn how to use it.

## 📦 Redis
- 📦 Module ID: 'tools-redis'
- 📁 Size: ~1Mo
- 📅 Release date: 2022-03-25
- 🧠 Developer: [nullptr-rs](https://github.com/nullptr-rs)
- 📄 Dependencies: [Jedis](https://mvnrepository.com/artifact/redis.clients/jedis)
- 📝 Description: A module to interact with Redis, including a connection manager and a Pub/Sub listener system.
### 🚦 Getting started
- Create a new instance of the Redis connection through the builder:
```java
final RedisConnection connection = new RedisConnection.Builder().withHost(() -> "127.0.0.1").withPassword(() -> "my-password").build();
```
- Now you can use the connection to interact with Redis:
```java
connection.execute(jedis -> jedis.set("foo", "bar"));

final String foo = connection.executeAndReturn(jedis -> {
    final String value = jedis.get("foo");
    return value + " world";
});
```
- Check the [Wiki](https://github.com/nullptr-rs/nullptr-tools/wiki) to learn how to use Pub/Sub system and advanced features.

## 🐳 Docker
- 📦 Module ID: 'tools-docker'
- 📁 Size: ~9.8Mo
- 📅 Release date: 2022-03-25
- 🧠 Developer: [nullptr-rs](https://github.com/nullptr-rs)
- 📄 Dependencies: [Docker Java](https://mvnrepository.com/artifact/com.github.docker-java/docker-java), [Docker Transport Zerodep](https://mvnrepository.com/artifact/com.github.docker-java/docker-java-transport-zerodep)
- 📝 Description: A module to interact with the Docker client, with wrappers and callbacks for all commands, and a Dockerfile generator. You can easily interact with containers, images, networks, Swarm and volumes.
### 🚦 Getting started
- Create a new instance of the Docker manager through the builder:
```java
final DockerManager docker = new DockerManager.Builder().withHost(DockerHost.TCP_DAEMON).build();
```
- Now you can get the managers for all the Docker commands:
```java
final DockerClient client = docker.getClient();

final DockerInfoManager info = docker.getInfoManager();
final DockerImageManager image = docker.getImageManager();
final DockerSwarmManager swarm = docker.getSwarmManager();
final DockerVolumeManager volume = docker.getVolumeManager();
final DockerNetworkManager network = docker.getNetworkManager();
final DockerContainerManager container = docker.getContainerManager();
```
- Check the [Wiki](https://github.com/nullptr-rs/nullptr-tools/wiki) to learn how to use each manager, custom registry and the Dockerfile generator.

# 🚧 Planned modules and features

- 📝 Yaml and Json configuration creator -> Config
- 🔧 ZooKeeper tools -> ZooKeeper
- ♻️ Placeholder registry in the I18n system -> Tools
- 🗂️ Provider system, to register instances of API -> Tools
- 🖥️ Commands creator for CLI apps -> CLI
- 📂 Reflection system -> Tools
- 📨 Discord bot tools, with prefix and slash commands handling, etc. -> Discord
- 🧭Spigot plugin tools, with messaging, GUI creator, plugin.yml generator, auto register for commands and listeners, etc. -> Spigot
- 🔐 Encryption tools, to secure your files and data -> Security
- 📦 Kafka tools -> Kafka
- 📈 MySQL tools -> MySQL
- 📊 MongoDB tools -> MongoDB
- 📈 PostgreSQL tools -> PostgreSQL
- 🏗️ Spigot-like build tools creator -> Build tools
- 🔌 Spigot-like plugin system -> Tools

# 💬 Any problem ?

You can open a [Discussion](https://github.com/nullptr-rs/nullptr-tools/discussions/new?assignees=&labels=help%20wanted&title=ask%3A+), and ask for help.

# 🐛 Find a bug ?

You can open an [Issue](https://github.com/nullptr-rs/nullptr-tools/issues/new?assignees=&labels=bug&template=BUG_REPORT.md&title=bug%3A+), describe the problem, and report it.

# ⭐ Support the project

If you like the project, you can leave a star, or consider [sponsoring](https://github.com/sponsors/nullptr-rs) me.

# 🤝 Contribute to the project

The project is open-source, so you can fork it and open a pull request to add new features or fix bugs.

# 📝 License

The project is released under the [GNU GPLv3](https://github.com/nullptr-rs/nullptr-tools/blob/master/LICENSE.md) license. To learn more about it and understand what it commits you to, check [this page](https://choosealicense.com/licenses/gpl-3.0/).

# 👨‍💻 Contributors

| [<img src="https://avatars.githubusercontent.com/u/56512795?s=128&v=4"><br><sub>@nullptr-rs</sub>](https://github.com/nullptr-rs) |
|:---------------------------------------------------------------------------------------------------------------------------------:|