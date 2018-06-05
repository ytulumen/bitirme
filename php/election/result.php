
<!DOCTYPE html>
<html>
<head>
  <title></title>
  <style type="text/css">
  #container { text-align: center; margin: 20px; }
h2 { color: #CCC; }
a { text-decoration: none; color: #EC5C93; }
.bar-main-container {
    margin: 10px ;
    width: 300px;
    height: 55px;
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    border-radius: 4px;
    font-family: sans-serif;
    font-weight: normal;
    font-size: 0.8em;
    color: #FFF;
}
.wrap { padding: 8px; }
.bar-percentage {
    float: left;
    background: rgba(0,0,0,0.13);
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    border-radius: 4px;
    padding: 9px 0px;
    width: 18%;
    height: 16px;
    margin-top: -15px;
}
.bar-container {
    float: right;
    -webkit-border-radius: 10px;
    -moz-border-radius: 10px;
    border-radius: 10px;
    height: 10px;
    background: rgba(0,0,0,0.13);
    width: 78%;
    margin: 0px 0px;
    overflow: hidden;
}
.bar-main-container .txt{
    padding-top: 5px;
    font-size: 16px;
    font-weight: bold;
}

.bar {
    float: left;
    background: #FFF;
    height: 100%;
    -webkit-border-radius: 10px 0px 0px 10px;
    -moz-border-radius: 10px 0px 0px 10px;
    border-radius: 10px 0px 0px 10px;
    -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=100)";
    filter: alpha(opacity=100);
    -moz-opacity: 1;
    -khtml-opacity: 1;
    opacity: 1;
}

/* COLORS */
.azure   { background: #38B1CC; }
.emerald { background: #2CB299; }
.violet  { background: #8E5D9F; }
.yellow  { background: #EFC32F; }
.red     { background: #E44C41; }
</style>


</head>
<body>

</body>
</html>


<?php
require 'connect.php';

$electionid = $_POST['electionid'];


$sqlElection = "SELECT * FROM election WHERE isVotable = '0'";
$result = mysqli_query($con, $sqlElection);

?>
<?php


while ($row = mysqli_fetch_assoc($result)) {
    echo "<div class='container'>";
    echo "<h3 style='margin-left: 10px'>".$row['title']."</h3>"."<br>";

    $i = 0;
    $totalVote = 0;
    $sqlCandidates = "SELECT * FROM candidates WHERE electionid='$electionid'";
    $resultCandidates = mysqli_query($con, $sqlCandidates);
    while ( $rowCandidates = mysqli_fetch_assoc($resultCandidates) ) {
      $totalVote += $rowCandidates["votecounter"];

     } 

      $sqlCandidates = "SELECT * FROM candidates WHERE electionid='$electionid'";
      $resultCandidates = mysqli_query($con, $sqlCandidates);
      while ( $rowCandidates = mysqli_fetch_assoc($resultCandidates) ) {
      
          $barColorArr = array('azure','emerald','violet','yellow','red');
      

        if(!array_key_exists($i, $barColorArr)){
            $i=0;
        }
        $barColor = $barColorArr[$i];


      /*echo $rowCandidates["id"].": \t";
      echo $rowCandidates["fname"]." ";
      
      echo $rowCandidates["surname"]." ";
      echo $rowCandidates["votecounter"];
      echo "<br>";
    */
    
    $votePercent = round(($rowCandidates["votecounter"] / $totalVote) * 100);
    $votePercent = !empty($votePercent)?$votePercent.'%':'0%';
?>

<div class="bar-main-container <?php echo $barColor; ?>">
  <div class="txt" style="margin-left: 150px"><?php echo $rowCandidates["fname"]; ?></div>
  <div class="wrap">
    <div class="bar-percentage"><u style="margin-left: 20px"><?php echo $rowCandidates["votecounter"]; ?></u></div>
    <div class="bar-container">
      <div class="bar" style="width: <?php echo $votePercent; ?>;"></div>
    </div>
  </div>
</div>

<?php $i++;} echo "</div>";
echo "<h3 style='margin-left: 10px'><b>Total Votes:</b>".$totalVote."</h3>";}
?>




