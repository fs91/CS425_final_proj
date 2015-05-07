<?php 
header("Content-Type: text/html; charset=utf-8");
if (isset($_POST["username"]) && isset($_POST["password"]) && isset($_POST["email"]) && strcmp($_POST["username"],"")!=0 && strcmp($_POST["password"],"")!=0 && strcmp($_POST["email"],"")!=0) {
		
	$username = $_POST["username"];
    $password = $_POST["password"];
    $email = $_POST["email"];
     
    $address = "127.0.0.1"; 
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "REG|$username|$password|$email\r\n";
    socket_write($socket, $out, strlen($out));
    $result = socket_read($socket, 2048);
    $result = substr($result, 0, -1);
	socket_close($socket);
	if(strcmp($result,"duplicate")==0 || strcmp($result,"error")==0)
	{
		echo "Duplicate username";
	}
	else {
		//echo "account created.";
		header("location: login.php");
		exit();
	}
}
?>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Please Login</title>
</head>
<body>
		<form id="form1" name="form1" method="post" action="reg.php">User Name:
		<input name="username" class="logininput" type="text" id="username" size="24" />
        <br>
        <br>
        Password:
        <input name="password" class="logininput" type="password" id="password" size="24" />
        <br>
        <br>
        email address:
        <input name="email" class="logininput" type="email" id="email" size="24" />
        </div>
        <br>
        <br>
        <input type="submit" class="button" name="button" id="button" value="register" />
        </form>
</body>