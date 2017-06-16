# OSPicture
Hosting the [images](http://ospicture.xyz/image/31b548211f) with [multiply uploads](http://ospicture.xyz/folder/08lahwa3cv), lossless optimization and less instances (200px, 500px and 400x320 for folders)

## Run
1. Create database ___OSPicture___ with _utf8_ encoding;
```
CREATE DATABASE OSPicture DEFAULT CHARACTER SET 'utf8' DEFAULT COLLATE 'utf8_unicode_ci';
```
2. In [__Utils__](https://github.com/MrChebik/OSPicture/blob/master/src/main/java/ru/mrchebik/bean/Utils.java) class, change _PATH_ variable to your folder in hard disk to store images;
3. In [__actions_min.js__](https://github.com/MrChebik/OSPicture/blob/master/src/main/resources/static/js/actions_min.js) ([__actions.js__](https://github.com/MrChebik/OSPicture/blob/master/src/main/resources/static/js/actions.js)), change _site_ variable to your host and port address (i.e. _http://localhost:8446/_);
4. In [__applications.properties__](https://github.com/MrChebik/OSPicture/blob/master/src/main/resources/application.properies):
    1. Change _server.port_ to your port you used in the third stage.
    2. Change _spring.datasource.url_ if you have own database.
    3. Change _spring.datasource.username_ and _spring.datasource.password_ to access your MySQL.
5. Execute in folder project:
```
mvn package
java -jar ./target/original*.war
```
6. Install image optimizations libraries and ImageMagick. My tips for ___RPM___ (_CentOS6_ and _Mageia_): [mozjpeg](https://gist.github.com/MrChebik/d5cd2920d49415122376ef2f600907ce) [optipng](https://gist.github.com/MrChebik/8c3594a521898b889d8acf4f419cbcbc), or you can find manuals on their pages below.
7. Follow to the link: [_http://localhost:8446_](http://localhost:8446/) or other you used in the third stage.

## Used libraries/projects
* [ImageMagick](https://github.com/ImageMagick/ImageMagick)
* [mozjpeg](https://github.com/mozilla/mozjpeg)
* [optipng](http://optipng.sourceforge.net)
