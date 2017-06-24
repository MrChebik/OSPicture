function actionDownload() {
    downloadPictureElem[0].click()
}
function actionImitationClick() {
    $("#label-upload")[0].click()
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
    picture.css('transform', 'rotateZ(' + rotateDeg + 'deg)');
}

function calcViewRotateDeg() {
    if ((Math.abs(rotateDeg) / 90) % 2 == 1) {
        if (!calculateView(1, 0)) {
            setMain();
            var width = picture.width();
            if (main.css("left") == "0px") {
                picture.css("max-width", main.height() - 140);
                picture.css("max-height", main.width() - 50);
            } else {
                picture.css("max-width", main.height());
                picture.css("max-height", main.width());
            }
        }
    } else {
        if (!calculateView(0, 1)) {
            setMain();
            picture.css("max-width", "100%");
            picture.css("max-height", "100%");
        }
    }
}

var site = window.location.protocol + "//" + window.location.host + "/";

function setMain() {
    body.css("background-color", "#34495E");
    if (screen.width > 480) {
        main.css("top", "70px");
        main.css("left", "25px");
        main.css("right", "25px");
        main.css("bottom", "70px");
    } else {
        main.css("top", "225px");
        main.css("left", "25px");
        main.css("right", "25px");
        main.css("bottom", "170px");
    }
    footer.css("background-color", "transparent");
    footer.css("bottom", $('.arrow-box').length ? "-44px" : "0");
}