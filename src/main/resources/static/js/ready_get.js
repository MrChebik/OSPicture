var notifDownload = 0, rotateDeg = 0;

notification = $('.notification');
notification.css("display", "block");
if ($('#format').length) {
    var resolution = $('#resolution').text().split("x"), notifRes = 0;
    setTimeout(function () {
        if (picture == undefined) {
            0 == notifDownload && (main.append($("<div/>", {class: "flowspinner"}), $("<span/>", {class: "download-info"}).text("Downloading")), notifDownload = 1)
        }
    }, 1000);
    var img = $("<img />", {
        'class': 'picture',
        'alt': 'Image'
    }).attr('src', '/img/' + $('#picture-key').val() + '.' + $('#format').data('format'))
        .on('load', function () {
            if (!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0) {
                main.append($('<span />').text('Broken image'));
            } else {
                notifDownload = 1;
                if ($('.flowspinner').length) {
                    $(".flowspinner").remove();
                    $(".download-info").remove()
                }
                main.append(img);
                picture = $('.picture');
                if (window.innerWidth < resolution[0] && window.innerHeight < resolution[1]) {
                    if (resolution[0] > resolution[1] && window.innerWidth > window.innerHeight) {
                        for (var i = 1; i < 11; i++) {
                            if (window.innerWidth * i >= resolution[0]) {
                                if (window.innerHeight * i <= resolution[1]) {
                                    notifRes = 1;
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                        if (notifRes == 1) {
                            settingPicture();
                            picture.css("max-height", "inherit");
                        }
                    } else if (resolution[0] < resolution[1] && window.innerWidth < window.innerHeight) {
                        for (var i = 1; i < 11; i++) {
                            console.log(window.innerWidth * i + " // " + window.innerHeight * i + " // " + i + " // " + $(window).width() + " // " + $(window).height());
                            if (window.innerHeight * i >= resolution[1]) {
                                if (window.innerWidth * i <= resolution[0]) {
                                    notifRes = 1;
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                        if (notifRes == 1) {
                            settingPicture();
                            picture.css("max-width", "inherit");
                        }
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
    main.css("overflow", "hidden");
    main.css("top", "0");
    main.css("left", "0");
    main.css("right", "0");
    main.css("bottom", "0");
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
                    img_folder[0].style.transition = "opacity .05s, transform .2s ease-in, box-shadow .2s ease-in";
                    img_folder[0].style.transform = "rotateX(0deg)";
                    img_folder[0].style.boxShadow = "none";
                    img_folder[0].style.opacity = "1";
                }, 100);
            }
        });
}