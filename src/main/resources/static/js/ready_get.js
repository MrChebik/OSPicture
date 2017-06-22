var notifDownload = 0, rotateDeg = 0, resolution, mainReady = $('.main'), transitSetting = "opacity .05s, transform .2s ease-in, box-shadow .2s ease-in", isResolution, footer = $('.footer'), isBlack, typeAnimation, isProcessing = false;
notification = $('.notification');
notification.css("display", "block");
setTimeout(function () {
    if ($('.arrow-box').length) {
        $('.footer').css("bottom", "-44px");
    }
    if ($('#download-picture').length) {
        addListenerDownload(mainReady, 'picture');
    } else if ($('.file').length) {
        var files_pictures = $('.file');
        for (var i = 0; i < files_pictures.length; i++) {
            addListenerDownload($(files_pictures[i]), 'image-folder')
        }
    }
}, 20);

function settingPicture() {
    mainReady.css("top", "0");
    mainReady.css("bottom", "0");
    if ($('.main').length < 2) {
        mainReady.css("left", "0");
        mainReady.css("right", "0");
    } else {
        if (typeAnimation == 'left') {
            mainReady.css("left", "-100%");
            mainReady.css("right", "100%");
        } else {
            mainReady.css("left", "100%");
            mainReady.css("right", "-100%");
        }
    }
    if (screen.width > 480) {
        footer.css("bottom", "-44px");
    }
    footer.css("background-color", "rgba(0,0,0,0.7)");
    $('body').css("background-color", "black");
    $('body').css("transition", "background-color .2s");
}

function addListenerDownload(element, type) {
    var isPicture = type == 'picture';
    if (isPicture) {
        resolution = $('#resolution').text().split("x");
        setTimeout(function () {
            if (picture == undefined) {
                if (notifDownload == 0) {
                    mainReady.append($("<div/>", {class: "flowspinner"}), $("<span/>", {class: "download-info"}).text("Downloading"));
                    notifDownload = 1;
                }
            } else {
                if ($(".main").length > 1) {
                    $(".picture").first().css("filter", "brightness(40%)");
                    $(".main").first().append($("<div/>", {class: "flowspinner"}));
                    notifDownload = 1;
                }
            }
        }, 400);
    }
    var img = $("<img />", {
        'class': type,
        'alt': 'Image'
    }).attr('src', (isPicture ? '/img/' : '/img_min/') + element.data('key') + '.' + element.data('format'))
        .on('load', function () {
            if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
                element.append($('<span />').text('Broken image'));
            } else {
                if (isPicture) {
                    notifDownload = 1;
                    if ($('.flowspinner').length) {
                        $(".flowspinner").remove();
                        $(".download-info").remove()
                    }
                }
                element.append(img[0]);
                if (isPicture) {
                    picture = $('.picture').last();
                    calculateView(0, 1);
                    if ($('.main').length > 1 && mainReady.css("top") != "0px") {
                        if (typeAnimation == 'right') {
                            mainReady.css("left", "calc(100% + 25px)");
                            mainReady.css("right", "calc(-100% + 25px");
                        } else {
                            mainReady.css("left", "calc(-100% + 25px)");
                            mainReady.css("right", "calc(100% + 25px");
                        }
                    }
                    if (mainReady.css("top") != "0px") {
                        $('.footer').css("bottom", $('.arrow-box').length ? "-44px" : "0px");
                        $('.footer').css("background-color", "transparent");
                    }
                }
                setTimeout(function () {
                    if ($('.main').length < 2) {
                        if (isPicture) {
                            img[0].style.transition = transitSetting + ",  max-width .2s ease-in, max-height .2s ease-in";
                        } else {
                            img[0].style.transition = "filter .2s ease-in, " + transitSetting;
                        }
                    }
                    img[0].style.transform = "rotateX(0deg)";
                    img[0].style.boxShadow = "none";
                    img[0].style.opacity = "1";
                    if (isPicture) {
                        setTimeout(function () {
                            mainReady.css("transition", "top .2s, bottom .2s, left .2s, right .2s");
                            img[0].style.transition = transitSetting + ",  max-width .2s ease-in, max-height .2s ease-in";
                            if ($('.main').length > 1) {
                                if (typeAnimation == 'left') {
                                    $('.main')[0].style.left = "100%";
                                    $('.main')[0].style.right = "-100%";
                                } else {
                                    $('.main')[0].style.left = "-100%";
                                    $('.main')[0].style.right = "100%";
                                }
                                if (mainReady.css("top") == "0px") {
                                    $('body').css("background-color", "black");
                                    mainReady.css("left", "0px");
                                    mainReady.css("right", "0px");
                                } else {
                                    $('body').css("background-color", "#34495E");
                                    mainReady.css("left", "25px");
                                    mainReady.css("right", "25px");
                                }
                                setTimeout(function () {
                                    $('.main').first().remove();
                                    isProcessing = false;
                                    main = $('.main');
                                    rotateDeg = 0;
                                }, 200);
                            } else {
                                isProcessing = false;
                            }
                        }, 40);
                        $(window).resize(function() {
                            calcViewRotateDeg();
                        });
                    }
                }, 20);
            }
        });
}

