const divUpload = document.querySelector(".box .avatar");
const inputUpload = document.querySelector(".box .input_avatar");
const password = document.querySelector(".box #matkhau");
const acceptPassword = document.querySelector(".box #xacnhan");
const buttonCreateAccount = document.querySelector(".box .button-submit");
// action upload when click into div
divUpload.addEventListener("click", ()=>{
    inputUpload.click();
})

// action uplen when choosed image
inputUpload.addEventListener("change", ()=>{
    if( inputUpload.files.length > 0){
        const reader = new FileReader();
            // action when file was readed
            reader.onload = function() {
                // Change avatar from file upload
                divUpload.innerHTML = `<img src="${reader.result}" style=" width: 100%; height: 100%;">`;
            }

            // Đọc tệp dưới dạng URL dữ liệu
            reader.readAsDataURL(inputUpload.files[0]);
    }
})



// action when xacnhan and matkhau similar
password.addEventListener("keyup", ()=>{
    if( password.value == acceptPassword.value && password.value != ""){
        buttonCreateAccount.style.display = "block";
    }else{
        buttonCreateAccount.style.display = "none";
    }
})
acceptPassword.addEventListener("keyup", ()=>{
    if( password.value == acceptPassword.value && acceptPassword.value != ""){
        buttonCreateAccount.style.display = "block";
    }else{
        buttonCreateAccount.style.display = "none";
    }
})