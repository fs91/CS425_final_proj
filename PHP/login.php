<?php 
session_start();
if (isset($_SESSION["username"])) {
    header("location: index.php");
    exit();
}
?>
<?php 
// Parse the log in form if the user has filled it out and pressed "Log In"
header("Content-Type: text/html; charset=utf-8");
if (isset($_POST["username"]) && isset($_POST["password"]) && strcmp($_POST["username"],"")!=0 && strcmp($_POST["password"],"")!=0) {
		
	$username = $_POST["username"];
    $password = $_POST["password"];
     
    $address = "127.0.0.1"; 
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "LOG|$username|$password\r\n";
    socket_write($socket, $out, strlen($out));
    $result = socket_read($socket, 2048);
    $result = substr($result, 0, -1);
	socket_close($socket);
	if(strcmp($result,"failed")==0 || strcmp($result,"error")==0)
	{
		echo "Wrong password";
	}
	else {
		$_SESSION["id"] = $result;
		$_SESSION["username"] = $username;
		$_SESSION["password"] = $password;
		header("location: index.php");
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
<a href="reg.php" >Register</a>
		<form id="form1" name="form1" method="post" action="login.php">User Name:
		<input name="username" class="logininput" type="text" id="username" size="24" />
        <br>
        <br>
        <div style="text-indent: 7;">
        Password:
        <input name="password" class="logininput" type="password" id="password" size="24" />
        </div>
        <br>
        <input type="submit" class="button" name="button" id="button" value="Log In" />
        </form>
</body>