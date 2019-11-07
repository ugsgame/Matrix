
$PAK = "map.pak";
$LIST = "list.txt";
$RES = "E:\\Dev\\ShuiHuHero\\dev\\Tech\\Client\\Resources\\Data\\Map\\map";

@TARGET = (
	'..\\..\\..\\win32\\Debug.win32'
);

$MSG = 0;

print ">>> Packing...\n\n";

open LIST, "<$LIST" || die("$!: $LIST not found.\n");
$cmd = "pak.exe $PAK > pak.log";
while (<LIST>)
{
	chomp();
	if ($_ !~ /^\/\// && $_ !~ /^\#/)
	{
		$cmd .= " ".$_;
	}
}
close LIST;

#print "$cmd\n";
system $cmd;

system "del /q $RES\\*.png";
system "copy/y *.png $RES";

foreach (@TARGET)
{
	if (-e $_)
	{
		my $cmd = "copy/y $PAK $_";
		#print "$cmd\n";
		system $cmd;
	}
}

if ($MSG && $ARGV[0] eq "")
{
#	system "pause";
	for ($i=15; $i>0; $i--)
	{
		print "$i.";
		sleep(1);
	}
}
system "pak.log";
