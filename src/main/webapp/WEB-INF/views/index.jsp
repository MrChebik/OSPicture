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
    <title>OSPicture - Hosting the images</title>
    <meta name="description"
          content="To upload images to the Internet. The maximum size of uploads is 10Mb. Supported formats: JPEG, PNG, WebP, BMP, GIF.">
    <meta name="keywords"
          content="хостинг картинок, загрузить картинку, раздать картинку, hosting images, upload image, share image">
    <script type="text/javascript">document.write("<img src='//counter.yadro.ru/hit?t38.6;r" + escape(document.referrer) + ((typeof(screen) == "undefined") ? "" : ";s" + screen.width + "*" + screen.height + "*" + (screen.colorDepth ? screen.colorDepth : screen.pixelDepth)) + ";u" + escape(document.URL) + ";" + Math.random() + "' alt='' style='display: none; position: absolute; left: -99999px'>")</script>
    <script async src="/js/actions_min.js"></script>
    <script async src="/js/ajax_min.js"></script>
    <c:choose>
        <c:when test="${folder == null && key == null}">
            <script async src="/js/drop_min.js"></script>
        </c:when>
        <c:otherwise>
            <script async src="/js/ajax_get_min.js"></script>
        </c:otherwise>
    </c:choose>
    <meta name="yandex-verification" content="63722a67b19d1b95"/>
    <meta name="google-site-verification" content="lPL5nvLypGsZ8ZDIsV_RjeUZ1SRjrHX9bfLkkaEHsJo"/>
    <meta name="wmail-verification" content="196a7986855ac910b031fd120a241c18">
    <meta name="msvalidate.01" content="16BF0EEBCF3387D9C22D717D9FA90F69">
</head>
<body>
${notFound}
<div class="notification">Copied to clipboard</div>
<input class="file-input" multiple type="file" id="file"
       onchange="this.files.length < 2 ? ajax_upload(this.files[0]) : ajax_uploads(this.files)">
<div class="header">
    <div class="nav-header" onclick="actionLogo()">OSPicture</div>
    <div class="toolbox">
        <label title="Upload" class="nav-icon" id="label-upload" for="file">
            <svg x="0px" y="0px" width="38px" height="38px" viewBox="0 0 92 92" class="toolbox-svg">
                <path d="M89,58.8V86c0,2.8-2.2,5-5,5H8c-2.8,0-5-2.2-5-5V58.8c0-2.8,2.2-5,5-5s5,2.2,5,5V81h66V58.8  c0-2.8,2.2-5,5-5S89,56,89,58.8z M29.6,29.9L41,18.2v43.3c0,2.8,2.2,5,5,5s5-2.2,5-5V18.3l11.4,11.6c1,1,2.3,1.5,3.6,1.5  c1.3,0,2.5-0.5,3.5-1.4c2-1.9,2-5.1,0.1-7.1L49.6,2.5C48.6,1.5,47.3,1,46,1c-1.3,0-2.6,0.5-3.6,1.5L22.5,22.9  c-1.9,2-1.9,5.1,0.1,7.1C24.5,31.9,27.7,31.8,29.6,29.9z"/>
            </svg>
        </label>
        <c:if test="${key != null}">
            <c:if test="${folder == null}">
                <a id="download-picture" href='/img/${key}'
                   download="${name}${format.equals('octet-stream') ? '' : '.'}${format.equals('octet-stream') ? '' : format}"></a>
                <div title="Download" class="nav-icon" onclick="actionDownload()">
                    <svg x="0px" y="0px" width="38px" height="38px" viewBox="0 0 92 92" class="toolbox-svg">
                        <path d="M89,58.8V86c0,2.8-2.2,5-5,5H8c-2.8,0-5-2.2-5-5V58.8c0-2.8,2.2-5,5-5s5,2.2,5,5V81h66V58.8  c0-2.8,2.2-5,5-5S89,56,89,58.8z M42.4,65c0.9,1,2.2,1.5,3.6,1.5s2.6-0.5,3.6-1.5l19.9-20.4c1.9-2,1.9-5.1-0.1-7.1  c-2-1.9-5.1-1.9-7.1,0.1L51,49.3V6c0-2.8-2.2-5-5-5s-5,2.2-5,5v43.3L29.6,37.7c-1.9-2-5.1-2-7.1-0.1c-2,1.9-2,5.1-0.1,7.1L42.4,65z"/>
                    </svg>
                </div>
            </c:if>
            <div title="Link" class="nav-icon copy-link"
                 onclick="actionCopyToClipboard('http://ospicture.xyz/${folder != null ? 'folder/' : 'image/'}${key}')">
                <svg x="0px" y="0px" width="38px" height="38px" viewBox="0 0 92 92" class="toolbox-svg">
                    <path d="M77.1,11.6C77.1,5.8,72.3,1,66.4,1c-5.9,0-10.7,4.8-10.7,10.6c0,4.4,2.7,8.2,6.6,9.8c-0.1,1.8-1.2,13-16.9,18.7  C38.3,42.7,33,46.2,30,49.8V21.4c4-1.6,6.4-5.4,6.4-9.8C36.4,5.8,31.5,1,25.7,1c-5.9,0-10.5,4.8-10.5,10.6c0,4.4,2.8,8.2,6.8,9.8  v49.2c-4,1.6-6.9,5.4-6.9,9.8c0,5.8,4.7,10.6,10.5,10.6c5.9,0,10.8-4.8,10.8-10.6c0-4.4-2.5-8.2-6.5-9.8v-3.2  c0-1.1,0.5-13.3,18.3-19.8c21.2-7.7,22.2-24.2,22.2-26.2C74.4,19.8,77.1,16,77.1,11.6z M25.6,7c2.6,0,4.6,2.1,4.6,4.6  s-2.1,4.6-4.6,4.6c-2.6,0-4.6-2.1-4.6-4.6S23,7,25.6,7z M25.6,85c-2.6,0-4.6-2.1-4.6-4.6s2.1-4.6,4.6-4.6c2.6,0,4.6,2.1,4.6,4.6  S28.1,85,25.6,85z M66.4,16.2c-2.6,0-4.6-2.1-4.6-4.6S63.9,7,66.4,7c2.6,0,4.6,2.1,4.6,4.6S69,16.2,66.4,16.2z"/>
                </svg>
            </div>
            <c:if test="${folder == null}">
                <div class="drop-down-link">
                    <div class="nav-drop" onclick="actionCopyToClipboard('http://ospicture.xyz/img/${key}')">Direct
                    </div>
                    <div class="nav-drop html-link"
                         onclick="actionCopyToClipboard('<a href=\'http://ospicture.xyz/image/${key}\'><img src=\'http://ospicture.xyz/img/${key}\' alt=\'Image from OSPicture\' style=\'border-radius:5px\'></a>')">
                        HTML
                    </div>
                    <div class="nav-drop bb2-link"
                         onclick="actionCopyToClipboard('[url=http://ospicture.xyz/image/${key}][img]http://ospicture.xyz/img/${key}[/img][/url]')">
                        BBCode
                    </div>
                </div>
            </c:if>
        </c:if>
    </div>
