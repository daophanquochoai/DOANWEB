const handleSearchUser = (event, value) => {
    if( event.keyCode === 13 ){
        window.location.href = '/users?q=' + value;
    }
}