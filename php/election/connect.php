<?php
$con = new mysqli("localhost", "root", "6161", "votingsystem");
if (!$con) {
    echo "Error: Unable to connect to MySQL." . PHP_EOL;
    echo "Debugging errno: " . $con->connect_errno . PHP_EOL;
    echo "Debugging error: " . $con->connect_errno . PHP_EOL;
    exit;
}
?>