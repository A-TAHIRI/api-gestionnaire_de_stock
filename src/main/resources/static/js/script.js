$(document).ready(function () {

    let fileUpload = $("#fileUploadModules"); // mon input type file
    fileUpload.simpleUpload({
        url : "/file/upload"
    }).on("upload:done", function (e,file, i , data) { // cette méthode sera appeler lorsque le telechargement sera terminer
        if(data.erreur == true){
            alert(data.message)
            return
        }
        //$("#pathImage").val(data.pathFile)
        //$("#image").attr("src","/file/image/"+data.pathFile)

        $.post("/admin/produit/photos",{"idProduit": $("#id").val(), "urlPhoto": data.pathFile}, function(response){
            displayPhotos(response)
        })
    })

    // lorsque un produit contient une seul image
    let fileUploadProduit = $("#fileUploadProduit"); // mon input type file
    fileUploadProduit.simpleUpload({
        url : "/file/upload"
    }).on("upload:done", function (e,file, i , data) { // cette méthode sera appeler lorsque le telechargement sera terminer
        if(data.erreur == true){
            alert(data.message)
            return
        }
        $("#photoPresentation").val(data.pathFile)
        $("#imageProduit").attr("src","/file/image/"+data.pathFile)
    })

    function displayPhotos(photos){
        if(photos == null){
            return
        }
        $("#listImages").html("")
        for(let i = 0; i < photos.length; i++){
            let element = $("<img>",{ // créer une balise img
                src : "/file/image/"+photos[i].lien,
                width : 100
            })
            $("#listImages").append(element)
        }
    }
})