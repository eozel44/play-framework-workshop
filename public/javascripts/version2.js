$("#contents").load("/login2")

function login() {
	const username = $("#loginName").val();     
	const password = $("#loginPass").val();
    $("#contents").load("/validate2?username="+username+"&password="+password)
}

function createUser() {
            const username = $("#createName").val();
            const password = $("#createPass").val();
    console.log(username)
}