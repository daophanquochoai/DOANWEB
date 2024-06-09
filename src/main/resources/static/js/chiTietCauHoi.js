//modal image full screen
const overplay = document.querySelector(".overplay")
const imageOverplay = document.querySelector(".overplay img");
const closeOverplay = document.querySelector(".overplay i");
// const imageMain = document.querySelector(".")

const modalImage = (urlImage) =>{
    overplay.style.display = "flex";
    imageOverplay.setAttribute("src",  urlImage);
}
closeOverplay.addEventListener("click", ()=>{ 
    overplay.style.display="none";
})



// binh luan
const buttonUpload = document.querySelector(".section_binhluan_box .binhluan_box .toolkit .upload_image");
const counterImage = document.querySelector(".section_binhluan_box .binhluan_box .counter_image");
const containerImage = document.querySelector(".section_binhluan_box .binhluan_box .container-image");
const Upload = document.querySelector(".section_binhluan_box .binhluan_box .inputUpload");

var counter = 0;

buttonUpload.addEventListener("click", ()=>{
    Upload.click();
})

Upload.addEventListener("change", ()=>{
    if(Upload.files.length > 0) {
        const selectedFile = Upload.files[0];
        const imageUrl = URL.createObjectURL(selectedFile);
        counter++;
        if( counter == 5){
            buttonUpload.style.display = "none";
        }
        // tao image
        const div = document.createElement("div");
        div.classList.add("image_upload");
        const image = document.createElement("img");
        image.classList.add("image_add");
        image.src = imageUrl;
        div.appendChild(image);
        const icon = document.createElement("i");
        icon.classList.add("fa-regular", "fa-circle-xmark", "icon_attach");
        icon.id = counter;
        const i = Upload.cloneNode(true);
        i.setAttribute("name","fileInput");
        // i.style.display = "none";
        div.appendChild(i);
        counterImage.innerHTML = counter;
        div.appendChild(icon);
        containerImage.appendChild(div);
        // them action xoa
        const iconAttach = document.getElementById(counter);
        iconAttach.addEventListener("click", ()=>{
            containerImage.removeChild(iconAttach.parentElement);
            if( counter == 5){
                buttonUpload.style.display = "inline-block";
            }
            counter--;
            counterImage.innerHTML = counter;
        })
    }
});


//loading
const buttonClick = document.querySelector(".section_binhluan_box .button_send");
const buttonLoading = document.querySelector(".section_binhluan_box .button_loading_binh_luan");
const baiDangId = document.querySelector(".bai_dang_id");
const nguoiBinhLuanId = document.querySelector(".nguoi_binh_luan");
const comment = document.getElementById("comment");

buttonLoading.style.display = "none";
buttonClick.addEventListener("click", ()=>{
    if(comment.value ){
        buttonClick.style.display = "none";
        buttonLoading.style.display = "inline-block";
        var message = {
            "baidang" : baiDangId.innerHTML,
            "nguoi" : nguoiBinhLuanId.value
        }
        sendNotice(message);
    }
})
