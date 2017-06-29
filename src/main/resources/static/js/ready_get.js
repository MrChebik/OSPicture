var notifDownload = 0, rotateDeg = 0, resolution, mainReady = $(".main"), transitSetting = "opacity .05s, transform .2s ease-in, box-shadow .2s ease-in", isResolution, body = $("body"), footer = $(".footer"), typeAnimation, isProcessing = false, resolutionElem = $("#resolution"), downloadPictureElem = $("#download-picture"), arrowLeftElem = $("#arrow-left"), arrowRightElem = $("#arrow-right"), fileInfoElem = $("#file-info"), sizeElem = $("#size"), formatElem = $("#format"), infoElem = $(".info"), directLinkElem = $("#direct-link"), htmlLinkElem = $("#html-link"), bbcodeLinkElem = $("#bbcode-link"), px200ELem = $("#px200"), px500Elem = $("#px500"), notification = $(".notification");
fileElem = $(".file");
notification.css("display", "block");

function addListenerDownload(element, type) {
    var isPicture = type === "picture";
    if (isPicture) {
        resolution = resolutionElem.text().split("x");
        setTimeout(function () {
            if (!picture) {
                if (notifDownload === 0) {
                    mainReady.append($("<div/>", {class: "flowspinner"}), $("<span/>", {class: "download-info"}).text("Downloading"));
                    notifDownload = 1;
                }
            } else {
                if ($(".main").length > 1) {
                    $(".picture").first().css("filter", "brightness(40%)");
                    $(".main").first().append($("<div/>", {class: "flowspinner"}));
                    notifDownload = 1;
                }
            }
        }, 400);
    }
    var img = $("<img />", {
        "class": type,
        "alt": "Image"
    }).attr("src", (isPicture ? "/img/" : "/img_min/") + element.data("key") + "." + element.data("format"))
        .on("load", function () {
            if (!this.complete || typeof this.naturalWidth === "undefined" || this.naturalWidth === 0) {
                element.append($("<span />").text("Broken image"));
            } else {
                if (isPicture) {
                    notifDownload = 1;
                    if ($(".flowspinner").length) {
                        $(".flowspinner").remove();
                        $(".download-info").remove();
                    }
                }
                element.append(img[0]);
                if (isPicture) {
                    picture = $(".picture").last();
                    calculateView(0, 1);
                    if ($(".main").length > 1 && mainReady.css("top") !== "0px") {
                        setMainReady(mainReady, typeAnimation !== "right");
                    }
                    if (mainReady.css("top") !== "0px") {
                        footer.css("bottom", $(".arrow-box").length ? "-44px" : "0px");
                        footer.css("background-color", "transparent");
                    }
                }
                setTimeout(function () {
                    if ($(".main").length < 2) {
                        if (isPicture) {
                            img[0].style.transition = transitSetting + ",  max-width .2s ease-in, max-height .2s ease-in";
                        } else {
                            img[0].style.transition = "filter .2s ease-in, " + transitSetting;
                        }
                    }
                    img[0].style.transform = "rotateX(0deg)";
                    img[0].style.boxShadow = "none";
                    img[0].style.opacity = "1";
                    if (isPicture) {
                        setTimeout(function () {
                            mainReady.css("transition", "top .2s, bottom .2s, left .2s, right .2s");
                            img[0].style.transition = transitSetting + ",  max-width .2s ease-in, max-height .2s ease-in";
                            var mainForFirst = $(".main");
                            if (mainForFirst.length > 1) {
                                setMainReady($(mainForFirst[0]), typeAnimation !== "left");
                                setMainReadyPx(mainReady.css("top") === "0px" ? "0px" : "25px");
                                setTimeout(function () {
                                    $(mainForFirst[0]).remove();
                                    isProcessing = false;
                                    main = $(".main");
                                    rotateDeg = 0;
                                }, 200);
                            } else {
                                isProcessing = false;
                            }
                        }, 40);
                        $(window).resize(function() {
                            calcViewRotateDeg();
                        });
                    }
                }, 20);
            }
        });
}

setTimeout(function () {
    if ($(".arrow-box").length) {
        if (screen.width < 480) {
            $(".footer").css("display", "none");
        } else {
            $(".footer").css("bottom", "-44px");
        }
    }
    if ($(".main").data("format") !== "") {
        addListenerDownload(mainReady, "picture");
    } else {
        for (var i = 0; i < fileElem.length; i++) {
            addListenerDownload($(fileElem[i]), "image-folder");
        }
    }
}, 20);