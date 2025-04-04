<?php

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *"); // Permettre les requêtes depuis n'importe où (pour le test)
header("Access-Control-Allow-Methods: POST");

// Connexion à la base de données
$host = "localhost";
$dbname = "database_java"; // Nom de la base de données
$username = "root";
$password = "";

// Création de la connexion
$conn = new mysqli($host, $username, $password, $dbname);

// Vérification de la connexion
if ($conn->connect_error) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Erreur de connexion à la base de données"]);
    exit;
}

// Vérification de la méthode HTTP
if ($_SERVER["REQUEST_METHOD"] === "POST") {

    // Vérifier si les données sont envoyées
    if (isset($_POST["nom"]) && isset($_POST["email"]) && isset($_POST["password"])) {
        $nom = trim($_POST["nom"]);
        $email = trim($_POST["email"]);
        $password = trim($_POST["password"]);

        // Vérification des valeurs
        if (!empty($nom) && !empty($email) && !empty($password)) {

            // Vérifier si l'email est valide
            if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
                http_response_code(400);
                echo json_encode(["success" => false, "message" => "Adresse email invalide"]);
                exit;
            }

            // Vérifier si l'email existe déjà
            $checkEmail = $conn->prepare("SELECT id FROM users WHERE email = ?");
            $checkEmail->bind_param("s", $email);
            $checkEmail->execute();
            $checkEmail->store_result();

            if ($checkEmail->num_rows > 0) {
                http_response_code(409); // Code 409 : Conflict
                echo json_encode(["success" => false, "message" => "Cet email est déjà enregistré"]);
                exit;
            }

            $checkEmail->close();

            // Hachage sécurisé du mot de passe
            $hashedPassword = password_hash($password, PASSWORD_DEFAULT);

            // Insérer l'utilisateur dans la base de données
            $stmt = $conn->prepare("INSERT INTO users (nom, email, password) VALUES (?, ?, ?)");
            $stmt->bind_param("sss", $nom, $email, $hashedPassword);

            if ($stmt->execute()) {
                http_response_code(201); // Code 201 : Created
                echo json_encode(["success" => true, "message" => "Utilisateur enregistré avec succès"]);
            } else {
                http_response_code(500);
                echo json_encode(["success" => false, "message" => "Erreur lors de l'enregistrement"]);
            }

            $stmt->close();
        } else {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Les champs ne peuvent pas être vides"]);
        }
    } else {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Données incomplètes"]);
    }
} else {
    http_response_code(405);
    echo json_encode(["success" => false, "message" => "Méthode non autorisée"]);
}

// Fermer la connexion
$conn->close();
