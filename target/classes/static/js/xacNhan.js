const textCount = document.querySelector(".section_xac_nhan .section_animation .text_count");
const buttonSubmit = document.querySelector(".section_xac_nhan .section_animation button");
const guiLai = document.querySelector(".section_xac_nhan .section_animation .gui_lai");

var time = 60

window.addEventListener("load",()=>{
    let couter = setInterval(()=>{
        if( time === 0 ){
            clearInterval(couter);
            buttonSubmit.style.display = "none";
            guiLai.classList.add("btn","btn-info");
            guiLai.style.color = "white";
        }
        textCount.textContent = time;
        time -= 1;
    }, 1000);
});
const submitForm = () =>{
    document.getElementById("hiddenForm").submit();
}
