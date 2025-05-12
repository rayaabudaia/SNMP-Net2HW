<?php
// system_info.php

// SNMP settings
$ip = "127.0.0.1";   // Change to your target SNMP agent IP
$community = "public";  // Read-write community string
$sysGroupOids = [
    "sysDescr"     => "1.3.6.1.2.1.1.1.0",
    "sysObjectID"  => "1.3.6.1.2.1.1.2.0",
    "sysUpTime"    => "1.3.6.1.2.1.1.3.0",
    "sysContact"   => "1.3.6.1.2.1.1.4.0",
    "sysName"      => "1.3.6.1.2.1.1.5.0",
    "sysLocation"  => "1.3.6.1.2.1.1.6.0",
    "sysServices"  => "1.3.6.1.2.1.1.7.0"
];

// Check if this is an UPDATE request
if (isset($_POST['oid']) && isset($_POST['value'])) {
    $oid = $_POST['oid'];
    $newValue = $_POST['value'];
    
    // Update the SNMP value
    $result = snmp2_set($ip, $community, $oid, "s", $newValue); // "s" means string type
    
    if ($result === false) {
        echo json_encode(["status" => "error", "message" => "Failed to update OID $oid"]);
    } else {
        echo json_encode(["status" => "success", "message" => "Successfully updated"]);
    }
    exit;
}

// Otherwise, return the system info
$data = [];

foreach ($sysGroupOids as $name => $oid) {
    if ($name != "sysServices") { // Skip sysServices
        $value = @snmp2_get($ip, $community, $oid);
        if ($value !== false) {
            // Clean value (remove type info like "STRING: ")
            $value = explode(":", $value, 2)[1] ?? $value;
            $value = trim($value);
            $data[$name] = $value;
        } else {
            $data[$name] = "Error fetching";
        }
    }
}

echo json_encode($data);
?>
