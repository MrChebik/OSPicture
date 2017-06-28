if (mainReady.data("left") !== "") {
    document.onkeydown = function (evt) {
        evt = evt || window.event;
        if (evt.keyCode === 37) {
            arrowLeftElem.click();
        } else if (evt.keyCode === 39) {
            arrowRightElem.click();
        }
    };
}

function settingNewPicture(message) {
    var newMainReady = $("<div>", {
        "class": "main"
    });
    mainReady.after(newMainReady);
    mainReady = $(".main").last();
    window.history.pushState("html", "OSPicture - Hosting the images", "/image/" + message.key);
    mainReady.data("key", message.key);
    mainReady.data("format", message.format);
    infoElem.css("bottom", "-44px");
    setTimeout(function () {
        resolutionElem.text(message.resolution);
        fileInfoElem.text(message.name);
        fileInfoElem.click(function () {
            actionCopyToClipboard(message.name);
        });
        sizeElem.text(message.size);
        formatElem.text(message.isOctetStream === "true" ? "octet-stream" : message.format);
        addListenerDownload(mainReady, "picture");
        setTimeout(function () {
            infoElem.css("bottom", "10px");
        }, 20);
    }, 200);
    downloadPictureElem.attr("href", "/img/" + message.key);
    downloadPictureElem.attr("download", message.name + (message.isOctetStream === "true" ? "" : ".") + (message.isOctetStream === "true" ? "" : message.format));
    directLinkElem.click(function () {
        actionCopyToClipboard(site + "img/" + message.key + "." + message.format);
    });
    htmlLinkElem.click(function () {
        actionCopyToClipboard("<a href=\"" + window.location.href + "\"><img src=\"" + site + "img/" + message.key + "." + message.format + "\" alt=\"Image from OSPicture\"></a>");
    });
    bbcodeLinkElem.click(function () {
        actionCopyToClipboard("[url=" + window.location.href + "][img]" + site + "img/" + message.key + "." + message.format + "[/img][/url]");
    });
    px200ELem.attr("href", "/" + message.px200Path);
    px500Elem.attr("href", "/" + message.px500Path);
    mainReady.data("left", message.folderLeft);
    mainReady.data("right", message.folderRight);
}

function actionDoLeft(message) {
    typeAnimation = "left";
    settingNewPicture(message);
}

function actionDoRight(message) {
    typeAnimation = "right";
    settingNewPicture(message);
}