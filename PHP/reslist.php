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

if (isset($_POST["resname"]) && isset($_POST["desc"]) && strcmp($_POST["resname"],"")!=0) {
		
	$resname = $_POST["resname"];
    $desc = $_POST["desc"];
     
    $address = "127.0.0.1"; 
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "RES|$resname|$desc\r\n";
    socket_write($socket, $out, strlen($out));
    $result = socket_read($socket, 2048);
    $result = substr($result, 0, -1);
	socket_close($socket);
	if(strcmp($result,"success")==0)
	{
		header("location: reslist.php");
		exit();
	}
}	

?>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Restaurant list</title>
</head>
<body>
	Welcome <?php echo $username?> 
	<br>
<a href="logout.php" >Logout</a>
<br>
<a href="index.php" >Back</a>
<br>
<br>
<br>
Restaurants:<br><br>
<?php 
	$address = "127.0.0.1"; 
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "VRL\r\n";
	socket_write($socket, $out, strlen($out));
	$result = substr(socket_read($socket, 2048),1,-1);
	if(strcmp($result,"")!=0) {
		$piece = explode("+", $result);
		foreach ($piece as &$tmp) {
    		$tmppiece = explode("|", $tmp);
			echo "<a href='resrev.php?rid=$tmppiece[0]'>$tmppiece[1] ($tmppiece[3])    $tmppiece[2]</a><br><br>";
		}
	}
	
?>
<br><br><br><br>
Add new restaurant:<br><br>
<form id="form1" name="form1" method="post" action="reslist.php">Restaurant name:
		<input name="resname" class="logininput" type="text" id="resname" size="24" />
        <br>
        <br>
        Restaurant Description:
        <input name="desc" class="logininput" type="text" id="desc" size="24" />
        <br>
        <br>
        <input type="submit" class="button" name="button" id="button" value="add" />
        </form>

</body>
</html>