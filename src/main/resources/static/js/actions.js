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
    footer.css("bottom", $(".arrow-box").length ? "-44px" : "0");
}

function setPictureReg(width, height) {
    picture.css("max-width", width);
    picture.css("max-height", height);
}

function calcViewRotateDeg() {
    if ((Math.abs(rotateDeg) / 90) % 2 === 1) {
        if (!calculateView(1, 0)) {
            setMain();
            const isLeft = main.css("left") === "0px";
            setPictureReg(main.height() - (isLeft ? 140 : 0), main.width() - (isLeft ? 50 : 0));
        }
    } else {
        if (!calculateView(0, 1)) {
            setMain();
            setPictureReg("100%", "100%");
        }
    }
}

function actionRotateZ(deg) {
    rotateDeg += deg;
    calcViewRotateDeg();
    picture.css("transform", "rotateZ(" + rotateDeg + "deg)");
}

var site = window.location.protocol + "//" + window.location.host + "/";