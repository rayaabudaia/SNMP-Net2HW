<?php
$host = "127.0.0.1";
$community = "private"; // Use RW community string for setting

$oids = [
    "sysContact" => "1.3.6.1.2.1.1.4.0",
    "sysName"    => "1.3.6.1.2.1.1.5.0",
    "sysLocation"=> "1.3.6.1.2.1.1.6.0"
];

// Handle POST for set
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    foreach ($oids as $key => $oid) {
        if (isset($_POST[$key])) {
            $value = $_POST[$key];
            $set = snmp2_set($host, $community, $oid, "s", $value);
            if ($set) {
                echo json_encode(["status" => "success", "message" => "$key updated"]);
            } else {
                echo json_encode(["status" => "error", "message" => "Failed to update $key"]);
            }
            exit;
        }
    }
    echo json_encode(["status" => "error", "message" => "No valid key"]);
    exit;
}

// GET for fetch
$data = [];
foreach ($oids as $key => $oid) {
    $value = snmp2_get($host, $community, $oid);
    if ($value !== false) {
        $data[$key] = trim(explode(":", $value)[1]);
    } else {
        $data[$key] = "Error";
    }
}

header('Content-Type: application/json');
echo json_encode($data);
?>
