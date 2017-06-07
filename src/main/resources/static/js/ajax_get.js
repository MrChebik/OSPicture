var notifDownload = 0;

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