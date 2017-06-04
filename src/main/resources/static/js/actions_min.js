function actionLogo() {
    "/" != window.location.pathname && (window.location.href = "/")
}
function actionDownload() {
    $("#download-picture")[0].click()
}
function actionClickInFolder(o) {
    window.location.href = site + o
}
function actionImitationClick() {
    $("#label-upload")[0].click()
}
function actionCopyToClipboard(text) {
    copyToClipboard(text);
    $('.notification').css("opacity", "1");
    setTimeout(function () {
        $('.notification').css("opacity", ".0");
    }, 2000);
}
var site = "http://ospicture.xyz/";