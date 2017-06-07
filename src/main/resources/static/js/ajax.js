var notification, main, picture;

function copyToClipboard(e) {
    var o = $("<input>");
    $("body").append(o), o.val(e).select(), document.execCommand("copy"), o.remove()
}
function dropEnter(e) {
    e.stopPropagation(), e.preventDefault(), dropZone.addClass("hover")
}
function dropLeave() {
    dropZone.removeClass("hover")
}
function doDrop(e) {
    e.stopPropagation(), e.preventDefault(), dropZone.removeClass("hover");
    var o = e.dataTransfer;
    if (!o && !o.files)return !1;
    var r = o.files;
    return o.dropEffect = "copy", r.length < 2 ? ajax_upload(r[0]) : ajax_uploads(r), !1
}
var dropZone, maxFileSize;
$(document).ready(function (e) {
    notification = $('.notification');
    main = $('.main');
    picture = $('.picture');
    dropZone = e("#dropZone"), maxFileSize = 1e7, void 0 == typeof window.FileReader && (dropZone.clearData(), dropZone.text("Not supported drag files! Sorry, but I still believe, you make update your browser :)"));
    notification.css("display", "block");
    if (picture.length) {
        ajax_get('/img/' + picture.data('key'), picture);
    } else if ($('.file').length) {
        var files_pictures = $('.file > .image-folder');
        for (var i = 0; i < files_pictures.length; i++) {
            ajax_get('/img_min/' + files_pictures[i].dataset.key, $(files_pictures[i]));
        }
    }
});

function ajax_upload(e) {
    if (e.size > maxFileSize) alert("File size is bigger than 10MB"); else {
        var a = new FormData;
        a.append("file", e), ajax_send(a, "is")
    }
}
function ajax_uploads(e) {
    for (var a = 0, i = 0; i < e.length; i++)a += e[i].size;
    if (a > maxFileSize) alert("Files size is bigger than 10MB"); else {
        for (var n = new FormData, i = 0; i < e.length; i++)n.append("multipartFiles", e[i]);
        ajax_send(n, "are")
    }
}

var notif = 0, wasError = 0, notifDownload = 0, prevPercent = 0, colors = ['#2ECC71', '#1ABC9C', '#F39C12', '#E67E22', '#E74C3C'], color = 0, percent, pie, progress, pieOfValue, dash, percentComplete = 0;

function ajax_get(path, element) {
    var xhr = new XMLHttpRequest();
    if (element.hasClass('picture')) {
        xhr.addEventListener('progress', function (evt) {
            if (evt.lengthComputable) {
                if (notifDownload == 0) {
                    setPartDwnldUpld('dwnld');
                    notifDownload = 1;
                }
                percentComplete = Math.ceil(evt.loaded / evt.total * 100);

                optimizeSpeedColor();
            }
        }, false);
    }
    xhr.onreadystatechange = function(){
        if (this.readyState == 4 && this.status == 200){
            if (element.hasClass('picture')) {
                if (progress != undefined) {
                    progress.remove();
                    $('.download-info').remove();
                }
            }
            var url = window.URL || window.webkitURL;
            element.attr('src', url.createObjectURL(this.response));
            setTimeout(function () {
                if (!element.hasClass('picture')) {
                    element.css('transition', 'filter .2s ease-in, opacity .2s ease-in, transform .2s ease-in, box-shadow .2s ease-in');
                }
                element.css('transform', 'rotateX(0deg)');
                if (element.hasClass('shadow')) {
                    element.css('boxShadow', '0 14px 28px rgba(0,0,0,0.25), 0 10px 10px rgba(0,0,0,0.22)');
                } else {
                    element.css('boxShadow', 'none');
                }
                element.css('opacity', '1');
                element.css('filter', 'blur(0px)');

                clearInterval(optimizeInterval);
                percentComplete = 0;
            }, 100);
        }
    };
    xhr.open('GET', path);
    xhr.responseType = 'blob';
    xhr.send();
}

