<?php
$host = "127.0.0.1";
$community = "public";

// TCP OID base
$TCP = "1.3.6.1.2.1.6.13.1";

// Specific OIDs
$tcpStateOid     = "$TCP.1";
$tcpLocalAddrOid = "$TCP.2";
$tcpLocalPortOid = "$TCP.3";
$tcpRemAddrOid   = "$TCP.4";
$tcpRemPortOid   = "$TCP.5";

// SNMP walk
$tcpStates     = snmp2_walk($host, $community, $tcpStateOid);
$tcpLocalAddrs = snmp2_walk($host, $community, $tcpLocalAddrOid);
$tcpLocalPorts = snmp2_walk($host, $community, $tcpLocalPortOid);
$tcpRemAddrs   = snmp2_walk($host, $community, $tcpRemAddrOid);
$tcpRemPorts   = snmp2_walk($host, $community, $tcpRemPortOid);

// Prepare an array for storing TCP connection data
$tcpConnections = [];
$total = count($tcpStates);

for ($i = 0; $i < $total; $i++) {
    $localAddr = trim(explode(":", $tcpLocalAddrs[$i])[1]);
    $localPort = trim(explode(":", $tcpLocalPorts[$i])[1]);
    $remoteAddr = trim(explode(":", $tcpRemAddrs[$i])[1]);
    $remotePort = trim(explode(":", $tcpRemPorts[$i])[1]);
    $stateVal = trim(explode(":", $tcpStates[$i])[1]);

    // Add each connection as an associative array
    $tcpConnections[] = [
        'index' => ($i + 1),
        'state' => $stateVal,
        'localAddr' => $localAddr,
        'localPort' => $localPort,
        'remoteAddr' => $remoteAddr,
        'remotePort' => $remotePort
    ];
}

// Set the header to indicate the content type is JSON
header('Content-Type: application/json');

// Output the array as a JSON string
echo json_encode($tcpConnections);
?>