</div>
<div class="main">
    <c:choose>
        <c:when test="${folder != null}">
            <c:forEach items="${files}" var="file">
                <div class="file" onclick="actionClickInFolder('image/${file.keyFile}')">
                    <img class="image-folder" alt="Image" data-key="${file.keyFile}">
                    <span class="name">${file.originalFilename}.${file.mimeType}</span>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${key != null}">
                    <img class="picture" alt="image" data-key="${key}">
                </c:when>
                <c:otherwise>
                    <form action="/dropImage">
                        <div id="dropZone" ondragenter="dropEnter(event);" ondragover="dropEnter(event);"
                             ondragleave="dropLeave();"
                             ondrop="return doDrop(event);"></div>
                    </form>
                    <p class="bold" onclick="actionImitationClick()">Upload Image</p>
                    <span class="drag-info">Drag files to this page</span>
                    <span class="click-info">Сlick on the button above</span>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</div>
<div class="footer"><c:choose>
    <c:when test="${folder == null && key != null}">
        <span title="File" id="file-info" onclick="actionCopyToClipboard('${name}')">${name}</span>
        <span title="Size">${size}</span>
        <span title="Resolution">${resolution}</span>
        <span title="Format">${format}</span></c:when>
    <c:otherwise>
        <a title="Email" id="email" href="mailto:ospicture@yandex.ru">ospicture@yandex.ru</a>
        <span>@ 2017 OSPicture</span></c:otherwise>
</c:choose>
</div>
</body>
</html>
<link rel="stylesheet" href="/css/stylesheet_min.css">
<c:choose>
    <c:when test="${folder != null}">
        <link rel="stylesheet" href="/css/with_folder_min.css">
    </c:when>
    <c:otherwise>
        <link rel="stylesheet" href="/css/without_folder_min.css">
        <c:choose>
            <c:when test="${key != null}">
                <link rel="stylesheet" href="/css/with_image_min.css">
            </c:when>
            <c:otherwise>
                <link rel="stylesheet" href="/css/without_image_min.css">
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
<%--<script src="/js/jquery.js"></script>--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="/js/ready_min.js"></script>
<c:choose>
    <c:when test="${folder == null && key == null}">
        <script src="/js/ready_drop_min.js"></script>
    </c:when>
    <c:otherwise>
        <script src="/js/ready_get_min.js"></script>
    </c:otherwise>
</c:choose>