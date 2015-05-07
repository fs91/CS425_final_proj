<?php

session_start();
if (!isset($_SESSION["username"])) {
    header("location: login.php"); 
    exit();
}
header("Content-Type: text/html; charset=UTF-8");
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

$rid = $_GET["rid"];

if (isset($_POST["rev"]) && strcmp($_POST["rev"],"")!=0) {
		
	$rev = $_POST["rev"];
         
    $address = "127.0.0.1";
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "PRE|$userid|$rid|$rev\r\n";
    socket_write($socket, $out, strlen($out));
    $result = socket_read($socket, 2048);
    $result = substr($result, 0, -1);
	socket_close($socket);
	if(strcmp($result,"success")==0)
	{
		header("location: resrev.php?rid=$rid");
		exit();
	}
}

?>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Restaurant reviews</title>
</head>
<body>
	Welcome <?php echo $username?> 
	<br>
<a href="logout.php" >Logout</a>
<br>
<a href="reslist.php" >Back</a>
<br>
<br>
<br>
Reviews:<br><br>
<?php 
	$address = "127.0.0.1"; 
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "VRR|$rid\r\n";
	socket_write($socket, $out, strlen($out));
	$result = substr(socket_read($socket, 2048),1,-1);
	if(strcmp($result,"")!=0) {
		$piece = explode("+", $result);
		foreach ($piece as &$tmp) {
    		$tmppiece = explode("|", $tmp);
			echo "$tmppiece[2] Says: $tmppiece[4]&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&#x1f44d($tmppiece[5]) &#x1f44e($tmppiece[6])<br><br>";
		}
	}
	
?>
<br><br><br><br>
Write review:<br><br>
<form id="form1" name="form1" method="post" action="resrev.php?rid=<?php echo $rid ?>">
		<input name="rev" class="logininput" type="text" id="rev" size="50" />
        <br>
        <br>
        <input type="submit" class="button" name="button" id="button" value="add" />
        </form>

</body>
</html>