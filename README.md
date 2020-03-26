# Quarkus Ngrok Extension

##### What is Ngrok?
Ngrok can create a http tunnel and give you a public URL with redirection to 
specified port on your local machine, which in our case will be a standard springs `http://localhost:8080` 
or whatever you set as `server.port`. For simply usage account is not needed. Tunnels created with 
free version will be available for 8 hours, so it is great tool for development and testing purposes! 
For more details you can check out their [site](https://ngrok.com/).

![](https://github.com/previousdeveloper/quarkus-ngrok-extension/raw/master/image.png)

### Dependency
- maven:

The ngrok extension is not available in Maven Central. For now you have to clone the repository and install the extension in your local maven repository.

```xml
<dependency>
      <groupId>quarkus.extension</groupId>
      <artifactId>ngrok</artifactId>
      <version>0.0.1</version>
</dependency>
```
### Configuration
```xml
ngrok.enabled=true
ngrok.windowsBinaryUrl=string
ngrok.linuxBinaryUrl=string
ngrok.osxBinaryUrl=string
ngrok.binaryCustom=string
ngrok.directory=string
ngrok.http.url=string

```
