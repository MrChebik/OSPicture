var notifDownload = 0;

function ajax_get(path, element) {
    var xhr = new XMLHttpRequest();
    if (element.hasClass('picture')) {
        xhr.onprogress = function () {
            if (notifDownload == 0) {
                main.append($('<div/>', {'class': 'flowspinner'}), $('<span/>', {'class': 'download-info'}).text('Downloading'));
                notifDownload = 1;
            }
        };
    }
    xhr.onreadystatechange = function(){
        if (this.readyState == 4 && this.status == 200){
            if (element.hasClass('picture')) {
                $('.flowspinner').remove();
                $('.download-info').remove();
            }
            var url = window.URL || window.webkitURL;
            element.attr('src', url.createObjectURL(this.response));
            setTimeout(function () {
                if (!element.hasClass('picture')) {
                    element.css('transition', 'opacity .05s, transform .2s ease-in, box-shadow .2s ease-in');
                }
                element.css('transform', 'rotateX(0deg)');
                element.css('boxShadow', 'none');
                element.css('opacity', '1');

                clearInterval(optimizeInterval);
                percentComplete = 0;
            }, 100);
        }
    };
    xhr.open('GET', path, true);
    xhr.responseType = 'blob';
    xhr.send();
}