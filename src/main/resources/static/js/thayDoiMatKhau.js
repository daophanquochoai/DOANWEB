const eyeMatKhau = document.querySelector(".section_xac_nhan .section_animation .hidden_display_mat_khau");
const eyeXacNhan = document.querySelector(".section_xac_nhan .section_animation .hidden_display_xac_nhan");
const inputMatKhau = document.querySelector(".section_xac_nhan .section_animation #matkhau");
const inputXacNhan = document.querySelector(".section_xac_nhan .section_animation #xacnhan");
const buttonSubmit = document.querySelector(".section_xac_nhan .section_animation .button-submit");

const state = {
    matkhau : true,
    xacnhan : true
}

const change = (property, element, input) => {
    if (state[property]) {
        element.classList.remove("fa-eye");
        element.classList.add("fa-solid", "fa-eye-slash");
        input.setAttribute("type", "text");
    } else {
        element.classList.remove("fa-eye-slash");
        element.classList.add("fa-solid", "fa-eye");
        input.setAttribute("type", "password");
    }
    state[property] = !state[property]; // sửa thành state[property]
}

// Hàm sẽ gọi khi click vào biểu tượng mắt của mật khẩu
eyeMatKhau.addEventListener("click", () => {change('matkhau', eyeMatKhau, inputMatKhau)});

// Hàm sẽ gọi khi click vào biểu tượng mắt của xác nhận mật khẩu
eyeXacNhan.addEventListener("click", () => {change('xacnhan', eyeXacNhan, inputXacNhan)});

// hiện thị button khi xác nhận được
inputXacNhan.addEventListener("keyup", ()=>{
    if( inputMatKhau.value != "" && inputXacNhan.value == inputMatKhau.value){
        buttonSubmit.style.display = "block";
    }else{
        buttonSubmit.style.display = "none";
    }
})
inputMatKhau.addEventListener("keyup", ()=>{
    if( inputXacNhan.value != "" && inputXacNhan.value == inputMatKhau.value){
        buttonSubmit.style.display = "block";
    }
    else{
        buttonSubmit.style.display = "none";
    }
})