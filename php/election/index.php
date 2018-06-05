<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="">

    <title>Çevrimiçi Seçim Sistemi</title>

    <!-- Bootstrap core CSS -->
    <link href="dist/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="utils/style.css" rel="stylesheet">
    <!-- <link href="utils/datepicker.css" rel="stylesheet">-->
     <link href="utils/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">


</head>

<body class="text-center">
<div class="container">
     <div class="jumbotron">
        <div class="row">
        <div class="col-md-12">
            <form action="result.php" method="post">
                <table class="table table-sm">
                    <thead>
                        <h2> Çevrimiçi Seçim Sistemi </h2>
                    </thead>
                    <tbody>
                    <tr>
                        <td valign="middle" align="right"><label class="col-form-label">Biten Seçimler:</label></td>
                        <td valign="middle" align="right">
                            <select class="form-control" name="electionid">
                               <?php
                                    require 'connect.php';

                                $sqlElection = "SELECT * FROM election WHERE isVotable = '0'";
                                $result = mysqli_query($con, $sqlElection);

                                while ($row = mysqli_fetch_assoc($result)) {

                                ?>

                                  <option value="<?= $row['electionid']; ?>"><?= $row['electionid']; ?></option>
                                
                                <?php
                                }
                               ?>
                            </select>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <button type="submit" class="btn btn-primary" name="show_result">Sonuçları Göster</button>
            </form>
        </div>
</div>
    </div>
    </div>

<script src="assets/js/vendor/jquery-slim.min.js"></script>
<script src="dist/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="utils/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="utils/locales/bootstrap-datetimepicker.fr.js" charset="UTF-8"></script>
<!--<script type="text/javascript">
    $('.form_datetime').datetimepicker({
        //language:  'fr',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1,
        minView: 2
    });
</script>

-->

<!--<script src="utils/datepicker.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('.date_current').datepicker({ //# ile id üzerinden yaparsanız yanlızca bir input çalışır
            format: "yyyy-mm-dd" //dd-mm-yyyy
        });
        $('.date_current').datepicker().datepicker("setDate", new Date());
    });
</script>
<script type="text/javascript">
    $(document).ready(function () {
        $('.date').datepicker({ //# ile id üzerinden yaparsanız yanlızca bir input çalışır
            format: "yyyy-mm-dd" //dd-mm-yyyy
        });
    });
</script>
-->
</body>
</html>
