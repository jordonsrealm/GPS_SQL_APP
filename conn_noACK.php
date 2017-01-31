<?php
DEFINE ('DB_USER','Robert_Tijerina');
DEFINE ('DB_PSWD','Password1');
DEFINE ('DB_HOST','localhost');
DEFINE ('DB_NAME','JordonTijerinaDB_1');

$conn = mysqli_connect(DB_HOST,DB_USER,DB_PSWD,DB_NAME);
if(!$conn)
{
echo mysql_error();
}
else
{
}

?>