function ajax_send(e, a) {
    $.ajax({
        url: "/upload/image" + ("are" == a ? "s" : ""),
        type: "PUT",
        data: e,
        cache: !1,
        processData: !1,
        contentType: !1,
        xhr: function () {
            var xhr = $.ajaxSettings.xhr();
            xhr.upload.addEventListener('progress', function (evt) {
                if (evt.lengthComputable) {
                    if (notif == 0) {
                        setPartDwnldUpld('upld');

                        $('.bold').hide();
                        $('.drag-info').hide();
                        $('.click-info').hide();
                        picture.hide();
                        $('.file').hide();
                        main.css('justify-content', 'center');
                        main.css('align-items', 'center');
                        main.css('flex-direction', 'column');
                        $('.upload-info').show();

                        notif = 1;
                    }

                    if (notif != 2) {
                        percentComplete = Math.ceil(evt.loaded / evt.total * 100);

                        optimizeSpeedColor();

                        if (percentComplete == 100) {
                            notif = 2;
                            setTimeout(function () {
                                if (wasError == 0) {
                                    $('.upload-info').remove();
                                    progress.remove();
                                    main.append($('<div/>', {'class': 'flowspinner'}), $('<span/>', {'class': 'optimize-info'}).text('Optimization'));
                                } else {
                                    wasError = 0;
                                }
                            }, 1000);
                        }
                    }
                }
            }, false);
            return xhr;
        },
        success: function (e) {
            window.location.href = e;
        },
        error: function () {
            clearInterval(optimizeInterval);
            percentComplete = 0;
            notif = 0;
            wasError = 1;
            progress.remove();
            $('.optimize-info').remove();
            $('.flowspinner').remove();
            $('.upload-info').remove();
            $('.bold').show();
            picture.show();
            $('.file').show();
            alert("Something was wrong, check the type of file.");
        }
    })
}

function setPartDwnldUpld(type) {
    var svgEl = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svgEl.setAttributeNS(null, "id", "progress-percent");
    svgEl.setAttributeNS(null, "width", "200");
    svgEl.setAttributeNS(null, "height", "200");
    var circleEl = document.createElementNS("http://www.w3.org/2000/svg", "circle");
    circleEl.setAttributeNS(null, "id", "circle");
    circleEl.setAttributeNS(null, "r", "90");
    circleEl.setAttributeNS(null, "cx", "100");
    circleEl.setAttributeNS(null, "cy", "100");
    var percentEl = document.createElementNS("http://www.w3.org/2000/svg", "text");
    percentEl.setAttributeNS(null, "id", "percent");
    percentEl.setAttributeNS(null, "x", "100");
    percentEl.setAttributeNS(null, "y", "-85");

    main.append(svgEl, $('<span/>', {'class':type == 'dwnld' ? 'download-info' : 'upload-info'}).text(type == 'dwnld' ? 'Downloading' : 'Uploading'));

    progress = $('#progress-percent');
    progress.append(circleEl, percentEl);
    percent = $('#percent');
    percent.text('0%');
    pie = $('#circle');

    pieOfValue = pie.css('strokeDasharray').split(' ')[1];
    dash = pieOfValue.charAt(pieOfValue.length - 1) == 'x' ? pieOfValue.substring(0, pieOfValue.length - 2) : pieOfValue;
}

var optimizeInterval;

function optimizeSpeedColor() {
    if (optimizeInterval == undefined) {
        optimizeInterval = setInterval(function () {
            console.log(percentComplete + " // " + prevPercent);
            if (percentComplete - prevPercent >= 25) {
                pie.css('transition', 'stroke-dasharray .01s ease-in, stroke .01s ease-in');
                percent.css('transition', 'fill .01s ease-in');
            } else if (percentComplete - prevPercent >= 10) {
                pie.css('transition', 'stroke-dasharray .1s ease-in, stroke .1s ease-in');
                percent.css('transition', 'fill .1s ease-in');
            } else if (percentComplete - prevPercent >= 5) {
                pie.css('transition', 'stroke-dasharray .2s ease-in, stroke .2s ease-in');
                percent.css('transition', 'fill .2s ease-in');
            } else {
                pie.css('transition', 'stroke-dasharray .3s ease-in, stroke .3s ease-in');
                percent.css('transition', 'fill .3s ease-in');
            }

            prevPercent = percentComplete;
        }, 1000);
    }

    color = percentComplete > 90 ? 0 : percentComplete > 75 ? 1 : percentComplete > 50 ? 2 : percentComplete > 25 ? 3 : 4;

    pie.css('stroke', colors[color]);
    percent.css('fill', colors[color]);

    pie.css('strokeDasharray', ((percentComplete * dash) / 100) + ' ' + dash);
    percent.text(percentComplete + '%');
}