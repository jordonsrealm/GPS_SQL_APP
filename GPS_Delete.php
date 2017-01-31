<?php

	require "conn_noACK.php";
	
	$sql_query = "DELETE FROM Location WHERE 1";
	
	$result=mysqli_query($conn,$sql_query);
	
	$num_rows = mysqli_num_rows($result);

	if($num_rows>0) {
		 while($row = mysqli_fetch_array($result)) {
			$r[]=$row;
		 }
		 print(json_encode($r));
	
	}
	else {
		echo "<h1>Error...</h1>".mysql_error($conn);
		echo "<h2>Doesn't work...</h2>";
	}
	
	mysqli_close($con);

?>