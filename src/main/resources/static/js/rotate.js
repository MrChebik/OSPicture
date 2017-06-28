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
    $(".url-input").css("background-color", "#2C3E50");
}

function setPictureReg(width, height) {
    picture.css("max-width", width);
    picture.css("max-height", height);
}

function calcViewRotateDeg() {
    if ((Math.abs(rotateDeg) / 90) % 2 === 1) {
        if (!calculateView(1, 0)) {
            setMain();
            var isLeft = main.css("left") === "0px";
            setPictureReg((main.height() - (isLeft ? 140 : 0)), (main.width() - (isLeft ? 50 : 0)));
        }
    } else {
        if (!calculateView(0, 1)) {
            setMain();
            setPictureReg("100%", "100%");
        }
    }
}

function settingPicture() {
    mainReady.css("top", "0");
    mainReady.css("bottom", "0");
    if ($(".main").length < 2) {
        mainReady.css("left", "0");
        mainReady.css("right", "0");
    } else {
        if (typeAnimation === "left") {
            mainReady.css("left", "-100%");
            mainReady.css("right", "100%");
        } else {
            mainReady.css("left", "100%");
            mainReady.css("right", "-100%");
        }
    }
    if (screen.width > 480) {
        footer.css("bottom", "-44px");
    }
    footer.css("background-color", "rgba(0,0,0,0.7)");
    body.css("background-color", "black");
    body.css("transition", "background-color .2s");
    $(".url-input").css("background-color", "rgba(0,0,0,.5)");
}

function setPicture(width, height) {
    picture.css("max-width", width);
    picture.css("max-height", height);
}

function calcPx() {
    return (main.height() + (screen.width < 480 ? 395 : main.height() === picture.css("max-width") ? 0 : main.css("left") === "0px" ? 0 : 140));
}

function calculateView(x, y) {
    isResolution = resolution[x] > resolution[y];
    if (window.innerWidth <= resolution[x] && window.innerHeight <= resolution[y]) {
        var isWindowInner = window.innerWidth > window.innerHeight, divideResolution = (resolution[x] / resolution[y] + "").substring(0, 3), isDivideResolutionAndWindow = divideResolution === (window.innerWidth / window.innerHeight + "").substring(0, 3);
        if (isResolution) {
            if (isWindowInner && (((screen.width / screen.height + "").substring(0, 3) === divideResolution && screen.width - window.innerWidth <= 211 && screen.height - window.innerHeight <= 211) || isDivideResolutionAndWindow)) {
                settingPicture();
                if (resolution[0] > resolution[1] && x === 0) {
                    setPicture("100%", "inherit");
                } else {
                    setPicture("inherit", (main.width() + (main.width() === picture.css("max-height") ? 0 : main.css("left") === "0px" ? 0 : 50)));
                }
                return true;
            }
        } else {
            if (!isWindowInner && isDivideResolutionAndWindow) {
                settingPicture();
                if (x === 0) {
                    setPicture("inherit", calcPx());
                } else {
                    setPicture(calcPx(), "inherit");
                }
                return true;
            }
        }
    }
    return false;
}

function setMainReady(element, isMinus) {
    element.css("left", "calc(" + (isMinus ? "-" : "+") + "100% + 25px)");
    element.css("right", "calc(" + (isMinus ? "+" : "-") + "100% + 25px)");
}

function setMainReadyPx(px) {
    body.css("background-color", px === "25px" ? "#34495E" : "black");
    mainReady.css("left", px);
    mainReady.css("right", px);
}