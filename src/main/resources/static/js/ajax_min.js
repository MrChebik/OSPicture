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

var notif = 0, wasError = 0;

function ajax_send(e, a) {
    $.ajax({
        url: "/upload/image" + ("are" == a ? "s" : ""),
        type: "PUT",
        data: e,
        cache: !1,
        processData: !1,
        contentType: !1,
        xhr: function () {
            var $ppc = $('.progress-pie-chart');
            var main = $('.main');
            var xhr = $.ajaxSettings.xhr();
            xhr.upload.addEventListener('progress', function (evt) {
                if (evt.lengthComputable) {
                    if (notif == 0) {
                        $ppc.show();
                        $('.bold').hide();
                        $('.drag-info').hide();
                        $('.click-info').hide();
                        $('.picture').hide();
                        $('.file').hide();
                        $ppc.removeClass('gt-50');
                        main.css('justify-content', 'center');
                        main.css('align-items', 'center');
                        main.css('flex-direction', 'column');
                        $('.upload-info').show();

                        notif = 1;
                    }
                    var percentComplete = Math.ceil(evt.loaded / evt.total * 100);
                    if (percentComplete > 50) {
                        $ppc.addClass('gt-50');
                    } else {
                        $ppc.removeClass('gt-50');
                    }
                    $('.ppc-progress-fill').css('transform','rotate('+ 360*percentComplete/100 +'deg)');
                    $('.ppc-percents span').html(percentComplete    +'%');
                    if (percentComplete == 100) {
                        notif = 0;
                        setTimeout(function () {
                            if (wasError == 0) {
                                $('.upload-info').hide();
                                $('.progress-pie-chart').hide();
                                $('.flowspinner').show();
                                $('.optimize-info').show();
                            } else {
                                wasError = 0;
                            }
                        }, 1000);
                    }
                }
            }, false);
            return xhr;
        },
        success: function (e) {
            window.location.href = e;
        },
        error: function () {
            notif = 0;
            wasError = 1;
            $('.progress-pie-chart').hide();
            $('.optimize-info').hide();
            $('.flowspinner').hide();
            $('.upload-info').hide();
            $('.bold').show();
            $('.picture').show();
            $('.file').show();
            alert("Something was wrong, check the type of file.");
        }
    })
}