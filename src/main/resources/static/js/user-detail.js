//preloading delete
const handleDeletePreloading = (id) => {
    const buttonDeletePost = document.querySelector(".button_delete" + id);
    const preDeletePost = document.querySelector(".button_delete_loading" + id)
    buttonDeletePost.style.display = "none";
    preDeletePost.style.display = "inline-block";
}