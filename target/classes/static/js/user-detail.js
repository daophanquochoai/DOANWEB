//preloading delete
const preDeletePost = document.querySelector(".button_delete_loading");
preDeletePost.style.display = "none";
const handleDeletePreloading = (id) => {
    const buttonDeletePost = document.querySelector(".button_delete" + id);
    const preDeletePost = document.querySelector(".button_delete_loading" + id)
    buttonDeletePost.style.display = "none";
    preDeletePost.style.display = "inline-block";
}

const previewImage = document.querySelector("[preview-image]");
if(previewImage)
{
    const inputImage = previewImage.querySelector("[image-input]");
    const seeImage = previewImage.querySelector("[image-see]");
    inputImage.addEventListener("change", (e) => {
        const [file] = inputImage.files;
        if(file)
        {
            seeImage.src = URL.createObjectURL(file);
        }
    })
}

const iconUploadAvater = document.querySelector("[icon-upload-avater]");
if(iconUploadAvater)
{
    iconUploadAvater.addEventListener("click", () => {
        const inputUpload = document.querySelector("[preview-image] input[type='file']");
        inputUpload.click();
    })
}