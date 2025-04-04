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
$secret_key = "58MYLUnDm8HyEjALpMn21U37s1oCZdT1MFBb5RssOm5zmg3svsVDb3W5bt4K4VqLOveO9U4JvdHJlG2snAKcEQ0zS68U29dl6bAGABsM605hUmUh4Q573zoDjl8ukceG";

$conn = new mysqli($host, $username, $password, $dbname);

if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Erreur de connexion à la base de données", "error" => $conn->connect_error]);
    exit;
}

function base64url_encode($data) {
    return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
}

function generate_jwt($payload, $secret_key) {
    $header = json_encode(["alg" => "HS256", "typ" => "JWT"]);
    $base64UrlHeader = base64url_encode($header);
    $base64UrlPayload = base64url_encode(json_encode($payload));
    $signature = hash_hmac('sha256', "$base64UrlHeader.$base64UrlPayload", $secret_key, true);
    $base64UrlSignature = base64url_encode($signature);
    return "$base64UrlHeader.$base64UrlPayload.$base64UrlSignature";
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    if (isset($_POST["email"]) && isset($_POST["password"])) {
        $email = trim($_POST["email"]);
        $password = trim($_POST["password"]);

        if (!empty($email) && !empty($password)) {
            $stmt = $conn->prepare("SELECT id, nom, telephone, password, score FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt->store_result();

            if ($stmt->num_rows > 0) {
                $stmt->bind_result($userId, $nom, $telephone, $hashedPassword, $score);
                $stmt->fetch();

                if (password_verify($password, $hashedPassword)) {
                    $payload = [
                        "iss" => "localhost",
                        "aud" => "localhost",
                        "iat" => time(),
                        "exp" => time() + 604800,
                        "data" => [
                            "id" => $userId,
                            "nom" => $nom,
                            "email" => $email,
                            "telephone" => $telephone,
                            "score" => $score,
                            "telephone" => $telephone,
                        ]
                    ];

                    $jwt = generate_jwt($payload, $secret_key);
                    http_response_code(200);
                    echo json_encode([
                        "success" => true,
                        "message" => "Connexion réussie",
                        "token" => $jwt   
                    ]);
                } else {
                    http_response_code(401);
                    echo json_encode(["success" => false, "message" => "Mot de passe incorrect"]);
                }
            } else {
                http_response_code(404);
                echo json_encode(["success" => false, "message" => "Utilisateur non trouvé"]);
            }
            $stmt->close();
        } else {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Données incomplètes"]);
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
