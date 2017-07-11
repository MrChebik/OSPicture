var keys = [];
var count = 0;
var formData = new FormData;

var oldValue = 0;
var oldInterval;

function getResoulution(reader, file, are, length) {
    var _URL = window.URL || window.webkitURL;
    var img = new Image();
    img.onload = function () {
        return ajaxSendChecksum(file, sha3_512(reader.result), this.width + "x" + this.height, file.size, are, length);
    };
    $(img).attr("src", _URL.createObjectURL(file));
}

function checkChecksum(file, are, length) {
    oldValue = "0";
    var reader = new FileReader();
    reader.onload = function () {
        return getResoulution(this, file, are, length);
    };
    reader.readAsArrayBuffer(file);
}

function clearValuesOfChecksum() {
    formData = new FormData;
    count = 0;
    keys = [];
    oldValue = 0;
    clearInterval(oldInterval);
}

function ajaxSendChecksum(file, checksum, resolution, size, are, length) {
    $.ajax({
        url: "/checksum?checksum=" + checksum + "&resolution=" + resolution + "&size=" + size,
        type: "POST",
        success(e) {
            if (e !== "none" && !are) {
                window.location.href = site + "image/" + e;
            }

            if (!are) {
                formData.append("file", file);
                ajaxSend(formData);
                clearValuesOfChecksum();
            } else {
                if (e === "none") {
                    formData.append("files", file);
                } else {
                    keys[count++] = e;
                }
                oldValue++;

                if (are === "are1") {
                    oldInterval = setInterval(function () {
                        if (oldValue === length) {
                            ajaxSends(formData, keys);
                            clearValuesOfChecksum();
                        }
                    }, 50);
                }
            }

            return e;
        }
    });
}