<?php
session_start();
if (!isset($_SESSION["username"])) {
    header("location: login.php"); 
    exit();
}
header("Content-Type: text/html; charset=utf-8");
//verify username and password
$userid = $_SESSION["id"];
$username = $_SESSION["username"];
$password = $_SESSION["password"];

$address = "127.0.0.1"; 
$port = "9890";
  
$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
socket_connect($socket, $address, $port);
$out = "LOG|$username|$password\r\n";
socket_write($socket, $out, strlen($out));
$result = socket_read($socket, 2048);
$result = substr($result, 0, -1);
socket_close($socket);

if (strcmp($result,$userid)!=0) {
	Session_destroy();
	header("location: login.php");
    exit();
}
?>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Welcome</title>
</head>
<body>
	Welcome <?php echo $username?> 
	<br>
<a href="logout.php" >Logout</a>
<br>
<br>
<br>
Joined Groups:<br>
<?php 
	$address = "127.0.0.1"; 
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "VGL|$userid\r\n";
	socket_write($socket, $out, strlen($out));
	$result = substr(socket_read($socket, 2048),1,-1);
	if(strcmp($result,"")!=0) {
		$piece = explode("+", $result);
		foreach ($piece as &$tmp) {
    		$tmppiece = explode("|", $tmp);
    		if(strcmp($tmppiece[0],"1")==0){
	    		echo "<a href='reslist.php'>$tmppiece[1]</a>--$tmppiece[2]<br>";
    		}
    		else {
				echo "$tmppiece[1]--$tmppiece[2]<br>";
			}
		}
	}
	echo "<br><br><br>Not joined Groups:<br>";
	$address = "127.0.0.1"; 
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "VUGL|$userid\r\n";
	socket_write($socket, $out, strlen($out));
	$result = substr(socket_read($socket, 2048),1,-1);
	if(strcmp($result,"")!=0) {
		$piece = explode("+", $result);
		foreach ($piece as &$tmp) {
    		$tmppiece = explode("|", $tmp);
			echo "$tmppiece[1]--$tmppiece[2] <a href='joingrp.php?uid=$userid&gid=$tmppiece[0]'>Join</a><br>";
		}
	}
	
?>
</body>
</html>