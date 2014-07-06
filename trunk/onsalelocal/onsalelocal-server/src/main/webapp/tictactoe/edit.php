<?php
    $path = dirname(__FILE__);
    $file = $_REQUEST["file"];
    $pfile = $path . $file;
    
    $content = stripslashes($_REQUEST["content"]);
    if($content) {
        file_put_contents($pfile, $content);
    }
?>

<html><body>

<?php echo $_REQUEST["file"]; ?>
<form method="post">
<input type="hidden" name="file" value="<?php echo $_REQUEST["file"]; ?>"/>
<textarea name="content" style="width:100%;height:480px;">
<?php
    $path = dirname(__FILE__);
    $file = $_REQUEST["file"];
    echo file_get_contents($pfile);
?>
</textarea>
<input type="submit" value="save"/>
</form>

</body></html>
