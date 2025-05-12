<!DOCTYPE html>
<html>
<head>
    <title>ICMP Statistics</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .table-container { display: flex; gap: 40px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        h2, h3 { margin-top: 0; }
    </style>
</head>
<div style="margin-top: 30px; text-align: center;">
        <button onclick="goToPage('main.html')" style="padding:10px 20px; margin:5px; background-color:#4CAF50; color:white; border:none; border-radius:5px;">Main Page</button>
        <button onclick="goToPage('page2.php')" style="padding:10px 20px; margin:5px; background-color:#2196F3; color:white; border:none; border-radius:5px;">Previous</button>
        <button onclick="goToPage('page1.html')" style="padding:10px 20px; margin:5px; background-color:#f44336; color:white; border:none; border-radius:5px;">Next</button>
    </div>
    <script>
    function goToPage(page) {
        window.location.href = page;
    }
    </script>
<body>

<h2>ICMP Statistics (SNMP)</h2>

<?php
$host = "127.0.0.1";
$community = "public";

$icmpOids = [
    "1"  => "icmpInMsgs",
    "2"  => "icmpInErrors",
    "3"  => "icmpInDestUnreachs",
    "4"  => "icmpInTimeExcds",
    "5"  => "icmpInParmProbs",
    "6"  => "icmpInSrcQuenchs",
    "7"  => "icmpInRedirects",
    "8"  => "icmpInEchos",
    "9"  => "icmpInEchoReps",
    "10" => "icmpInTimestamps",
    "11" => "icmpInTimestampReps",
    "12" => "icmpInAddrMasks",
    "13" => "icmpInAddrMaskReps",
    "14" => "icmpOutMsgs",
    "15" => "icmpOutErrors",
    "16" => "icmpOutDestUnreachs",
    "17" => "icmpOutTimeExcds",
    "18" => "icmpOutParmProbs",
    "19" => "icmpOutSrcQuenchs",
    "20" => "icmpOutRedirects",
    "21" => "icmpOutEchos",
    "22" => "icmpOutEchoReps",
    "23" => "icmpOutTimestamps",
    "24" => "icmpOutTimestampReps",
    "25" => "icmpOutAddrMasks",
    "26" => "icmpOutAddrMaskReps"
];


$results_get = [];
foreach ($icmpOids as $oidNum => $name) {
    $oid = "1.3.6.1.2.1.5.$oidNum.0";
    $value = @snmp2_get($host, $community, $oid);
    if ($value !== false) {
        $value = trim(explode(":", $value)[1]);
    } else {
        $value = "Error";
    }
    $results_get[] = ['id' => $oidNum, 'name' => $name, 'value' => $value];
}


$icmp_walk = [];
$results = @snmp2_walk($host, $community, "1.3.6.1.2.1.5");

if ($results !== false) {
    $i = 1; // OID index starts from 1 in $icmpOids
    foreach ($results as $entry) {
        if (!isset($icmpOids[$i])) {
            break;
        }
        $parts = explode(":", $entry);
        $name = $icmpOids[$i];
        $value = trim($parts[1]);
        $icmp_walk[] = ['id' => $i, 'name' => $name, 'value' => $value];
        $i++;
    }
}






?>

<div class="table-container">

    <div>
        <h3>Method 1: snmp2_get()</h3>
        <table>
            <tr><th>ID</th><th>Name</th><th>Value</th></tr>
            <?php foreach ($results_get as $row): ?>
                <tr>
                    <td><?= htmlspecialchars($row['id']) ?></td>
                    <td><?= htmlspecialchars($row['name']) ?></td>
                    <td><?= htmlspecialchars($row['value']) ?></td>
                </tr>
            <?php endforeach; ?>
        </table>
    </div>


    <div>
        <h3>Method 2: snmp2_walk()</h3>
        <table>
            <tr><th>ID</th><th>Name</th><th>Value</th></tr>
            <?php foreach ($icmp_walk as $row): ?>
                <tr>
                    <td><?= htmlspecialchars($row['id']) ?></td>
                    <td><?= htmlspecialchars($row['name']) ?></td>
                    <td><?= htmlspecialchars($row['value']) ?></td>
                </tr>
            <?php endforeach; ?>
        </table>
    </div>
</div>

</body>
</html>