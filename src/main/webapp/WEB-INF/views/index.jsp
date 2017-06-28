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
          content="хостинг картинок, загрузить картинку, раздать картинку, hosting images, upload image, share image, ospicture">
    <script async src="/js/actions_min.js"></script>
    <script async src="/js/ajax_min.js"></script>
    <script async src="/js/drop_min.js"></script>
    <meta name="yandex-verification" content="63722a67b19d1b95"/>
    <meta name="google-site-verification" content="lPL5nvLypGsZ8ZDIsV_RjeUZ1SRjrHX9bfLkkaEHsJo"/>
    <meta name="wmail-verification" content="196a7986855ac910b031fd120a241c18">
    <meta name="msvalidate.01" content="16BF0EEBCF3387D9C22D717D9FA90F69">
</head>
<body ondragenter="dropEnter(event);" ondragover="dropEnter(event);"
      ondragleave="dropLeave();"
      ondrop="return doDrop(event);">
<div class="notification">Copied to clipboard</div>
<input class="file-input" multiple type="file" id="file"
       onchange="this.files.length < 2 ? ajaxUpload(this.files[0]) : ajaxUploads(this.files)">
<div id="dropZone" ondragenter="dropEnter(event);" ondragover="dropEnter(event);"
     ondragleave="dropLeave();"
     ondrop="return doDrop(event);">
    <div class="border"></div>
