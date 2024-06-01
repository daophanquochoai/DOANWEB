// hien thi va tat unsave
const handelModalDelete = (id) => {
    const buttonUnSave = document.querySelector(".section_body_main .section_body_bottom .section_box_saved .box_saved .item_saved .saved_right .button_unsave" + id);
    const modalUnSave = document.querySelector(".section_body_main .section_body_bottom .section_box_saved .box_saved .item_saved .saved_right .saved_right_modal" + id);
    console.log(buttonUnSave)
    console.log(modalUnSave)
    if( modalUnSave.style.display === "none" || modalUnSave.style.display === ""){
        modalUnSave.style.display = "block";
    }else{
        modalUnSave.style.display = "none";
    }
}

// change page setting
const buttonSaved = document.querySelector(".section_body .body_bottom .navbar_saved");
const buttonSetting = document.querySelector(".section_body .body_bottom .navbar_setting");
const pageSaved = document.querySelector(".section_body .section_body_bottom .box_saved");
const pageTitleSaved = document.querySelector(".section_body .section_body_bottom .saved_title");
const pageSetting = document.querySelector(".section_body .section_body_bottom .section_body_setting");

buttonSetting.addEventListener("click", ()=>{
   pageSetting.style.display = "flex";
   pageSaved.style.display = "none";
   pageTitleSaved.style.display = "none";
   buttonSaved.classList.remove("active");
   buttonSetting.classList.add("active");
});

buttonSaved.addEventListener("click", ()=>{
    pageSetting.style.display = "none";
    pageSaved.style.display = "block";
    pageTitleSaved.style.display = "flex";
    buttonSaved.classList.add("active");
    buttonSetting.classList.remove("active");
});


//search
const handleSearch = (event, value) => {
    if (event.keyCode === 13) {
        window.location.href = '/saved?q=' + value;
    }
}

// thay doi mat khau
const inputPass = document.getElementById("floatingInput");
const inputPassAgian = document.getElementById("floatingPassword");
const buttonThayDoi = document.getElementById("button_thay_doi");

if( inputPass){
    inputPass.addEventListener("keyup", ()=>{
        if( inputPass.value != "" ){
            if( inputPass.value === inputPassAgian.value){
                buttonThayDoi.disabled = false;
            }else{
                buttonThayDoi.disabled = true;
            }
        }else{
            buttonThayDoi.disabled = true;
        }
    });
}
if( inputPassAgian ){
    inputPassAgian.addEventListener("keyup", ()=>{
        if( inputPassAgian.value != "" ){
            if( inputPass.value === inputPassAgian.value){
                buttonThayDoi.disabled = false;
            }else{
                buttonThayDoi.disabled = true;
            }
        }else{
            buttonThayDoi.disabled = true;
        }
    });
}