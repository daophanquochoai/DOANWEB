// add tag or use tag
const uploadIcon = document.querySelector(".modal-body .container-image .upload_icon");
const inputUpload = document.querySelector(".modal-body .container-image #fileInput");
const box = document.querySelector(".modal-body .container-image .box");
const count = document.querySelector(".counter");
const boxTag = document.querySelector(".form-floating .box_tag");
const addBox = document.querySelector(".form-floating .add_tag");
const menuTag = document.querySelector(".form-floating .menu-tag");
const buttonAddTag = document.querySelector(".form-floating .add_tag .btn-add-tag");
const inputAddTag = document.querySelector(".form-floating .add_tag .input-add-tag");
const containerTag = document.querySelector(".form-floating .container_tag");


var counter = 0;
uploadIcon.addEventListener("click", ()=>{
    inputUpload.click();
})
// upload image and render in display
inputUpload.addEventListener("change", ()=>{
    if(inputUpload.files.length > 0) {
        const selectedFile = inputUpload.files[0];
        const imageUrl = URL.createObjectURL(selectedFile);
        counter++;
        if( counter == 5){
            uploadIcon.style.display = "none";
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
        const i = inputUpload.cloneNode(true);
        i.setAttribute("name","fileInput");
        i.style.display = "none";
        div.appendChild(i);
        count.innerHTML = counter;
        div.appendChild(icon);
        box.appendChild(div);
        // them action xoa
        const iconAttach = document.getElementById(counter);
        iconAttach.addEventListener("click", ()=>{
            box.removeChild(iconAttach.parentElement);
            if( counter == 5){
                uploadIcon.style.display = "inline-block";
            }
            counter--;
            count.innerHTML = counter;
        })
        //
    }
})

// add tag or use tag
const getTagOrAddTag = (e) => {
    if( e === "add" ){
        addBox.style.display = "flex";
    }
    else{
        // hide tag add
        addBox.style.display = "none";
        // remove tag selected
        var li = Array.from(menuTag.children).filter( l => l.textContent.trim() === e);
        menuTag.removeChild(li[0]);
        const span = createElement(e);
        boxTag.appendChild(span);
    }
}

// action add tag
buttonAddTag.addEventListener("click", ()=>{
    var text = inputAddTag.value;
    if( Array.from(containerTag.children).filter( t => t.textContent.trim() === text).length > 0){
        return;
    }else{
        var check = Array.from(menuTag.children).filter( l => l.textContent.trim() === text);
        // check 
        if( check.length > 0 ){
            menuTag.removeChild(check[0]);
        }
        inputAddTag.value = "";
        // create tag
        const span = createElement(text);
        boxTag.appendChild(span);
    }

})

// const text = document.querySelector(".text");

// text.addEventListener("click", ()=>{
//     sendNotice();
// })

// preloading
const buttonTaoBaiViet = document.querySelector(".tao_bai_viet")
const buttonLoadingTaoBaiViet = document.querySelector(".button_loading");
const inputTieuDe = document.querySelector(".input_title")
const inputNoiDung = document.querySelector(".input_noi_dung")

buttonTaoBaiViet.addEventListener("click", ()=>{
    if( inputNoiDung.value != "" && inputTieuDe.value != ""){
        buttonTaoBaiViet.style.display = "none";
        buttonLoadingTaoBaiViet.style.display = "inline-block";
    }
})