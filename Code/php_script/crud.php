<?php
if(isValidRequest(array('action'))){
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
	case "REGISTER" :
	register_user();
	break;
	case "GET_ALL_SPOTS" :
	get_spots();
	break;
	default :
	$result = array();
	$result["success"] = 0;
	$result["response"] = "Nothing is happening. What a disappointment.";
	echo json_encode($result);
	break;
}
}

function authenticate_user(){
	require_once(__DIR__ . '/db_connect.php');
	$result = array();
	$result["success"] = 0;
	$result["response"] = "HTTP Request failed : lacking data";
	$user_id = -1;
	if(isValidRequest(array('email', 'password')))
	{
		$result["response"] ="Trying to connect : " . $_POST['email'];
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
			$stmt->closeCursor();
			if (!empty($data)){
				$result["success"] = 1;
				$user_id = $data[0];
			}
			else
				$result["success"] = 0;
		}
		catch(PDOException $e) 
		{
			$result["success"] = 0;
			$result["response"] = "Echec de l\'insertion ! " . $e->getMessage();
		}
	}
	echo json_encode($result);
	return $user_id;
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
	$result["response"] = "HTTP Request failed : lacking data";
	if(isValidRequest(array('lat', 'lng', 'email', 'password', 'time_when_ready')))
	{
		try
		{
		// utilisation d'un PDO avec prepare / execute pour l'Insertion
			$auth = dbconnexion();
			$user_id = authenticate_user();
			$stmt = $auth->prepare(
				'INSERT INTO parking_spot (lat, lng, user_id, date_offer, time_when_ready, reserved)
				VALUES (:lat, :lng, :user_id, :date_offer, :time_when_ready, false);');
			/*** bind les variables au statement pour s'assurer des entrées ***/
			$stmt->bindParam(':lat', $_POST['lat'], PDO::PARAM_STR);
			$stmt->bindParam(':lng', $_POST['lng'], PDO::PARAM_STR);
			$stmt->bindParam(':user_id', $user_id, PDO::PARAM_INT);
			$stmt->bindParam(':date_offer', date_create()->format('Y-m-d H:i:s'), PDO::PARAM_STR);
			$stmt->bindParam(':time_when_ready', $_POST['time_when_ready'], PDO::PARAM_STR);
			$stmt->execute();
			$stmt->closeCursor();
			$result["success"] = 1;
			$result["response"] ="Spot added at " . $_POST['lat'] . " lat, " . $_POST['lng'] . " lng for user " . $_POST['email'];
		}
		catch(PDOException $e) 
		{
			$result["success"] = 0;
			$result["response"] = "Echec de l\'insertion ! " . $e->getMessage();
		}
	}
	echo json_encode($result);
}

/*Inscription d'un utilisateur*/
function register_user(){
	require_once(__DIR__ . '/db_connect.php');
	$result = array();
	$result["success"] = 0;
	$result["response"] = "HTTP Request failed : lacking data";
	if(isValidRequest(array('email', 'password')))
	{
		try
		{
		// utilisation d'un PDO avec prepare / execute pour l'Insertion
			$auth = dbconnexion();
			$stmt = $auth->prepare(
				'INSERT INTO user (mail, crypted_pwd) VALUES (:email, :pwd);');
			/*** bind les variables au statement pour s'assurer des entrées ***/
			$stmt->bindParam(':email', $_POST['email'], PDO::PARAM_STR);
			$stmt->bindParam(':pwd', $_POST['password'], PDO::PARAM_STR);
			$stmt->execute();
			$stmt->closeCursor();

			$result["success"] = 1;
			$result["response"] = "Registered " . $_POST['email'];
		}
		catch(PDOException $e) 
		{
			$result["success"] = 0;
			$result["response"] = "Echec de l\'inscription ! " . $e->getMessage();
		}
	}
	echo json_encode($result);
}

function get_spots(){
	require_once(__DIR__ . '/db_connect.php');
	$result = array();
	$result["response"] = "Nothing yet";
	if(isValidRequest(array('user_lat', 'user_lng', 'radius')))
	{
		try
		{
			$auth = dbconnexion();
			$sql = "Select count(*) from parking_spot where 
			((parking_spot.lat - 42.2)^2 + (parking_spot.lng - 2.2)^2 < 1000^2);";
			if ($res = $auth->query($sql)) {

				/* Récupère le nombre de lignes qui correspond à la requête SELECT */
				if ($res->fetchColumn() > 0) {
		// utilisation d'un PDO avec prepare / execute pour l'Insertion
					$stmt = $auth->prepare(
						"Select count(*) from parking_spot where 
			((parking_spot.lat - 42.2)^2 + (parking_spot.lng - 2.2)^2 < 1000^2);", array(PDO::MYSQL_ATTR_USE_BUFFERED_QUERY => true));
					/*** bind les variables au statement pour s'assurer des entrées ***/
					$stmt->bindParam(':user_lat', $_POST['user_lat'], PDO::PARAM_STR);
					$stmt->bindParam(':user_lng', $_POST['user_lng'], PDO::PARAM_STR);
					$stmt->bindParam(':radius', $_POST['radius'], PDO::PARAM_STR);
					$stmt->execute();
					$stmt->store_result();
					$rows = $stmt->num_rows;
					$result["nb_rows"] = $rows;
			// check for empty result
					if ($rows > 0) {  
						$result["spots"] = array();
						while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
        // temp spot array
							$spot = array();
							$spot["lat"] = $row["lat"];
							$spot["lng"] = $row["lng"];
        // push single spot into final response array
							array_push($result["spots"], $spot);
						}
    // success
						$result["success"] = 1;

					} else {
    // no products found
						$result["success"] = 0;
						$result["response"] = "No spots found";	
					}
				}
			}
			catch(PDOException $e) 
			{
				$result["success"] = 0;
				$result["response"] = "Echec de la recherche ! " . $e->getMessage();
			}
		}
		echo json_encode($result);
	}
	?>
