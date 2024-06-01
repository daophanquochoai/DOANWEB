// filter add tag into div 
const selectTag = document.querySelector(".section_question .section_box .form-select")
const tagGroup = document.querySelector(".section_question .section_box .tag_group")

selectTag.addEventListener("change", ()=>{
    var selectedOption = selectTag.options[selectTag.selectedIndex];
    tagGroup.appendChild(createTag(selectedOption.value))
})

const createTag = (tagName) => {
    // create tag
    const span = document.createElement("span");
    span.style.outline = "1px solid " + "#" + randomColor();
    span.style.color = "#" + randomColor();
    span.classList.add("p-1");
    span.textContent = tagName;
    span.style.margin = "3px 5px";
    span.style.borderRadius = "5px";
    span.style.position = "relative";
    // create remove tag
    const iconRemove = document.createElement("i");
    iconRemove.classList.add("fa-solid", "fa-circle-xmark");
    iconRemove.style.color = "#" + randomColor();
    iconRemove.style.position = "absolute";
    iconRemove.style.font = "12px"
    iconRemove.style.top = "-6px";
    iconRemove.style.right = "-6px";
    iconRemove.style.cursor = "pointer";
    // luu gia tri upload
    const input = document.createElement("input");
    input.style.display = "none";
    input.value = tagName;
    input.setAttribute("name", "tags");
    span.appendChild(input);
    // add elemant
    span.appendChild(iconRemove);
    // xoa di option trong select
    deleteTagInSelect(tagName);
    // action remove
    iconRemove.addEventListener("click", ()=>{
        xoaTag(iconRemove, tagName);
    })
    return span;
}

const xoaTag = (e, tagName) =>{
    tagGroup.removeChild(e.parentElement)
    const option = document.createElement("option");
    option.value = tagName;
    option.text = tagName;
    selectTag.appendChild(option);
}

const deleteTagInSelect = (value) => {
    var options = selectTag.getElementsByTagName("option");
    for (var i = 0; i < options.length; i++) {
        if( options[i].value === value ){
            selectTag.removeChild(options[i]);
            selectTag.selectedIndex = 0;
            return;
        }
    }
}

// modal filter
const filterButton = document.querySelector(".section_question .bottom-top-filter");
const filterBox = document.querySelector('.section_question .section_box');

filterButton.addEventListener("click", ()=>{
    if (filterBox.style.display === "none" || filterBox.style.display === "") {
        filterBox.style.display = "block";
        filterButton.style.background = "rgba(13, 202, 240, 0.1)";
    } else {
        filterBox.style.display = "none";
        filterButton.style.background = "white"
    }
})