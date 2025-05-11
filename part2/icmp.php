<?php
header('Content-Type: application/json');  // Set the content type to JSON

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

echo json_encode($results_get);  // Ensure that you're returning valid JSON.
?>
