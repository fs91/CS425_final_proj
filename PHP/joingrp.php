<?php
	$uid = $_GET["uid"];
	$gid = $_GET["gid"];
	
	$address = "127.0.0.1"; 
	$port = "9890";
  
	$socket = socket_create(AF_INET, SOCK_STREAM, getprotobyname('tcp'));
	socket_connect($socket, $address, $port);
	$out = "GRP|$uid|$gid\r\n";
	socket_write($socket, $out, strlen($out));
	$result = substr(socket_read($socket, 2048),1,-1);
	
	header("location: index.php");
?>