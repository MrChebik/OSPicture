# OSPicture
[![Build Status](https://travis-ci.org/MrChebik/OSPicture.svg?branch=master)](https://travis-ci.org/MrChebik/OSPicture)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d403a319ed024a21b8db2956c7f97087)](https://www.codacy.com/app/mrchebik/OSPicture?utm_source=github.com&utm_medium=referral&utm_content=MrChebik/OSPicture&utm_campaign=badger)
[![codebeat badge](https://codebeat.co/badges/a41dc171-5bd3-49af-b910-2ec60f18e147)](https://codebeat.co/projects/github-com-mrchebik-ospicture-master)
[![Code Climate](https://codeclimate.com/github/MrChebik/OSPicture/badges/gpa.svg)](https://codeclimate.com/github/MrChebik/OSPicture)
[![Issue Count](https://codeclimate.com/github/MrChebik/OSPicture/badges/issue_count.svg)](https://codeclimate.com/github/MrChebik/OSPicture)
> Hosting the images with multiply uploads, checking of checksum, lossless optimization.

## Demonstation
![OSPicture - WebP animation](https://github.com/MrChebik/OSPicture/blob/master/ospicture-demonstration.webp?raw=true)
Home - Image - Full Screen Image - Folder

## Getting Started
1. Setting _utf8_ encoding in _MySQL_:
    1. Go to _/etc/my.cnf_
    2. Remove ___;___ before _collation_server_ and _character_set_server_, or add this:
```
[mysql]
default-character-set = utf8
  
[mysqld]
collation_server = utf8_unicode_ci
character_set_server = utf8
```
2. Install image optimizations libraries and ImageMagick:
    * My tips for ___RPM___ (_CentOS6_ and _Mageia_): [mozjpeg](https://gist.github.com/MrChebik/d5cd2920d49415122376ef2f600907ce) and [optipng](https://gist.github.com/MrChebik/8c3594a521898b889d8acf4f419cbcbc)
    * Also you can find manuals on their pages:
        [ImageMagick](https://github.com/ImageMagick/ImageMagick)
        [mozjpeg](https://github.com/mozilla/mozjpeg)
        [optipng](http://optipng.sourceforge.net)

## Run
1. Clone repository:
```
$ git clone https://github.com/MrChebik/OSPicture.git
$ cd OSPicture
```
2. Launch:
```
$ mvn spring-boot:run \
--Dspring.datasource.username=USERNAME_DB \
--Dspring.datasource.password=PASSWORD_DB \
--Dpath.pictures=FOLDER_TO_STORE_IMAGES
```
* You can change properties in [__application.properties__](https://github.com/MrChebik/OSPicture/blob/master/src/main/resources/application.properties) and run without this parameters.
3. Follow to the link: [_http://localhost:8446/_](http://localhost:8446/)



## License
This project is licensed under the GPL-3.0 License - see the [LICENSE](https://github.com/MrChebik/OSPicture/blob/master/LICENSE) file for details.

## Tasks
- [ ] B+ Tree structure of pictures. 