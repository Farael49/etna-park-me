<?php
switch($_POST['action']) // switch post data, protection against CSRF
{
	case "AUTHENTICATE":
	authenticate_user();
	break;
	case "OFFER_SPOT" :
	add_spot();
	break;
	case "SEARCH_SPOT" : 
	search_spot_in_area();
	break;
	case "UPDATE_SPOT" :
	update_spot();
	case "REMOVE_SPOT" :
	remove_spot();
	break;
	default :
	$result = array();
        $result["success"] = 0;
	$result["response"] = "Nothing is happening. What a disappointment.";
	echo json_encode($result);
	break;
}

function authenticate_user(){
require_once(__DIR__ . '/db_connect.php');
	$result = array();
	$result["success"] = 0;
	$result["response"] = "Failed to authenticate";
	if(isValidRequest(array('email', 'password')))
	{
		try
		{
		// utilisation d'un PDO avec prepare / execute pour l'Insertion
			$auth = dbconnexion();
			$stmt = $auth->prepare(
				'Select id from user where mail = :email and crypted_pwd = :password;');
			/*** bind les variables au statement pour s'assurer des entrées ***/
			$stmt->bindParam(':email', $_POST['email'], PDO::PARAM_STR);
			$stmt->bindParam(':password', $_POST['password'], PDO::PARAM_STR);
			$stmt->execute();
	                $data = $stmt->fetch();
			if (!empty($data))
				$result["success"] = 1;
			else
				$result["success"] = 0;
			$result["response"] ="WE ARE TRYING TO CONNECT : " . $stmt->queryString;
               }
               catch(PDOException $e) 
               {
		$result["success"] = 0;
               	$result["response"] = "Echec de l\'insertion ! " . $e->getMessage();
               }
	}
         echo json_encode($result);

}


//DRY solution to avoid multiple isset/empty 
function isValidRequest($keys)
{
	foreach($keys as $key)
	{
		if(!isset($_POST[$key]) || empty($_POST[$key]))
			return FALSE;
	}
	return TRUE;
}

/*Ajout d'un spot de parking*/
function add_spot(){
	require_once(__DIR__ . '/db_connect.php');
	$result = array();
	$result["success"] = 0;
	$result["response"] = "Nothing happened yet.";
	if(isValidRequest(array('lat', 'lng', 'user_id', 'time_when_ready')))
	{
		try
		{
		// utilisation d'un PDO avec prepare / execute pour l'Insertion
			$auth = dbconnexion();
			$stmt = $auth->prepare(
				'INSERT INTO parking_spot (lat, lng, user_id, date_offer, time_when_ready, reserved)
				VALUES (:lat, :lng, :user_id, :date_offer, :time_when_ready, false);');
			/*** bind les variables au statement pour s'assurer des entrées ***/
			$stmt->bindParam(':lat', $_POST['lat'], PDO::PARAM_STR);
			$stmt->bindParam(':lng', $_POST['lng'], PDO::PARAM_STR);
			$stmt->bindParam(':user_id', $_POST['user_id'], PDO::PARAM_INT);
			$stmt->bindParam(':date_offer', date_create()->format('Y-m-d H:i:s'), PDO::PARAM_STR);
			$stmt->bindParam(':time_when_ready', $_POST['time_when_ready'], PDO::PARAM_STR);
			$stmt->execute();
        		
			$result["success"] = 1;
			$result["response"] ="WE ARE TRYING TO ADD SOMETHING : " . $stmt->queryString;
               }
               catch(PDOException $e) 
               {
		$result["success"] = 0;
               	$result["response"] = "Echec de l\'insertion ! " . $e->getMessage();
               }
	}
         echo json_encode($result);
       }


       ?>