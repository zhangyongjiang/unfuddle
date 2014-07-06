<html><body>

<?php

function startsWith($haystack, $needle)
{
    return !strncmp($haystack, $needle, strlen($needle));
}

function endsWith($haystack, $needle)
{
    $length = strlen($needle);
    if ($length == 0) {
        return true;
    }

    return (substr($haystack, -$length) === $needle);
}

    $path = dirname(__FILE__);

$iterator = new RecursiveIteratorIterator(
                new RecursiveDirectoryIterator($path), 
            RecursiveIteratorIterator::SELF_FIRST);

$lastdir = $path;
foreach($iterator as $file) {
    $filedir = dirname($file);
    if($file->isFile() && $filedir != $path) {
        if($lastdir != $path && $lastdir != $filedir) {
            echo "<br/>";
        }
        $lastdir = $filedir;
        
        $fname = substr($file->getRealpath(), strlen($path));
        $viewname = substr($fname, 1);
	$showLink = endsWith($viewname, "html");
        echo "<a target='right' href='edit.php?file=$fname'>edit</a> ";        
	if($showLink) {
        	echo "<a target='view' href='$viewname'>";
	}        
        echo "$viewname";
	if($showLink) {        
        	echo "</a> ";     
	}   
        echo "<br/>\n";
    }
}

?>

</body></html>
