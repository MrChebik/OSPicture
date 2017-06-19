var notifDownload = 0, rotateDeg = 0, mainReady = $('.main');

notification = $('.notification');
notification.css("display", "block");
if ($('#format').length) {
    var resolution = $('#resolution').text().split("x");
    setTimeout(function () {
        if (picture == undefined) {
            0 == notifDownload && (mainReady.append($("<div/>", {class: "flowspinner"}), $("<span/>", {class: "download-info"}).text("Downloading")), notifDownload = 1)
        }
    }, 1000);
    var img = $("<img />", {
        'class': 'picture',
        'alt': 'Image'
    }).attr('src', '/img/' + $('#picture-key').val() + '.' + $('#format').data('format'))
        .on('load', function () {
            if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
                mainReady.append($('<span />').text('Broken image'));
            } else {
                notifDownload = 1;
                if ($('.flowspinner').length) {
                    $(".flowspinner").remove();
                    $(".download-info").remove()
                }
                mainReady.append(img);
                picture = $('.picture');
                if (window.innerWidth < resolution[0] && window.innerHeight < resolution[1]) {
                    if (resolution[0] > resolution[1] && window.innerWidth > window.innerHeight && (((screen.width / screen.height) + "").substring(0, 3) == ((resolution[0] / resolution[1]) + "").substring(0, 3) && screen.width - window.innerWidth <= 211 && screen.height - window.innerHeight <= 211) || ((window.innerWidth / window.innerHeight) + "").substring(0, 3) == ((resolution[0] / resolution[1]) + "").substring(0, 3)) {
                        settingPicture();
                        picture.css("max-height", "inherit");
                    } else if (resolution[0] < resolution[1] && ((window.innerWidth / window.innerHeight) + "").substring(0, 3) == ((resolution[0] / resolution[1]) + "").substring(0, 3)) {
                        settingPicture();
                        picture.css("max-width", "inherit");
                    }
                }
                setTimeout(function () {
                    picture.css("transform", "rotateX(0deg)");
                    picture.css("boxShadow", "none");
                    picture.css("opacity", "1");
                }, 100);
            }
        });
} else if ($('.file').length) {
    var files_pictures = $('.file');
    for (var i = 0; i < files_pictures.length; i++) {
        addListenerDownload(files_pictures[i])
    }
}

function settingPicture() {
    mainReady.css("overflow", "hidden");
    mainReady.css("top", "0");
    mainReady.css("left", "0");
    mainReady.css("right", "0");
    mainReady.css("bottom", "0");
    $('body').css("background-color", "black");
}

function addListenerDownload(element) {
    var img_folder = $("<img />", {
        'class': 'image-folder',
        'alt': 'Image'
    }).attr('src', '/img_min/' + element.dataset.key + '.' + element.dataset.format)
        .on('load', function () {
            if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
                element.append($('<span />').text('Broken image'));
            } else {
                element.append(img_folder[0]);
                setTimeout(function () {
                    img_folder[0].style.transition = "filter .2s ease-in, opacity .05s, transform .2s ease-in, box-shadow .2s ease-in";
                    img_folder[0].style.transform = "rotateX(0deg)";
                    img_folder[0].style.boxShadow = "none";
                    img_folder[0].style.opacity = "1";
                }, 100);
            }
        });
}