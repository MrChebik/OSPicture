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
var dropZone;