var resolution,isResolution,typeAnimation,notifDownload=0,rotateDeg=0,mainReady=$(".main"),transitSetting="opacity .05s, transform .2s ease-in, box-shadow .2s ease-in",body=$("body"),footer=$(".footer"),isProcessing=!1,resolutionElem=$("#resolution"),downloadPictureElem=$("#download-picture"),arrowLeftElem=$("#arrow-left"),arrowRightElem=$("#arrow-right"),fileInfoElem=$("#file-info"),sizeElem=$("#size"),formatElem=$("#format"),infoElem=$(".info"),directLinkElem=$("#direct-link"),htmlLinkElem=$("#html-link"),bbcodeLinkElem=$("#bbcode-link"),px200ELem=$("#px200"),px500Elem=$("#px500"),notification=$(".notification");fileElem=$(".file"),notification.css("display","block");function addListenerDownload(a,b){var c="picture"===b;c&&(resolution=resolutionElem.text().split("x"),setTimeout(function(){picture?1<$(".main").length&&($(".picture").first().css("filter","brightness(40%)"),$(".main").first().append($("<div/>",{class:"flowspinner"})),notifDownload=1):0==notifDownload&&(mainReady.append($("<div/>",{class:"flowspinner"}),$("<span/>",{class:"download-info"}).text("Downloading")),notifDownload=1)},400));var d=$("<img />",{"class":b,alt:"Image"}).attr("src","/img/"+(c?"":"400_")+a.data("key")+"."+a.data("format")).on("load",function(){this.complete&&"undefined"!=typeof this.naturalWidth&&0!==this.naturalWidth?(c&&(notifDownload=1,$(".flowspinner").length&&($(".flowspinner").remove(),$(".download-info").remove())),a.append(d[0]),c&&(picture=$(".picture").last(),calculateView(0,1),1<$(".main").length&&"0px"!==mainReady.css("top")&&setMainReady(mainReady,"right"!==typeAnimation),"0px"!==mainReady.css("top")&&(footer.css("bottom",$(".arrow-box").length?"-44px":"0px"),footer.css("background-color","transparent"))),setTimeout(function(){2>$(".main").length&&(c?d[0].style.transition=transitSetting+",  max-width .2s ease-in, max-height .2s ease-in":d[0].style.transition="filter .2s ease-in, "+transitSetting),d[0].style.transform="rotateX(0deg)",d[0].style.boxShadow="none",d[0].style.opacity="1",c&&(setTimeout(function(){mainReady.css("transition","top .2s, bottom .2s, left .2s, right .2s"),d[0].style.transition=transitSetting+",  max-width .2s ease-in, max-height .2s ease-in";var e=$(".main");1<e.length?(setMainReady($(e[0]),"left"!==typeAnimation),setMainReadyPx("0px"===mainReady.css("top")?"0px":"25px"),setTimeout(function(){$(e[0]).remove(),isProcessing=!1,main=$(".main"),rotateDeg=0},200)):isProcessing=!1},40),$(window).resize(function(){calcViewRotateDeg()}))},20)):a.append($("<span />").text("Broken image"))})}setTimeout(function(){if($(".arrow-box").length&&(480>screen.width?$(".footer").css("display","none"):$(".footer").css("bottom","-44px")),""!==$(".main").data("format"))addListenerDownload(mainReady,"picture");else for(var a=0;a<fileElem.length;a++)addListenerDownload($(fileElem[a]),"image-folder")},20);