function calculateView(x, y) {
    isResolution = resolution[x] > resolution[y];
    if (window.innerWidth <= resolution[x] && window.innerHeight <= resolution[y]) {
        var isWindowInner = window.innerWidth > window.innerHeight, divideResolution = (resolution[x] / resolution[y] + "").substring(0, 3), isDivideResolutionAndWindow = divideResolution == (window.innerWidth / window.innerHeight + "").substring(0, 3);
        if (isResolution && isWindowInner && (((screen.width / screen.height + "").substring(0, 3) == divideResolution && screen.width - window.innerWidth <= 211 && screen.height - window.innerHeight <= 211) || isDivideResolutionAndWindow)) {
            settingPicture();
            if (resolution[0] > resolution[1] && x == 0) {
                picture.css("max-width", "100%");
                picture.css("max-height", "inherit");
            } else {
                picture.css("max-width", "inherit");
                picture.css("max-height", main.width() + (main.width() == picture.css("max-height") ? 0 : main.css("left") == "0px" ? 0 : 50));
            }
            return true;
        } else if (!isResolution && !isWindowInner && isDivideResolutionAndWindow) {
            settingPicture();
            if (x == 0) {
                picture.css("max-width", "inherit");
                picture.css("max-height", "100%");
            } else {
                picture.css("max-width", main.height() + (screen.width < 480 ? 395 : main.height() == picture.css("max-width") ? 0 : main.css("left") == "0px" ? 0 : 140));
                picture.css("max-height", "inherit");
            }
            return true;
        }
    }
    return false;
}

if (mainReady.data("left") != "") {
    document.onkeydown = function (evt) {
        evt = evt || window.event;
        if (evt.keyCode == 37) {
            $('#arrow-left').click();
        } else if (evt.keyCode == 39) {
            $('#arrow-right').click();
        }
    }
}

function actionDoLeft(message) {
    typeAnimation = 'left';
    settingNewPicture(message)
}

function actionDoRight(message) {
    typeAnimation = 'right';
    settingNewPicture(message)
}

function settingNewPicture(message) {
    var newMainReady = $('<div>', {
        'class': 'main'
    });
    mainReady.after(newMainReady);
    mainReady = $('.main').last();
    window.history.pushState("html", "OSPicture - Hosting the images", "/image/" + message.key);
    mainReady.data("key", message.key);
    mainReady.data("format", message.format);
    $('.info').css("bottom", "-44px");
    setTimeout(function () {
        $('#resolution').text(message.resolution);
        $('#file-info').text(message.name);
        $('#file-info').click(function () {
            actionCopyToClipboard(message.name);
        });
        $('#size').text(message.size);
        $('#format').text(message.isOctetStream == 'true' ? 'octet-stream' : message.format);
        addListenerDownload(mainReady, 'picture');
        setTimeout(function () {
            $('.info').css("bottom", "10px");
        }, 20);
    }, 200);
    $('#download-picture').attr("href", "/img/" + message.key);
    $('#download-picture').attr("download", message.name + (message.isOctetStream == 'true' ? '' : '.') + (message.isOctetStream == 'true' ? '' : message.format));
    $('#direct-link').click(function () {
        actionCopyToClipboard(site + 'img/' + message.key + '.' + message.format);
    });
    $('#html-link').click(function () {
        actionCopyToClipboard('<a href=\'' + window.location.href + '\'><img src=\'' + site + 'img/' + message.key + '.' + message.format + '\' alt=\'Image from OSPicture\'></a>');
    });
    $('#bbcode-link').click(function () {
        actionCopyToClipboard('[url=' + window.location.href + '][img]' + site + 'img/' + message.key + '.' + message.format + '[/img][/url]');
    });
    $('#px200').attr("href", "/" + message.px200Path);
    $('#px500').attr("href", "/" + message.px500Path);
    mainReady.data("left", message.folderLeft);
    mainReady.data("right", message.folderRight);
}