const buttonXacNhan = document.querySelector(".button_xac_nhan");
const buttonLoadEmail = document.querySelector(".button_loading_email");
const inputReceiver = document.querySelector(".email_receiver");

buttonXacNhan.addEventListener("click", ()=>{
    if( inputReceiver.value != ""){
        buttonXacNhan.style.display = "none";
        buttonLoadEmail.style.display = "inline-block";
    }
})