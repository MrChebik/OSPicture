function dropEnter(o) {
    o.stopPropagation(), o.preventDefault(), dropZone.addClass("hover")
}
function dropLeave() {
    dropZone.removeClass("hover")
}
function doDrop(o) {
    o.stopPropagation(), o.preventDefault(), dropZone.removeClass("hover");
    var e = o.dataTransfer;
    if (!e && !e.files)return !1;
    var r = e.files;
    return e.dropEffect = "copy", r.length < 2 ? ajax_upload(r[0]) : ajax_uploads(r), !1
}
var dropZone;