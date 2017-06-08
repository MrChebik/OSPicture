var timeout = 100, checking = function() {
    setTimeout(function () {
        timeout--;
        if (typeof notifDownload !== 'undefined') {
            clearTimeout(checking);
            onreadyCustom();
        } else if (timeout > 0) {
            checking();
        } else {
            alert('Missing the document')
        }
    }, 100);
};

checking();

function onreadyCustom() {
    notification = $('.notification');
    picture = $('.picture');
    notification.css("display", "block");
    if (picture.length) {
        ajax_get('/img/' + picture.data('key'), picture);
    } else if ($('.file').length) {
        var files_pictures = $('.file > .image-folder');
        for (var i = 0; i < files_pictures.length; i++) {
            ajax_get('/img_min/' + files_pictures[i].dataset.key, $(files_pictures[i]));
        }
    }
}