function ajax_upload(file) {
    if (file.size > maxFileSize) {
        alert("File size is bigger than 10MB");
        return;
    }
    var dataFile = new FormData;
    dataFile.append("file", file);

    ajax_send(dataFile, "is");
}

function ajax_uploads(files) {
    var currentFilesSize = 0;
    for (var i = 0; i < files.length; i++) {
        currentFilesSize += files[i].size;
    }
    if (currentFilesSize > maxFileSize) {
        alert("Files size is bigger than 10MB");
        return;
    }

    var dataFile = new FormData;
    for (var i = 0; i < files.length; i++) {
        dataFile.append("multipartFiles", files[i]);
    }

    ajax_send(dataFile, "are");
}

function ajax_send(dataFile, type) {
    $.ajax({
        url: '/upload/image' + (type == 'are' ? 's' : ''),
        type: "PUT",
        data: dataFile,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data, status) {
            status == "success" ? window.location.href = data : alert(data);
        },
        error: function () {
            alert("Something was wrong, check the type of file.");
        }
    });
}