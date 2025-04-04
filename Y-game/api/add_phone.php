<?php

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

ini_set('display_errors', 1);
error_reporting(E_ALL);

$host = "localhost";
$dbname = "database_java"; // Nom de la base de données
$username = "root";
$password = "";

$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Erreur de connexion à la base de données"]);
    exit;
}

// Vérifier si les données sont envoyées via POST
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    // Vérifier que 'email' et 'phone' sont envoyés dans la requête
    if (isset($_POST["email"]) && isset($_POST["phone"])) {
        $email = trim($_POST["email"]);
        $phone = trim($_POST["phone"]);

        // Vérifier si le numéro de téléphone est valide (ex : 10 chiffres)
        if (!empty($email) && preg_match("/^[0-9]{10}$/", $phone)) {
            // Vérifier si l'utilisateur existe
            $stmt = $conn->prepare("SELECT id FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt->store_result();

            if ($stmt->num_rows > 0) {
                $stmt->bind_result($userId);
                $stmt->fetch();
                $stmt->close();

                // Mettre à jour le numéro de téléphone
                $updateStmt = $conn->prepare("UPDATE users SET telephone = ? WHERE email = ?");
                $updateStmt->bind_param("ss", $phone, $email);
                
                if ($updateStmt->execute()) {
                    http_response_code(200);
                    echo json_encode(["success" => true, "message" => "Numéro de téléphone mis à jour avec succès"]);
                } else {
                    http_response_code(500);
                    echo json_encode(["success" => false, "message" => "Erreur lors de la mise à jour du numéro de téléphone"]);
                }
                $updateStmt->close();
            } else {
                http_response_code(404);
                echo json_encode(["success" => false, "message" => "Utilisateur non trouvé"]);
            }
        } else {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Données invalides ou numéro de téléphone incorrect"]);
        }
    } else {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Données incomplètes"]);
    }
} else {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Méthode non autorisée"]);
}

$conn->close();

?>
