var keyInput, site = "http://ospicture.xyz/";
function actionLogo() {
    if (window.location.pathname != '/') {
        window.location.href = '/';
    }
}
function actionGet() {
    keyInput = $('.key-input').val();
    if (keyInput.length > 19) {
        if (keyInput.indexOf(site) == -1) {
            if (window.location.pathname != '/' + keyInput) {
                window.location.href = site + $('.key-input').val();
            }
        } else {
            if (window.location != keyInput) {
                window.location.href = keyInput;
            }
        }
    }
}
function actionGetFromInput(event) {
    if (event.which == 13) {
        actionGet();
    }
}
function actionDownload() {
    $('#download-picture')[0].click();
}

function actionClickInFolder(key) {
    window.location.href=site + key;
}