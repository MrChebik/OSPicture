$(document).ready(function (e) {
    dropZone = e("#dropZone"), void 0 == typeof window.FileReader && (dropZone.clearData(), dropZone.text("Not supported drag files! Sorry, but I still believe, you make update your browser :)"));
});