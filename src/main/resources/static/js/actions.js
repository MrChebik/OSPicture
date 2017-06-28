function actionDownload() {
    downloadPictureElem[0].click();
}
function actionImitationClick() {
    $("#label-upload")[0].click();
}
function actionCopyToClipboard(text) {
    copyToClipboard(text);
    notification.css("opacity", "1");
    notification.css("top", "100px");
    setTimeout(function () {
        notification.css("opacity", ".0");
        notification.css("top", "80px");
    }, 2000);
}

function actionRotateZ(deg) {
    rotateDeg += deg;
    calcViewRotateDeg();
    picture.css("transform", "rotateZ(" + rotateDeg + "deg)");
}

function actionSendURL(event) {
    if (event.keyCode === 13) {
        ajaxSendURL($(".url-input").val());
        $(".url-input").val("");
    }
}

var site = window.location.protocol + "//" + window.location.host + "/";