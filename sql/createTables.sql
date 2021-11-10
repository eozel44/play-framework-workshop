CREATE TABLE users (
	id int AUTO_INCREMENT PRIMARY KEY, 
	username varchar(20) NOT NULL, 
	password varchar(200) NOT NULL
);

CREATE TABLE tasks (
	task_id int AUTO_INCREMENT PRIMARY KEY, 
	user_id int NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	text varchar(2000) NOT NULL
);
