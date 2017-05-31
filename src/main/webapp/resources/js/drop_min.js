var dropZone, maxFileSize;
$(document).ready(function ($) {
    dropZone = $('#dropZone');
    maxFileSize = 10000000;
    if (typeof(window.FileReader) == undefined) {
        dropZone.clearData();
        dropZone.text("Not supported drag files! Sorry, but I still believe, you make update your browser :)");
    }
});
function copyToClipboard(element) {
    var $temp = $("<input>");
    $("body").append($temp);
    $temp.val(element).select();
    document.execCommand("copy");
    $temp.remove();
}
function dropEnter(e) {
    e.stopPropagation();
    e.preventDefault();
    dropZone.addClass('hover');
    $('.info').css('border', 'none');
}
function dropLeave() {
    dropZone.removeClass('hover');
    $('.info').css('border', '3px dashed dimgray');
}
function doDrop(e) {
    e.stopPropagation();
    e.preventDefault();
    dropZone.removeClass('hover');
    $('.info').css('border', '3px dashed dimgray');
    var dt = e.dataTransfer;
    if (!dt && !dt.files) {
        return false;
    }
    var files = dt.files;
    dt.dropEffect = "copy";
    if (files.length < 2) {
        ajax_upload(files[0]);
    } else {
        ajax_uploads(files);
    }
    return false;
}