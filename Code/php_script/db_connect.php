<?php
function dbconnexion(){ // fonction de connexion à la base de données, retourne le PDO crée
	require_once(__DIR__ . '/config.php');
	try
	{
		$auth = new PDO('mysql:host='.DB_HOST.';dbname='.DB_NAME.'', ''.DB_USER.'', ''.DB_PASSWORD.'');
		$auth->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$auth->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
	} 
	catch(PDOException $e)
	{
		echo '<div class="errors">Echec de la connexion ' . $e->getMessage().'</div>';
		exit;
	}
	return $auth;
} 
?>