</div>
<div class="windw">
    <div class="header">
        <div class="toolbox">
            <a class="nav-header" href="/" tabindex="-1">OSPicture</a>
            <c:if test="${isFromFolder != null}">
                <a class="nav-header" href="/folder/${isFromFolder}" tabindex="-1">Folder</a>
            </c:if>
        </div>
        <div class="toolbox toolbox-center">
            <div class="upload-box">
                <label title="Upload" class="nav-icon" id="label-upload" for="file">
                    <svg x="0px" y="0px" width="38px" height="38px" viewBox="0 0 92 92" class="toolbox-svg">
                        <path d="M89,58.8V86c0,2.8-2.2,5-5,5H8c-2.8,0-5-2.2-5-5V58.8c0-2.8,2.2-5,5-5s5,2.2,5,5V81h66V58.8  c0-2.8,2.2-5,5-5S89,56,89,58.8z M29.6,29.9L41,18.2v43.3c0,2.8,2.2,5,5,5s5-2.2,5-5V18.3l11.4,11.6c1,1,2.3,1.5,3.6,1.5  c1.3,0,2.5-0.5,3.5-1.4c2-1.9,2-5.1,0.1-7.1L49.6,2.5C48.6,1.5,47.3,1,46,1c-1.3,0-2.6,0.5-3.6,1.5L22.5,22.9  c-1.9,2-1.9,5.1,0.1,7.1C24.5,31.9,27.7,31.8,29.6,29.9z"></path>
                    </svg>
                </label>
                <div class="url-box">
                    <input class="url-input" type="text" placeholder="URL" onkeydown="actionSendURL(event)">
                </div>
            </div>
        </div>
        <c:if test="${key != null}">
            <div class="toolbox">
                <c:choose>
                    <c:when test="${folder == null}">
                        <a id="download-picture" href='/img/${key}'
                           download="${name}${isOctetStream ? '' : '.'}${isOctetStream ? '' : format}"></a>
                    </c:when>
                    <c:otherwise>
                        <a id="download-picture" href='/zip_folder/${key}'
                           download="${key}.zip"></a>
                    </c:otherwise>
                </c:choose>
                <div title="Download" class="nav-icon" onclick="actionDownload()">
                    <svg x="0px" y="0px" width="38px" height="38px" viewBox="0 0 92 92" class="toolbox-svg">
                        <path d="M89,58.8V86c0,2.8-2.2,5-5,5H8c-2.8,0-5-2.2-5-5V58.8c0-2.8,2.2-5,5-5s5,2.2,5,5V81h66V58.8  c0-2.8,2.2-5,5-5S89,56,89,58.8z M42.4,65c0.9,1,2.2,1.5,3.6,1.5s2.6-0.5,3.6-1.5l19.9-20.4c1.9-2,1.9-5.1-0.1-7.1  c-2-1.9-5.1-1.9-7.1,0.1L51,49.3V6c0-2.8-2.2-5-5-5s-5,2.2-5,5v43.3L29.6,37.7c-1.9-2-5.1-2-7.1-0.1c-2,1.9-2,5.1-0.1,7.1L42.4,65z"></path>
                    </svg>
                </div>
                <c:if test="${folder == null}">
                    <div class="nav-icon" onclick="actionRotateZ(-90)">
                        <svg width="38px" height="38px" viewBox="0 0 22 22" class="rotate">
                            <polyline points="1 4 1 10 7 10"></polyline>
                            <path d="M3.51,15A9,9,0,1,0,5.64,5.64L1,10"></path>
                        </svg>
                    </div>
                    <div class="nav-icon" onclick="actionRotateZ(90)">
                        <svg width="38px" height="38px" viewBox="2 0 22 22" class="rotate">
                            <polyline points="23 4 23 10 17 10"></polyline>
                            <path d="M20.49,15a9,9,0,1,1-2.12-9.36L23,10"></path>
                        </svg>
                    </div>
                </c:if>
                <div title="Link" class="nav-icon copy-link"
                     onclick="actionCopyToClipboard(window.location.href)">
                    <svg x="0px" y="0px" width="38px" height="38px" viewBox="0 0 92 92" class="toolbox-svg">
                        <path d="M77.1,11.6C77.1,5.8,72.3,1,66.4,1c-5.9,0-10.7,4.8-10.7,10.6c0,4.4,2.7,8.2,6.6,9.8c-0.1,1.8-1.2,13-16.9,18.7  C38.3,42.7,33,46.2,30,49.8V21.4c4-1.6,6.4-5.4,6.4-9.8C36.4,5.8,31.5,1,25.7,1c-5.9,0-10.5,4.8-10.5,10.6c0,4.4,2.8,8.2,6.8,9.8  v49.2c-4,1.6-6.9,5.4-6.9,9.8c0,5.8,4.7,10.6,10.5,10.6c5.9,0,10.8-4.8,10.8-10.6c0-4.4-2.5-8.2-6.5-9.8v-3.2  c0-1.1,0.5-13.3,18.3-19.8c21.2-7.7,22.2-24.2,22.2-26.2C74.4,19.8,77.1,16,77.1,11.6z M25.6,7c2.6,0,4.6,2.1,4.6,4.6  s-2.1,4.6-4.6,4.6c-2.6,0-4.6-2.1-4.6-4.6S23,7,25.6,7z M25.6,85c-2.6,0-4.6-2.1-4.6-4.6s2.1-4.6,4.6-4.6c2.6,0,4.6,2.1,4.6,4.6  S28.1,85,25.6,85z M66.4,16.2c-2.6,0-4.6-2.1-4.6-4.6S63.9,7,66.4,7c2.6,0,4.6,2.1,4.6,4.6S69,16.2,66.4,16.2z"></path>
                    </svg>
                </div>
                <c:if test="${folder == null}">
                    <div class="drop-down-link">
                        <div id="direct-link" class="nav-drop"
                             onclick="actionCopyToClipboard(site + 'img/${key}.${format}')">
                            Direct
                        </div>
                        <div id="html-link" class="nav-drop html-link"
                             onclick="actionCopyToClipboard('<a href=\'' + window.location.href + '\'><img src=\'' + site + 'img/${key}.${format}\' alt=\'Image from OSPicture\'></a>')">
                            HTML
                        </div>
                        <div id="bbcode-link" class="nav-drop bb2-link"
                             onclick="actionCopyToClipboard('[url=' + window.location.href + '][img]' + site + 'img/${key}.${format}[/img][/url]')">
                            BBCode
                        </div>
                    </div>
                </c:if>
            </div>
        </c:if>
    </div>
    <div class="main" data-key="${key}" data-format="${format}" data-left="${folderLeft}" data-right="${folderRight}">
        <c:choose>
            <c:when test="${folder != null}">
                <c:forEach items="${files}" var="file">
                    <a class="file" data-key="${file.filename}" data-format="${file.format}"
                       href="/image/${file.filename}"></a>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:if test="${key == null}">
                    <p class="bold" onclick="actionImitationClick()">Upload Image</p>
                    <span class="drag-info">Drag files to this page</span>
                    <span class="click-info">Сlick on the button above</span>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
    <c:if test="${key != null}">
        <div class="popup-footer"></div>
    </c:if>
    <div class="footer">
        <c:choose>
            <c:when test="${folder == null && key != null}">
                <div class="types">
                    <a id="px200" class="type-px" href="/${px200Path}"
                       tabindex="-1">${px200TRUE == null ? '200px' : 'INITIAL'}</a>
                    <a id="px500" class="type-px" href="/${px500Path}"
                       tabindex="-1">${px500TRUE == null ? '500px' : 'INITIAL'}</a>
                </div>
                <div class="info">
                    <span title="File" id="file-info" onclick="actionCopyToClipboard('${name}')">${name}</span>
                    <span title="Size" id="size">${size}</span>
                    <span title="Resolution" id="resolution">${resolution}</span>
                    <span title="Format" id="format">${isOctetStream ? 'octet-stream' : format}</span>
                </div>
            </c:when>
            <c:otherwise>
                <a title="Email" id="email" href="mailto:mrchebik@yandex.ru" tabindex="-1">mrchebik@yandex.ru</a>
                <a class="github" href="https://github.com/MrChebik/OSPicture" tabindex="-1">
                    <svg x="0px" y="0px" width="38px" height="38px" viewBox="0 0 16 16" class="toolbox-svg">
                        <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"></path>
                    </svg>
                </a>
                <span>@ 2017 OSPicture</span></c:otherwise>
        </c:choose>
    </div>
    <c:if test="${isFromFolder != null}">
        <div class="arrow-box">
            <div id="arrow-left" class="nav-icon" onclick="ajaxGetInfo(mainReady.data('left'), 'left')">
                <svg width="38px" height="38px" viewBox="0 0 22 22" class="rotate">
                    <line x1="20" y1="12" x2="4" y2="12"></line>
                    <polyline points="10 18 4 12 10 6"></polyline>
                </svg>
            </div>
            <div id="arrow-right" class="nav-icon" onclick="ajaxGetInfo(mainReady.data('right'), 'right')">
                <svg width="38px" height="38px" viewBox="0 0 22 22" class="rotate">
                    <line x1="4" y1="12" x2="20" y2="12"></line>
                    <polyline points="14 6 20 12 14 18"></polyline>
                </svg>
            </div>
        </div>
    </c:if>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="/js/ready_min.js"></script>
<script src="/js/ready_drop_min.js"></script>
<c:if test="${folder != null || key != null}">
    <script src="/js/ready_get_min.js"></script>
    <script src="/js/rotate_min.js"></script>
</c:if>
<c:if test="${isFromFolder != null}">
    <script src="/js/arrows_min.js"></script>
</c:if>