var picture,optimizeInterval,fileElem;function copyToClipboard(b){var c=$("<input>");$("body").append(c),c.val(b).select(),document.execCommand("copy"),c.remove()}function clearOthers(){"0px"===main.css("top")&&main.css("background-color","black"),$(".flowspinner").remove(),$(".bold").show(),picture&&picture.show(),fileElem&&fileElem.show(),alert("Something was wrong, check the type of file.")}function ajaxSend(b,c){$.ajax({url:"/upload/image"+("are"===c?"s":""),type:"PUT",data:b,cache:!1,processData:!1,contentType:!1,xhr(){var d=$.ajaxSettings.xhr();return d.upload.addEventListener("progress",function(f){if(f.lengthComputable){if(!$("#progress-percent").length){"0px"===main.css("top")&&main.css("background-color","#34495E");var g=document.createElementNS("http://www.w3.org/2000/svg","svg");g.setAttributeNS(null,"id","progress-percent"),g.setAttributeNS(null,"width","200"),g.setAttributeNS(null,"height","200");var h=document.createElementNS("http://www.w3.org/2000/svg","circle");h.setAttributeNS(null,"id","circle"),h.setAttributeNS(null,"r","90"),h.setAttributeNS(null,"cx","100"),h.setAttributeNS(null,"cy","100");var j=document.createElementNS("http://www.w3.org/2000/svg","text");j.setAttributeNS(null,"id","percent"),j.setAttributeNS(null,"x","100"),j.setAttributeNS(null,"y","-85"),main.append(g,$("<span/>",{"class":"upload-info"}).text("Uploading")),progress=$("#progress-percent"),progress.append(h,j),percent=$("#percent"),percent.text("0%"),pie=$("#circle"),pieOfValue=pie.css("strokeDasharray").split(" ")[1],dash="x"===pieOfValue.charAt(pieOfValue.length-1)?pieOfValue.substring(0,pieOfValue.length-2):pieOfValue,$(".bold").hide(),$(".drag-info").hide(),$(".click-info").hide(),picture&&picture.hide(),fileElem&&fileElem.hide(),main.css("justify-content","center"),main.css("align-items","center"),main.css("flex-direction","column"),$(".upload-info").show(),notif=1}2!=notif&&(percentComplete=Math.ceil(100*(f.loaded/f.total)),optimizeInterval&&(optimizeInterval=setInterval(function(){25<=percentComplete-prevPercent?(pie.css("transition","stroke-dasharray .01s ease-in, stroke .01s ease-in"),percent.css("transition","fill .01s ease-in")):10<=percentComplete-prevPercent?(pie.css("transition","stroke-dasharray .1s ease-in, stroke .1s ease-in"),percent.css("transition","fill .1s ease-in")):5<=percentComplete-prevPercent?(pie.css("transition","stroke-dasharray .2s ease-in, stroke .2s ease-in"),percent.css("transition","fill .2s ease-in")):(pie.css("transition","stroke-dasharray .3s ease-in, stroke .3s ease-in"),percent.css("transition","fill .3s ease-in")),prevPercent=percentComplete},1e3)),color=90<percentComplete?0:75<percentComplete?1:50<percentComplete?2:25<percentComplete?3:4,pie.css("stroke",colors[color]),percent.css("fill",colors[color]),pie.css("strokeDasharray",percentComplete*dash/100+" "+dash),percent.text(percentComplete+"%"),100===percentComplete&&(notif=2,setTimeout(function(){0===wasError?($(".upload-info").remove(),progress.remove(),main.append($("<div/>",{"class":"flowspinner"}),$("<span/>",{"class":"optimize-info"}).text("Optimization"))):wasError=0},1e3)))}},!1),d},success(d){window.location.href=site+d},error(){clearInterval(optimizeInterval),percentComplete=0,notif=0,wasError=1,progress.remove(),$(".optimize-info").remove(),$(".upload-info").remove(),clearOthers()}})}function ajaxUpload(b){if(b)if(b.size>maxFileSize)alert("File size is bigger than 10MB");else{var c=new FormData;c.append("file",b),ajaxSend(c,"is")}}function ajaxUploads(b){var d,c=0;for(d=0;d<b.length;d++)c+=b[d].size;if(c>maxFileSize)alert("Files size is bigger than 10MB");else{var f=new FormData;for(d=0;d<b.length;d++)f.append("multipartFiles",b[d]);ajaxSend(f,"are")}}var percent,pie,progress,pieOfValue,dash,notif=0,wasError=0,prevPercent=0,colors=["#2ECC71","#1ABC9C","#F39C12","#E67E22","#E74C3C"],color=0,percentComplete=0;function ajaxGetInfo(b,c){isProcessing||(isProcessing=!0,$.ajax({url:"/info_image/"+b,type:"GET",success(d){"left"===c?actionDoLeft(d):actionDoRight(d)}}))}function ajaxSendURL(b){picture&&picture.hide(),$(".bold").hide(),$(".drag-info").hide(),$(".click-info").hide(),fileElem&&fileElem.hide(),"0px"===main.css("top")&&main.css("background-color","#34495E"),main.append($("<div/>",{"class":"flowspinner"})),$.ajax({url:"/upload?url="+b,type:"PUT",success(c){window.location.href=site+c},error(){clearOthers()}})}