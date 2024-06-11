//--show model---
const addTag = document.querySelector(".add-tag");
if(addTag)
{
    const modalAddTag = document.querySelector(".modal-add-tag");
    addTag.addEventListener("click", () => {
        modalAddTag.style.display = "flex";
    });
};


const closeModal = document.querySelector(".close-modal");
if(closeModal)
{
    const modalAddTag = document.querySelector(".modal-add-tag");
    closeModal.addEventListener("click", () => {
        modalAddTag.style.display = "none";
    });
}
//--show model---

const sortNew = document.querySelector("[sort-new]");
const sortDefault = document.querySelector("[sort-macdinh]");

if(sortNew)
{
    const url = new URL(window.location.href);
    console.log(url);
    sortNew.addEventListener("click", () => {
        url.searchParams.set("sort", "thoigiantao");
        window.location.href = url.href;
    })
}

if(sortDefault)
{
    const url = new URL(window.location.href);
    console.log(url);
    sortDefault.addEventListener("click", () => {
        url.searchParams.delete("sort");
        window.location.href = url.href;
    })
}

const url = new URL(window.location.href);
if(url.search.includes("sort=thoigiantao"))
{
    sortNew.classList.add("active");
}
else
{
    sortDefault.classList.add("active");
}
const handleKeyPressTag = (event, value) =>{
    if (event.keyCode === 13) {
        let arr = window.location.search.split(/[&?]/);
        let url = "/tag?";
        if( arr.length > 1){
            let check = false;
            for( let i = 1 ; i < arr.length ; i++ ){
                console.log(arr[i])
                if( arr[i].startsWith("q=")){
                    check = true;
                    url += ("q=" + value);
                }else if(  arr[i].startsWith("page=") ){
                    url += ("page=1");
                }
                else {
                    url += arr[i];
                }
                if( i != arr.length -1){
                    url += "&";
                }
            }
            if( !check ) url += ("&q=" + value);
        }else{
            url += ("q=" + value);
        }

        window.location.href = url;
    }
}

let arr = window.location.search.split(/[&?]/);
if( arr.length > 1) {
    let check = false;
    for (let i = 1; i < arr.length; i++) {
        console.log(arr[i])

    }
}
