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
    dropZone = e("#dropZone"), maxFileSize = 1e7, void 0 == typeof window.FileReader && (dropZone.clearData(), dropZone.text("Not supported drag files! Sorry, but I still believe, you make update your browser :)"));
    $('.notification').css("display", "block");
});