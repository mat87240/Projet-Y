<?php

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

ini_set('display_errors', 1);
error_reporting(E_ALL);

$host = "localhost";
$dbname = "database_java";
$username = "root";
$password = "";

// Connexion à la base de données
$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Erreur de connexion à la base de données"]);
    exit;
}

// Vérifier si les données sont envoyées via POST
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    if (isset($_POST["email"]) && isset($_POST["score"])) {
        $email = trim($_POST["email"]);
        $score = intval($_POST["score"]); // Convertir en entier

        if (!empty($email) && is_numeric($score)) {
            // Vérifier si l'utilisateur existe et récupérer le score actuel
            $stmt = $conn->prepare("SELECT id, score FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt->store_result();

            if ($stmt->num_rows > 0) {
                $stmt->bind_result($userId, $currentScore);
                $stmt->fetch();
                $stmt->close();

                // Vérifier si le nouveau score est supérieur
                if ($score > $currentScore) {
                    // Mettre à jour le score seulement si c'est plus grand
                    $updateStmt = $conn->prepare("UPDATE users SET score = ? WHERE email = ?");
                    $updateStmt->bind_param("is", $score, $email);

                    if ($updateStmt->execute()) {
                        http_response_code(200);
                        echo json_encode(["success" => true, "message" => "Score mis à jour avec succès"]);
                    } else {
                        http_response_code(500);
                        echo json_encode(["success" => false, "message" => "Erreur lors de la mise à jour du score"]);
                    }
                    $updateStmt->close();
                } else {
                    http_response_code(200);
                    echo json_encode(["success" => false, "message" => "Le score n'a pas été mis à jour car il est inférieur ou égal à l'ancien"]);
                }
            } else {
                http_response_code(404);
                echo json_encode(["success" => false, "message" => "Utilisateur non trouvé"]);
            }
        } else {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Données invalides"]);
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
