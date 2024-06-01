// pagination
const getPageList = (totalPages, page, maxLength) => {
    const range = (start, end) => {
        return Array.from(Array(end - start + 1), (_, i) => i + start);
    }

    // vidu: có 30 sản phẩm, mỗi trang hiển thị 3 sản phẩm
    // --> tổng số trang (totalPages) = 10
    // giả sử cho maxLength = 7, còn page là currentPage

    var sideWidth = maxLength < 9 ? 1 : 2;  //--> sideWidth = 1
    var leftWidth = (maxLength - sideWidth * 2 - 3) >> 1;  //  2 chia đôi  --> leftWidth = 1
    var rightWidth = (maxLength - sideWidth * 2 - 3) >> 1;  // --> rightWidth = 1
    // Tức là nếu ở trang hiện tại thì tối đa bên trái chỉ hiển thị thêm 2 ô

    // số trang <= số pagi thì in luôn tất cả
    if(totalPages <= maxLength) {
        return range(1, totalPages);
    }

    // page=1 2 3 4
    if(page <= maxLength - sideWidth - 1 - rightWidth) {
        // 1 2 3 4 5 ... 10
        // concat(0, range(totalPages - sideWidth + 1, totalPages)) lưu ý số 0
        return range(1, maxLength - sideWidth - 1).concat(0, range(totalPages - sideWidth + 1, totalPages));
    }

    // page >= 7
    if(page >= totalPages - sideWidth - 1 - rightWidth) {
        // 1 ... 6 7 8 9 10
        // concat(0, range(0, [6, 7, 8, 9, 10]))
        return range(1, sideWidth).concat(0, range(totalPages - sideWidth - 1 - rightWidth - leftWidth, totalPages))
    }

    // 1... [ 5, 6] ... 10
    // 1 ... [4 -> 6] ... [10]
    return range(1, sideWidth).concat(0, range(page - leftWidth, page + rightWidth), 0, range(totalPages - sideWidth + 1, totalPages));
}

var numberOfItem = document.querySelectorAll(".card-content .card").length;
var limitPerPage = 3;
var totalPages = Math.ceil(numberOfItem / limitPerPage);
var paginationSize = 7;  // ~~ độ dài tối đa của phân trang ~~ maxLength  [1, 2, 3, 4, 5, ..., 10]
var currentPage;

const showPage = (whichPage) => {
    //Nếu trang không hợp lệ thì không có gì thay đổi
    if(whichPage < 1 || whichPage > totalPages) {
        return false;
    }
    currentPage = whichPage;

    // ------------------Vì code dựng sẵn ra giao diện nên làm như này -----------------------
    // Ban đầu ẩn tất cả
    var cards = document.querySelectorAll(".card-content .card");
    cards.forEach(card => card.style.display = "none");

    // hiển thị nội dung 
    var startIndex = (currentPage - 1) * limitPerPage;
    var endIndex = Math.min(startIndex + limitPerPage, numberOfItem);
    for (var i = startIndex; i < endIndex; i++) {
        cards[i].style.display = "block";
    }
    // ------------------Vì code dựng sẵn ra giao diện nên làm như này -----------------------


    var pagination = document.querySelector(".pagination");
    pagination.innerHTML = '';

    // hiển thị thanh pagination
    getPageList(totalPages, currentPage, paginationSize).forEach(item => {
        var li = document.createElement("li");
        li.classList.add("page-item");
        li.classList.add(item ? "current-page" : "dots");
        li.classList.toggle("active", item === currentPage);

        var a = document.createElement("a");
        a.classList.add("page-link");
        a.href = "javascript:void(0)";
        a.textContent = item || "...";

        li.appendChild(a);
        pagination.appendChild(li);
    });

    // Nút pre
    var prevItem = document.createElement("li");
    prevItem.classList.add("page-item");
    prevItem.classList.add("previous-page");
    var prevLink = document.createElement("a");
    prevLink.classList.add("page-link");
    prevLink.href = "javascript:void(0)";
    prevLink.textContent = "Prev";
    prevItem.appendChild(prevLink);
    pagination.insertBefore(prevItem, pagination.firstChild);

    //Nút next
    var nextItem = document.createElement("li");
    nextItem.classList.add("page-item");
    nextItem.classList.add("next-page");
    var nextLink = document.createElement("a");
    nextLink.classList.add("page-link");
    nextLink.href = "javascript:void(0)";
    nextLink.textContent = "Next";
    nextItem.appendChild(nextLink);
    pagination.appendChild(nextItem);

    document.querySelector(".previous-page").classList.toggle("disable", currentPage === 1);
    document.querySelector(".next-page").classList.toggle("disable", currentPage === totalPages);

    return true;
}

showPage(1);

// const listLiNotActive = document.querySelectorAll(".pagination li.current-page:not(.active)");
// listLiNotActive.forEach(li => {
//     li.addEventListener("click", () => {
//         const a = li.querySelector("a");
//         showPage(parseInt(a.text));
//     })
// })

document.addEventListener("click", function(event) {
    if (event.target.matches(".pagination li.current-page:not(.active)")) {
        const a = event.target.querySelector("a");
        const pageNumber = parseInt(parseInt(a.text));
        if (!isNaN(pageNumber)) {
            showPage(pageNumber);
        }
    }
    else if (event.target.matches(".pagination li.current-page:not(.active) a")) {
        const pageNumber = parseInt(event.target.textContent);
        if (!isNaN(pageNumber)) {
            showPage(pageNumber);
        }
    }
    else if(event.target.matches(".pagination li.next-page"))
    {
        showPage(currentPage + 1);
    }
    else if(event.target.matches(".pagination li.next-page a"))
    {
        showPage(currentPage + 1);
    }
    else if(event.target.matches(".pagination li.previous-page"))
    {
        showPage(currentPage - 1);
    }
    else if(event.target.matches(".pagination li.previous-page a"))
    {
        showPage(currentPage - 1);
    }
});

// document.querySelector(".next-page").addEventListener("click", () => {
//     return showPage(currentPage + 1);
// })



