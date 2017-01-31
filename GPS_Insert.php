<?php
require "conn.php";
$input_LAT = $_POST["latitude"];
$input_LONG = $_POST["longitude"];
$date = time();
$date_value = date("Y-m-d H:m:s",$date);

$sql_query = "insert into Location values('$input_LAT','$input_LONG','$date_value');";

if(mysqli_query($conn,$sql_query)){
echo "<h3>Data Insertion Success...</h3>";
# mail("4047471669@mmst5.tracfone.com","GPS Data Inserted","GPS_Data inserted: Latitude: $input_LAT and Longitude: $input_LONG at $date_value");
}
else{
echo $date;
echo $date_value;
echo "Date insertion error...".mysqli_error($conn);
}

mysqli_close($con);

?>