var notification, main, picture, maxFileSize, optimizeInterval;

function copyToClipboard(e) {
    var o = $("<input>");
    $("body").append(o), o.val(e).select(), document.execCommand("copy"), o.remove()
}

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

var notif = 0, wasError = 0, prevPercent = 0, colors = ['#2ECC71', '#1ABC9C', '#F39C12', '#E67E22', '#E74C3C'], color = 0, percent, pie, progress, pieOfValue, dash, percentComplete = 0;

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
                    if (!$('#progress-percent').length) {
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

                        main.append(svgEl, $('<span/>', {'class':'upload-info'}).text('Uploading'));

                        progress = $('#progress-percent');
                        progress.append(circleEl, percentEl);
                        percent = $('#percent');
                        percent.text('0%');
                        pie = $('#circle');

                        pieOfValue = pie.css('strokeDasharray').split(' ')[1];
                        dash = pieOfValue.charAt(pieOfValue.length - 1) == 'x' ? pieOfValue.substring(0, pieOfValue.length - 2) : pieOfValue;

                        $('.bold').hide();
                        $('.drag-info').hide();
                        $('.click-info').hide();
                        if (picture != undefined) {
                            picture.hide();
                        }
                        $('.file').hide();
                        main.css('justify-content', 'center');
                        main.css('align-items', 'center');
                        main.css('flex-direction', 'column');
                        $('.upload-info').show();

                        notif = 1;
                    }

                    if (notif != 2) {
                        percentComplete = Math.ceil(evt.loaded / evt.total * 100);

                        if (optimizeInterval == undefined) {
                            optimizeInterval = setInterval(function () {
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
            window.location.href = site + e;
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