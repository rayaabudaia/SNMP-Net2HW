<!DOCTYPE html>
<html>
<head>
    <title>TCP Connection Table (via SNMP)</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
    </style>
</head>
<div style="margin-top: 30px; text-align: center;">
        <button onclick="goToPage('main.html')" style="padding:10px 20px; margin:5px; background-color:#4CAF50; color:white; border:none; border-radius:5px;">Main Page</button>
        <button onclick="goToPage('page1.html')" style="padding:10px 20px; margin:5px; background-color:#2196F3; color:white; border:none; border-radius:5px;">Previous</button>
        <button onclick="goToPage('page3.php')" style="padding:10px 20px; margin:5px; background-color:#f44336; color:white; border:none; border-radius:5px;">Next</button>
    </div>
    <script>
    function goToPage(page) {
        window.location.href = page;
    }
    </script>
<body>

<h2>TCP Connection Table</h2>

<?php
$host = "127.0.0.1";
$community = "public";

$TCP = "1.3.6.1.2.1.6.13.1";

$tcpStateOid     = "$TCP.1";
$tcpLocalAddrOid = "$TCP.2";
$tcpLocalPortOid = "$TCP.3";
$tcpRemAddrOid   = "$TCP.4";
$tcpRemPortOid   = "$TCP.5";

$tcpStates     = snmp2_walk($host, $community, $tcpStateOid);
$tcpLocalAddrs = snmp2_walk($host, $community, $tcpLocalAddrOid);
$tcpLocalPorts = snmp2_walk($host, $community, $tcpLocalPortOid);
$tcpRemAddrs   = snmp2_walk($host, $community, $tcpRemAddrOid);
$tcpRemPorts   = snmp2_walk($host, $community, $tcpRemPortOid);

echo "<table>";
echo "<tr>
    <th>Index</th>
    <th>State</th>
    <th>Source IP</th>
    <th>Source Port</th>
    <th>Destination IP</th>
    <th>Destination Port</th>
</tr>";

$total = count($tcpStates);
for ($i = 0; $i < $total; $i++) {
    $localAddr = trim(explode(":", $tcpLocalAddrs[$i])[1]);
    $localPort = trim(explode(":", $tcpLocalPorts[$i])[1]);
    $remoteAddr = trim(explode(":", $tcpRemAddrs[$i])[1]);
    $remotePort = trim(explode(":", $tcpRemPorts[$i])[1]);
    $stateVal = trim(explode(":", $tcpStates[$i])[1]);

    echo "<tr>";
    echo "<td>" . ($i + 1) . "</td>";
    echo "<td>$stateVal</td>";
    echo "<td>$localAddr</td>";
    echo "<td>$localPort</td>";
    echo "<td>$remoteAddr</td>";
    echo "<td>$remotePort</td>";
    echo "</tr>";
}
echo "</table>";
?>

</body>
</html>