function settingPicture(){mainReady.css("overflow","hidden"),mainReady.css("top","0"),mainReady.css("left","0"),mainReady.css("right","0"),mainReady.css("bottom","0"),$("body").css("background-color","black")}function addListenerDownload(i){var n=$("<img />",{class:"image-folder",alt:"Image"}).attr("src","/img_min/"+i.dataset.key+"."+i.dataset.format).on("load",function(){this.complete&&void 0!==this.naturalWidth&&0!=this.naturalWidth?(i.append(n[0]),setTimeout(function(){n[0].style.transition="opacity .05s, transform .2s ease-in, box-shadow .2s ease-in",n[0].style.transform="rotateX(0deg)",n[0].style.boxShadow="none",n[0].style.opacity="1"},100)):i.append($("<span />").text("Broken image"))})}var notifDownload=0,rotateDeg=0,mainReady=$(".main");if(notification=$(".notification"),notification.css("display","block"),$("#format").length){var resolution=$("#resolution").text().split("x"),notifRes=0;setTimeout(function(){void 0==picture&&0==notifDownload&&(mainReady.append($("<div/>",{class:"flowspinner"}),$("<span/>",{class:"download-info"}).text("Downloading")),notifDownload=1)},1e3);var img=$("<img />",{class:"picture",alt:"Image"}).attr("src","/img/"+$("#picture-key").val()+"."+$("#format").data("format")).on("load",function(){if(this.complete&&void 0!==this.naturalWidth&&0!=this.naturalWidth){if(notifDownload=1,$(".flowspinner").length&&($(".flowspinner").remove(),$(".download-info").remove()),mainReady.append(img),picture=$(".picture"),window.innerWidth<resolution[0]&&window.innerHeight<resolution[1])if(resolution[0]>resolution[1]&&window.innerWidth>window.innerHeight){for(i=1;i<11;i++)if(window.innerWidth*i>=resolution[0]){window.innerHeight*i>=resolution[1]&&(notifRes=1);break}1==notifRes&&(settingPicture(),picture.css("max-height","inherit"))}else if(resolution[0]<resolution[1]&&window.innerWidth<window.innerHeight){for(var i=1;i<11;i++)if(console.log(window.innerWidth*i+" // "+window.innerHeight*i+" // "+i+" // "+$(window).width()+" // "+$(window).height()),window.innerHeight*i>=resolution[1]){if(window.innerWidth*i<=resolution[0]){notifRes=1;break}break}1==notifRes&&(settingPicture(),picture.css("max-width","inherit"))}setTimeout(function(){picture.css("transform","rotateX(0deg)"),picture.css("boxShadow","none"),picture.css("opacity","1")},100)}else mainReady.append($("<span />").text("Broken image"))})}else if($(".file").length)for(var files_pictures=$(".file"),i=0;i<files_pictures.length;i++)addListenerDownload(files_pictures[i]);