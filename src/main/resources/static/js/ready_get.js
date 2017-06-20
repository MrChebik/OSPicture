var notifDownload = 0, rotateDeg = 0, resolution, mainReady = $('.main'), transitSetting = "opacity .05s, transform .2s ease-in, box-shadow .2s ease-in", isResolution;

notification = $('.notification');
notification.css("display", "block");
setTimeout(function () {
    if ($('#download-picture').length) {
        resolution = $('#resolution').text().split("x");
        setTimeout(function () {
            if (picture == undefined) {
                0 == notifDownload && (mainReady.append($("<div/>", {class: "flowspinner"}), $("<span/>", {class: "download-info"}).text("Downloading")), notifDownload = 1)
            }
        }, 500);
        addListenerDownload(mainReady, 'picture');
    } else if ($('.file').length) {
        var files_pictures = $('.file');
        for (var i = 0; i < files_pictures.length; i++) {
            addListenerDownload($(files_pictures[i]), 'image-folder')
        }
    }
}, 20);

function settingPicture() {
    var footer = $('.footer');

    mainReady.css("top", "0");
    mainReady.css("left", "0");
    mainReady.css("right", "0");
    mainReady.css("bottom", "0");
    $('body').css("transition", "background-color .2s ease-in");
    $('body').css("background-color", "black");
    if (screen.width >= window.innerWidth) {
        footer.css("transition", "bottom .2s, background-color .2s");
        footer.css("bottom", "-40px");
    }
    footer.css("background-color", "rgba(0,0,0,0.7)");
}

function addListenerDownload(element, type) {
    var isPicture = type == 'picture';
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
                    picture = $('.picture');
                    calculateView(0, 1);
                }
                setTimeout(function () {
                    if (isPicture) {
                        img[0].style.transition = transitSetting + ",  max-width .2s ease-in, max-height .2s ease-in";
                    } else {
                        img[0].style.transition = "filter .2s ease-in, " + transitSetting;
                    }
                    img[0].style.transform = "rotateX(0deg)";
                    img[0].style.boxShadow = "none";
                    img[0].style.opacity = "1";
                    if (isPicture) {
                        setTimeout(function () {
                            mainReady.css("transition", "top .2s, bottom .2s, left .2s, right .2s");
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
                picture.css("max-height", main.width() + 50);
            }
            return true;
        } else if (!isResolution && !isWindowInner && isDivideResolutionAndWindow) {
            settingPicture();
            if (x == 0) {
                picture.css("max-width", "inherit");
                picture.css("max-height", "100%");
            } else {
                picture.css("max-width", main.height() + 465);
                picture.css("max-height", "inherit");
            }
            return true;
        }
    }
    return false;
}