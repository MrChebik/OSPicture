<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mrchebik
  Date: 15.05.17
  Time: 12:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>OSPicture</title>
    <meta name="description" content="To download or upload an image to the Internet. The maximum size of uploads is 10Mb. Supported formats: JPEG, PNG, WebP, BMP, GIF.">
    <meta name="keywords" content="хостинг, hosting, jpg, jpeg, webp, gif, png, image, images, download, free, upload, image, скачать, загрузить, бесплатно, картинка, картинки, фото, изображение, picture, format, облако, cloud, downloads, pictures, фотки, изображения, share, раздать, osp, ospicture">
    <script type="text/javascript"> (function (d, w, c) { (w[c] = w[c] || []).push(function() { try { w.yaCounter44795542 = new Ya.Metrika({ id:44795542, clickmap:true, trackLinks:true, accurateTrackBounce:true, webvisor:true }); } catch(e) { } }); var n = d.getElementsByTagName("script")[0], s = d.createElement("script"), f = function () { n.parentNode.insertBefore(s, n); }; s.type = "text/javascript"; s.async = true; s.src = "https://mc.yandex.ru/metrika/watch.js"; if (w.opera == "[object Opera]") { d.addEventListener("DOMContentLoaded", f, false); } else { f(); } })(document, window, "yandex_metrika_callbacks"); </script> <noscript><div><img src="https://mc.yandex.ru/watch/44795542" style="position:absolute; left:-9999px;" alt="" /></div></noscript>
    <script async src="/resources/js/ajax_min.js"></script>
    <script async src="/resources/js/actions_min.js"></script>
    <meta name="yandex-verification" content="63722a67b19d1b95" />
    <meta name="google-site-verification" content="lPL5nvLypGsZ8ZDIsV_RjeUZ1SRjrHX9bfLkkaEHsJo" />
    <meta name="wmail-verification" content="196a7986855ac910b031fd120a241c18">
    <meta name="msvalidate.01" content="16BF0EEBCF3387D9C22D717D9FA90F69">
</head>
<body>
<div class="header">
    <div class="nav-header" onclick="actionLogo()">
        OSPicture
    </div>
    <div class="toolbox">
    <input class="file-input" multiple="true" type="file" id="file" onchange="this.files.length < 2 ? ajax_upload(this.files[0]) : ajax_uploads(this.files)"><label class="nav-header" for="file"><img class="icon" src="/resources/img/upload.png" alt="Upload"></label>
    <c:if test="${key != null}">
        <c:if test="${folder == null}">
            <a id="download-picture" href='/img/${key}' download="${name}${format.equals('octet-stream') ? '' : '.'}${format.equals('octet-stream') ? '' : format}"></a>
            <div class="nav-header" onclick="actionDownload()"><img class="icon tool download-img" src="/resources/img/download.png" alt="Download"></div>
        </c:if>
        <div class="nav-header" onclick="copyToClipboard('http://ospicture.xyz/${folder != null ? '/folder/' : ''}${key}')"><img class="icon tool link-img" src="/resources/img/link.png" alt="Copy link"></div>
    </c:if>
    </div>
</div>
<div class="main">
    <c:choose>
        <c:when test="${folder != null}">
            <c:forEach items="${files}" var="file">
                <div class="file" onclick="actionClickInFolder('${file.keyFile}')">
                    <img class="image-folder" src="/img_min/${file.keyFile}" alt="Image">
                    <span class="name">${file.originalFilename.length() > 18 ? (file.originalFilename.substring(0, 16).concat('..')) : file.originalFilename}.${file.mimeType}</span>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${key != null}">
                    <img class="picture" src="/img/${key}" alt="image">
                </c:when>
                <c:otherwise>
                    <form action="/dropImage" enctype="multipart/form-data">
                        <div id="dropZone"
                             ondragenter="dropEnter(event);" ondragover="dropEnter(event);"
                             ondragleave="dropLeave();" ondrop="return doDrop(event);">
                        </div>
                    </form>
                    <p class="bold">Upload Image</p>
                    <span class="drag-info">Drag files to this page</span>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</div>
<div class="footer">
    <c:choose>
        <c:when test="${folder == null && key != null}">
            <span>File: ${name}</span>
            <span>Size: ${size}</span>
            <span>Resolution: ${resolution}</span>
            <span>Format: ${format}</span>
        </c:when>
        <c:otherwise><span>Email: ospicture@yandex.ru</span><span>@ 2017 OSPicture</span></c:otherwise>
    </c:choose>
</div>
</body>
</html>
<link rel="stylesheet" href="/resources/css/stylesheet_min.css">
<c:choose>
    <c:when test="${folder != null}">
        <link rel="stylesheet" href="/resources/css/with_folder_min.css">
    </c:when>
    <c:otherwise>
        <link rel="stylesheet" href="/resources/css/without_folder_min.css">
        <c:choose>
            <c:when test="${key != null}">
                <link rel="stylesheet" href="/resources/css/with_image_min.css">
            </c:when>
            <c:otherwise>
                <link rel="stylesheet" href="/resources/css/without_image_min.css">
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="/resources/js/drop_min.js"></script>