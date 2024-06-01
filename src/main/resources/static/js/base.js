const randomColor = () => {
    const randomColor = Math.floor(Math.random()*16777215).toString(16);
    return randomColor;
}
  

const createElement = (text) => {
        // create tag
        const span = document.createElement("span");
        span.style.outline = "1px solid " + "#" + randomColor();
        span.style.color = "#" + randomColor();
        span.classList.add("p-1");
        span.textContent = text;
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
        input.value = text;
        input.setAttribute("name", "tags");
        span.appendChild(input);
        // add elemant
        span.appendChild(iconRemove);

        // action remove
        iconRemove.addEventListener("click", ()=>{
            boxTag.removeChild(iconRemove.parentElement);
            const li = document.createElement("li");
            const button = document.createElement("button");
            button.classList.add("dropdown-item");
            button.setAttribute("type", "button");
            button.onclick = function () {
                getTagOrAddTag(iconRemove.parentElement.textContent);
            };
            button.textContent = iconRemove.parentElement.textContent;
            li.appendChild(button);
            menuTag.appendChild(li);
        })
        return span;
}

// nhap input tu giua ra
function limitToOneCharacterAndMoveCaret(input) {
    // Nếu độ dài của giá trị nhập vào là lớn hơn 1, cắt bớt
    if (input.value.length > 1) {
        input.value = input.value.slice(0, 1);
    }

    // Di chuyển con trỏ đến vị trí cuối cùng
    input.setSelectionRange(input.value.length, input.value.length);
}





// websocket 
const connect = () => {
    var socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, connectSuccess, connectError);
}

const connectSuccess = () => {
    console.log("Kết nối thành công");
    // Đăng ký lắng nghe cho kênh "/notice/all" sau khi kết nối thành công
    stompClient.subscribe("/notice/all", receiverMessage);
}

const connectError = () => {
    console.log("Kết nối không thành công");
}

window.addEventListener("load", () => {
    connect();
})

const sendNotice = (message) =>{
    var notice = {
        baidang : message.baidang,
        nguoicomment : message.nguoi,
    }
    stompClient.send("/app/comment", {}, JSON.stringify(notice));
}



// Trong mã JavaScript
const arrayFollowElement = document.querySelector(".array_follow");
const arrayFollowData = JSON.parse(arrayFollowElement.getAttribute("data-array-follow"));
const infoId = document.querySelector("header .info_id");
const noticeList = document.querySelector(".box_notice .notice_list");
const noticeNumber = document.querySelector(".header_info .notice_number");
const receiverMessage = (payload) =>{
    var message = JSON.parse(payload.body);
    if( arrayFollowData != null ){
        if( arrayFollowData.includes(message.baiDangId) && message.nguoiCommentId != infoId.textContent ){
            noticeNumber.textContent = +noticeNumber.textContent + 1;
            const a = document.createElement("a");
            const div = document.createElement("div");
            const li = document.createElement("li");
            li.textContent ="Bài đăng của bạn đã có " + message.tenNguoiComment + " bình luận";
            const span = document.createElement("span");
            span.textContent = message.thoiGian;
            div.appendChild(li);
            div.appendChild(span);
            div.classList.add("notice_item");
            a.appendChild(div);
            a.href = window.location.origin + "/question/" + message.baiDangId;
            div.style.backgroundColor = "#d9d4d4";
            // noticeList.appendChild(a);
            if (noticeList.firstChild) {
                noticeList.insertBefore(a, noticeList.firstChild);
            } else {
                noticeList.appendChild(a);
            }
        }
        if( infoId.textContent == message.nguoiDangId ){
            noticeNumber.textContent = +noticeNumber.textContent + 1;
            const a = document.createElement("a");
            const div = document.createElement("div");
            const li = document.createElement("li");
            li.textContent = "Bài đăng của bạn đã có " +  message.tenNguoiComment  + " bình luận";
            const span = document.createElement("span");
            span.textContent = message.thoiGian;
            div.appendChild(li);
            div.appendChild(span);
            div.classList.add("notice_item");
            a.appendChild(div);
            a.href = window.location.origin + "/question/" + message.baiDangId;
            div.style.backgroundColor = "#d9d4d4";
            // noticeList.appendChild(a);
            if (noticeList.firstChild) {
                noticeList.insertBefore(a, noticeList.firstChild);
            } else {
                noticeList.appendChild(a);
            }
        }
    }
}

// hien, tat thong bao
const buttonNotice = document.querySelector(".box_notice .header_info_notice");

const dispayList = () => {
    if (noticeList.style.display === "none" || noticeList.style.display === "") {
        noticeList.style.display = "block";
    } else {
        noticeList.style.display = "none";
    }
};


buttonNotice.addEventListener("click", dispayList);

document.body.addEventListener('click', function(event) {
    if (event.target !== buttonNotice && !buttonNotice.contains(event.target)) {
        noticeList.style.display = 'none';
    }
});

// search question in header
const handleKeyPress = (event, value) =>{
    if (event.keyCode === 13) {
        window.location.href = '/question?q=' + value;
    }
}
const handleKeyPressQuestion = (event, value) =>{
    if (event.keyCode === 13) {
        let arr = window.location.search.split(/[&?]/);
        let url = "/question?";
        if( arr.length > 1){
            let check = false;
            for( let i = 1 ; i < arr.length ; i++ ){
                if( arr[i].startsWith("q=")){
                    check = true;
                    url += ("q=" + value);
                }else {
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