const csrfToken=$("#csrfToken").val();
const validateRoute =$("#validateRoute").val();
const tasksRoute = $("#tasksRoute").val();
const createRoute = $("#createRoute").val();
const deleteRoute = $("#deleteRoute").val();
const addRoute = $("#addRoute").val();
const logoutRoute = $("#logoutRoute").val();

function login() {
	const username = $("#loginName").val();
	const password = $("#loginPass").val();

    fetch(validateRoute, { 
		method: 'POST',
		headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
		body: JSON.stringify({ username, password })
	}).then(res=>res.json()).then(data=>{

		if(data){
			document.getElementById("login-section").hidden = true;
			document.getElementById("task-section").hidden = false;
			document.getElementById("login-message").innerHTML = "";
      		document.getElementById("create-message").innerHTML = "";
			loadTasks();
		}else{

			document.getElementById("create-message").innerHTML = "User Creation Failed";
		}       	
    });
}

function loadTasks(){

	const ul = document.getElementById("task-list");
  ul.innerHTML = "";
  fetch(tasksRoute).then(res => res.json()).then(tasks => {
    for (let i = 0; i < tasks.length; ++i) {
      const li = document.createElement("li");
      const text = document.createTextNode(tasks[i]);
      li.appendChild(text);
      li.onclick = e => {
        fetch(deleteRoute, { 
          method: 'POST',
          headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
          body: JSON.stringify(i)
        }).then(res => res.json()).then(data => {
          if(data) {
            document.getElementById("task-message").innerHTML = "";
            loadTasks();
          } else {
            document.getElementById("task-message").innerHTML = "Failed to delete.";
          }
        });
      }
      ul.appendChild(li);
    }
  });
}

function addTask() {
	let task = document.getElementById("newTask").value;
	fetch(addRoute, { 
		  method: 'POST',
		  headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
		  body: JSON.stringify(task)
	  }).then(res => res.json()).then(data => {
	  if(data) {
		loadTasks();
		document.getElementById("newTask").value = "";
		document.getElementById("task-message").innerHTML = "";
	  } else {
		document.getElementById("task-message").innerHTML = "Failed to add.";
	  }
	});
  }
  
  function logout() {
	fetch(logoutRoute).then(res => res.json()).then(tasks => {
	  document.getElementById("login-section").hidden = false;
	  document.getElementById("task-section").hidden = true;
	});
  }

  function createUser() {
	const username = document.getElementById("createName").value;
	const password = document.getElementById("createPass").value;
	fetch(createRoute, { 
		  method: 'POST',
		  headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
		  body: JSON.stringify({ username, password })
	  }).then(res => res.json()).then(data => {
	  if(data) {
		document.getElementById("login-section").hidden = true;
		document.getElementById("task-section").hidden = false;
		document.getElementById("login-message").innerHTML = "";
		document.getElementById("create-message").innerHTML = "";
		loadTasks();
	  } else {
		document.getElementById("create-message").innerHTML = "User Creation Failed";
	  }
	});
  }
